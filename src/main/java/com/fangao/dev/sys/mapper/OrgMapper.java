package com.fangao.dev.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fangao.dev.sys.dto.JdcDTO;
import com.fangao.dev.sys.dto.UserInfoDTO;
import com.fangao.dev.sys.entity.Org;
import com.fangao.dev.sys.vo.ReceptionOrgSelectedVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 系统组织表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2018-11-07
 */
@Mapper
public interface OrgMapper extends BaseMapper<Org> {

    /**
     * <p>
     * 查询指定顶级组织 ID 下所有组织列表
     * </p>
     *
     * @param topOrgId 顶级组织 ID
     * @return
     */
    List<Org> selectTopList(@Param("topOrgId") Long topOrgId);

    /**
     * <p>
     * 查询所有组织列表
     * </p>
     *
     * @param all
     * @return
     */
    List<Org> listAll(@Param("all") Boolean all);

    /**
     * 查询所有拥有接待员的组织
     * @return
     */
    List<ReceptionOrgSelectedVO> listReceptionOrg();

    /**
     * 根据orgId查询组织下所有roleId角色
     * @param orgId 组织ID
     * @param roleId 角色ID
     * @return 用户列表
     */
    List<UserInfoDTO> getUserByOrgIdAndRoleId(@Param("orgId")long orgId, @Param("roleId")long roleId);

    /**
     * 根据接待处id和责任单位id 查询该责任单位的处理中数据
     * @param orgId 接待处id
     * @param unitId 责任单位id
     * @return
     */
    List<JdcDTO> getJdcDtoByOrgIdAndUnitId(@Param("orgId")long orgId, @Param("unitId")long unitId);

    /**
     * 根据接待处id和责任单位id 查询该责任单位的处理中数据(有起止时间限制)
     * @param orgId 接待处id
     * @param unitId 责任单位id
     * @param  beginDate 开始时间
     * @param  endDate 结束日期
     * @return
     */
    List<JdcDTO> getJdcDtoByOrgIdAndUnitIdAndTime(@Param("orgId")long orgId, @Param("unitId")long unitId, @Param("beginDate") Date beginDate,@Param("endDate") Date endDate );

    @Select("Select * from sys_org where name = #{org_name}")
    Org getByName(@Param("org_name")String org_name);

    @Select("SELECT DISTINCT a.* FROM sys_org a JOIN sys_user_org b ON a.id=b.org_id WHERE b.user_id=#{user_id}")
    Org getOrgByUserId(@Param("user_id") Long user_id);
}
