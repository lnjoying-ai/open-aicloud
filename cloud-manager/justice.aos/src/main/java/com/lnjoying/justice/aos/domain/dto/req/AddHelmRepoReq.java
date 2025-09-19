package com.lnjoying.justice.aos.domain.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.aos.db.model.TblRepoInfo;
import com.lnjoying.justice.aos.validation.Enum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/5 11:10
 */

@Data
@ApiModel(value = "AddHelmRepoReq")
public class AddHelmRepoReq implements Serializable
{
    public static final String PATTERN_REPO_NAME = "^[a-zA-Z0-9][a-zA-Z0-9-_.@]{0,63}$";

    @ApiModelProperty(value = "repo_name")
    @JsonProperty(value = "repo_name")
    @NotBlank(message = "repo_name must not blank")
    @Pattern(regexp = PATTERN_REPO_NAME, message = " name that starts with a letter or number, can contain numbers, letters and -_.@, length 1-64 bytes")
    private String repoName;

    @ApiModelProperty(value = "repo_url")
    @JsonProperty(value = "repo_url")
    @NotBlank(message = "repo_url must not blank")
    private String repoUrl;

    @ApiModelProperty(value = "authentication method(0:not authenticated;1:basic authentication;3:secret authentication)")
    @JsonProperty(value = "auth_method")
    @Enum(clazz = TblRepoInfo.AuthenticationMethod.class, message = "0:not authenticated;1:basic authentication;3:secret authentication")
    private int authMethod = 0;

    @JsonProperty(value = "basic_auth")
    private BasicAuth basicAuth;

    @JsonProperty(value = "secret_auth")
    private SecretAuth secretAuth;

    @Size(min = 1, message = "access cannot be empty")
    private List<String> access;

    private Map<String, String> labels;


    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class BasicAuth
    {
        private String username;

        private String password;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static final class SecretAuth
    {
        private String cert;

        private String key;

        private String caCert;
    }

    @Override
    public String toString()
    {
        return "AddHelmRepoReq{" +
                "repoName='" + repoName + '\'' +
                ", repoUrl='" + repoUrl + '\'' +
                ", labels=" + labels +
                '}';
    }
}
