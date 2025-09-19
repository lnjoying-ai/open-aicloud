package com.lnjoying.justice.ims.db.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.*;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import static com.lnjoying.justice.ims.db.model.TblImsRegistryPreDownload.PreDownloadStatus.CREATING;

@ApiModel(value = "TblImsRegistryPreDownload")
@Data
@Builder
@ToString
public class TblImsRegistryPreDownload
{
    /**
     * id
     */
    @ApiModelProperty(value = " id")
    private String id;

    /**
     * registry id
     */
    @ApiModelProperty(value = "registry id")
    private String registryId;

    /**
     * node id
     */
    @ApiModelProperty(value = "node id")
    private String nodeId;

    /**
     * repos,separated by comma discard
     * use single repo
     */
    @ApiModelProperty(value = "repos,separated by comma")
    private String repos;

    /**
     * record status(1:creating;2:downloading;3:completed
     * )
     */
    @ApiModelProperty(value = "record status(1:creating;2:downloading;3:completed,)")
    private Integer status;

    /**
     * user id
     */
    @ApiModelProperty(value = "user id")
    private String userId;

    /**
     * bp id
     */
    @ApiModelProperty(value = "bp id")
    private String bpId;

    /**
     * create time
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "create time")
    private LocalDateTime createTime;

    /**
     * update time
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "update time")
    private LocalDateTime updateTime;

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

    public enum PreDownloadStatus
    {
        /**
         * creating :before asynchronous sending
         */
        CREATING(10),

        /**
         * downloading :after asynchronous sending
         */
        DOWNLOADING(11),

        /**
         * success : download status
         */
        Success(0),

        /**
         * auth failed : download status
         */
        AUTH_FAILED(1),

        /**
         * pull failed : download status
         */
        PULL_FAILED(2),

        /**
         * push failed : download status
         */
        PUSH_FAILED(3),

        /**
         * image not exist : download status
         */
        IMAGE_NOT_EXIST(4),

        /**
         * server is unreachable : download status
         */
        SERVER_IS_UNREACHABLE(5);

        private final int value;

        PreDownloadStatus(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }
    }

    public enum SimplePreDownloadStatus
    {
        /**
         * downloading
         */
        DOWNLOADING(100, "downloading", "下载中"),

        /**
         * download completed
         */
        DOWNLOAD_COMPLETED(101, "download completed", "下载完成"),

        /**
         * download failed
         */
        DOWNLOAD_FAILED(102, "download failed", "下载失败");

        SimplePreDownloadStatus(int code, String enName, String cnName)
        {
            this.code = code;
            this.enName = enName;
            this.cnName = cnName;
        }


        private final int code;

        private final String enName;

        private final String cnName;

        public static Map<SimplePreDownloadStatus, List<Integer>> simpleMap = new HashMap<>();

        /**
         * key = full status --> value == simple status
         */
        public static Map<Integer, SimplePreDownloadStatus> fullMap = new HashMap<>();

        static
        {
            List<Integer> downloadingList = Lists.newArrayList(PreDownloadStatus.CREATING.value(), PreDownloadStatus.DOWNLOADING.value());
            simpleMap.put(DOWNLOADING, downloadingList);

            List<Integer> downloadCompletedList = Lists.newArrayList(PreDownloadStatus.Success.value());
            simpleMap.put(DOWNLOAD_COMPLETED, downloadCompletedList);

            List<Integer> downloadFailedList = Lists.newArrayList(PreDownloadStatus.AUTH_FAILED.value(), PreDownloadStatus.PULL_FAILED.value(),
                    PreDownloadStatus.PUSH_FAILED.value(), PreDownloadStatus.IMAGE_NOT_EXIST.value(), PreDownloadStatus.SERVER_IS_UNREACHABLE.value());
            simpleMap.put(DOWNLOAD_FAILED, downloadFailedList);

            simpleMap.entrySet().stream().forEach(entry -> {
                List<Integer> values = entry.getValue();
                values.stream().forEach(value -> {
                    int key = entry.getKey().code;
                    fullMap.put(value, fromValue(key));
                });
            });
        }

        public static SimplePreDownloadStatus fromValue(int code)
        {
            return Arrays.stream(SimplePreDownloadStatus.values()).filter(x -> x.getCode() == code).findFirst().get();
        }

        /**
         * input full status , output simple status
         * @param status
         * @return
         */
        public static SimplePreDownloadStatus getSimpleStatus(int status)
        {
            return fullMap.get(status);
        }


        public  Map<String, String> toMap()
        {
            Map<String, String> res = new HashMap<>();
            res.put("en", this.getEnName());
            res.put("cn", this.getCnName());
            return res;
        }

        public int getCode()
        {
            return code;
        }

        public String getEnName()
        {
            return enName;
        }

        public String getCnName()
        {
            return cnName;
        }
    }


}