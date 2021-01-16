package com.fangao.dev.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fangao.dev.sys.dto.DjcDTO;
import com.fangao.dev.sys.dto.StatisDTO;
import com.fangao.dev.sys.entity.PetitionEventInfo;

import java.util.List;

/**
 * Created by wdai on 2019-3-26.
 */
public interface DjcService {
    R<Boolean> addInfo(DjcDTO djcDTO);

    String addInfoGetEventNum(DjcDTO djcDTO);

    PetitionEventInfo addEventInfo(DjcDTO djcDTO);

    boolean addEventInfoAndProcess(DjcDTO djcDTO);

    boolean addEventInfoAndProcess(List<DjcDTO> djcDTOList);

    IPage<DjcDTO> page(Page page, DjcDTO djcDTO);

    boolean editInfo(DjcDTO djcDTO);

    // 登记数
    long statisDJNum(int type);

    // 登记批次和人次
    StatisDTO statisDJCountAndPeople(String start , String end);

    // 日登记批次和人次
    List<StatisDTO> statisEveryDayCountAndPeople(String start , String end);
}
