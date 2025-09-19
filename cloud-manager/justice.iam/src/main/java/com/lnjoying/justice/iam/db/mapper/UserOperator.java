package com.lnjoying.justice.iam.db.mapper;

import com.lnjoying.justice.iam.db.model.TblIamRole;
import com.lnjoying.justice.iam.db.model.TblRoleInfo;
import com.lnjoying.justice.iam.db.model.TblUserInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserOperator
{
//    List<UserRole> getUserRolesByUserId(String userId);

    @Results(id = "role", value = {
            @Result(column = "id", property = "roleId"),
            @Result(column = "platform", property = "platform"),
            @Result(column = "role", property = "role")
    })
    @Select("select t2.* from (select * from tbl_iam_attachment where target_type = 3 and principal_type = 1 and principal_id=#{userId}) t1 " +
            "join tbl_iam_role t2 on t1.target_id  = cast(t2.role_id as VARCHAR)")
    public List<TblIamRole> getUserRolesByUserId(@Param("userId") String userId);


    @Results(id = "result1", value = {
            @Result(column = "id", property = "roleId"),
            @Result(column = "platform", property = "platform"),
            @Result(column = "role", property = "role")
    })
    @Select("select t2.* from (select * from tbl_iam_attachment where target_type = 3 and principal_type = 1 and principal_id=#{userId}) t1 " +
            "join (select * from tbl_iam_role where platform = #{serviceCode}) t2 on t1.target_id  = cast(t2.role_id as VARCHAR)")
    public List<TblIamRole> getUserRolesByUserIdAndServiceCode(@Param("userId") String userId, @Param("serviceCode") String serviceCode);

    @Results(id = "result2", value = {
            @Result(column = "user_id", property = "userId"),
            @Result(column = "bp_id", property = "bpId"),
            @Result(column = "user_name", property = "userName"),
            @Result(column = "platform", property = "platform"),
            @Result(column = "role", property = "role")
    })
    @Select("select t3.* from (select * from tbl_user_info) t3 join " +
            "(select distinct t1.principal_id from (select * from tbl_iam_attachment where target_type = 3 and principal_type = 1  ) t1 " +
            "join (select  * from tbl_iam_role where platform = #{serviceCode} and role = #{role}) t2  " +
            "on t1.target_id  = cast(t2.role_id as VARCHAR))t4 on t3.user_id = t4.principal_id")
    public List<TblUserInfo> getUserInfosByRoleAndServiceCode(@Param("role") String role, @Param("serviceCode") String serviceCode);
}
