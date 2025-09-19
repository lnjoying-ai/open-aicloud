package com.lnjoying.justice.cmp.domain.info.baremetal;

import com.lnjoying.justice.cmp.db.model.TblCmpPubkey;
import com.micro.core.common.Utils;
import lombok.Data;

@Data
public class PubkeyDetailInfo
{
    private String pubkeyId;
    private String name;
    private String pubkey;
    private String description;
    private String createTime;
    private String updateTime;

    private String eeBp;
    private String eeUser;
    private String eeBpName;
    private String eeUserName;

    public void setPubkeyDetailInfo(TblCmpPubkey tblCmpPubkey)
    {
        this.pubkeyId = tblCmpPubkey.getPubkeyId();
        this.pubkey = tblCmpPubkey.getPubkey();
        this.name = tblCmpPubkey.getName();
        this.description = tblCmpPubkey.getDescription();
        this.createTime = Utils.formatDate(tblCmpPubkey.getCreateTime());
        this.updateTime = Utils.formatDate(tblCmpPubkey.getUpdateTime());

        this.eeBp = tblCmpPubkey.getEeBp();
        this.eeUser = tblCmpPubkey.getEeUser();
    }
}
