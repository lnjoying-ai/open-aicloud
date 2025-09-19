package com.lnjoying.justice.iam.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.iam.authz.opa.common.data.DataBundle;
import com.lnjoying.justice.iam.authz.opa.common.data.DataInputUtils;
import com.lnjoying.justice.iam.authz.opa.common.data.OpaRule;
import com.lnjoying.justice.iam.authz.opa.common.data.OpaUser;
import com.lnjoying.justice.iam.authz.opa.pep.AdHocAccessDecision;
import com.lnjoying.justice.iam.authz.opa.pep.Enforcer;
import com.lnjoying.justice.iam.authz.opa.pep.Resource;
import com.lnjoying.justice.iam.authz.opa.pep.User;
import com.lnjoying.justice.iam.db.model.TblIamAttachment;
import com.lnjoying.justice.iam.db.model.TblIamPolicy;
import com.lnjoying.justice.iam.db.model.TblIamProject;
import com.lnjoying.justice.iam.db.model.TblUserInfo;
import com.lnjoying.justice.iam.db.repo.*;
import com.lnjoying.justice.iam.domain.dto.request.authz.AttachReq;
import com.lnjoying.justice.iam.domain.dto.request.authz.CheckActionsReq;
import com.lnjoying.justice.iam.domain.dto.request.authz.DetachReq;
import com.lnjoying.justice.iam.domain.dto.response.authz.AttachmentsRsp;
import com.lnjoying.justice.iam.domain.dto.response.authz.CheckActionsRsp;
import com.lnjoying.justice.iam.domain.dto.response.authz.RoleAttachmentsRsp;
import com.lnjoying.justice.iam.domain.dto.response.policy.TblIamPolicyDetail;
import com.lnjoying.justice.iam.domain.model.ActionsCache;
import com.lnjoying.justice.iam.domain.model.DefaultPolicyParser;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.common.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.lnjoying.justice.iam.authz.opa.pap.rego.Effect.ALLOW;
import static com.lnjoying.justice.iam.authz.opa.pap.rego.Effect.DENY;
import static com.lnjoying.justice.iam.db.model.TblIamAttachment.PrincipalType.*;
import static com.lnjoying.justice.iam.db.model.TblIamAttachment.TargetType.POLICY;
import static com.lnjoying.justice.iam.utils.ServiceCombRequestUtils.*;
import static com.lnjoying.justice.schema.common.ErrorCode.*;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/14 15:44
 */

@Slf4j
@Service
public class AuthzService
{

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthzRepository authzRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DisruptorService disruptorService;

    @Autowired
    private Enforcer opaEnforcer;

    @Autowired
    private ActionsCache actionsCache;

    @Autowired
    private ProjectRepository projectRepository;

    @Transactional(rollbackFor = Exception.class)
    public void addAttachments(AttachReq req)
    {
        List<String> policyIds = req.getPolicyIds();
        List<Long> roleIds = req.getRoleIds();
        checkTargets(policyIds, roleIds);

        List<TblIamAttachment> attachments = Lists.newArrayList();
        // todo check more userid
        if (!CollectionUtils.isEmpty(policyIds))
        {
            checkPolicyIdsExist(policyIds);

            List<TblIamAttachment> attachmentsFromPolicyIds = buildAttachmentsFromPolicyIds(req, policyIds);
            attachments.addAll(attachmentsFromPolicyIds);

            publishAuthzData(req.getProjectId(), req.getPrincipalType(), req.getPrincipalId(), policyIds);
        }

        // principal type is user or group
        if (req.getPrincipalType() != ROLE.value() && !CollectionUtils.isEmpty(roleIds))
        {
            checkRoleIdsExist(roleIds);
            List<Long> existRoleIds = authzRepository.selectAllRoleIdsByAttachments(req.getProjectId(), req.getPrincipalType(), req.getPrincipalId());
            List<TblIamAttachment> attachmentsFromRoleIds = buildAttachmentsFromRoleIds(req, roleIds);
            attachments.addAll(attachmentsFromRoleIds);

            setUserKind(req.getPrincipalType(), req.getPrincipalId(), roleIds);
            // publish data

            List<Long> allRoleIds = new ArrayList<>();
            if (!CollectionUtils.isEmpty(existRoleIds))
            {
                allRoleIds.addAll(existRoleIds);
            }
            allRoleIds.addAll(roleIds);
            publishRoleData(req.getProjectId(), req.getPrincipalId(), allRoleIds);

        }

        authzRepository.batchInsert(attachments);
    }


    @Transactional(rollbackFor = Exception.class)
    public void deleteAttachments(DetachReq req)
    {
        List<String> policyIds = req.getPolicyIds();
        List<Long> roleIds = req.getRoleIds();
        checkTargets(policyIds, roleIds);

        try
        {
            if (!CollectionUtils.isEmpty(policyIds))
            {
                policyIds.stream().parallel().forEach(policyId -> {
                    TblIamAttachment tblIamAttachment = authzRepository.selectOne(req.getProjectId(), req.getPrincipalId(), req.getPrincipalType(), policyId, POLICY.value());
                    if (Objects.nonNull(tblIamAttachment))
                    {
                        authzRepository.deleteByPrimaryKey(tblIamAttachment.getId());

                        TblIamPolicy tblIamPolicy = policyRepository.selectByPrimaryKey(policyId);
                        if (Objects.nonNull(tblIamPolicy))
                        {
                            Integer attachmentCount = tblIamPolicy.getAttachmentCount();
                            int count = Objects.nonNull(attachmentCount) ? attachmentCount.intValue() : 0;
                            boolean attachable = count -1 > 0 ? true : false;
                            tblIamPolicy.setAttachable(attachable);
                            policyRepository.updateByPrimaryKeySelective(tblIamPolicy);
                        }
                    }
                    unPublishAuthzData(req, policyId);
                });
            }

            // principal type is user or group
            if (req.getPrincipalType() != ROLE.value() && !CollectionUtils.isEmpty(roleIds))
            {

                List<Long> allRoleIds = authzRepository.selectAllRoleIdsByAttachments(req.getProjectId(), req.getPrincipalType(), req.getPrincipalId());
                HashSet<Long> allRoleIdsSet = Sets.newHashSet(allRoleIds);
                HashSet<Long> deletedRoleIdsSet = Sets.newHashSet(roleIds);
                roleIds.stream().parallel().forEach(roleId -> {
                    TblIamAttachment tblIamAttachment = authzRepository.selectOne(req.getProjectId(), req.getPrincipalId(), req.getPrincipalType(), String.valueOf(roleId), ROLE.value());
                    if (Objects.nonNull(tblIamAttachment))
                    {
                        authzRepository.deleteByPrimaryKey(tblIamAttachment.getId());
                    }
                });

                // setting the role that is not deleted
                Sets.SetView<Long> difference = Sets.difference( allRoleIdsSet, deletedRoleIdsSet);
                setUserKind(req.getPrincipalType(), req.getPrincipalId(), Lists.newArrayList(difference));
                if (!difference.isEmpty())
                {
                    List<Long> notDeletedRoleIds = difference.stream().collect(Collectors.toList());
                    publishRoleData(req.getProjectId(), req.getPrincipalId(), notDeletedRoleIds);
                }

            }
        }
        catch (Exception e)
        {
            log.error("delete  attachments , type: {}, error: {}", req.getPrincipalType(), e);
            throw new WebSystemException(IAM_AUTHZ_DETACH_ERROR, ErrorLevel.ERROR);
        }

    }


    public AttachmentsRsp getAttachmentList(String policyName, String policyType, String queryBpId, String queryUserId, String groupId, String roleId,String projectId,
                                            Integer pageSize, Integer pageNum)
    {
        return authzRepository.getAttachmentList(policyName, policyType, queryBpId, queryUserId, groupId, roleId, projectId, pageSize, pageNum);
    }


    public RoleAttachmentsRsp getRoleAttachmentList(String roleName, String roleType, String queryBpId, String queryUserId, String groupId, String projectId, Integer pageSize, Integer pageNum)
    {
        return authzRepository.getRoleAttachmentList(roleName, roleType, queryBpId, queryUserId, groupId, projectId, pageSize, pageNum);
    }

    private  List<TblIamAttachment> buildAttachmentsFromRoleIds(AttachReq req, List<Long> roleIds)
    {
        List<TblIamAttachment> tblIamAttachments = roleIds.stream().map(roleId ->
        {
            TblIamAttachment tblIamAttachment = new TblIamAttachment();
            tblIamAttachment.setId(Utils.assignUUId());
            tblIamAttachment.setPrincipalType(req.getPrincipalType());
            tblIamAttachment.setPrincipalId(req.getPrincipalId());
            tblIamAttachment.setTargetId(String.valueOf(roleId));
            tblIamAttachment.setTargetType(TblIamAttachment.TargetType.ROLE.value());
            tblIamAttachment.setProjectId(req.getProjectId());
            tblIamAttachment.setProjectName(req.getProjectName());
            tblIamAttachment.setDescription(req.getDescription());
            tblIamAttachment.setBpId(req.getBpId());
            tblIamAttachment.setUserId(req.getUserId());
            return tblIamAttachment;
        }).collect(Collectors.toList());
        return tblIamAttachments;
    }


    public CheckActionsRsp checkActions(CheckActionsReq checkActionsReq)
    {
        CheckActionsRsp checkActionsRsp = new CheckActionsRsp();
        List<String> actionsReq = checkActionsReq.getActions();

        List<String> allPermittedActions = getAllPermittedActions(actionsReq);
        if (!CollectionUtils.isEmpty(allPermittedActions))
        {
            List<CheckActionsRsp.ActionDecision> actionDecisions = actionsReq.stream().map(action ->
            {
                CheckActionsRsp.ActionDecision actionDecision = new CheckActionsRsp.ActionDecision();
                actionDecision.setAction(action);
                if (isContains(allPermittedActions, action))
                {
                    actionDecision.setDecision(ALLOW.name());
                }
                else
                {
                    actionDecision.setDecision(DENY.name());
                }

                return actionDecision;
            }).collect(Collectors.toList());
            checkActionsRsp.setActions(actionDecisions);
        }
        else
        {
            List<CheckActionsRsp.ActionDecision> actionDecisions = actionsReq.stream().map(action ->
            {
                CheckActionsRsp.ActionDecision actionDecision = new CheckActionsRsp.ActionDecision();
                actionDecision.setAction(action);
                actionDecision.setDecision(DENY.name());
                return actionDecision;
            }).collect(Collectors.toList());
            checkActionsRsp.setActions(actionDecisions);
        }

        return checkActionsRsp;

    }

    private static boolean isContains(List<String> allPermittedActions, String action)
    {
        boolean result = allPermittedActions.stream().map(permittedAction ->
        {
            if (StringUtils.isNotBlank(permittedAction))
            {
                if (!permittedAction.endsWith("*"))
                {
                    return permittedAction.equalsIgnoreCase(action);
                }
                else
                {
                    String prefix = permittedAction.substring(0, permittedAction.length() - 1);
                    return StringUtils.startsWithIgnoreCase(action, prefix);
                }
            }
            return false;
        }).anyMatch(permit -> permit.booleanValue());
        return result;
    }

    private List<String> getAllPermittedActions(List<String> actionsReq)
    {
        List<String> allPermittedActions = new ArrayList<>();
        User user = buildUser();
        Set<String> resourceTypes = actionsReq.stream().map(action ->
        {
            ActionsCache.ServiceAction serviceAction = actionsCache.get(action);
            return serviceAction.getResourceType();
        }).collect(Collectors.toSet());

        if (!CollectionUtils.isEmpty(resourceTypes))
        {
            List<CompletableFuture<AdHocAccessDecision>> completableFutures = resourceTypes.stream().map(resourceType -> CompletableFuture.supplyAsync(() ->
            {
                Resource resource = buildResource(resourceType);
                AdHocAccessDecision adHocAccessDecision = opaEnforcer.query(user, resource);
                return adHocAccessDecision;
            }).exceptionally(err ->
            {
                log.error("opa ad hoc query failed:{}", err);
                return null;
            })).filter(Objects::nonNull).collect(Collectors.toList());

            List<AdHocAccessDecision> adHocAccessDecisions = completableFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(adHocAccessDecisions))
            {
                List<String> permittedActions = adHocAccessDecisions.stream().map(adHocAccessDecision ->
                {
                    List<Map<String, List<String>>> result = adHocAccessDecision.getResult();
                    if (!CollectionUtils.isEmpty(result))
                    {
                        List<String> collect = result.stream().flatMap(map -> map.values().stream()).flatMap(Collection::stream).collect(Collectors.toList());
                        return collect;
                    }
                    return null;
                }).filter(Objects::nonNull).flatMap(Collection::stream).collect(Collectors.toList());

                if (!CollectionUtils.isEmpty(permittedActions))
                {
                    allPermittedActions.addAll(permittedActions);
                }
            }
        }
        return allPermittedActions;
    }

    private  List<TblIamAttachment> buildAttachmentsFromPolicyIds(AttachReq req, List<String> policyIds)
    {
        List<TblIamAttachment> tblIamAttachments = policyIds.stream().map(policyId ->
        {
            TblIamAttachment tblIamAttachment = new TblIamAttachment();
            tblIamAttachment.setId(Utils.assignUUId());
            tblIamAttachment.setPrincipalType(req.getPrincipalType());
            tblIamAttachment.setPrincipalId(req.getPrincipalId());
            tblIamAttachment.setTargetId(policyId);
            tblIamAttachment.setTargetType(POLICY.value());
            tblIamAttachment.setProjectId(req.getProjectId());
            tblIamAttachment.setProjectName(req.getProjectName());
            tblIamAttachment.setDescription(req.getDescription());
            tblIamAttachment.setBpId(req.getBpId());
            tblIamAttachment.setUserId(req.getUserId());
            return tblIamAttachment;
        }).collect(Collectors.toList());
        return tblIamAttachments;
    }


    private void checkPolicyIdsExist(List<String> policyIds)
    {
        int count = policyRepository.countPolicyIds(policyIds);
        if (policyIds.size() != count)
        {
            log.error("some policy ids do not exist");
            throw new WebSystemException(IAM_AUTHZ_SOME_POLICY_IDS_NOT_EXIST, ErrorLevel.ERROR);
        }
    }


    private void checkRoleIdsExist(List<Long> roleIds)
    {
        int count = roleRepository.countRoleIds(roleIds);
        if (roleIds.size() != count)
        {
            log.error("some role ids do not exist");
            throw new WebSystemException(IAM_AUTHZ_SOME_ROLE_IDS_NOT_EXIST, ErrorLevel.ERROR);
        }
    }


    private void checkTargets(List<String> policyIds, List<Long> roleIds)
    {
        if (CollectionUtils.isEmpty(policyIds) && CollectionUtils.isEmpty(roleIds))
        {
            log.error("missing policyIds or roleIds any one");
            throw new WebSystemException(IAM_AUTHZ_MISSING_TARGET, ErrorLevel.ERROR);
        }
    }

    private String getProject(String projectId)
    {
        return null;
    }




    public void publishAuthzData(String projectId, int  principalType, String principalId, List<String> policyIds)
    {

        policyIds.forEach(policyId -> {
            TblIamPolicyDetail tblIamPolicyDetail = policyRepository.selectByIdAndBpId(policyId, null);
            List<String> ruleNames = new ArrayList<>();
            if (Objects.nonNull(tblIamPolicyDetail))
            {
               ruleNames.addAll(DefaultPolicyParser.DEFAULT_INSTANCE.extractConditionRuleNames(tblIamPolicyDetail.getDocument()));
            }

            if (USER.value() == principalType)
            {
                String userId = principalId;
                String defaultProjectId = getDefaultProjectIdByUserId(projectId, userId);
                publishRuleData(userId, defaultProjectId, policyId, ruleNames, OpaRule.RuleType.USER);
            }
            else if (ROLE.value() == principalType)
            {
                String roleId = principalId;
                publishRuleData(roleId, null, policyId, ruleNames,  OpaRule.RuleType.ROLE);
            }
        });



       // attachments.stream().forEach(attachment -> {disruptorManager.publish(attachment, POLICY_TYPE);});
    }

    private void unPublishAuthzData(DetachReq req, String policyId)
    {
        if (USER.value() == req.getPrincipalType())
        {
            String userId = req.getPrincipalId();
            String defaultProjectId = getDefaultProjectIdByUserId(req.getProjectId(), userId);
            publishRuleData(userId, defaultProjectId, policyId, Collections.emptyList(), OpaRule.RuleType.USER);
        }
        else if (ROLE.value() == req.getPrincipalType())
        {
            String roleId = req.getPrincipalId();
            publishRuleData(roleId, null, policyId,  Collections.emptyList(),  OpaRule.RuleType.ROLE);
        }
    }

    public void publishRuleData(String key, String projectId, String policyId, List<String> ruleNames, OpaRule.RuleType type)
    {

        OpaRule opaRule = new OpaRule();
        opaRule.setType(type);
        opaRule.setKey(key);
        opaRule.setProjectId(projectId);
        opaRule.setPolicyId(policyId);
        opaRule.setRules(ruleNames);
        DataBundle dataBundle = DataInputUtils.parseInput(opaRule);
        if (Objects.nonNull(dataBundle))
        {
            disruptorService.publish(dataBundle);
        };

    }

    public void publishRoleData(String projectIdReq, String principalId, List<Long> roleIds)
    {
        if (!CollectionUtils.isEmpty(roleIds))
        {

            String userId = principalId;
            TblUserInfo userInfo = userRepository.getUserById(userId);
            OpaUser opaUser = new OpaUser();
            opaUser.setUserKey(userId);
            OpaUser.UserAttribute userAttribute = new OpaUser.UserAttribute();
            userAttribute.setId(userId);
            userAttribute.setEmail(userInfo.getEmail());
            opaUser.setAttributes(userAttribute);

            Map<String, List<String>> roles = new HashMap<>();
            String projectId = StringUtils.isNotBlank(projectIdReq) ? projectIdReq : getDefaultProjectIdByUserId(null, userId);
            roles.put(projectId, roleIds.stream().map(String::valueOf).collect(Collectors.toList()));
            opaUser.setRoleAssignments(roles);
            DataBundle dataBundle = DataInputUtils.parseInput(opaUser);
            if (Objects.nonNull(dataBundle))
            {
                disruptorService.publish(dataBundle);
            }
        }
        
    }

    private User buildUser()
    {
        User user = new User();
        user.setKey(getUserId());
        Map<String, String> userAttributes = new HashMap<>();
        userAttributes.put("bpId", getBpId());
        userAttributes.put("bpName", getBpName());
        userAttributes.put("userKind", getKind());
        userAttributes.put("userName", getUserName());
        user.setAttributes(userAttributes);
        return user;
    }

    private Resource buildResource(String type)
    {
        Resource resource = new Resource();
        resource.setType(type);
        return resource;
    }

    /**
     * Use the default project id when there is no project Id in req
     * @param projectId
     * @param userId
     * @return
     */
    private String getDefaultProjectIdByUserId(String projectId, String userId)
    {
        if (StringUtils.isNotBlank(projectId))
        {
            return projectId;
        }


        TblIamProject tblIamProject = projectRepository.selectDefaultProject(null, userId);
        if (Objects.nonNull(tblIamProject))
        {
            projectId = tblIamProject.getId();
        }
        else
        {
            log.error("default project not exist, userId:{}", userId);
            throw new WebSystemException(Project_Not_Exist, ErrorLevel.ERROR);
        }

        return projectId;
    }


    private void setUserKind( Integer principalType, String principalId, List<Long> roleIds)
    {
        if (USER.value() == principalType)
        {
            Integer kind = roleRepository.selectKindByRoleIds(roleIds);
            if (Objects.nonNull(kind) && kind.intValue() != -1)
            {
                if (!isAdmin())
                {
                    // smaller the greater the permissions
                    if (kind < getKindValue())
                    {
                        throw new WebSystemException(IAM_AUTHZ_ATTACH_ERROR, ErrorLevel.ERROR);
                    }
                }
                TblUserInfo tblUserInfo = new TblUserInfo();
                tblUserInfo.setUserId(principalId);
                tblUserInfo.setKind(kind);
                userRepository.updateByPrimaryKeySelective(tblUserInfo);
            }
        }
    }

}
