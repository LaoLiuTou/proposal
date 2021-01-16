package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.sys.entity.DictPetitionEventPlace;
import com.fangao.dev.sys.entity.Org;
import com.fangao.dev.sys.entity.Role;
import com.fangao.dev.sys.entity.User;
import com.fangao.dev.sys.mapper.DictPetitionEventPlaceMapper;
import com.fangao.dev.sys.mapper.Org_placeMapper;
import com.fangao.dev.sys.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 事发地字典表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
@Service
public class DictPetitionEventPlaceServiceImpl extends ServiceImpl<DictPetitionEventPlaceMapper, DictPetitionEventPlace> implements IDictPetitionEventPlaceService {

    @Autowired
    private IPetitionEventInfoService petitionEventInfoService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    @Lazy
    private IOrgService orgService;
    @Autowired
    @Lazy
    private IUserService userService;
    @Autowired
    @Lazy
    private Org_placeMapper orgPlaceMapper;

    @Override
    public IPage<DictPetitionEventPlace> page(Page page, DictPetitionEventPlace dictPetitionEventPlace) {
        QueryWrapper<DictPetitionEventPlace> qw = new QueryWrapper<>();
        qw.setEntity(dictPetitionEventPlace);
        return super.page(page, qw);
    }

    @Override
    public List<DictPetitionEventPlace> listEffective() {
        return super.list(Wrappers.<DictPetitionEventPlace>query().select("id", "name")
                .eq("status", 0).orderByDesc("sort"));
    }

    @Override
    public List<DictPetitionEventPlace> listEffectiveByUserId(Long user_id) {
        List<Role> roles = roleService.getRolesByUserId(user_id);
        boolean isJdc = false;
        for (Role role:roles){
            if(role.getName().equals("接待员")) {
                isJdc = true;
                break;
            }
        }
        if(isJdc){
            Org org = orgService.getOrgByUserId(user_id);
            User user = userService.getOne(new QueryWrapper<User>().eq("id",user_id));
            if(org != null && user != null) {
                List<String> operators = new ArrayList<>();
                operators.add(user.getUsername());
                return getEventPlacesByOrgAndOperator(org.getName(),operators);
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<DictPetitionEventPlace> listEffectiveByReceptionOrgId(Long reception_org_id) {
        Org org = orgService.getOne(new QueryWrapper<Org>().eq("id",reception_org_id));
        List<String> operators = userService.getUsernamesByRoleNameAndOrgId("接待员",reception_org_id);
        if(org != null) return getEventPlacesByOrgAndOperator(org.getName(),operators);
        return new ArrayList<>();
    }

    private List<DictPetitionEventPlace> getEventPlacesByOrgAndOperator(String orgName,List<String> operators){
        List<String> eventPlaceNames = new ArrayList<>();
        List<DictPetitionEventPlace> returnList = new ArrayList<>();
        if(orgName.equals("接待一处") || orgName.equals("接待二处")){
            eventPlaceNames = orgPlaceMapper.getPlace_nameByOrgName(orgName);
        }else{
            returnList = listEffective();
        }
        if(CollectionUtils.isNotEmpty(eventPlaceNames)){
            for(String placeName:eventPlaceNames){
                returnList.addAll(super.list(new QueryWrapper<DictPetitionEventPlace>()
                        .select("id", "name").like("name",placeName).eq("status", 0).orderByDesc("sort")));
            }
        }
        for(String operator: operators){
            returnList.addAll(super.list(new QueryWrapper<DictPetitionEventPlace>()
                    .select("id", "name").eq("operator",operator).eq("status", 0).orderByDesc("sort")));
        }
        return returnList;
    }

    @Override
    public boolean save(DictPetitionEventPlace dictPetitionEventPlace) {
        if (null == dictPetitionEventPlace) {
            return false;
        }
        return super.save(dictPetitionEventPlace);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        DictPetitionEventPlace dictPetitionEventPlace = new DictPetitionEventPlace();
        dictPetitionEventPlace.setId(id);
        dictPetitionEventPlace.setStatus(status.intValue() > -1 ? 0 : -1);
        return updateById(dictPetitionEventPlace);
    }

    @Override
    public boolean removeById(Serializable id) {
        ArrayList<Serializable> ids = new ArrayList<Serializable>(){{
            add(id);
        }};
        Assert.fail(!petitionEventInfoService.checkCanDelete("event_place_id",ids), "存在关联信访事项操作无效");
        return super.removeById(id);
    }

    @Override
    public List<DictPetitionEventPlace> getByName(String name){
        return baseMapper.getByName(name);
    }
}
