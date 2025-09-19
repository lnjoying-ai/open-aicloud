package com.lnjoying.justice.omc.service.notify.sender.sms;

import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/10 19:27
 */

@Data
public class BatchSmsRsp {
    //短信请求响应返回码
    String code;
    //短信总计费条数
    String total_fee;
    String create_date;
    //响应返回中文描述
    String msg;
    List<SingleSmsRsp> data;
    String uid;
}