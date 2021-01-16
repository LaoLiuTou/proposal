package com.fangao.dev.sys.vo;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class DataProvinceCountVO {
    private Long id;
    private String name;
    private Integer count=0;
    private Integer people=0;
    private Integer countLetter=0;
    private Integer peopleLetter=0;
    private Integer countVisit=0;
    private Integer peopleVisit=0;
    private Integer countInternet=0;
    private Integer peopleInternet=0;
    private Integer countMobile=0;
    private Integer peopleMobile=0;
    private Integer countWeChat=0;
    private Integer peopleWeChat=0;
    private Integer countAdvise=0;
    private Integer peopleAdvise=0;
    private Integer countPhone=0;
    private Integer peoplePhone=0;
    private Integer countEmail=0;
    private Integer peopleEmail=0;
}
