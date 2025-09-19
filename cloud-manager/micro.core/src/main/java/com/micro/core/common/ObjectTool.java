package com.micro.core.common;

import io.netty.buffer.ByteBufInputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectTool
{
    public  static byte[] obj2byte(Object obj) throws Exception
    {
        byte[] ret = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(obj);
        out.close();
        ret = baos.toByteArray();
        baos.close();
        return ret;
    }

    public  static Object byte2obj(byte[] bytes) throws Exception
    {
        Object ret = null;
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(bais);
        ret = in.readObject();
        in.close();
        return ret;
    }

    public  static String obj2str(Object obj) throws Exception
    {
        byte [] objByte = obj2byte(obj);
        return Utils.byteToHexString(objByte);
    }

    public  static Object str2obj(String str) throws Exception
    {
        return byte2obj(Utils.hexToByteArray(str));
    }

}
