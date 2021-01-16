package com.fangao.dev.sys.dto;

import io.swagger.models.auth.In;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProblemDTO {
    private String name;
    private Integer event;
    private Integer come;
}
