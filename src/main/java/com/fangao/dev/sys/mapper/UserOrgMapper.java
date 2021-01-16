package com.fangao.dev.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fangao.dev.sys.entity.UserOrg;
import com.fangao.dev.sys.vo.UserOrgSelectedVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统用户组织关联表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2018-09-16
 */

@Mapper
public interface UserOrgMapper extends BaseMapper<UserOrg> {


    /**
     * <p>
     * 根据用户 ID 查询用户所属组织 VO
     * </p>
     *
     * @param userId 用户 ID
     * @return
     */
    List<UserOrgSelectedVO> selectSelectedVO(@Param("userId") Long userId);
}
