package com.lnjoying.justice.schema.service.apiserver;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

public interface TipMessageService
{
    Integer sendEmail(@ApiParam(name = "dst") String dst, @ApiParam(name = "paramMap") Map<String, String> paramMap, @ApiParam(name = "template")String template);
    Integer sendBatchEmail(@ApiParam(name = "dst")String[] dst, @ApiParam(name = "paramMap")Map<String, String> paramMap, @ApiParam(name = "template")String template);
    Integer sendSingleSms(@ApiParam(name = "notifyParams") List<NotifyParam> notifyParams, @ApiParam(name = "mobile") String mobile, @ApiParam(name = "template")String template);
    Integer sendBatchSms(@ApiParam(name = "notifyParams") List<NotifyParam> notifyParams, @ApiParam(name = "mobiles") List<String> mobiles, @ApiParam(name = "template")String template);

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    final class NotifyParam
    {
        private String key;
        private String value;
    }
}
