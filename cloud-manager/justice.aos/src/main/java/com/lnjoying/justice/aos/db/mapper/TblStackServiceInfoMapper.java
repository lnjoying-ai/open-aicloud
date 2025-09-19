package com.lnjoying.justice.aos.db.mapper;

import com.lnjoying.justice.aos.db.model.TblStackServiceInfo;
import com.lnjoying.justice.aos.db.model.TblStackServiceInfoExample;
import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MapperModel(value="stack_service")
public interface TblStackServiceInfoMapper {
    long countByExample(TblStackServiceInfoExample example);

    int deleteByExample(TblStackServiceInfoExample example);

    int deleteByPrimaryKey(String serviceId);

    int insert(TblStackServiceInfo record);

    int insertSelective(TblStackServiceInfo record);

    List<TblStackServiceInfo> selectByExample(TblStackServiceInfoExample example);

    TblStackServiceInfo selectByPrimaryKey(String serviceId);

    int updateByExampleSelective(@Param("record") TblStackServiceInfo record, @Param("example") TblStackServiceInfoExample example);

    int updateByExample(@Param("record") TblStackServiceInfo record, @Param("example") TblStackServiceInfoExample example);

    int updateByPrimaryKeySelective(TblStackServiceInfo record);

    int updateByPrimaryKey(TblStackServiceInfo record);

    /**
     * Query according to conditions
     * @param serviceName
     * @param stackName
     * @param userId
     * @param statusCollection
     * @param region
     * @param site
     * @param nodeId
     * @param pageSize
     * @param startRow
     * @return
     */
    List<TblStackServiceInfo> selectAllService(@Param("stackName")String stackName, @Param("serviceName")String serviceName, @Param("userId")String userId, @Param("statusCollection")List<Integer> statusCollection,
                                 @Param("region")String region, @Param("site")String site, @Param("nodeId")String nodeId,
                                 @Param("pageSize")int pageSize, @Param("startRow")int startRow, @Param("notInStatusCollection")List<Integer> notInStatusCollection);

    /**
     * count all
     * @param serviceName
     * @param stackName
     * @param userId
     * @param statusCollection
     * @param region
     * @param site
     * @param nodeId
     * @return
     */
    int countAllService(@Param("stackName")String stackName, @Param("serviceName")String serviceName, @Param("userId")String userId,@Param("statusCollection")List<Integer> statusCollection,
                 @Param("region")String region, @Param("site")String site, @Param("nodeId")String nodeId, @Param("notInStatusCollection")List<Integer> notInStatusCollection);
}