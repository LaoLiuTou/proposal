package com.fangao.dev.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fangao.dev.sys.entity.Org;
import com.fangao.dev.sys.entity.Org_place;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface Org_placeMapper extends BaseMapper<Org> {
    @Select("select place_name from org_place where org_name = #{org_name} ORDER BY id")
    List<String> getPlace_nameByOrgName(String org_name);
}
