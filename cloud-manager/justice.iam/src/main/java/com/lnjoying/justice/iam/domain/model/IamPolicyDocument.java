package com.lnjoying.justice.iam.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/6 16:26
 */

@Data
public class IamPolicyDocument
{
    private String policyId;

    private String versionId;

    private String document;

    /**
     * whether it is a system policy
     */
    private boolean system;

    private String bpId;

    public IamPolicyDocument()
    {

    }

    public IamPolicyDocument( String policyId, String versionId, String document)
    {
        this.policyId = policyId;
        this.versionId = versionId;
        this.document = document;
    }

    public IamPolicyDocument(String policyId, String versionId, String document, boolean system, String bpId)
    {
        this.policyId = policyId;
        this.versionId = versionId;
        this.document = document;
        this.system = system;
        this.bpId = bpId;
    }
}
