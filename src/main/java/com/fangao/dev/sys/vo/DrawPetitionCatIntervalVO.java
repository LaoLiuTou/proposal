package com.fangao.dev.sys.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class DrawPetitionCatIntervalVO {
    private String type;
    private long value;
    private String cat;
}
