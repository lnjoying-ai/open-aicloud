package com.lnjoying.justice.iam.db.mapper;
import java.util.Collection;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import com.lnjoying.justice.iam.common.constant.IamResources;
import com.lnjoying.justice.iam.db.model.TblUserInfo;
import com.lnjoying.justice.iam.db.model.TblUserInfoExample;
import com.lnjoying.justice.iam.domain.dto.response.authz.AttachmentEntitiesRsp;
import com.lnjoying.justice.schema.common.UserMetaDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@MapperModel(IamResources.USER)
public interface TblUserInfoMapper {
    long countByExample(TblUserInfoExample example);

    int deleteByExample(TblUserInfoExample example);

    int deleteByPrimaryKey(String userId);

    int insert(TblUserInfo record);

    int insertSelective(TblUserInfo record);

    List<TblUserInfo> selectByExample(TblUserInfoExample example);

    TblUserInfo selectByPrimaryKey(String userId);

    int updateByExampleSelective(@Param("record") TblUserInfo record, @Param("example") TblUserInfoExample example);

    int updateByExample(@Param("record") TblUserInfo record, @Param("example") TblUserInfoExample example);

    int updateByPrimaryKeySelective(TblUserInfo record);

    int updateByPrimaryKey(TblUserInfo record);

    TblUserInfo selectByWeixin(@Param("weixin")String weixin);

    String selectUserNameByUserId(@Param("userId")String userId);

    String selectBpNameByUserId(@Param("userId")String userId);

    List<AttachmentEntitiesRsp.User> selectAllByUserIdIn(@Param("userIdCollection")Collection<String> userIdCollection);

    List<TblUserInfo> selectByBpId(@Param("bpId")String bpId);

    Integer countByBpId(@Param("bpId")String bpId);

    List<UserMetaDTO> selectByBpIdAndCreateTimeBetween(@Param("bpIds")List<String> bpIds, @Param("userIds")List<String> userIds, @Param("minCreateTime") Date minCreateTime,
                                                       @Param("maxCreateTime")Date maxCreateTime, @Param("sortDirection")String sortDirection,
                                                       @Param("userId")String userId, @Param("userName")String userName);




}