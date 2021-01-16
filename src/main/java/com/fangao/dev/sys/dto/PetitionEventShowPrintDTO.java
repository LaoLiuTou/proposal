package com.fangao.dev.sys.dto;

import com.fangao.dev.core.bean.SuperEntity;
import com.fangao.dev.sys.entity.PetitionEventExtraHandle;
import com.fangao.dev.sys.entity.PetitionEventExtraMeeting;
import com.fangao.dev.sys.entity.PetitionEventExtraPraise;
import com.fangao.dev.sys.entity.PetitionEventExtraRevisit;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
public class PetitionEventShowPrintDTO extends SuperEntity {
    @ApiModelProperty(value = "事项名称")
    private String name;
    @ApiModelProperty(value = "姓名")
    private String petitioner;
    @ApiModelProperty(value = "身份证号")
    private String idcard;
    @ApiModelProperty(value = "户籍地址")
    private String permanentAddress;
    @ApiModelProperty(value = "居住地")
    private String currentAddress;
    @ApiModelProperty(value = "联系电话")
    private String mobileNo;
    @ApiModelProperty(value = "来访日期")
    private String visitDate;
    @ApiModelProperty(value = "信访件编号")
    private String eventNum;
    @ApiModelProperty(value = "登记地点")
    private String djOrg;
    @ApiModelProperty(value = "反映问题")
    private String eventContent;
    @ApiModelProperty(value = "主要诉求")
    private String mainDemand;
    @ApiModelProperty(value = "是否重点领域")
    private String isKeyArea;
    @ApiModelProperty(value = "重点领域分类")
    private String keyArea;
    @ApiModelProperty(value = "事发地")
    private String eventPlace;
    @ApiModelProperty(value = "来访人数")
    private String comeNum;
    @ApiModelProperty(value = "涉及人数")
    private String involveNum;
    @ApiModelProperty(value = "是否集体访")
    private String isGroupVisit;
    @ApiModelProperty(value = "随访人信息")
    private String followPerson;
    @ApiModelProperty(value = "是否重访")
    private String isRepeat;
    @ApiModelProperty(value = "问题分类")
    private String contentType;
    @ApiModelProperty(value = "上访地点")
    private String visitPlaceStatus;
    @ApiModelProperty(value = "上访异常地点")
    private String abnormalPlace;
    @ApiModelProperty(value = "上访行为")
    private String visitActionStatus;
    @ApiModelProperty(value = "上访异常行为")
    private String abnormalAction;
    @ApiModelProperty(value = "责任单位")
    private String dutyUnit;
    @ApiModelProperty(value = "配合单位")
    private String coUnit;
    @ApiModelProperty(value = "稳控单位名称")
    private String wkdwName;
    @ApiModelProperty(value = "是否办结")
    private String solveState;
    @ApiModelProperty(value = "是否滞留")
    private String isHoldUp;
    @ApiModelProperty(value = "是否跟访")
    private String isFollow;
    @ApiModelProperty(value = "跟访内容")
    private String followContent;
    @ApiModelProperty(value = "工作建议")
    private String workProposal;
    @ApiModelProperty(value = "问题症结")
    private String crux;
    @ApiModelProperty(value = "问题是否属实")
    private String isVerified;
    @ApiModelProperty(value = "诉求合理性")
    private String isReasonable;
    @ApiModelProperty(value = "核实情况")
    private String verifyDetail;
    @ApiModelProperty(value = "理由")
    private String unreasonableReason;
    @ApiModelProperty(value = "最新工作进展")
    private String newestProgress;
    @ApiModelProperty(value = "是否领导包案")
    private String isLeaderHad;
    @ApiModelProperty(value = "领导层级姓名")
    private String leader;
    @ApiModelProperty(value = "是否有最终处理意见")
    private String isFinalOpinion;
    @ApiModelProperty(value = "最终处理意见")
    private String finalOpinion;
    @ApiModelProperty(value = "化解方式")
    private String dictSolve;
    @ApiModelProperty(value = "满意度")
    private String dictSatisfaction;
    @ApiModelProperty(value = "回访情况记录")
    private List<PetitionEventExtraRevisit> extraRevisit;
    @ApiModelProperty(value = "上级表扬记录")
    private List<PetitionEventExtraPraise> extraPraise;
    @ApiModelProperty(value = "督办记录")
    private List<PetitionEventExtraHandle> extraHandle;
    @ApiModelProperty(value = "协调会记录")
    private List<PetitionEventExtraMeeting> extraMeeting;
}
