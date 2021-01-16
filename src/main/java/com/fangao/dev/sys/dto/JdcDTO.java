package com.fangao.dev.sys.dto;

import com.fangao.dev.sys.entity.PetitionEventInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ykb on 2019-3-27.
 */
@Data
@Accessors(chain = true)
public class JdcDTO extends PetitionEventInfo {
    @ApiModelProperty("内容类型id的字符串形式，用于接收前台数据")
    private String progressContent;

    @ApiModelProperty("事项进展处理单位编号")
    private long handUnitId;

    @ApiModelProperty("内容类型id的字符串形式，用于接收前台数据")
    private String contentTypeIdStr;

    @ApiModelProperty("配合单位编号")
    private String coUnitIds;

    @ApiModelProperty("异常上方地点")
    private String abnormalPlaceIds;

    @ApiModelProperty("上访异常行为")
    private String abnormalActionIds;

    @ApiModelProperty(value = "名称")
    private String petitionerName;

    @ApiModelProperty(value = "接待人真实姓名，用于展示")
    private String receptionName;

    @ApiModelProperty(value = "负责单位名称，用于展示")
    private String dutyUnitName;

    @ApiModelProperty(value = "事发地名称，用于展示")
    private String eventPlaceName;

    @ApiModelProperty(value = "内容分类名称，用于展示")
    private String contentTypeName;

    @ApiModelProperty(value = "一级内容分类名称，用于展示")
    private String firstContentTypeName;

    @ApiModelProperty(value = "性别 0、男，1、女")
    private Integer sex;

    @ApiModelProperty(value = "民族")
    private String nation;

    @ApiModelProperty(value = "内容分类ids，用于展示")
    private String contentTypeIds;

    @ApiModelProperty(value = "出生日期")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date birthDate;

    @ApiModelProperty(value = "户籍地址")
    private String permanentAddress;

    @ApiModelProperty(value = "现住地址")
    private String currentAddress;

    @ApiModelProperty(value = "手机号")
    private String mobileNo;

    @ApiModelProperty(value = "所关联事项，0：无关联事项，1：所关联事项为处理中，2：所关联事项为已处理")
    private Integer repeatFlag;

    @ApiModelProperty(value = "所关联事项id")
    private Long repeatId;

    @ApiModelProperty(value = "登记组织名称")
    private String djOrg;

    @ApiModelProperty(value = "接待组织名称")
    private String receptionOrg;

    @ApiModelProperty(value = "事发地名称")
    private String eventPlace;

    @ApiModelProperty(value = "随访人信息")
    private String followPerson;

    @ApiModelProperty(value = "重点领域名称")
    private String keyArea;

    @ApiModelProperty(value = "异常地点")
    private String abnormalPlace;

    @ApiModelProperty(value = "异常行为")
    private String abnormalAction;

    @ApiModelProperty(value = "配合单位")
    private String coUnit;

    @ApiModelProperty(value = "领导层级ID")
    private String leaderLevelId;

    @ApiModelProperty(value = "领导层级")
    private String leaderLevel;

    @ApiModelProperty(value = "领导姓名")
    private String leader;

    @ApiModelProperty(value = "事项状态名称")
    private String statusName;

    @ApiModelProperty(value = "是否重点领域名称")
    private String isKeyAreaName;

    @ApiModelProperty(value = "上访地点是否正常名称")
    private String visitPlaceStatusName;

    @ApiModelProperty(value = "上访行为是否正常名称")
    private String visitActionStatusName;

    @ApiModelProperty(value = "是否集体访名称")
    private String isGroupVisitName;

    @ApiModelProperty(value = "是否重访名称")
    private String isRepeatName;

    @ApiModelProperty(value = "是否滞留名称")
    private String isHoldUpName;

    @ApiModelProperty(value = "是否跟访名称")
    private String isFollowName;

    @ApiModelProperty(value = "问题是否属实名称")
    private String isVerifiedName;

    @ApiModelProperty(value = "诉求合理性名称")
    private String isReasonableName;

    @ApiModelProperty(value = "是否领导包案名称")
    private String isLeaderHadName;

    @ApiModelProperty(value = "是否有最终处理意见名称")
    private String isFinalOpinionName;

    @ApiModelProperty(value = "高级搜索")
    private String highSearch;

    @ApiModelProperty(value = "办结状态")
    private String solveStateName;

    @ApiModelProperty(value = "随访人信息")
    private List<Map> followerList;

    @ApiModelProperty(value = "删除审核状态，0：待审核，1：通过，2：驳回")
    private int deleteCheckStatus=-1;

    @ApiModelProperty(value = "随访人信息，用于编辑")
    private List<FollowPersonDTO> followers;


}
