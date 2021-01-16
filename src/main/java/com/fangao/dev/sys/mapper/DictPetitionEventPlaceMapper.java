package com.fangao.dev.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fangao.dev.sys.entity.DictPetitionEventPlace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 事发地字典表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
@Mapper
public interface DictPetitionEventPlaceMapper extends BaseMapper<DictPetitionEventPlace> {
    @Select("select * from dict_petition_event_place where name LIKE CONCAT('%',#{name},'%')")
    List<DictPetitionEventPlace> getByName(String name);
}
