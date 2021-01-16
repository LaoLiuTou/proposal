package com.fangao.dev.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fangao.dev.sys.dto.CountPetitonerAgeDTO;
import com.fangao.dev.sys.dto.CountPetitonerNumDTO;
import com.fangao.dev.sys.dto.CountPetitonerSexDTO;
import com.fangao.dev.sys.dto.DjcDTO;
import com.fangao.dev.sys.entity.PetitionerInfo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 访问人信息表
 * </p>
 *
 * @author ykb
 * @since 2019-03-24
 */
public interface IPetitionerInfoService extends IService<PetitionerInfo> {
    /*查询带事项编号的信访人员信息，按时间倒序排列*/
    IPage<DjcDTO> page(Page page, DjcDTO djcDTO);

    /*根据set值只更新部分字段的信息*/
    int updateForSet(PetitionerInfo petitionerInfo);

    /*统计每天信访人数*/
    List<CountPetitonerNumDTO> countPetitonerNumOfDate();

    /*统计男女比例*/
    List<CountPetitonerSexDTO> countPetitonerSex();

    /*统计年龄段人数*/
    Map<String,Object> countPetitonerAge();
}
