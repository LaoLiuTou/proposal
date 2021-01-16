package com.fangao.dev.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fangao.dev.sys.dto.DjcDTO;
import com.fangao.dev.sys.dto.JdcDTO;
import com.fangao.dev.sys.dto.JdcExportExcelDTO;
import com.fangao.dev.sys.dto.JdcImportExcelErrorListDTO;
import com.fangao.dev.sys.vo.DrawPetitionLineVO;
import com.fangao.dev.sys.vo.DrawPetitionPieVO;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by wdai on 2019-3-26.
 */
public interface JdcImportExcelService {

    /**
     * @Description: 获取要导出的excel的文件
     */
    JdcImportExcelErrorListDTO jdcImportExcel(long orgId, String path,String fileName) throws IOException;
}
