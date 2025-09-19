package com.lnjoying.justice.iam.db.mapper;
import java.util.Collection;
import java.util.List;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import com.lnjoying.justice.iam.common.constant.IamResources;
import com.lnjoying.justice.iam.db.model.TblIamRole;
import com.lnjoying.justice.iam.domain.dto.response.role.TblIamRoleDetail;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;

@Mapper
@MapperModel(IamResources.ROLE)
public interface TblIamRoleMapper
{
    /**
     * delete by primary key
     *
     * @param roleId primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(Long roleId);

    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(TblIamRole record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblIamRole record);

    /**
     * select by primary key
     *
     * @param roleId primary key
     * @return object by primary key
     */
    TblIamRole selectByPrimaryKey(Long roleId);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblIamRole record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblIamRole record);

    Integer countByName(@Param("projectId") String projectId, @Param("bpId") String bpId, @Param("platform") String platform, @Param("role") String role);

    Integer countByRoleId(@Param("roleId") Long roleId);

    List<TblIamRoleDetail> selectAll(@Param("projectId")String projectId, @Param("bpId")String bpId, @Param("platform")String platform, @Param("role")String role,
                                     @Param("roleType")Integer roleType);



    TblIamRoleDetail selectByRoleIdAndBpId(@Param("roleId")Long roleId,@Param("bpId")String bpId);


    List<TblIamRoleDetail> selectAllByRoleIdIn(@Param("roleIdCollection")Collection<Long> roleIdCollection);


    List<TblIamRole> selectByRoleType(@Param("roleType")Integer roleType);

    Integer countByRoleIdIn(@Param("roleIdCollection")Collection<Long> roleIdCollection);

    Integer selectKindByRoleIdIn(@Param("roleIdCollection")Collection<Long> roleIdCollection);

    Integer countByProjectId(@Param("projectId")String projectId);

	int deleteByProjectId(@Param("projectId")String projectId);

}