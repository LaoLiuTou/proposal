package com.fangao.dev.sys.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class StatisDTO {
    private long id;
    private String ids;
    private Date date;
    private String year;
    private String day;
    private String item;
    private double percent;
    private long batch;
    private long people;
    private long fiveOver;
    private long fiftyOver;
    private long oneHundredOver;
    private long twoHundredOver;

    public StatisDTO(long batch, long people, long fiveOver, long fiftyOver, long oneHundredOver, long twoHundredOver) {
        this.batch = batch;
        this.people = people;
        this.fiveOver = fiveOver;
        this.fiftyOver = fiftyOver;
        this.oneHundredOver = oneHundredOver;
        this.twoHundredOver = twoHundredOver;
    }

    public StatisDTO(long id, String item, double percent, long batch, long people) {
        this.id = id;
        this.item = item;
        this.percent = percent;
        this.batch = batch;
        this.people = people;
    }
}
