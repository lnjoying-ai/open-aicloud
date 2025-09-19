package com.lnjoying.justice.schema.entity.cmp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcGpuVmInstance
{
    private String instanceId;
    private String instanceName;
    private Integer chargeType;
    private Date createTime;
    private Date expireTime;
    private String bpId;
    private String bpName;
    private String userId;
    private String userName;
    private String gpuName;
    private Integer gpuCount;
    private BigDecimal duration;
}
