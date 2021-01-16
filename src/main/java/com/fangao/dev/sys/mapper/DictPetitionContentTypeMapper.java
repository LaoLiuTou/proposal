package com.fangao.dev.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fangao.dev.sys.entity.DictPetitionContentType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 内容分类字典表 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2018-09-15
 */
@Mapper
public interface DictPetitionContentTypeMapper extends BaseMapper<DictPetitionContentType> {

    /**
     * <p>
     * 根据 id 查询所有子节点
     * </p>
     *
     * @param id ID
     * @return
     */
    List<DictPetitionContentType> queryChildrenIds(Serializable id);

    @Select("select * from dict_petition_content_type where name = #{name}")
    DictPetitionContentType getByName(String name);
}
