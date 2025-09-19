package com.lnjoying.justice.servicemanager.handler;

import com.lnjoying.justice.servicemanager.config.SecurityModeConfig;
import com.lnjoying.justice.commonweb.util.AESUtils;
import lombok.SneakyThrows;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EncryptTypeHandler implements TypeHandler<String>
{
    @SneakyThrows
    @Override
    public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
    {
        if (SecurityModeConfig.securityMode)
        {
            ps.setString(i, AESUtils.cbcEncrypt(parameter, SecurityModeConfig.aesCbcKey, SecurityModeConfig.aesCbcIv));
        }
        else
        {
            ps.setString(i, parameter);
        }
    }

    @Override
    public String getResult(ResultSet rs, String columnName) throws SQLException
    {
        if (SecurityModeConfig.securityMode)
        {
            return AESUtils.cbcDecrypt(rs.getString(columnName), SecurityModeConfig.aesCbcKey, SecurityModeConfig.aesCbcIv);
        }
        else
        {
            return rs.getString(columnName);
        }
    }

    @Override
    public String getResult(ResultSet rs, int columnIndex) throws SQLException
    {
        if (SecurityModeConfig.securityMode)
        {
            return AESUtils.cbcDecrypt(rs.getString(columnIndex), SecurityModeConfig.aesCbcKey, SecurityModeConfig.aesCbcIv);
        }
        else
        {
            return rs.getString(columnIndex);
        }
    }

    @Override
    public String getResult(CallableStatement cs, int columnIndex) throws SQLException
    {
        if (SecurityModeConfig.securityMode)
        {
            return AESUtils.cbcDecrypt(cs.getString(columnIndex), SecurityModeConfig.aesCbcKey, SecurityModeConfig.aesCbcIv);
        }
        else
        {
            return cs.getString(columnIndex);
        }
    }
}
