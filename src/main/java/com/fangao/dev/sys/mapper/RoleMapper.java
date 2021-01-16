package com.fangao.dev.sys.mapper;

import com.fangao.dev.sys.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 系统角色表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    @Select("SELECT DISTINCT a.* FROM sys_role a JOIN sys_user_role b ON a.id=b.role_id WHERE b.user_id=#{user_id}")
    List<Role> getRolesByUserId(@Param(value = "user_id") Long user_id);
}
