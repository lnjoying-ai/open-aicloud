package com.lnjoying.justice.api.entity;

import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import lombok.Data;

import java.util.List;

@Data
public class BatchSmsRsp
{
    //短信请求响应返回码
    String code;
    //短信总计费条数
    String total_fee;
    String create_date;
    //响应返回中文描述
    String msg;
    List<SingleSmsRsp> data;
    String uid;

    public static BatchSmsRsp of(SendSmsResponse sendSmsResponse)
    {
        BatchSmsRsp batchSmsRsp = new BatchSmsRsp();

        if (sendSmsResponse == null || sendSmsResponse.getBody() == null)
        {
            batchSmsRsp.setCode("-1");

        }
        else if (sendSmsResponse.getBody().getCode().equalsIgnoreCase("OK"))
        {
            batchSmsRsp.setCode("0");
            batchSmsRsp.setMsg(sendSmsResponse.getBody().getMessage());
        }
        else
        {
            batchSmsRsp.setCode("0");
            batchSmsRsp.setMsg(sendSmsResponse.getBody().getMessage());
        }

        return batchSmsRsp;
    }
}
