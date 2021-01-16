package com.fangao.dev.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fangao.dev.sys.entity.DataProvince;
import com.fangao.dev.sys.vo.DataProvinceContentTypeVO;
import com.fangao.dev.sys.vo.DataProvinceCountVO;
import com.fangao.dev.sys.vo.DataProvinceSolveVO;

import java.util.List;

/**
 * <p>
 * 接口
 * </p>
 *
 */
public interface IDataProvinceService extends IService<DataProvince> {
    IPage<DataProvince> page(Page page, DataProvince data);
    List<DataProvinceSolveVO> statisSolve(String start,String end);
    List<DataProvinceCountVO> statisCount(String start,String end);
    List<DataProvinceContentTypeVO> statisContentType(String start, String end);
}
