package com.fangao.dev.sys.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.api.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangao.dev.sys.entity.SnapshotPetitionLeader;
import com.fangao.dev.sys.mapper.SnapshotPetitionLeaderMapper;
import com.fangao.dev.sys.service.ISnapshotPetitionLeaderService;
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
public class SnapshotPetitionLeaderServiceImpl extends ServiceImpl<SnapshotPetitionLeaderMapper, SnapshotPetitionLeader> implements ISnapshotPetitionLeaderService {

    @Autowired
    private IPetitionEventInfoService petitionEventInfoService;

    @Override
    public IPage<SnapshotPetitionLeader> queryLeaderPage(IPage<SnapshotPetitionLeader> page, SnapshotPetitionLeader SnapshotPetitionLeader) {
        return page.setRecords(this.baseMapper.queryLeaderPage(page,SnapshotPetitionLeader));
    }

    @Override
    public List<SnapshotPetitionLeader> listEffective() {
        return super.list(Wrappers.<SnapshotPetitionLeader>query().select("id", "name","leader_level_id")
                .eq("status", 0).orderByDesc("sort"));
    }
    @Override
    public List<SnapshotPetitionLeader> listEffectiveByOrgIdAndLevelId(Long orgId,Long LevelId) {
        return super.list(Wrappers.<SnapshotPetitionLeader>query().select("id", "name","leader_level_id")
                .eq("status", 0)
                .eq("org_id", orgId)
                .eq("leader_level_id", LevelId)
                .orderByDesc("sort"));
    }
    @Override
    public boolean checkCanDelete(Serializable leaderLevelId) {
        int count =super.count(Wrappers.<SnapshotPetitionLeader>query().eq("leader_level_id",leaderLevelId));
        return count<=0;
    }

    @Override
    public boolean save(SnapshotPetitionLeader SnapshotPetitionLeader) {
        if (null == SnapshotPetitionLeader) {
            return false;
        }
        return super.save(SnapshotPetitionLeader);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        SnapshotPetitionLeader SnapshotPetitionLeader = new SnapshotPetitionLeader();
        SnapshotPetitionLeader.setId(id);
        SnapshotPetitionLeader.setStatus(status.intValue() > -1 ? 0 : -1);
        return updateById(SnapshotPetitionLeader);
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
