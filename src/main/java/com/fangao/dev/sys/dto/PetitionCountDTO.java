package com.fangao.dev.sys.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class PetitionCountDTO {
    private long count;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date visitDate;
}
