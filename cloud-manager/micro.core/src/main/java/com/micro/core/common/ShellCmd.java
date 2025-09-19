package com.micro.core.common;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class ShellCmd
{
    private static Logger LOGGER = LogManager.getLogger();
    public static String runCmd(String [] cmdArray)
    {
        Process pro = null;
        try
        {
            LOGGER.info("run cmd:" + StringUtils.join(cmdArray, " "));
            pro = Runtime.getRuntime().exec(cmdArray);

            boolean bl = pro.waitFor(120, TimeUnit.SECONDS);
            LOGGER.info("run cmd over.result="+bl);
            if (!bl)
            {
                LOGGER.error("run cmd failed");
                return null;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            StringBuffer strbr = new StringBuffer();
            String line;
            while ((line = br.readLine())!= null)
            {
                strbr.append(line).append("\n");
            }

            String result = strbr.toString();
            return result;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static Process runCmd2(String [] cmdArray)
    {
        Process pro = null;
        try
        {
            LOGGER.info("run cmd:" + StringUtils.join(cmdArray, " "));
            pro = Runtime.getRuntime().exec(cmdArray);
            return pro;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static String waitResult(Process pro)
    {
        try
        {
            boolean bl = pro.waitFor(120, TimeUnit.SECONDS);
            LOGGER.info("run cmd over.result="+bl);
            if (!bl)
            {
                LOGGER.error("run cmd failed");
                return null;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            StringBuffer strbr = new StringBuffer();
            String line;
            while (true)
            {
                if (!((line = br.readLine())!= null)) break;
                strbr.append(line).append("\n");

            }

            String result = strbr.toString();
            return result;
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
