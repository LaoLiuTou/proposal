package com.fangao.dev.sys.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

//导入excel之前先进行导入数据的判断，该实体是错误信息提示
@Data
@Accessors(chain = true)
public class JdcImportExcelErrorListDTO {

    @ApiModelProperty("出错的文件名")
    private String errorFileName;

    @ApiModelProperty("检测出的错误")
    private Integer errorNums;

    @ApiModelProperty("导入的总事项个数")
    private Integer totalEventNums;

    @ApiModelProperty("导入成功事项个数")
    private Integer successEventNums;

    @ApiModelProperty("导入失败事项个数")
    private Integer errorEventNums;

    @ApiModelProperty("错误内容")
    private List<JdcImportExcelErrorDTO> errorList;


}
