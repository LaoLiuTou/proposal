package com.fangao.dev.sys.dto;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fangao.dev.sys.entity.PetitionEventProgress;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 事项进展表
 * </p>
 *
 * @author ykb
 * @since 2019-03-27
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("petition_event_progress")
public class PetitionEventProgressDTO extends PetitionEventProgress {

    @ApiModelProperty("处理单位名称")
    private String unitName;

    @ApiModelProperty("添加人名称")
    private String userName;


}
