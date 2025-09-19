package com.lnjoying.justice.ecrm.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.ecrm.db.model.TblEdgeUpgradeInfo;
import com.lnjoying.justice.ecrm.domain.UpgradePlan;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

@Data
public class UpgradeLog
{
    @SerializedName("node_id")
    @JsonProperty("node_id")
    private String nodeId;

    @SerializedName("upgrade_status")
    @JsonProperty("upgrade_status")
    private Integer upgradeStatus;

    @SerializedName("create_time")
    @JsonProperty("create_time")
    private Date createTime;

    @SerializedName("update_time")
    @JsonProperty("update_time")
    private Date updateTime;

    @SerializedName("upgrade_plans")
    @JsonProperty("upgrade_plans")
    private List<UpgradePlan> upgradePlans;

    @SerializedName("old_version")
    @JsonProperty("old_version")
    private String oldVersion;

    @SerializedName("new_version")
    @JsonProperty("new_version")
    private String newVersion;

    @SerializedName("oper_user")
    @JsonProperty("oper_user")
    private String operUser;

    @SerializedName("oper_bp")
    @JsonProperty("oper_bp")
    private String operBp;

    @SerializedName("region_id")
    @JsonProperty("region_id")
    private String regionId;

    @SerializedName("site_id")
    @JsonProperty("site_id")
    private String siteId;

    public static UpgradeLog of (TblEdgeUpgradeInfo tblEdgeUpgradeInfo)
    {
        UpgradeLog upgradeLog = new UpgradeLog();
        BeanUtils.copyProperties(tblEdgeUpgradeInfo, upgradeLog);
        List<UpgradePlan> upgradePlans = JsonUtils.fromJson(tblEdgeUpgradeInfo.getUpgradePlan(), new TypeToken<List<UpgradePlan>>(){}.getType());
        upgradeLog.setUpgradePlans(upgradePlans);
        return upgradeLog;
    }
}
