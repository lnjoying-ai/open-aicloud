package com.lnjoying.justice.iam.common.constant;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/9 15:46
 */

public final class ConstraintConstants
{
    private ConstraintConstants() {}

    public static final String PATTERN_NAME = "[a-zA-Z0-9-_]{1,64}$";

    public static final String MESSAGE_NAME = "name that starts with a letter or number, can contain numbers, letters and -_, length 1-64 bytes";


}
