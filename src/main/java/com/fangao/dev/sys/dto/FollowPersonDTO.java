package com.fangao.dev.sys.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class FollowPersonDTO{
    @ApiModelProperty(value = "事项id")
    private String eventId;

    @ApiModelProperty(value = "身份证号")
    private String idcard;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "性别 0、男，1、女")
    private Integer sex;

    @ApiModelProperty(value = "民族")
    private String nation;

    @ApiModelProperty(value = "出生日期")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date birthDate;

    @ApiModelProperty(value = "户籍地址")
    private String permanentAddress;

    @ApiModelProperty(value = "现住地址")
    private String currentAddress;

    @ApiModelProperty(value = "手机号")
    private String mobileNo;

}
