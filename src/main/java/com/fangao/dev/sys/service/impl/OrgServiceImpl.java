package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.sys.dto.*;
import com.fangao.dev.sys.entity.*;
import com.fangao.dev.sys.mapper.LinkReceptorOrgMapper;
import com.fangao.dev.sys.mapper.OrgMapper;
import com.fangao.dev.sys.mapper.Org_placeMapper;
import com.fangao.dev.sys.service.*;
import com.fangao.dev.sys.vo.ReceptionOrgSelectedVO;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 系统组织表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-11-07
 */
@Service
public class OrgServiceImpl extends ServiceImpl<OrgMapper, Org> implements IOrgService {

    public static final String 宽城 = "宽城";
    @Autowired
    private IUserOrgService userOrgService;
    @Autowired
    private ILinkReceptorOrgService linkReceptorOrgService;

    @Override
    public Org getOrgByUserId(Long user_id) {
        return baseMapper.getOrgByUserId(user_id);
    }

    @Override
    public List<Org> selectTopList(Long topOrgId) {
        return baseMapper.selectTopList(topOrgId);
    }

    @Override
    public List<Org> listTopOrg() {
        return super.list(Wrappers.<Org>query().select("id",
                "name").inSql("id","select id from sys_org where id=rid"));
    }

    @Override
    public List<Org> listAll(Boolean all) {
        return baseMapper.listAll(all);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public Boolean addTopOrg(Org org) {
        return this.save(org)
                && super.update(org,Wrappers.<Org>update().set("rid",org.getId()).eq("id",org.getId()));
    }

    @Override
    public Integer childNode(Serializable id) {
        return count(new QueryWrapper<Org>().eq("pid",id).eq("deleted",0));
        /*return count(Wrappers.<Org>lambdaQuery().eq(Org::getPid,
                id).eq(Org::getDeleted, 0));*/
    }

    @Override
    public boolean save(Org org) {
        if (null == org) {
            return false;
        }
        return super.save(org.initialName());
    }

    @Override
    public boolean removeById(Serializable id) {
        Assert.fail(this.childNode(id) > 0, "存在子节点不允许删除");
        Assert.fail(userOrgService.relation(id), "存在用户关联不允许删除");
        Assert.fail(linkReceptorOrgService.list(new QueryWrapper<LinkReceptorOrg>().eq("org_id",id)).size()>0
                ,"存在接待人员关联不允许删除");
        return super.removeById(id);
    }

    @Override
    public List<ReceptionOrgSelectedVO> listReceptionOrg() {
        return baseMapper.listReceptionOrg();
    }

    @Override
    public List<UserInfoDTO> getUserByOrgIdAndRoleId(long orgId, long roleId) {
        return baseMapper.getUserByOrgIdAndRoleId(orgId,roleId);
    }
    @Override
    public List<JdcDTO> getJdcDtoByOrgIdAndUnitId(long orgId, long unitId ,boolean isUseDateLimit,Date beginDate, Date endDate){
        if(isUseDateLimit){
            return baseMapper.getJdcDtoByOrgIdAndUnitIdAndTime(orgId,unitId,beginDate,endDate);
        }else{
            return baseMapper.getJdcDtoByOrgIdAndUnitId(orgId,unitId);
        }

    }

}
