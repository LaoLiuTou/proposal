package com.fangao.dev.sys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class StatisQueryEventDTO {
    private String start;
    private String end;
    private String name;
    private String petitionerName;
    private Long receptionOrgId;
    private Long eventPlaceId;
    private Long cityUnitId;
    private Long contentTypeId;
    private Long dutyUnitId;
    private List<Long> dutyUnitIds;
    private List<Long> contentTypeIds;
}
