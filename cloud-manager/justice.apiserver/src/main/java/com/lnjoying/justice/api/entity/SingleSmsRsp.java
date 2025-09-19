package com.lnjoying.justice.api.entity;

import lombok.Data;

@Data
public class SingleSmsRsp
{
    // 成功发送的短信计费条数，
    // 计费规则如下：
    // 70个字一条，超出70个字时按每67字一条计费
    //（英文按字母个数计算）
    String fee;
    String mobile;
    //短信标识符（用于匹配状态报告），一个手机号对应一个sid
    String sid;
}
