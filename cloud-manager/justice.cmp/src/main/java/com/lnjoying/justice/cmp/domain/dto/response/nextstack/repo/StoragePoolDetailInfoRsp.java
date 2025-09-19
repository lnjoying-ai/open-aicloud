package com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo;

import com.lnjoying.justice.cmp.db.model.TblCmpStoragePool;
import com.micro.core.common.Utils;
import lombok.Data;

@Data
public class StoragePoolDetailInfoRsp
{
    private String poolId;

    private String paras;

    private Integer type;

    private String name;

    private String description;

    private String createTime;

    private String updateTime;

    private String sid;

    public void setStoragePoolDetailInfoRsp(TblCmpStoragePool storagePool)
    {
        this.poolId = storagePool.getStoragePoolId();

        this.paras = storagePool.getParas();

        this.type = storagePool.getType();

        this.name = storagePool.getName();

        this.description = storagePool.getDescription();

        this.sid = storagePool.getSid();

        this.createTime = Utils.formatDate(storagePool.getCreateTime());

        this.updateTime = Utils.formatDate(storagePool.getUpdateTime());
    }
}
