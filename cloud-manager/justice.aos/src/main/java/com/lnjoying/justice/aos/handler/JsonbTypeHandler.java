package com.lnjoying.justice.aos.handler;

import com.lnjoying.justice.aos.util.JacksonUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/8 19:33
 */
@Slf4j
@MappedTypes({Object.class})
public class JsonbTypeHandler extends BaseTypeHandler<Object>
{

    @SneakyThrows
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Object o, JdbcType jdbcType) throws SQLException
    {
        PGobject jsonObject = new PGobject();
        jsonObject.setType("jsonb");
        if (o instanceof String)
        {
            jsonObject.setValue((String)o);
        }
        else
        {
            jsonObject.setValue(JacksonUtils.objToStr(o));
        }

        preparedStatement.setObject(i, jsonObject);
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, String s) throws SQLException
    {
        return resultSet.getString(s);
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, int i) throws SQLException
    {
        return resultSet.getString(i);
    }

    @Override
    public Object getNullableResult(CallableStatement callableStatement, int i) throws SQLException
    {
        return callableStatement.getString(i);
    }
}
