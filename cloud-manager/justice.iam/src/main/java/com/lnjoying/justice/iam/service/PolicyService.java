package com.lnjoying.justice.iam.service;

import com.google.common.collect.Lists;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.iam.authz.opa.client.OpaClient;
import com.lnjoying.justice.iam.authz.opa.common.policy.DeletePolicyBundle;
import com.lnjoying.justice.iam.authz.opa.common.policy.PolicyBundle;
import com.lnjoying.justice.iam.authz.opa.common.policy.PolicyDocumentUtils;
import com.lnjoying.justice.iam.authz.opa.common.policy.UpdatePolicyBundle;
import com.lnjoying.justice.iam.authz.opa.util.RegoSyntaxChecker;
import com.lnjoying.justice.iam.db.model.*;
import com.lnjoying.justice.iam.db.repo.*;
import com.lnjoying.justice.iam.domain.dto.request.policy.AddPolicyReq;
import com.lnjoying.justice.iam.domain.dto.request.policy.AddPolicyVersionReq;
import com.lnjoying.justice.iam.domain.dto.request.policy.PatchPolicyVersionReq;
import com.lnjoying.justice.iam.domain.dto.request.policy.UpdatePolicyReq;
import com.lnjoying.justice.iam.domain.dto.response.authz.AttachmentEntitiesRsp;
import com.lnjoying.justice.iam.domain.dto.response.policy.PoliciesRsp;
import com.lnjoying.justice.iam.domain.dto.response.policy.TblIamPolicyDetail;
import com.lnjoying.justice.iam.domain.dto.response.resource.TblIamCommonResourceSummary;
import com.lnjoying.justice.iam.domain.model.IamPolicy;
import com.lnjoying.justice.iam.domain.model.IamPolicyDocument;
import com.lnjoying.justice.iam.domain.model.IamPolicyVersion;
import com.lnjoying.justice.iam.utils.NameGenerator;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.common.RandomName;
import com.micro.core.common.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.lnjoying.justice.iam.authz.opa.pap.rego.generated.ResourceSetGenerator.*;
import static com.lnjoying.justice.iam.authz.opa.pap.rego.util.FormatterUtils.lowerHyphen2LowerUnderscore;
import static com.lnjoying.justice.iam.common.constant.IamConstants.SUBTRACT;
import static com.lnjoying.justice.iam.db.model.TblIamAttachment.PrincipalType.fromValue;
import static com.lnjoying.justice.iam.db.model.TblIamPolicy.PolicyType.CUSTOM;
import static com.lnjoying.justice.iam.db.model.TblIamPolicy.PolicyType.SYSTEM;
import static com.lnjoying.justice.iam.db.model.TblIamPolicy.allRulesInDoc;
import static com.lnjoying.justice.iam.utils.ServiceCombRequestUtils.isAdmin;
import static com.lnjoying.justice.schema.common.ErrorCode.*;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/10 19:04
 */

@Slf4j
@Service
public class PolicyService
{
    private static final String POLICY_VERSION_PREFIX = "v";

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private DisruptorService disruptorService;

    @Autowired
    private OpaClient opaClient;

    @Autowired
    private AuthzRepository authzRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private AuthzService authzService;


    public PoliciesRsp getPolicyList(String name, String queryBpId, Integer policyType, Integer pageSize, Integer pageNum)
    {
        return policyRepository.getPolicyList(name, queryBpId, policyType, pageSize, pageNum);
    }

    @Transactional(rollbackFor = Exception.class)
    public Object addPolicy(AddPolicyReq req)
    {
        String bpId = req.getBpId();

        String policyName = generatePolicyName(req, bpId);

        if (checkPolicyNameExists(policyName, bpId))
        {
            throw new WebSystemException(IAM_POLICY_NAME_EXIST, ErrorLevel.ERROR);
        }

        if (checkRegoDocError(req))
        {
            throw new WebSystemException(IAM_REGO_DOC_INCORRECT, ErrorLevel.ERROR);
        }
        // add policy
        TblIamPolicy tblIamPolicy = new TblIamPolicy();
        String id = Utils.assignUUId();
        tblIamPolicy.setId(id);
        // todo check only admin can set system type
        BeanUtils.copyProperties(req, tblIamPolicy);
        tblIamPolicy.setName(policyName);
        // the default version is v1
        String version = getVersion(1);
        tblIamPolicy.setDefaultVersion(version);
        tblIamPolicy.setAutogen(false);
        tblIamPolicy.setAttachmentCount(0);
        tblIamPolicy.setAttachable(false);
        tblIamPolicy.setBase(false);
        if (isAdmin())
        {
            tblIamPolicy.setPolicyType(SYSTEM.value());
        }
        else
        {
            tblIamPolicy.setPolicyType(CUSTOM.value());
        }

        // todo set arn tags
        tblIamPolicy.setCreateTime(new Date());
        tblIamPolicy.setUpdateTime(tblIamPolicy.getCreateTime());
        policyRepository.insertSelective(tblIamPolicy);

        // add policy version
        TblIamPolicyVersion tblIamPolicyVersion = new TblIamPolicyVersion();
        tblIamPolicyVersion.setPolicyId(id);
        tblIamPolicyVersion.setCreateTime(new Date());
        tblIamPolicyVersion.setUpdateTime(tblIamPolicyVersion.getCreateTime());
        // todo check document whether the format and content are correct
        String policyDocument = req.getPolicyDocument();
        tblIamPolicyVersion.setDocument(policyDocument);
        tblIamPolicyVersion.setVersionId(version);
        policyRepository.insertPolicyVersionSelective(tblIamPolicyVersion);

        // publish policy
        boolean systemType = SYSTEM.equals(TblIamPolicy.PolicyType.fromValue(tblIamPolicy.getPolicyType())) ? true : false;

        IamPolicyDocument iamPolicyDocument = new IamPolicyDocument(id, version, policyDocument, systemType, req.getBpId());
        publishPolicyDocument(iamPolicyDocument, false);
        return tblIamPolicy.getId();

    }

    public void deletePolicy(String policyId, String queryBpId)
    {
        TblIamPolicyDetail policy = policyRepository.selectByIdAndBpId(policyId, queryBpId);
        if (Objects.nonNull(policy))
        {
            // delete associated resources if there is no policy versions
            if (hasAssociatedResources(policyId))
            {
                log.warn("policy:{} has associated resource", policyId);
                //throw new WebSystemException(IAM_POLICY_HAS_ASSOCIATED_RESOURCES, ErrorLevel.ERROR);
            }

            deleteDefaultPolicyVersion(policyId, policy.getDefaultVersion());

        }

    }


    @Transactional(rollbackFor = {Exception.class})
    public void updatePolicy(String policyId, UpdatePolicyReq req)
    {
        if (!checkPolicyExists(policyId, req.getBpId()))
        {
            throw new WebSystemException(IAM_POLICY_NOT_EXIST, ErrorLevel.ERROR);
        }

        TblIamPolicy tblIamPolicy = new TblIamPolicy();
        BeanUtils.copyProperties(req, tblIamPolicy);
        tblIamPolicy.setId(policyId);
        tblIamPolicy.setUpdateTime(new Date());

        // add a new version directly
        TblIamPolicyDetail policy = policyRepository.selectByIdAndBpId(policyId, req.getBpId());
        String defaultVersion = policy.getDefaultVersion();
        TblIamPolicyVersion tblIamPolicyVersion = policyRepository.selectPolicyVersionByPolicyIdAndVersionId(policyId, defaultVersion);
        String policyDocument = req.getPolicyDocument();
        if (tblIamPolicyVersion.getDocument().equals(policyDocument))
        {
            policyRepository.updateByPrimaryKeySelective(tblIamPolicy);
            // boolean systemType = SYSTEM.equals(TblIamPolicy.PolicyType.fromValue(policy.getPolicyType().intValue())) ? true : false;
            // IamPolicyDocument iamPolicyDocument = new IamPolicyDocument(policyId, defaultVersion, policyDocument, systemType, req.getBpId());
            // publishPolicyDocument(iamPolicyDocument, false);
        }
        else
        {
            tblIamPolicyVersion.setDocument(policyDocument);
            String latestVersionId = policyRepository.selectLatestVersionIdByPolicyId(policyId);
            String nextVersion = getNextVersion(latestVersionId);
            tblIamPolicyVersion.setVersionId(nextVersion);
            tblIamPolicyVersion.setUpdateTime(new Date());
            tblIamPolicyVersion.setDescription(req.getDescription());
            policyRepository.insertPolicyVersionSelective(tblIamPolicyVersion);

            tblIamPolicy.setDefaultVersion(nextVersion);
            policyRepository.updateByPrimaryKeySelective(tblIamPolicy);

            // delete old and publish new policy
            deletePolicyInOpaServer(policyId, defaultVersion);
            boolean systemType = SYSTEM.equals(TblIamPolicy.PolicyType.fromValue(policy.getPolicyType().intValue())) ? true : false;
            IamPolicyDocument iamPolicyDocument = new IamPolicyDocument(policyId, nextVersion, policyDocument, systemType, req.getBpId());
            publishPolicyDocument(iamPolicyDocument, false);
        }

    }

    public Object getPolicy(String policyId, String queryBpId)
    {
        TblIamPolicyDetail policy = policyRepository.selectByIdAndBpId(policyId, queryBpId);
        return IamPolicy.of(policy);
    }


    public Object getPolicyVersions(String policyId)
    {
        List<TblIamPolicyVersion> tblIamPolicyVersions = policyRepository.selectPolicyVersionsByPolicyId(policyId);
        TblIamPolicy policy = getPolicy(policyId);
        String defaultVersion = policy.getDefaultVersion();
        return tblIamPolicyVersions.stream().map(tblIamPolicyVersion -> {return IamPolicyVersion.of(tblIamPolicyVersion, defaultVersion);}).collect(Collectors.toList());
    }


    public Object getPolicyVersion(String policyId, String versionId)
    {
        TblIamPolicyVersion tblIamPolicyVersion = policyRepository.selectPolicyVersionByPolicyIdAndVersionId(policyId, versionId);
        TblIamPolicy policy = getPolicy(policyId);
        String defaultVersion = policy.getDefaultVersion();
        return IamPolicyVersion.of(tblIamPolicyVersion, defaultVersion);
    }


    public Object addPolicyVersion(String policyId, AddPolicyVersionReq req)
    {

        TblIamPolicyVersion tblIamPolicyVersion = new TblIamPolicyVersion();
        tblIamPolicyVersion.setPolicyId(policyId);
        tblIamPolicyVersion.setCreateTime(new Date());
        tblIamPolicyVersion.setUpdateTime(tblIamPolicyVersion.getCreateTime());
        tblIamPolicyVersion.setDocument(req.getPolicyDocument());
        tblIamPolicyVersion.setDescription(req.getDescription());

        String latestVersionId = policyRepository.selectLatestVersionIdByPolicyId(policyId);
        tblIamPolicyVersion.setVersionId(getNextVersion(latestVersionId));
        policyRepository.insertPolicyVersionSelective(tblIamPolicyVersion);
        return null;
    }


    public void deletePolicyVersion(String policyId, String versionId)
    {

        TblIamPolicy policy = getPolicy(policyId);
        if (policy.getDefaultVersion().equals(versionId))
        {
            Integer count = policyRepository.countPolicyVersions(policyId, null);
            if (Objects.nonNull(count))
            {
                if (count > 1)
                {
                    throw new WebSystemException(IAM_POLICY_VERSION_DEFAULT_NOT_SET_ERROR, ErrorLevel.ERROR);
                }
                else if (count == 1)
                {
                    deletePolicyInOpaServer(policyId, versionId);

                }
            }


        }
        policyRepository.deletePolicyVersionByPolicyIdAndVersionId(policyId, versionId);

        // 如果是默认版本，则需要删除opa中的策略


    }

    private void deletePolicyInOpaServer(String policyId, String versionId)
    {
        Consumer<PolicyBundle> deleteConsumer = policyBundle -> {
            disruptorService.publish(new DeletePolicyBundle(policyBundle.getOpaPolicyId()));
        };

        getAndDealPolicyBundles(policyId, versionId, deleteConsumer);
    }


    public void patchPolicyVersion(String policyId, String versionId, PatchPolicyVersionReq req)
    {
        // check version exists
        if (Objects.nonNull(req) && req.isDefaultVersion())
        {
            if(checkPolicyVersionExists(policyId, versionId))
            {
                // update policy
                TblIamPolicy tblIamPolicy = new TblIamPolicy();
                tblIamPolicy.setDefaultVersion(versionId);
                tblIamPolicy.setId(policyId);
                policyRepository.updateByPrimaryKeySelective(tblIamPolicy);

                // Obtain the previous default version and delete the corresponding policy in the opa store.
                TblIamPolicy policy = getPolicy(policyId);
                String oldVersion = policy.getDefaultVersion();
                List<PolicyBundle> oldPolicyBundles = getPolicyBundles(policyId, oldVersion);

                List<String> deletePolicyBundles = new ArrayList<>();
                if (!CollectionUtils.isEmpty(oldPolicyBundles))
                {
                    List<String> collect = oldPolicyBundles.stream().map(policyBundle -> policyBundle.getOpaPolicyId()).collect(Collectors.toList());
                    deletePolicyBundles.addAll(collect);
                }

                // construct the newly added policy bundles
                List<PolicyBundle> newPolicyBundles = getPolicyBundles(policyId, oldVersion);

                UpdatePolicyBundle updatePolicyBundle = new UpdatePolicyBundle();
                updatePolicyBundle.setDeleteBundleList(deletePolicyBundles);
                updatePolicyBundle.setAddBundleList(newPolicyBundles);
                disruptorService.publish(updatePolicyBundle);

            }

        }
    }

    public void loadPolicyBundleFromDb()
    {
        // base policy
        loadPolicyBundleFromDb(true);

        // other policy
        loadPolicyBundleFromDb(false);
    }

    public void loadPolicyBundleFromDb(boolean base)
    {

        try
        {
            List<IamPolicyDocument> allDocuments = policyRepository.selectByBase(base);
            if (!CollectionUtils.isEmpty(allDocuments))
            {
                allDocuments.stream().forEach(policyDocument -> {
                    if (StringUtils.isNotBlank(policyDocument.getDocument()))
                    {
                        publishPolicyDocument(policyDocument, true);
                    }

                });
            }

        }
        catch (Exception e)
        {
            log.error("load policy bundle from db failed:{}", e);
        }

    }

    public void loadAllResourceTypesFromDb()
    {
        try
        {
            List<TblIamCommonResourceSummary> commonResources =  resourceRepository.selectAllResources();
            if (!CollectionUtils.isEmpty(commonResources))
            {
                List<PolicyBundle> policyBundles = commonResources.stream().map(commonResource ->
                {
                    Map<String, String> params = new HashMap<>();
                    String colonResourceName = colonResourceName(commonResource.getServiceIamCode(), commonResource.getName());
                    String doubleUnderscoreResourceName = doubleUnderscoreResourceName(commonResource.getServiceIamCode(), commonResource.getName());
                    params.put(FTL_DATA_RESOURCE, colonResourceName);
                    params.put(FTL_DATA_RESOURCE_NAME, lowerHyphen2LowerUnderscore(doubleUnderscoreResourceName));
                    String rego = INSTANCE.generate(params);
                    PolicyBundle policyBundle = new PolicyBundle();
                    policyBundle.setOpaPolicyId(OPA_POLICY_ID_PREFIX + doubleUnderscoreResourceName);
                    policyBundle.setRego(rego);
                    return policyBundle;
                }).collect(Collectors.toList());

                if (!CollectionUtils.isEmpty(policyBundles))
                {
                    publishDocument(policyBundles, true);
                }
            }
        }
        catch (Exception e)
        {
            log.error("load all resource type from db failed:{}", e);
        }
    }


    public void loadAuthzDatasFromDb()
    {
        try
        {
            // (user,group,role) ---> policy
            List<TblIamAttachment> policyAttachments = authzRepository.selectByPolicyIdAndBPId(null, null);
            if (!CollectionUtils.isEmpty(policyAttachments))
            {
                policyAttachments.forEach(attachment -> {
                    try
                    {
                        authzService.publishAuthzData(attachment.getProjectId(), attachment.getPrincipalType(), attachment.getPrincipalId(), Lists.newArrayList(attachment.getTargetId()));
                    }
                    catch (Exception e)
                    {
                        log.error("publish policy authz data failed, attatchment:{}, error:{}", attachment, e);
                    }
                });
            }

            // (user, group) --> role
            List<TblIamAttachment> roleAttachments = authzRepository.selectAllRoleAttachments(null, null, null);
            if (!CollectionUtils.isEmpty(roleAttachments))
            {
                Map<String, Map<String, List<TblIamAttachment>>> groupedRecords = roleAttachments.stream()
                        .collect(Collectors.groupingBy( record -> StringUtils.isBlank(record.getProjectId())  ? "defaultProjectId" : record.getProjectId(),
                                Collectors.groupingBy(TblIamAttachment::getPrincipalId)));
                groupedRecords.forEach((projectId, attachmentMap) -> {
                    try
                    {
                        attachmentMap.forEach((principalId, attachments) -> {
                            try
                            {
                                List<Long> targets = attachments.stream().map(attachment ->
                                {
                                    return Long.parseLong(attachment.getTargetId());
                                }).collect(Collectors.toList());
                                if (!CollectionUtils.isEmpty(targets))
                                {
                                    if (projectId.equals("defaultProjectId"))
                                    {
                                        authzService.publishRoleData(null, principalId, targets);
                                    }
                                    else
                                    {
                                        authzService.publishRoleData(projectId, principalId, targets);
                                    }
                                }
                            }
                            catch (Exception e)
                            {
                                log.error("publish role authz data failed, attatchment:{}, error:{}", attachmentMap, e);
                            }

                        });
                    }
                    catch (Exception e)
                    {
                        log.error("publish all role authz data failed, attatchment:{}, error:{}", attachmentMap, e);
                    }
                });

            }
        }
        catch (Exception e)
        {
            log.error("load authz data from db failed:{}", e);
        }

    }

    public TblIamPolicy getPolicy(String id)
    {
        TblIamPolicy tblIamPolicy = policyRepository.selectByPrimaryKey(id);
        if (Objects.isNull(tblIamPolicy))
        {
            throw new WebSystemException(IAM_POLICY_NOT_EXIST, ErrorLevel.ERROR);
        }

        return tblIamPolicy;
    }

    public Object getPolicyEntities(String policyId, String queryBpId)
    {
        List<AttachmentEntitiesRsp.User> users = Lists.newArrayList();
        List<AttachmentEntitiesRsp.Role> roles = Lists.newArrayList();
        List<AttachmentEntitiesRsp.Group> groups = Lists.newArrayList();

        if (!checkPolicyExists(policyId, null))
        {
            throw new WebSystemException(IAM_POLICY_NOT_EXIST, ErrorLevel.ERROR);
        }

        List<TblIamAttachment> attachments = authzRepository.selectByPolicyIdAndBPId(policyId, queryBpId);
        if (!CollectionUtils.isEmpty(attachments))
        {
            List<String> userIds = Lists.newArrayList();
            List<Long> roleIds = Lists.newArrayList();
            List<String> groupIds = Lists.newArrayList();

            attachments.forEach(attachment -> {
                TblIamAttachment.PrincipalType principalType = fromValue(attachment.getPrincipalType());
                switch (principalType)
                {
                    case USER:
                        userIds.add(attachment.getPrincipalId());
                        break;
                    case GROUP:
                       groupIds.add(attachment.getPrincipalId());
                        break;
                    case ROLE:
                        roleIds.add(Long.parseLong(attachment.getPrincipalId()));
                        break;
                    default:
                       log.error("wrong principal type");

                }
            });

            if (!CollectionUtils.isEmpty(userIds))
            {
                // get up to 100 at a time
                int userSize = userIds.size();
                for (int i = 0; i < userSize; i = i + 100)
                {
                    List<AttachmentEntitiesRsp.User> userList = userRepository.selectAllByUserIdIn(userIds.subList(i, 100 >= userSize - i ? userSize : 100 + i));
                    users.addAll(userList);
                }
            }

            if (!CollectionUtils.isEmpty(roleIds))
            {
                int roleSize = roleIds.size();
                for (int i = 0; i < roleSize; i = i + 100)
                {
                    List<AttachmentEntitiesRsp.Role> roleList = roleRepository.selectAllByRoleIdIn(roleIds.subList(i, 100 >= roleSize - i ? roleSize : 100 + i));
                    roles.addAll(roleList);
                }
            }

            if (!CollectionUtils.isEmpty(groupIds))
            {
                // todo group detail list
            }
        }

        return AttachmentEntitiesRsp.builder().users(users).roles(roles).groups(groups).build();
    }

    public List<TblIamPolicy> selectBasePoliciesByName(String name)
    {
        return policyRepository.selectByNameAndBase(name, true);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteDefaultPolicyVersion(String policyId, String versionId)
    {
        deletePolicyInOpaServer(policyId, versionId);
        policyRepository.deletePolicyByPrimaryKey(policyId);
        policyRepository.deletePolicyVersionByPolicyIdAndVersionId(policyId, null);
    }


    private void publishPolicyDocument(IamPolicyDocument policyDocument, boolean filter)
    {
        // parse document then filter out what already exists and then push to queue
        List<PolicyBundle> policyBundles = PolicyDocumentUtils.parseDocument(policyDocument, false);
        publishDocument(policyBundles, filter);
    }


    private void publishDocument(List<PolicyBundle> policyBundles, boolean filter)
    {
        if (filter)
        {
            // get the policy ids stored in the opa store
            List<String> idsInOpaStore = opaClient.listPolicyIds();
            PolicyDocumentUtils.filter(policyBundles, idsInOpaStore);
        }

        if (!CollectionUtils.isEmpty(policyBundles))
        {
            policyBundles.forEach(policyBundle ->
               {
                   disruptorService.publish(policyBundle);
               }
            );

        }
    }


    private boolean hasAssociatedResources(String policyId)
    {
        List<TblIamPolicyVersion> tblIamPolicyVersions = policyRepository.selectPolicyVersionsByPolicyId(policyId);
        if (!CollectionUtils.isEmpty(tblIamPolicyVersions))
        {
            return true;
        }

        return false;
    }



    private boolean checkPolicyNameExists(String policyName, String bpId)
    {
        int count = policyRepository.countByNameAndBpId(policyName, bpId);
        return count > 0 ? true : false;
    }

    private boolean checkPolicyExists(String policyId, String bpId)
    {
        int count = policyRepository.countByIdAndBpId(policyId, bpId);
        return count > 0 ? true : false;
    }

    private boolean checkPolicyVersionExists(String policyId, String versionId)
    {
        int count = policyRepository.countPolicyVersions(policyId, versionId);
        return count > 0 ? true : false;
    }

    public String getVersion(int versionNumber)
    {
        return POLICY_VERSION_PREFIX + versionNumber;
    }

    private String getNextVersion(String version)
    {
        if (version.startsWith(POLICY_VERSION_PREFIX))
        {
            int versionNumber = Integer.parseInt(version.substring(POLICY_VERSION_PREFIX.length())) + 1;
            return POLICY_VERSION_PREFIX + versionNumber;
        }

        log.error("the wrong version number should start with v");
        return version + RandomName.getRandomName(4);
    }


    private String generatePolicyName(AddPolicyReq req, String bpId)
    {
        StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(TblIamPolicy.PolicyType.fromValue(req.getPolicyType()).getShortName());
        if (StringUtils.isNotBlank(bpId))
        {
            nameBuilder.append(SUBTRACT);
            nameBuilder.append(bpId);
        }
        nameBuilder.append(SUBTRACT);
        return genPolicyName(nameBuilder.toString(), 6);
    }

    public String getPolicyName(String policyNamePrefix, int versionNumber)
    {
        return NameGenerator.getName(policyNamePrefix, versionNumber);
    }

    private String getNextPolicyName(String name)
    {
        return NameGenerator.getNextName(name);
    }



    private void getAndDealPolicyBundles(String policyId, String versionId, Consumer<PolicyBundle> consumer)
    {
        List<PolicyBundle> policyBundles = getPolicyBundles(policyId, versionId);
        if (!CollectionUtils.isEmpty(policyBundles))
        {
            policyBundles.forEach(policyBundle ->
            {
                consumer.accept(policyBundle);
            });
        }

    }

    private List<PolicyBundle> getPolicyBundles(String policyId, String versionId)
    {
        // delete policy in opa store
        TblIamPolicyVersion tblIamPolicyVersion = policyRepository.selectPolicyVersionByPolicyIdAndVersionId(policyId, versionId);
        IamPolicyDocument iamPolicyDocument = new IamPolicyDocument(policyId, versionId, tblIamPolicyVersion.getDocument());
        List<PolicyBundle> policyBundles = PolicyDocumentUtils.parseDocument(iamPolicyDocument, true);
        return policyBundles;
    }


    private String genPolicyName(String prefix, int count)
    {
        String policyName = prefix + RandomName.getRandomName(count);
        List<TblIamPolicy> tblIamPolicies = policyRepository.selectByNameAndBase(policyName, false);

        if (!CollectionUtils.isEmpty(tblIamPolicies))
        {
            if (count < 20)
            {
                return genPolicyName(prefix, count + 1);
            }
            else
            {
                log.info("unable to generate appropriate policyName");
                throw new WebSystemException(IAM_GEN_NAME_ERROR, ErrorLevel.ERROR);
            }
        }
        else
        {
            return policyName;
        }

    }

    /**
     * if there is an error return true
     * @param req
     * @return
     */
    private static boolean checkRegoDocError(AddPolicyReq req)
    {
        List<String> regoRules = allRulesInDoc(req.getPolicyDocument());
        if (CollectionUtils.isEmpty(regoRules))
        {
            return true;
        }
        else
        {

            long errorCount = regoRules.stream().filter(regoRule ->
            {
                return RegoSyntaxChecker.regoSyntaxCheckError(regoRule);
            }).count();

            if (errorCount > 0)
            {
                return true;
            }
        }
        return false;
    }
}
