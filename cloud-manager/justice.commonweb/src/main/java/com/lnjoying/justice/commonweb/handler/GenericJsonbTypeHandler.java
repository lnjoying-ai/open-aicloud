package com.lnjoying.justice.commonweb.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lnjoying.justice.commonweb.util.JacksonUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/9/26 15:09
 */

public class GenericJsonbTypeHandler<T> extends BaseTypeHandler<T>
{

    private final Class<T> type;

    public GenericJsonbTypeHandler(Class<T> type)
    {
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, T t, JdbcType jdbcType) throws SQLException
    {
        try
        {
            String json = JacksonUtils.objToStrDefault(t);
            preparedStatement.setObject(i, json, JdbcType.OTHER.TYPE_CODE);
        }
        catch (Exception e)
        {
            throw new SQLException("Error converting List<TagItem> to JSON", e);
        }
    }

    @Override
    public T getNullableResult(ResultSet resultSet, String s) throws SQLException
    {
        String json = resultSet.getString(s);
        return convertJsonToList(json);

    }

    @Override
    public T getNullableResult(ResultSet resultSet, int i) throws SQLException
    {
        String json = resultSet.getString(i);
        return convertJsonToList(json);
    }

    @Override
    public T getNullableResult(CallableStatement callableStatement, int i) throws SQLException
    {
        String json = callableStatement.getString(i);
        return convertJsonToList(json);
    }

    private T convertJsonToList(String json) throws SQLException {
        if (json == null) {
            return null;
        }
        try {
            return JacksonUtils.strToObjTypeDefault(json, new TypeReference<T>(){});
        } catch (Exception e) {
            throw new SQLException("Error converting JSON to List<TagItem>", e);
        }
    }
}
