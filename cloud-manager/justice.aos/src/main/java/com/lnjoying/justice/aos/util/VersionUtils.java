package com.lnjoying.justice.aos.util;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/5 16:20
 */
public class VersionUtils
{

    /**
     * Compare the size of the version number. If the former is larger, it will return a positive number (true),
     * the latter will return a negative number (false), and if it is equal, it will return 0 (false)
     * @param version1
     * @param version2
     * @return
     */
    public static int versionCompare(String version1, String version2)
    {
        String[] versionArr1 = version1.split("\\.");
        String[] versionArr2 = version2.split("\\.");
    
        int minLen = Math.min(versionArr1.length, versionArr2.length);
        int diff = 0;
    
        for (int i = 0; i < minLen; i++)
        {
            String v1 = versionArr1[i];
            String v2 = versionArr2[i];
            diff = v1.length() - v2.length();
            if (diff == 0)
            {
                diff = v1.compareTo(v2);
            }
            if (diff != 0)
            {
                break;
            }
        }
        
        diff = (diff != 0) ? diff : (versionArr1.length - versionArr2.length);
        return diff;
    }


}
