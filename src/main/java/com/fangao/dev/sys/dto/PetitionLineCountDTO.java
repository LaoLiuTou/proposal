package com.fangao.dev.sys.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class PetitionLineCountDTO {
    private long id;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM")
    private Date visitDate;
}
