package com.lnjoying.justice.omc.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lnjoying.justice.commonweb.model.TagItem;
import com.lnjoying.justice.commonweb.util.JacksonUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2024/9/25 14:48
 */

public class TagListJsonbTypeHandler extends BaseTypeHandler<List<TagItem>>
{
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List<TagItem> tagItems, JdbcType jdbcType) throws SQLException
    {
        try
        {
            PGobject jsonObject = new PGobject();
            jsonObject.setType("jsonb");
            jsonObject.setValue(JacksonUtils.objToStrDefault(tagItems));

            preparedStatement.setObject(i, jsonObject);
        }
        catch (Exception e)
        {
            throw new SQLException("Error converting List<TagItem> to JSON", e);
        }

    }

    @Override
    public List<TagItem> getNullableResult(ResultSet resultSet, String s) throws SQLException
    {
        String json = resultSet.getString(s);
        return convertJsonToList(json);
    }

    @Override
    public List<TagItem> getNullableResult(ResultSet resultSet, int i) throws SQLException
    {
        String json = resultSet.getString(i);
        return convertJsonToList(json);
    }

    @Override
    public List<TagItem> getNullableResult(CallableStatement callableStatement, int i) throws SQLException
    {
        String json = callableStatement.getString(i);
        return convertJsonToList(json);
    }

    private List<TagItem> convertJsonToList(String json) throws SQLException {
        if (json == null) {
            return null;
        }
        try {
            return JacksonUtils.strToObjTypeDefault(json, new TypeReference<List<TagItem>>(){});
        } catch (Exception e) {
            throw new SQLException("Error converting JSON to List<TagItem>", e);
        }
    }
}
