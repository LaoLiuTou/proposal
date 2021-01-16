package com.fangao.dev.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fangao.dev.sys.dto.PetitionEventProgressDTO;
import com.fangao.dev.sys.entity.PetitionEventProgress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 事项进展表 Mapper 接口
 * </p>
 *
 * @author ykb
 * @since 2019-03-27
 */

@Mapper
public interface PetitionEventProgressMapper extends BaseMapper<PetitionEventProgress> {

    List<PetitionEventProgressDTO> listProgressByEventId(@Param(value = "eventId")Long eventId);
}
