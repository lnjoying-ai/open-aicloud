package com.lnjoying.justice.iam.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value="tbl_iam_assignment")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblIamAssignment {
    /**
    * userId or groupId
    */
    @ApiModelProperty(value="userId or groupId")
    private String actorId;

    /**
    * projectId
    */
    @ApiModelProperty(value="projectId")
    private String targetId;

    /**
    * type(1:user;2:group)
    */
    @ApiModelProperty(value="type(1:user;2:group)")
    private Integer type;

    public enum AssignmentType
    {
        USER(1),

        GROUP(2);

        private final int value;

        AssignmentType(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }
    }
}