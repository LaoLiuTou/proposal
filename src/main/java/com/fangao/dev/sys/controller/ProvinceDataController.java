package com.fangao.dev.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.fangao.dev.core.web.BaseController;
import com.fangao.dev.sys.entity.DataProvince;
import com.fangao.dev.sys.service.IDataProvinceService;
import com.fangao.dev.sys.service.IProvinceDataService;
import com.fangao.dev.sys.utils.FileUtil;
import com.fangao.dev.sys.vo.DataProvinceContentTypeVO;
import com.fangao.dev.sys.vo.DataProvinceCountVO;
import com.fangao.dev.sys.vo.DataProvinceSolveVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * <p>
 * 省数据管理 前端控制器
 * </p>
 *
 */
@Api(tags = {"省数据"})
@RestController
@RequestMapping("/province/data")
public class ProvinceDataController extends BaseController<IDataProvinceService,DataProvince>{
    @Autowired
    private IProvinceDataService provinceDataService;

    @ApiOperation(value = "分页查询")
    @GetMapping("/page")
    public R<IPage<DataProvince>> page(DataProvince data) {
        return success(baseService.page(getPage(), data));
    }

    @ApiOperation(value = "上传excel")
    @PostMapping("/uploadExcel")
    public R<Boolean> upload(MultipartFile file) throws IOException,ParseException {
        byte[] fileBytes = file.getBytes();
        String filePath = ResourceUtils.getURL("classpath:").getPath()+"province/";
        //取得当前上传文件的文件名称
        String originalFilename = file.getOriginalFilename();
        //生成文件名
        String fileName =originalFilename;
        FileUtil.uploadFile(fileBytes, filePath, fileName);
        return success(provinceDataService.importExcel(filePath+ originalFilename,originalFilename));
    }

    @ApiOperation(value = "办结率统计")
    @GetMapping("/statis/solve")
    public R<List<DataProvinceSolveVO>> statisSolve(@RequestParam("queryCheckDate") String queryCheckDate){
        String [] str = StringUtils.isEmpty(queryCheckDate)?(new String[]{"",""}):queryCheckDate.split("~");
        return success(baseService.statisSolve(str[0].trim(),str[1].trim()));
    }

    @ApiOperation(value = "信访数量统计")
    @GetMapping("/statis/count")
    public R<List<DataProvinceCountVO>> statisCount(@RequestParam("queryCheckDate") String queryCheckDate){
        String [] str = StringUtils.isEmpty(queryCheckDate)?(new String[]{"",""}):queryCheckDate.split("~");
        return success(baseService.statisCount(str[0].trim(),str[1].trim()));
    }

    @ApiOperation(value = "内容分类统计")
    @GetMapping("/statis/content/type")
    public R<List<DataProvinceContentTypeVO>> statisContentType(@RequestParam("queryCheckDate") String queryCheckDate){
        String [] str = StringUtils.isEmpty(queryCheckDate)?(new String[]{"",""}):queryCheckDate.split("~");
        return success(baseService.statisContentType(str[0].trim(),str[1].trim()));
    }
}
