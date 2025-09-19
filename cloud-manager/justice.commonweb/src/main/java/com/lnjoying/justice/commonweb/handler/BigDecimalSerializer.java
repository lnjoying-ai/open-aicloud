package com.lnjoying.justice.commonweb.handler;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/8/23 20:48
 */

public class BigDecimalSerializer extends JsonSerializer<BigDecimal>
{

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException
    {
        gen.writeNumber(value.setScale(2, RoundingMode.HALF_DOWN));
    }
}
