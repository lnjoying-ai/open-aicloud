package com.lnjoying.justice.cis.domain.model.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {

    private String messageId;

    private String result;

    private boolean readed;

    private BasicMessageInfo basicInfo;

    private EnumMessageType messageType;

    private String sourceAppStore;

    private String targetAppStore;

    private String time;

    private String description;

    private String atpTestStatus;

    private String atpTestTaskId;

    private String atpTestReportUrl;

    private String packageDownloadUrl;

    private String iconDownloadUrl;

    private String demoVideoDownloadUrl;
}
