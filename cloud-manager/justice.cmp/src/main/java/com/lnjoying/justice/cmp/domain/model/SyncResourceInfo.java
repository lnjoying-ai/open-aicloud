package com.lnjoying.justice.cmp.domain.model;

import com.lnjoying.justice.cmp.common.DataSyncLevel;
import lombok.Data;

import java.util.Objects;

@Data
public class SyncResourceInfo
{
    private String cloudId;

    private String resourceId;

    private String syncMsg;

    private DataSyncLevel dataSyncLevel;

    private Integer count;

    private Long nextSyncTime;

    public SyncResourceInfo(String cloudId, String resourceId, String syncMsg, DataSyncLevel dataSyncLevel)
    {
        this.cloudId = cloudId;
        this.resourceId = resourceId;
        this.syncMsg = syncMsg;
        this.dataSyncLevel = dataSyncLevel;
        this.count = this.dataSyncLevel == null ? null : dataSyncLevel.getCount();
    }

    public boolean reduceLevel(long time)
    {
        if (this.count == null || this.dataSyncLevel == null)
        {
            return false;
        }
        else
        {
            if (this.count == 0)
            {
                if (this.dataSyncLevel.next() == null)
                {
                    return false;
                }
                else
                {
                    this.dataSyncLevel = this.dataSyncLevel.next();
                    this.count = this.dataSyncLevel.getCount();
                }
                return reduceLevel(time);
            }
            else
            {
                this.count = this.count - 1;
                this.nextSyncTime = time + this.dataSyncLevel.getIntervalTime();
                return true;
            }
        }
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
        SyncResourceInfo syncResourceInfo = (SyncResourceInfo) o;
        return Objects.equals(this.cloudId, syncResourceInfo.cloudId) &&
                Objects.equals(this.resourceId, syncResourceInfo.resourceId) &&
                Objects.equals(this.syncMsg, syncResourceInfo.syncMsg);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(cloudId, resourceId, syncMsg);
    }
}
