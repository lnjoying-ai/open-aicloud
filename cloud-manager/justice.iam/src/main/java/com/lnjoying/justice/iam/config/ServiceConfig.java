package com.lnjoying.justice.iam.config;

public class ServiceConfig {

    private ServiceConfig() {
    }

    public static final String PATTERN_VERIFICATION_CODE = "^\\d{6,6}$";

    //public static final String PATTERN_USERNAME = "^[a-zA-Z][a-zA-Z0-9_]{5,63}$";
    public static final String PATTERN_USERNAME = "[a-zA-Z0-9-_.@]{1,64}$";

    public static final String PATTERN_PASSWORD =
        "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~!@#$%^&*()_+\\-={}:\";<>?,./]).{6,18}$";

    public static final String PATTERN_TELEPHONE = "^1[3456789]\\d{9}$";

    public static final String PATTERN_MAILADDRESS = "^[\\.a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";


}
