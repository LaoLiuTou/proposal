package com.fangao.dev.sys.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class DrawPetitionLineVO {
    private String date;
    private long value;
    private String type;
    private double rate;
}
