package com.fangao.dev.sys.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

//导入excel之前先进行导入数据的判断，该实体是错误信息提示
@Data
@Accessors(chain = true)
public class JdcImportExcelErrorDTO {

    @ApiModelProperty("错误行号")
    private Integer errorLineNumber;

    @ApiModelProperty("事件编号")
    private String eventNum;

    @ApiModelProperty("错误内容表头")
    private String tableHeadText;

    @ApiModelProperty("错误内容")
    private String errorContent;

    @ApiModelProperty("错误原因")
    private String reason;

}
