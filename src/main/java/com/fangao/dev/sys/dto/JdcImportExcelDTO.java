package com.fangao.dev.sys.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class JdcImportExcelDTO extends JdcDTO {

    @ApiModelProperty("是否领导包案文字内容  是/否")
    private String isLeaderHadText;

    @ApiModelProperty("领导层级ID")
    private Long dictPetitionLeaderLevelId;

    @ApiModelProperty("领导层级文字内容 ")
    private String dictPetitionLeaderLevelText;

    @ApiModelProperty("领导姓名 ")
    private String leaderIdText;

    @ApiModelProperty("是否属实文字内容  是/否")
    private String isVerifiedText;

    @ApiModelProperty("是否有理文字内容  是/部分/否")
    private String isReasonableText;

    @ApiModelProperty("是否最终意见  是/否")
    private String isFinalOpinionText;
}
