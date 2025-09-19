package com.lnjoying.justice.schema.service.apiserver;

import java.util.List;

public interface NotifyMessageService
{
    Integer notifyAdmin(List<TipMessageService.NotifyParam> notifyParams, String template);
}
