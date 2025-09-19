package com.lnjoying.justice.api.entity;

import lombok.Data;

@Data
public class CopoteSmsRsp
{
    //响应状态
    String rspcod;
    //消息批次号
    String msgGroup;
    //数据校验结果
    boolean success;

    public BatchSmsRsp toBatchSmsRsp()
    {
        BatchSmsRsp smsRsp = new BatchSmsRsp();
        if (rspcod == null || rspcod.equalsIgnoreCase("IllegalMac") || rspcod.equalsIgnoreCase("IllegalSignId")
                || rspcod.equalsIgnoreCase("InvalidMessage") || rspcod.equalsIgnoreCase("InvalidUsrOrPwd")
                || rspcod.equalsIgnoreCase("NoSignId") || rspcod.equalsIgnoreCase("TooManyMobiles"))
        {
            smsRsp.setCode("-1");
        }
        else if (rspcod.equalsIgnoreCase("succes"))
        {
            smsRsp.setCode("0");
        }
        else
        {
            smsRsp.setCode("-1");
        }
        return smsRsp;
    }
}
