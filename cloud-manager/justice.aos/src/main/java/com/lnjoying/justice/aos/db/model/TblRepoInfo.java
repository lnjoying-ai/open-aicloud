package com.lnjoying.justice.aos.db.model;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.lnjoying.justice.schema.common.ErrorCode.AUTH_METHOD_NOT_CORRECT;

@ApiModel(value = "tbl_repo_info")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblRepoInfo
{
    /**
     * repo id
     */
    @ApiModelProperty(value = "repo id")
    private Integer id;

    /**
     * repo name
     */
    @ApiModelProperty(value = "repo name")
    private String repoName;

    /**
     * repo url
     */
    @ApiModelProperty(value = "repo url")
    private String repoUrl;

    /**
     * authentication method(0:not authenticated;1:basic authentication;3:secret authentication)
     */
    @ApiModelProperty(value = "authentication method(0:not authenticated;1:basic authentication;3:secret authentication)")
    private Integer authMethod;

    /**
     * auth info
     */
    @ApiModelProperty(value = "auth info")
    private Object authInfo;

    /**
     * index file hash
     */
    @ApiModelProperty(value = "index file hash")
    private String hash;

    /**
     * status(1: created;2: data sync;3: activation: execute the add operation successfully;4: authentication failed;5: Data download failed)
     */
    @ApiModelProperty(value = "status(1: created;2: data sync;3: activation: execute the add operation successfully;4: authentication failed;5: Data download failed)")
    private Integer status;

    /**
     * label
     */
    @ApiModelProperty(value = "label")
    private Object labels;

    /**
     * access(list, push, remove)
     */
    @ApiModelProperty(value = "access(list, push, remove)")
    private Object access;

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

    public enum AuthenticationMethod
    {
       // authentication method(0:not authenticated;1:basic authentication;3:sercret authentication)
        /**
         * 0:not authenticated
         */
        NOT_AUTHENTICATED(0),

        /**
         * 1:basic authentication
         */
        BASIC_AUTHENTICATION(1),

        /**
         * 3:secret authentication
         */
        SECRET_AUTHENTICATION(3);

        private final int value;

        AuthenticationMethod(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }

        public static AuthenticationMethod fromValue(Integer value)
        {
            return Arrays.stream(AuthenticationMethod.values()).filter(x -> x.value() == value).findFirst().orElseThrow(() -> new WebSystemException(AUTH_METHOD_NOT_CORRECT, ErrorLevel.ERROR));
        }
    }

    public enum AccessType
    {
        /**
         * repo can be accessed by list
         */
        LIST("list"),

        /**
         * repo can import chart
         */
        PUSH("push"),

        /**
         * repo can delete charts
         */
        REMOVE("remove");
        private final String verb;

        public static List<String> accessTypeList = new ArrayList<>();

        static
        {
            accessTypeList = Arrays.stream(AccessType.values()).map(accessType -> accessType.getVerb()).collect(Collectors.toList());
        }

        AccessType(String verb)
        {
            this.verb = verb;
        }

        public String getVerb()
        {
            return verb;
        }

        public static boolean contain(String verb)
        {
            return accessTypeList.contains(verb);
        }
    }

    public enum RepoStatus
    {
        //"status(1: created;2: data sync;3: activation: execute the add operation successfully;4: authentication failed;5: Data download failed)")
        CREATED(1),

        DATA_SYNC(2),

        ACTIVATION(3),

        AUTHENTICATION_FAILED(4),

        DATA_DOWNLOAD_FAILED(5),

        OTHER_ERROR(6);
        private final int value;

        RepoStatus(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return value;
        }
    }
}