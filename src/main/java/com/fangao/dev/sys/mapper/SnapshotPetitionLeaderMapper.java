package com.fangao.dev.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fangao.dev.sys.entity.SnapshotPetitionLeader;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 领导字典表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
@Mapper
public interface SnapshotPetitionLeaderMapper extends BaseMapper<SnapshotPetitionLeader> {

    /**
     * <p>
     * 分页查询领导
     * </p>
     *
     * @return
     */
    List<SnapshotPetitionLeader> queryLeaderPage(IPage<SnapshotPetitionLeader> page, @Param(value = "leader") SnapshotPetitionLeader SnapshotPetitionLeader);
}
