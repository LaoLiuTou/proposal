package com.fangao.dev.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fangao.dev.core.bean.SuperEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("data_province_org")
public class DataProvinceOrg extends SuperEntity {
    private Long pid;
    private Long rid;
    private Integer level;
    private String name;
    private String showName;
}
