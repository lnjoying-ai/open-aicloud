package com.lnjoying.justice.iam.authz.opa.common.policy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lnjoying.justice.commonweb.util.Base64Utils;
import com.lnjoying.justice.commonweb.util.JacksonUtils;
import com.lnjoying.justice.iam.db.model.TblIamPolicy;
import com.lnjoying.justice.iam.domain.model.DefaultPolicyParser;
import com.lnjoying.justice.iam.domain.model.IamPolicyDocument;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.lnjoying.justice.iam.db.model.TblIamPolicy.DocumentType.REGO;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/6 15:57
 */

@Slf4j
public final class PolicyDocumentUtils
{

    private static final String SUBTRACT = "-";

    private PolicyDocumentUtils()
    {

    }


    public static List<PolicyBundle> parseDocument(IamPolicyDocument iamPolicyDocument, boolean skipParseRegoDocument)
    {
        List<PolicyBundle> policyBundles = new ArrayList<>();

        if (Objects.nonNull(iamPolicyDocument))
        {
            String policyId = iamPolicyDocument.getPolicyId();
            String versionId = iamPolicyDocument.getVersionId();

            String documentText = iamPolicyDocument.getDocument();

            if (StringUtils.isNotBlank(documentText))
            {
                TblIamPolicy.Document<TblIamPolicy.RegoDocument> document = null;
                try
                {
                    document = JacksonUtils.strToObjType(documentText, new TypeReference<TblIamPolicy.Document<TblIamPolicy.RegoDocument>>(){});
                    String type = document.getType();
                    if (REGO.name().equalsIgnoreCase(type))
                    {
                        AtomicInteger serialNumber = new AtomicInteger(0);
                        document.getStatement().stream().forEach(rule -> {
                            TblIamPolicy.RegoDocument rego = (TblIamPolicy.RegoDocument) rule;
                            PolicyBundle policyBundle = new PolicyBundle();
                            policyBundle.setOpaPolicyId(opaPolicyId(policyId, versionId, serialNumber));
                            policyBundle.setPackageName(rego.getPackageName());
                            String ruleDoc = Base64Utils.decodeToString(rego.getRule());
                            if (!skipParseRegoDocument)
                            {
                                policyBundle.setRego(DefaultPolicyParser.DEFAULT_INSTANCE.rewriteDoc(ruleDoc, iamPolicyDocument.isSystem(), iamPolicyDocument.getBpId()));
                            }
                            policyBundles.add(policyBundle);
                        });
                    }
                }
                catch (IOException e)
                {
                    log.error("parse document failed, error:{}", e);
                }
            }
        }

        return policyBundles;
    }

    public static void filter(List<PolicyBundle> policyBundles, List<String> policyIdsInOpaStore)
    {
        policyBundles.removeIf(policyBundle -> policyIdsInOpaStore.contains(policyBundle.getOpaPolicyId()));
    }


    public static String opaPolicyId(String policyId, String versionId, AtomicInteger serialNumber)
    {
        return policyId + SUBTRACT + versionId + SUBTRACT + serialNumber.incrementAndGet();
    }
}
