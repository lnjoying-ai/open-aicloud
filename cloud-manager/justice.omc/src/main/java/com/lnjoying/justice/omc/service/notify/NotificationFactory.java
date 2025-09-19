package com.lnjoying.justice.omc.service.notify;

import com.lnjoying.justice.omc.service.notify.notification.EmailNotification;
import com.lnjoying.justice.omc.service.notify.notification.PhoneNotification;
import com.lnjoying.justice.omc.service.notify.notification.SmsNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/10 19:23
 */

@Component
public class NotificationFactory
{
    @Autowired
    private EmailNotification emailNotification;

    @Autowired
    private SmsNotification smsNotification;

    @Autowired
    private PhoneNotification phoneNotification;

    public Notification createNotificationService(int notificationType) {
        switch (notificationType) {
            case 0:
                return emailNotification;
            case 1:
                return smsNotification;
            case 2:
                return phoneNotification;
            default:
                throw new IllegalArgumentException("Invalid notification type: " + notificationType);
        }
    }
}
