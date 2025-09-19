package com.lnjoying.justice.iam.db.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

@ApiModel(value = "tbl_iam_action")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblIamAction
{
    @ApiModelProperty(value = "")
    private String id;

    @ApiModelProperty(value = "")
    private String projectId;

    @ApiModelProperty(value = "")
    private String actionName;

    /**
     * action type(1:list;2:write;3:read)
     */
    @ApiModelProperty(value = "action type(1:list;2:write;3:read)")
    private Integer actionType;

    /**
     * enable(1:enable;-1:disable)
     */
    @ApiModelProperty(value = "enable(1:enable;-1:disable)")
    private Integer enable;

    @ApiModelProperty(value = "")
    private String description;

    @ApiModelProperty(value = "")
    private String associatedApis;

    /**
     * create time
     */
    @ApiModelProperty(value = "create time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * update time
     */
    @ApiModelProperty(value = "update time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "")
    private String resourceId;

    public enum ActionType
    {
        LIST(1),

        WRITE(2),

        READ(3);

        private final int value;

        ActionType(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }

        public static int getActionType(String operatorId)
        {
            if (StringUtils.isNotBlank(operatorId))
            {
                if (operatorId.contains("list"))
                {
                    return LIST.value();
                }
                else if (operatorId.contains("get"))
                {
                    return READ.value();
                }
                else
                {
                    return WRITE.value();
                }
            }

            return WRITE.value();
        }


        public enum ActionEnableStatus
        {
            ENABLE(1),

            DISABLE(-1);

            private final int value;

            ActionEnableStatus(int value)
            {
                this.value = value;
            }

            public int value()
            {
                return this.value;
            }
        }
    }
}