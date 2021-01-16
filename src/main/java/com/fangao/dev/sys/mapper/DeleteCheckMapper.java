package com.fangao.dev.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fangao.dev.sys.dto.CheckDTO;
import com.fangao.dev.sys.entity.DeleteCheck;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 删除审核表 Mapper 接口
 * </p>
 *
 * @author ykb
 * @since 2019-03-27
 */

@Mapper
public interface DeleteCheckMapper extends BaseMapper<DeleteCheck> {

    List<CheckDTO> selectDeleteCheck(Page page,@Param(value = "checkDTO") CheckDTO checkDTO);
}
