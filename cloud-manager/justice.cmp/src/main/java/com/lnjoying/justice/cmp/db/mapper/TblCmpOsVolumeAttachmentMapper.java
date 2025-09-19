package com.lnjoying.justice.cmp.db.mapper;

import com.lnjoying.justice.cmp.db.model.TblCmpOsVolumeAttachment;
import com.lnjoying.justice.cmp.db.model.TblCmpOsVolumeAttachmentExample;
import com.lnjoying.justice.cmp.db.model.TblCmpOsVolumeAttachmentKey;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

public interface TblCmpOsVolumeAttachmentMapper {
    long countByExample(TblCmpOsVolumeAttachmentExample example);

    int deleteByExample(TblCmpOsVolumeAttachmentExample example);

    int deleteByPrimaryKey(TblCmpOsVolumeAttachmentKey key);

    int insert(TblCmpOsVolumeAttachment record);

    int insertSelective(TblCmpOsVolumeAttachment record);

    List<TblCmpOsVolumeAttachment> selectByExample(TblCmpOsVolumeAttachmentExample example);

    TblCmpOsVolumeAttachment selectByPrimaryKey(TblCmpOsVolumeAttachmentKey key);

    int updateByExampleSelective(@Param("record") TblCmpOsVolumeAttachment record, @Param("example") TblCmpOsVolumeAttachmentExample example);

    int updateByExample(@Param("record") TblCmpOsVolumeAttachment record, @Param("example") TblCmpOsVolumeAttachmentExample example);

    int updateByPrimaryKeySelective(TblCmpOsVolumeAttachment record);

    int updateByPrimaryKey(TblCmpOsVolumeAttachment record);

    Set<String> getVolumeAttachmentIds(@Param("cloudId")String cloudId, @Param("volumeId")String volumeId);

    Set<String> getServerVolumeAttachmentIds(@Param("cloudId")String cloudId, @Param("serverId")String serverId);
}