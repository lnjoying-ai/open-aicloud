package com.lnjoying.justice.iam.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.iam.db.model.TblIamProject;
import com.lnjoying.justice.iam.domain.dto.response.project.TblIamProjectDetail;
import com.lnjoying.justice.iam.utils.tree.Tree;
import com.lnjoying.justice.iam.utils.tree.TreeI;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Comparator;
import java.util.Date;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/9 19:27
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IamProject implements TreeI
{
    public static final String DEFAULT_PROJECT_ID = "__default";

    public static final String DEFAULT_PROJECT_NAME = "__default";

    @ApiModelProperty(value="")
    private String id;

    /**
     * project name
     */
    @ApiModelProperty(value="project name")
    private String name;

    @ApiModelProperty(value="")
    private String displayName;

    @ApiModelProperty(value="")
    private String description;

    /**
     * status(1:normal;-1:delete)
     */
    @ApiModelProperty(value="status(1:normal;-1:delete)")
    private Integer status;

    /**
     * enable(1:enable;-1:disable)
     */
    @ApiModelProperty(value="enable(1:enable;-1:disable)")
    private Integer enable;

    @ApiModelProperty(value="")
    private String parentId;

    @ApiModelProperty(value="")
    private String parentName;


    @ApiModelProperty(value="")
    private String parentDisplayName;

    /**
     * create time
     */
    @ApiModelProperty(value="create time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * update time
     */
    @ApiModelProperty(value="update time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value="")
    private String bpId;

    @ApiModelProperty(value="")
    private String userId;

    /**
     * username
     */
    @ApiModelProperty(value = "user name")
    private String userName;

    /**
     * bp name
     */
    @ApiModelProperty(value = "bp name")
    private String bpName;

    public static IamProject of (TblIamProjectDetail tblIamProject)
    {
        IamProject iamProject = new IamProject();
        BeanUtils.copyProperties(tblIamProject, iamProject);
        return iamProject;
    }

    public static class IamProjectComparator implements Comparator<Tree<IamProject>>
    {

        public static final IamProjectComparator DEFAULT_COMPARATOR = new IamProjectComparator();

        @Override
        public int compare(Tree<IamProject> o1, Tree<IamProject> o2)
        {
            return o2.getNodeData().getCreateTime().compareTo(o1.getNodeData().getCreateTime());
        }
    }

    @Override
    public String getTreeId()
    {
        return getId();
    }

    @Override
    public String getTreeParentId()
    {
        return getParentId();
    }

    @Override
    public String getTreeParentName()
    {
        return getParentName();
    }

    @Override
    public String getTreeParentDisplayName()
    {
        return getParentDisplayName();
    }

    @Override
    public String getTreeName()
    {
        return getName();
    }

}
