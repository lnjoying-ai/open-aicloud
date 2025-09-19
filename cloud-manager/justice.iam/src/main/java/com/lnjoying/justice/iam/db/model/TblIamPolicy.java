package com.lnjoying.justice.iam.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;
import com.lnjoying.justice.commonweb.util.Base64Utils;
import com.lnjoying.justice.commonweb.util.JacksonUtils;
import com.lnjoying.justice.iam.authz.opa.common.policy.PolicyBundle;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.lnjoying.justice.iam.db.model.TblIamPolicy.DocumentType.REGO;

@ApiModel(value = "tbl_iam_policy")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblIamPolicy
{
    @ApiModelProperty(value = "")
    private String id;

    @ApiModelProperty(value = "")
    private String name;

    @ApiModelProperty(value = "")
    private String description;

    @ApiModelProperty(value = "")
    @ResourceInstanceName
    private String displayName;

    /**
     * policy type(1:system;2:custom;)
     */
    @ApiModelProperty(value = "policy type(1:system;2:custom;)")
    private Integer policyType;

    @ApiModelProperty(value = "")
    private Boolean autogen;

    @ApiModelProperty(value = "")
    private String defaultVersion;

    @ApiModelProperty(value = "")
    private Integer attachmentCount;

    @ApiModelProperty(value = "")
    private Boolean attachable;

    @ApiModelProperty(value = "")
    private Object tags;

    @ApiModelProperty(value = "")
    private String arn;

    /**
     * create time
     */
    @ApiModelProperty(value = "create time")
    private Date createTime;

    /**
     * update time
     */
    @ApiModelProperty(value = "update time")
    private Date updateTime;

    @ApiModelProperty(value = "")
    private String bpId;

    @ApiModelProperty(value = "")
    private String userId;

    @ApiModelProperty(value = "")
    private Boolean base;

    public enum PolicyType
    {
        SYSTEM(1, "s"),

        CUSTOM(2, "c");

        private final int value;

        public String getShortName()
        {
            return shortName;
        }

        private final String shortName;

        PolicyType(int value, String shortName)
        {
            this.value = value;
            this.shortName = shortName;
        }

        public int value()
        {
            return this.value;
        }

        public static PolicyType fromValue(int value) throws WebSystemException
        {
            switch (value)
            {
                case 1:
                    return SYSTEM;
                case 2:
                    return CUSTOM;
                default:
                    throw new WebSystemException(ErrorCode.ILLEGAL_ARGUMENT, ErrorLevel.INFO);
            }
        }
    }


    @Data
    public static final class Document<T>
    {
        private String version;

        private String type;

        private List<T> statement;
    }

    public enum DocumentType
    {
        REGO,

        JSON;
    }

    @Data
    public static final class RegoDocument
    {
        @JsonProperty("package")
        private String packageName;

        private String rule;
    }

    /**
     * get all rules from rego doc
     * @param regoDoc
     * @return all rules
     */
    public static List<String> allRulesInDoc(String regoDoc)
    {
        List<String> regoRules = new ArrayList<String>();
        Document<RegoDocument> document = JacksonUtils.strToObjTypeDefault(regoDoc, new TypeReference<Document<RegoDocument>>(){});
        if (Objects.isNull(document))
        {
            return regoRules;
        }

        String type = document.getType();
        if (REGO.name().equalsIgnoreCase(type))
        {
            document.getStatement().stream().forEach(rule -> {
                TblIamPolicy.RegoDocument rego = (TblIamPolicy.RegoDocument) rule;
                String ruleDoc = Base64Utils.decodeToString(rego.getRule());
                regoRules.add(ruleDoc);
            });
        }

        return regoRules;
    }


}