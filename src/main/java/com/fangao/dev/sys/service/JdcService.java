package com.fangao.dev.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fangao.dev.sys.dto.*;
import com.fangao.dev.sys.entity.PetitionEventInfo;
import com.fangao.dev.sys.entity.PetitionEventProgress;
import com.fangao.dev.sys.vo.DrawPetitionLineVO;
import com.fangao.dev.sys.vo.DrawPetitionPieVO;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by wdai on 2019-3-26.
 */
public interface JdcService {
    boolean editInfo(DjcDTO djcDTO);

    IPage<JdcDTO> djdrPage(Page page, JdcDTO jdcDTO);

    R<Boolean> addEventInfo(JdcDTO jdcDTO);

    R<Boolean> editEventInfo(JdcDTO jdcDTO);

    IPage<JdcDTO> xxglPage(Page page, JdcDTO jdcDTO);

    List<Map<String,Object>> xxglPageExport(JdcDTO jdcDTO);

    R<Boolean> addProgress(String leaderName,Long levelId,JdcDTO jdcDTO);

    List<JdcDTO> getRepeatList(JdcDTO jdcDTO);

    // 接待数
    long statisJDNum(int type);

    // 接待批次和人次
    StatisDTO statisJDCountAndPeople(String start , String end);

    // 重访数
    long statisCFNum(int type);

    // 纯件批次和人次
    StatisDTO statisCJCountAndPeople(String start , String end);

    // 已处理数
    long statisYCLNum(int type);

    // 已处理批次和人次
    StatisDTO statisYCLCountAndPeople(String start , String end);

    // 待接待、处理中、已处理 饼图
    List<DrawPetitionPieVO> statisPetitionPie(int type);

    // 登记数、接待数 柱形折线图
    List<DrawPetitionLineVO> statisPetitionLine(int type);
    /**
     * @Description: 获取要导出的excel的文件
     */
    List<File> jdcExportExcelFileList(long orgId,JdcExportExcelDTO jdcExportExcelDTO);
    File jdcExportWeek_Report_ExcelFileList(long orgId,JdcExportWeekReportExcelDTO jdcExportWeekReportExcelDTO);
    File jdcExportBriefing_ExcelFileList(String begin_date,String end_date);
    List<JdcBriefingDTO> jdcExportBriefing(String begin_date,String end_date);
    boolean addPep(PetitionEventProgress pep);

    void exportExcelNew(String begin_date,String end_date,HttpServletResponse response);
}
