package com.fangao.dev.sys.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;


@Data
@Accessors(chain = true)
public class DataProvinceSolveVO {
    private Long id;
    private String name;
    private Integer count;
    private Integer inDateCount;
    private Integer overDateCount;
    private Double datePercent;
    private Integer solveCount;
    private Integer unSolveCount;
    private Double solvePercent;
}
