package com.lnjoying.justice.aos.helm.util;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/4 10:33
 */

public class HelmTools
{
    public static void verifyLocalHelmBinary()
    {

    }

    public static String convertToOciUrl(String remoteUrl) {
        if (remoteUrl.startsWith("http://")) {
            remoteUrl = remoteUrl.replaceFirst("http://", "oci://");
        } else if (remoteUrl.startsWith("https://")) {
            remoteUrl = remoteUrl.replaceFirst("https://", "oci://");
        } else {
            remoteUrl = "oci://" + remoteUrl;
        }

        String ociUrl = remoteUrl.replace("/chartrepo", "");

        return ociUrl;
    }
}
