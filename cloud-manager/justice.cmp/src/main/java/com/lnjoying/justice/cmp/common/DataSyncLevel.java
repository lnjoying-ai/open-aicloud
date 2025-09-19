package com.lnjoying.justice.cmp.common;

public enum DataSyncLevel
{
    LEVEL_1(1, 5 * 60 * 1000, 6,null),
    LEVEL_2(2, 60 * 1000, 10, LEVEL_1),
    LEVEL_3(3, 30 * 1000, 1, LEVEL_2),
    LEVEL_4(4, 10 * 1000, 1, LEVEL_3),
    LEVEL_5(5, 5 * 1000, 1, LEVEL_4),
    LEVEL_6(6, 1000, 1, LEVEL_5);

    private Integer level;
    private int intervalTime;
    private int count;
    private DataSyncLevel nextLevel;

    DataSyncLevel(int level, int intervalTime, int count, DataSyncLevel dataSyncLevel)
    {
        this.level = level;
        this.intervalTime = intervalTime;
        this.count = count;
        this.nextLevel = dataSyncLevel;
    }

    public int getLevel()
    {
        return level;
    }

    public int getIntervalTime()
    {
        return intervalTime;
    }

    public int getCount()
    {
        return count;
    }

    public DataSyncLevel next()
    {
        return nextLevel;
    }
}
