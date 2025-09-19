package com.lnjoying.justice.omc.domain.dto.req;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Map;

//    "alerts":[
//        {
//            "status":"resolved",
//            "labels":{
//                "alertname":"hostCpuUsageAlert",
//                "instance":"192.168.199.24:9100",
//                "severity":"page"
//            },
//            "annotations":{
//                "description":"192.168.199.24:9100 CPU 使用率超过 85% (当前值为: 0.9973333333333395)",
//                "summary":"机器 192.168.199.24:9100 CPU 使用率过高"
//            },
//            "startsAt":"2020-02-29T19:45:21.799548092+08:00",
//            "endsAt":"2020-02-29T19:49:21.799548092+08:00",
//            "generatorURL":"http://localhost.localdomain:9090/graph?g0.expr=sum+by%28instance%29+%28avg+without%28cpu%29+%28irate%28node_cpu_seconds_total%7Bmode%21%3D%22idle%22%7D%5B5m%5D%29%29%29+%3E+0.85&g0.tab=1",
//            "fingerprint":"368e9616d542ab48"
//        }
//    ],
@Data
public class WebhookInfoReq
{
    List<Alert> alerts;

    @JsonIgnoreProperties
    @Data
    public static class Alert
    {
        private String status;

        // Label value pairs for purpose of aggregation, matching, and disposition
        // dispatching. This must minimally include an "alert name" label.
        private Map<String, String> labels;

        // Extra key/value information which does not define alert identity.
        private Map<String, String> annotations;

        private String startsAt;

        private String endsAt;
    }

    @Data
    public static class Annotations
    {
        private String description;

        private String summary;

        private String ruleId;

        private String threshold;
    }
}
