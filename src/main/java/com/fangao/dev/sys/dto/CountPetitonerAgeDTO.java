package com.fangao.dev.sys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class CountPetitonerAgeDTO {
    private String item;
    private long count;
    private double percent;
}
