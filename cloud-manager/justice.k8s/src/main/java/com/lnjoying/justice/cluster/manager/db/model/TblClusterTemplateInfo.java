package com.lnjoying.justice.cluster.manager.db.model;

import com.lnjoying.justice.cluster.manager.common.ClsResources;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;
import com.lnjoying.justice.commonweb.mapper.MapperModel;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

@ApiModel(value = "TblClusterTemplateInfo")
public class TblClusterTemplateInfo implements Serializable {
    private String id;

    @ResourceInstanceName
    private String name;

    private String description;

    private String clusterId;

    private String members;

    private Integer type;

    private String clusterType;

    private String tags;

    private String owner;

    private String bp;

    private String creator;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId == null ? null : clusterId.trim();
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members == null ? null : members.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getClusterType() {
        return clusterType;
    }

    public void setClusterType(String clusterType) {
        this.clusterType = clusterType == null ? null : clusterType.trim();
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags == null ? null : tags.trim();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner == null ? null : owner.trim();
    }

    public String getBp() {
        return bp;
    }

    public void setBp(String bp) {
        this.bp = bp == null ? null : bp.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public enum TemplateType{

        /**
         * created template
         */
        CREATED_TEMPLATE(0, "created template"),

        /**
         * templates exported from user created clusters
         */
        FROM_CLUSTER(1, "templates exported from user created clusters");

        private int value;

        private String name;

        TemplateType(int value, String name)
        {
            this.value = value;
            this.name = name;
        }

        public int getValue()
        {
            return value;
        }

        public String getName()
        {
            return name;
        }

        public static int fromName(String name)
        {
            return Arrays.stream(TemplateType.values()).filter(templateType -> templateType.getName().equals(name))
                    .findFirst().orElseGet(() -> CREATED_TEMPLATE).getValue();
        }
    }

    public static class ClusterTemplateVersion {

        private String versionId;

        private String version;

        private String templateId;

        private String templateName;

        public String getVersion()
        {
            return version;
        }

        public void setVersion(String version)
        {
            this.version = version;
        }

        public String getTemplateName()
        {
            return templateName;
        }

        public void setTemplateName(String templateName)
        {
            this.templateName = templateName;
        }

        public String getVersionId()
        {
            return versionId;
        }

        public void setVersionId(String versionId)
        {
            this.versionId = versionId;
        }

        public String getTemplateId()
        {
            return templateId;
        }

        public void setTemplateId(String templateId)
        {
            this.templateId = templateId;
        }
    }
}