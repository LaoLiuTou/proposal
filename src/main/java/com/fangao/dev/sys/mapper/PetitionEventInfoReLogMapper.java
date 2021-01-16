package com.fangao.dev.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fangao.dev.sys.dto.*;
import com.fangao.dev.sys.entity.PetitionEventInfo;
import com.fangao.dev.sys.entity.PetitionEventInfoReLog;
import com.fangao.dev.sys.vo.DrawPetitionPieVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 信访事项重访历史表 Mapper 接口
 * </p>
 *
 */

@Mapper
public interface PetitionEventInfoReLogMapper extends BaseMapper<PetitionEventInfoReLog> {
}
