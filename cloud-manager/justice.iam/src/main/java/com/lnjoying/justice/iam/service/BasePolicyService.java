package com.lnjoying.justice.iam.service;

import com.lnjoying.justice.commonweb.util.BaseConfigPathUtils;
import com.lnjoying.justice.commonweb.util.FileUtils;
import com.lnjoying.justice.commonweb.util.JacksonUtils;
import com.lnjoying.justice.iam.db.model.TblIamPolicy;
import com.lnjoying.justice.iam.db.model.TblIamPolicyVersion;
import com.lnjoying.justice.iam.db.repo.PolicyRepository;
import com.lnjoying.justice.iam.utils.TimeUtils;
import com.micro.core.common.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.lnjoying.justice.commonweb.util.FileUtils.FILE_SEPARATOR;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/16 19:08
 */

@Slf4j
@Service
public class BasePolicyService
{
    public static final String BASE_POLICY_NAME = "iam-base-policy";

    public static final String BASE_POLICY_PATH = "base";

    @Autowired
    private PolicyService policyService;

    @Autowired
    private PolicyRepository policyRepository;

    public void checkBasePolicy()
    {
        try
        {
            List<TblIamPolicy> tblIamPolicies = policyService.selectBasePoliciesByName(BASE_POLICY_NAME);
            if (!CollectionUtils.isEmpty(tblIamPolicies))
            {
                return;
            }

            String basePath = BaseConfigPathUtils.getRegoConfigPath() + BASE_POLICY_PATH;
            if (!Files.exists(Paths.get(basePath)))
            {
                log.error("no base policy files found");
            }

            Map<String, String> files = FileUtils.listFilesContentWithBase64Encode(basePath, true);

            if (!CollectionUtils.isEmpty(files))
            {
                TblIamPolicy policy = createPolicy();
                createVersion(policy, files);
            }

        }
        catch(Exception e)
        {
            log.error("check base policies error: {}", e);
        }

    }

    private void createVersion(TblIamPolicy policy, Map<String, String> files)
    {
        TblIamPolicyVersion tblIamPolicyVersion = new TblIamPolicyVersion();
        tblIamPolicyVersion.setPolicyId(policy.getId());
        tblIamPolicyVersion.setCreateTime(new Date());
        tblIamPolicyVersion.setUpdateTime(tblIamPolicyVersion.getCreateTime());
        tblIamPolicyVersion.setVersionId(policy.getDefaultVersion());

        TblIamPolicy.Document<TblIamPolicy.RegoDocument> document = new TblIamPolicy.Document<>();
        document.setVersion(TimeUtils.date());
        document.setType(TblIamPolicy.DocumentType.REGO.name());
        List<TblIamPolicy.RegoDocument> regoDocuments = new ArrayList<>();
        files.forEach((path, rule) -> {
            TblIamPolicy.RegoDocument regoDocument = new TblIamPolicy.RegoDocument();
            regoDocument.setPackageName(path.replace(FILE_SEPARATOR, "."));
            regoDocument.setRule(rule);
            regoDocuments.add(regoDocument);
        });
        document.setStatement(regoDocuments);
        tblIamPolicyVersion.setDocument(JacksonUtils.objToStrDefault(document));
        policyRepository.insertPolicyVersionSelective(tblIamPolicyVersion);
    }

    private TblIamPolicy createPolicy()
    {
        TblIamPolicy tblIamPolicy = new TblIamPolicy();
        tblIamPolicy.setAutogen(true);
        tblIamPolicy.setAttachable(false);
        tblIamPolicy.setAttachmentCount(0);
        tblIamPolicy.setId(Utils.assignUUId());
        tblIamPolicy.setName(BASE_POLICY_NAME);
        tblIamPolicy.setDefaultVersion(policyService.getVersion(1));
        tblIamPolicy.setBase(true);
        tblIamPolicy.setDescription("base policies for system");
        tblIamPolicy.setPolicyType(TblIamPolicy.PolicyType.SYSTEM.value());
        tblIamPolicy.setCreateTime(new Date());
        tblIamPolicy.setUpdateTime(tblIamPolicy.getCreateTime());
        policyRepository.insertSelective(tblIamPolicy);
        return tblIamPolicy;
    }


}
