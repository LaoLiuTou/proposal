package com.fangao.dev.sys.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fangao.dev.sys.dto.UserDTO;
import com.fangao.dev.sys.dto.UserInfoDTO;
import com.fangao.dev.sys.dto.UserResourceDTO;
import com.fangao.dev.sys.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 系统用户表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2018-09-16
 */

@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * <p>
     * 分页查询用户
     * </p>
     *
     * @return
     */
    List<User> queryUser(IPage<User> page, @Param(value = "user") User user);

    /**
     * 查询所有角色为接待处的人员，带组织名称
     * @return
     */
    List<UserDTO> listReceptionUser();

    /**
     * 查询用户权限
     * @param userId
     * @param resourceId
     * @return
     */
    List<UserResourceDTO> queryByUserIdAndResourceId(@Param(value = "userId") long userId,@Param(value = "resourceId") long resourceId);

    /**
     * 查询个人信息
     * @param id
     * @return
     */
    List<UserInfoDTO> queryUserInfo(@Param(value = "id") long id);

    @Select("SELECT DISTINCT a.username FROM sys_user a " +
            "JOIN sys_user_role b ON a.id = b.user_id " +
            "JOIN sys_role c ON b.role_id = c.id AND c.`name`=#{role_name} " +
            "JOIN sys_user_org d ON a.id = d.user_id " +
            "WHERE d.org_id=#{org_id}")
    List<String> getUsernamesByRoleNameAndOrgId(@Param(value = "role_name") String role_name,@Param(value = "org_id") Long org_id);
}
