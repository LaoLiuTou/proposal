package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.sys.entity.DictPetitionLeader;
import com.fangao.dev.sys.mapper.DictPetitionLeaderMapper;
import com.fangao.dev.sys.service.IDictPetitionLeaderService;
import com.fangao.dev.sys.service.IPetitionEventInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 领导字典表 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
@Service
public class DictPetitionLeaderServiceImpl extends ServiceImpl<DictPetitionLeaderMapper, DictPetitionLeader> implements IDictPetitionLeaderService {

    @Autowired
    private IPetitionEventInfoService petitionEventInfoService;

    @Override
    public IPage<DictPetitionLeader> queryLeaderPage(IPage<DictPetitionLeader> page, DictPetitionLeader dictPetitionLeader) {
        return page.setRecords(this.baseMapper.queryLeaderPage(page,dictPetitionLeader));
    }

    @Override
    public List<DictPetitionLeader> listEffective() {
        return super.list(Wrappers.<DictPetitionLeader>query().select("id", "name","leader_level_id")
                .eq("status", 0).orderByDesc("sort"));
    }
    @Override
    public List<DictPetitionLeader> listEffectiveByOrgIdAndLevelId(Long orgId,Long LevelId) {
        return super.list(Wrappers.<DictPetitionLeader>query().select("id", "name","leader_level_id")
                .eq("status", 0)
                .eq("org_id", orgId)
                .eq("leader_level_id", LevelId)
                .orderByDesc("sort"));
    }
    @Override
    public boolean checkCanDelete(Serializable leaderLevelId) {
        int count =super.count(Wrappers.<DictPetitionLeader>query().eq("leader_level_id",leaderLevelId));
        return count<=0;
    }

    @Override
    public boolean save(DictPetitionLeader dictPetitionLeader) {
        if (null == dictPetitionLeader) {
            return false;
        }
        return super.save(dictPetitionLeader);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        DictPetitionLeader dictPetitionLeader = new DictPetitionLeader();
        dictPetitionLeader.setId(id);
        dictPetitionLeader.setStatus(status.intValue() > -1 ? 0 : -1);
        return updateById(dictPetitionLeader);
    }

    @Override
    public boolean removeById(Serializable id) {
        ArrayList<Serializable> ids = new ArrayList<Serializable>(){{
            add(id);
        }};
        Assert.fail(!petitionEventInfoService.checkCanDelete("leader_id",ids), "存在关联信访事项操作无效");
        return super.removeById(id);
    }
}
