package com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class InstanceGroupsBaseRsp
{
    private long totalNum;

    private List<InstanceGroupInfo> instanceGroupInfos;

    @Data
    public static class InstanceGroupInfo
    {
        private String instanceGroupId;

        private String name;

        private String description;

        private long instanceCount;

        @JsonFormat(shape=JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
        private Date createTime;
    }
}
