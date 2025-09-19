package com.lnjoying.justice.iam.db.model;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "tbl_iam_attachment")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblIamAttachment
{
    @ApiModelProperty(value = "")
    private String id;

    @ApiModelProperty(value = "")
    private String projectId;

    @ApiModelProperty(value = "")
    private String projectName;

    @ApiModelProperty(value = "")
    private String principalId;

    /**
     * principal type(1:user;2:group;3:role)
     */
    @ApiModelProperty(value = "principal type(1:user;2:group;3:role)")
    private Integer principalType;

    @ApiModelProperty(value = "")
    private String targetId;

    /**
     * target type(1:policy; 3:role)
     */
    @ApiModelProperty(value = "target type(1:policy; 3:role)")
    private Integer targetType;

    @ApiModelProperty(value = "")
    private String description;

    /**
     * attach time
     */
    @ApiModelProperty(value = "attach time")
    private Date attachTime;

    @ApiModelProperty(value = "")
    private String bpId;

    @ApiModelProperty(value = "")
    private String userId;

    public enum PrincipalType
    {
        USER(1),

        GROUP(2),

        ROLE(3);

        private final int value;

        PrincipalType(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }

        public static PrincipalType fromValue(int value) throws WebSystemException
        {
            switch (value)
            {
                case 1:
                    return USER;
                case 2:
                    return GROUP;
                case 3:
                    return ROLE;
                default:
                    throw new WebSystemException(ErrorCode.ILLEGAL_ARGUMENT, ErrorLevel.INFO);
            }
        }
    }


    public enum TargetType
    {
        POLICY(1),

        ROLE(3);

        private final int value;

        TargetType(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }


    }
}