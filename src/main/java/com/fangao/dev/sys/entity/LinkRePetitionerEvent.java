package com.fangao.dev.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fangao.dev.core.bean.SuperEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 重访事项与信访人关系表
 * </p>
 *
 * @author jobob
 * @since 2018-10-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("link_re_petitioner_event")
@NoArgsConstructor
public class LinkRePetitionerEvent extends SuperEntity {
    @ApiModelProperty("所关联事项id")
    private Long linkEventId;

    @ApiModelProperty("当前事项id")
    private Long eventId;

    @ApiModelProperty("信访人身份证")
    private String idcard;

    @ApiModelProperty("重访日期")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date visitDate;

    public LinkRePetitionerEvent(Long linkEventId, Long eventId, String idcard, Date visitDate) {
        this.linkEventId = linkEventId;
        this.eventId = eventId;
        this.idcard = idcard;
        this.visitDate = visitDate;
    }
}
