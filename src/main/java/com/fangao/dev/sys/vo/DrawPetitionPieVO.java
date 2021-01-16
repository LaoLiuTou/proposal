package com.fangao.dev.sys.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class DrawPetitionPieVO {
    private String item;
    private long count;
    private double percent;
}
