package com.dashboard.pojo.dto;

import lombok.Data;

@Data
public class TalentData {
    private Integer departmentCount;
    private Integer majorCount;
    private Integer courseCount;
    private StudentStats studentStats;
    private StudentLevels studentLevels;

    @Data
    public static class StudentStats {
        private Integer total;
        private Integer undergraduate;
        private Integer master;
        private Integer phd;
        private Integer totalIncrement;
        private Integer undergraduateIncrement;
        private Integer masterIncrement;
        private Integer phdIncrement;
    }

    @Data
    public static class StudentLevels {
        private Integer undergraduateTotal;
        private Integer masterTotal;
        private Integer phdTotal;
    }
}
