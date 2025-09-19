package com.lnjoying.justice.ims.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.lnjoying.justice.ims.db.model.TblImsRegistryProject;
import com.lnjoying.justice.ims.harbor.model.Repository;
import com.lnjoying.justice.ims.harbor.model.SearchRepository;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.threeten.bp.OffsetDateTime;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.lnjoying.justice.ims.util.ThreetenUtils.convertFrom;

/**
 * registry repo info
 *
 * @author merak
 **/
@ApiModel(value = "ImsRegistryRepo")
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ImsRegistryRepo
{
    public final static int REPO_NAME_LENGTH = 2;

    private String registryId;

    private String projectId;

    private String projectName;

    /**
     * bp id
     */
    @ApiModelProperty(value = "bp id")
    private String bpId;

    /**
     * user id
     */
    @ApiModelProperty(value = "user id")
    private String userId;

    /**
     * user name
     */
    @ApiModelProperty(value = "user name")
    private String userName;

    /**
     * bp name
     */
    @ApiModelProperty(value = "bp name")
    private String bpName;

    private String repoName;

    /**
     * example: project_name/repo_name
     */
    private String name;

    private String description;

    private String digest;

    @ApiModelProperty(value = "size", notes = "repo size, byte")
    private long size;

    private List<String> labels;

    private Long pullCount;

    private Long artifactCount;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "create time")
    private LocalDateTime createTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "create time")
    private LocalDateTime updateTime;

    public static ImsRegistryRepo of(Repository repository, TblImsRegistryProject registryProject)
    {
        ImsRegistryRepo imsRegistryRepo = new ImsRegistryRepo();
        String name = repository.getName();

        imsRegistryRepo.setRepoName(repoName(name));
        imsRegistryRepo.setName(name);
        imsRegistryRepo.setArtifactCount(repository.getArtifactCount());
        imsRegistryRepo.setPullCount(repository.getPullCount());
        imsRegistryRepo.setCreateTime(convertFrom(repository.getCreationTime().toLocalDateTime()));
        imsRegistryRepo.setUpdateTime(convertFrom(repository.getUpdateTime().toLocalDateTime()));
        imsRegistryRepo.setDescription(repository.getDescription());
        imsRegistryRepo.setRegistryId(registryProject.getRegistryId());
        imsRegistryRepo.setProjectId(registryProject.getProjectId());
        imsRegistryRepo.setProjectName(registryProject.getProjectName());
        imsRegistryRepo.setUserId(registryProject.getUserId());
        imsRegistryRepo.setUserName(registryProject.getUserName());
        imsRegistryRepo.setBpId(registryProject.getBpId());
        imsRegistryRepo.setBpName(registryProject.getBpName());
        return imsRegistryRepo;
    }

    public static ImsRegistryRepo of(SearchRepository repository)
    {
        ImsRegistryRepo imsRegistryRepo = new ImsRegistryRepo();
        String name = repository.getRepositoryName();
        imsRegistryRepo.setRepoName(repoName(name));
        imsRegistryRepo.setName(name);
        imsRegistryRepo.setArtifactCount(repository.getArtifactCount().longValue());
        imsRegistryRepo.setPullCount(repository.getPullCount().longValue());
        return null;
    }

    public static String repoName(String name)
    {
        if (StringUtils.isNotBlank(name))
        {
            String[] split = name.split("/", 2);
            if (split.length == REPO_NAME_LENGTH)
            {
                String repoName = split[1];
                return repoName;
            }
        }
        return "";
    }

    public static String projectName(String name)
    {
        if (StringUtils.isNotBlank(name))
        {
            String[] split = name.split("/", 2);
            if (split.length == REPO_NAME_LENGTH)
            {
                String projectName = split[0];
                return projectName;
            }
        }
        return "";
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        ImsRegistryRepo that = (ImsRegistryRepo) o;
        return registryId.equals(that.registryId) && projectId.equals(that.projectId) && projectName.equals(that.projectName) && repoName.equals(that.repoName);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(registryId, projectId, projectName, repoName);
    }
}
