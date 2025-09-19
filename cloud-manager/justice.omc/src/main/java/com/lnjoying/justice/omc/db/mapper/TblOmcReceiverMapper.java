package com.lnjoying.justice.omc.db.mapper;
import com.lnjoying.justice.commonweb.mapper.MapperModel;
import com.lnjoying.justice.omc.common.OmcResources;
import com.lnjoying.justice.omc.domain.model.ReceiverDetail;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.lnjoying.justice.omc.db.model.TblOmcReceiver;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@MapperModel(OmcResources.RECEIVER)
public interface TblOmcReceiverMapper
{
    /**
     * delete by primary key
     *
     * @param id primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(String id);

    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(TblOmcReceiver record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblOmcReceiver record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    TblOmcReceiver selectByPrimaryKey(String id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblOmcReceiver record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblOmcReceiver record);

    List<TblOmcReceiver> selectAll(@Param("bpId")String bpId,@Param("userId")String userId, @Param("name")String name);

    ReceiverDetail selectById(@Param("id")String id, @Param("alarmId")String alarmId);


}