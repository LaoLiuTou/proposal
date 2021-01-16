package com.fangao.dev.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fangao.dev.sys.dto.FollowPersonDTO;
import com.fangao.dev.sys.entity.FollowPerson;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * s随访人基本信息表 Mapper 接口
 * </p>
 *
 * @author ykb
 * @since 2019-03-24
 */

@Mapper
public interface FollowPersonMapper extends BaseMapper<FollowPerson> {

    List<FollowPersonDTO> getFollowPersonListByEventId(@Param(value = "eventId") long id);
}
