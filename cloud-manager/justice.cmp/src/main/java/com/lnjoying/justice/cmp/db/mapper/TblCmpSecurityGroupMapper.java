package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.common.CmpResources;
import com.lnjoying.justice.cmp.db.model.TblCmpSecurityGroup;
import com.lnjoying.justice.cmp.db.model.TblCmpSecurityGroupExample;
import com.lnjoying.justice.cmp.db.model.TblCmpSecurityGroupKey;
import java.util.List;
import java.util.Set;

import com.lnjoying.justice.cmp.domain.dto.response.nextstack.network.SecurityGroupRspVo;
import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

@MapperModel(value = CmpResources.NS_SECURITY_GROUP)
public interface TblCmpSecurityGroupMapper {
    long countByExample(TblCmpSecurityGroupExample example);

    int deleteByExample(TblCmpSecurityGroupExample example);

    int deleteByPrimaryKey(TblCmpSecurityGroupKey key);

    int insert(TblCmpSecurityGroup record);

    int insertSelective(TblCmpSecurityGroup record);

    List<TblCmpSecurityGroup> selectByExample(TblCmpSecurityGroupExample example);

    TblCmpSecurityGroup selectByPrimaryKey(TblCmpSecurityGroupKey key);

    int updateByExampleSelective(@Param("record") TblCmpSecurityGroup record, @Param("example") TblCmpSecurityGroupExample example);

    int updateByExample(@Param("record") TblCmpSecurityGroup record, @Param("example") TblCmpSecurityGroupExample example);

    int updateByPrimaryKeySelective(TblCmpSecurityGroup record);

    int updateByPrimaryKey(TblCmpSecurityGroup record);

    List<SecurityGroupRspVo> selectByUserId(@Param("cloudId") String cloudId,
                                            @Param("userId") String userId,
                                            @Param("name") String name,
                                            @Param("sgId") String sgId,
                                            @Param("phaseStatus")int phaseStatus,
                                            @Param("phaseStatusIsEqual") boolean phaseStatusIsEqual,
                                            @Param("pageSize") Integer pageSize,
                                            @Param("startRow") Integer startRow);

    long countByUserId(@Param("cloudId") String cloudId,
                       @Param("userId") String userId,
                       @Param("name") String name,
                       @Param("sgId") String sgId,
                       @Param("phaseStatus")int phaseStatus,
                       @Param("phaseStatusIsEqual") boolean phaseStatusIsEqual);

    Set<String> getSgIds(@Param("cloudId")String cloudId);

    int updateByEeIdSelective(TblCmpSecurityGroup record);

    TblCmpSecurityGroup selectByEeId(@Param("cloudId") String cloudId, @Param("eeId") String eeId);
}