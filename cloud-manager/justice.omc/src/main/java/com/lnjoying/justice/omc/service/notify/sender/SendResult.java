package com.lnjoying.justice.omc.service.notify.sender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

import static com.lnjoying.justice.omc.service.notify.sender.SendResult.SendStatus.FAILED;
import static com.lnjoying.justice.omc.service.notify.sender.SendResult.SendStatus.SUCCESS;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/10 20:20
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendResult
{

    private SendStatus status;

    private String message;

    public static SendResult  success(String message) {
        return SendResult.builder().status(SUCCESS).message(message).build();
    }

    public static SendResult fail(String message) {
        return SendResult.builder().status(FAILED).message(message).build();
    }

    public enum SendStatus
    {
        SUCCESS,

        FAILED;
    }
}
