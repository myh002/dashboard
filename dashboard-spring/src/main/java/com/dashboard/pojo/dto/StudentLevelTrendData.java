package com.dashboard.pojo.dto;

import lombok.Data;

@Data
public class StudentLevelTrendData {
    private String year;
    private Integer undergraduate;
    private Integer master;
    private Integer phd;

    public StudentLevelTrendData() {}

    public StudentLevelTrendData(String year, Integer undergraduate, Integer master, Integer phd) {
        this.year = year;
        this.undergraduate = undergraduate;
        this.master = master;
        this.phd = phd;
    }
}
