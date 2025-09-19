package com.lnjoying.justice.iam.handler;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/4/6 14:48
 */

public class StringSerializer extends JsonSerializer<String>
{
    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException
    {
        if (value == null) {
            gen.writeString("");
        } else {
            gen.writeString(value);
        }
    }
}
