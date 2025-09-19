package com.micro.core.persistence.redis;

import com.micro.core.common.Utils;
import io.vertx.core.buffer.Buffer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtil
{
    public static Buffer serialize(Object object)
    {
        if (object == null)
        {
            return null;
        }

        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try
        {
            //serialize
            Buffer buffer =  Buffer.buffer();

            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            buffer.appendBytes(bytes);
            return buffer;
        }
        catch (Exception e)
        {
            return null;

        }
    }


    public static String serializeStr(Object object)
    {
        if (object == null)
        {
            return null;
        }

        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try
        {
            //serialize
            Buffer buffer =  Buffer.buffer();

            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return Utils.byteToHexString(bytes);
        }
        catch (Exception e)
        {
            return null;

        }
    }

    public static Object unserializeStr(String str)
    {
        ByteArrayInputStream bais = null;
        try
        {
            byte [] bytes = Utils.hexToByteArray(str);

            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        }
        catch (Exception e)
        {

        }
        return null;
    }

    public static Object unserialize( byte[] bytes)
    {
        ByteArrayInputStream bais = null;
        try
        {
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        }
        catch (Exception e)
        {

        }
        return null;
    }
}
