package com.fangao.dev.sys.mapper;

import com.fangao.dev.sys.entity.UserRole;
import com.fangao.dev.sys.vo.UserRoleSelectedVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统用户角色关联表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2018-09-16
 */

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {


    /**
     * <p>
     * 根据用户 ID 查询用户选择角色 VO
     * </p>
     *
     * @param userId 用户 ID
     * @return
     */
    List<UserRoleSelectedVO> selectSelectedVO(@Param("userId") Long userId);
}
