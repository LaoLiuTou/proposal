package com.fangao.dev.sys.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DataProvinceQueryDTO {
    private Long id;
    private String name;
    private Long formatId;
    private Long contentTypeId;
    private Integer solve;
    private Integer count;
    private Integer people;
}
