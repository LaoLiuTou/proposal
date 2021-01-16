package com.fangao.dev.sys.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProvinceDataImportExcelErrorDTO {
    private String message;
}
