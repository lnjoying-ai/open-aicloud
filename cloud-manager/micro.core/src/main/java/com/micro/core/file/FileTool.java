package com.micro.core.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;
import static com.micro.core.common.Utils.bytesToString;

public class FileTool
{
    public static String getPath(String path)
    {
        String srcPath = path;
        if (null == path)
        {
            return "";
        }
        if ( !path.startsWith("/"))
        {
            srcPath = "/" + path;
        }

        return srcPath;
    }

    public static boolean exist(String path)
    {
        File file = new File(path);
        return file.exists();
    }
}
