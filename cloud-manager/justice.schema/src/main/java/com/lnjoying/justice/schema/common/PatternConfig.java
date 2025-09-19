package com.lnjoying.justice.schema.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternConfig
{
    public static final String PATTERN_VERIFICATION_CODE = "^\\d{4,6}$";
    
    //use for user name
    public static final String PATTERN_USERNAME = "^[a-zA-Z][a-zA-Z0-9_]{5,29}$";
    
    //use for container name, cluster name, container stack name, template name
    public static final String PATTERN_NAME = "^[a-zA-Z0-9-]{1,64}$";
    
    //use for container name, cluster name, container stack name, template name
    public static final String PATTERN_VERSION = "^[a-zA-Z0-9-.]{1,64}$";
    
    public static final String PATTERN_PASSWORD =
            "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~!@#$%^&*()_+\\-={}:\";<>?,./]).{6,18}$";
    
//    public static final String PATTERN_TELEPHONE = "^1[0123456789]\\d{9}$";
    public static final String PATTERN_TELEPHONE = "^((\\+86)|(86))?[1][0-9]\\d{9}$";
    
    public static final String PATTERN_MAILADDRESS = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    
    public static void isMobileNO(String mobiles)
    {
        Pattern p = Pattern.compile(PATTERN_VERSION);
        Matcher m = p.matcher(mobiles);
        System.out.println(m.matches()+"---");
    }
    
    public static void main(String[] args)
    {
        // TODO Auto-generated method stub
        isMobileNO("a13927.");
    }
    
}
