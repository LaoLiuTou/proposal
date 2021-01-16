package com.fangao.dev.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fangao.dev.sys.dto.DataProvinceQueryDTO;
import com.fangao.dev.sys.entity.DataProvince;
import com.fangao.dev.sys.vo.DataProvinceCountVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 */

@Mapper
public interface DataProvinceMapper extends BaseMapper<DataProvince> {

    List<DataProvince> queryData(IPage<DataProvince> page, @Param(value = "data") DataProvince data);

    List<DataProvinceQueryDTO> querySolve(@Param(value = "start") String start, @Param(value = "end") String end);

    List<DataProvinceQueryDTO> queryCount(@Param(value = "start") String start, @Param(value = "end") String end);

    List<DataProvinceQueryDTO> queryContentType(@Param(value = "start") String start, @Param(value = "end") String end);
}
