package com.lnjoying.justice.schema.constant;

public interface WebConstants
{
    /** Header that holds SiteWhere error string on error response */
    String HEADER_LNJOYING_ERROR = "X-Lnjoying-Error";

    String HEADER_LNJOYING_ERROR_LEVEL = "X-Lnjoying-Error-Level";

    /** Header that holds SiteWhere error code on error response */
    String HEADER_LNJOYING_ERROR_CODE = "X-Lnjoying-Error-Code";

    //the period of validity for lnjoying token. 15min
    int  LNJOYING_TOKEN_INDATE = 90000;
    int  LNJOYING_VRFCTOKEN_INDATE = 120;
    String  ACCESS_TOKEN_NAME = "Access-Token";
    String  HEADER_ACCESS_TOKEN_NAME = "X-Access-Token";
    String  LNJOYING_VRFC = "vrfc";
}
