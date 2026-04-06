package com.dashboard.pojo.dto;

import lombok.Data;

@Data
public class YearlyDisciplineData {
    private String year;
    private DisciplinesData disciplines;
    private MajorsData majors;
    private DegreePointsData degreePoints;
    private Integer colleges;

    @Data
    public static class DisciplinesData {
        private Integer provincialKey;
        private Integer provincialEmphasis;
        private Integer firstClassConstruction;
        private Integer firstClass;
    }

    @Data
    public static class MajorsData {
        private Integer ministryFeature;
        private Integer provincialFeature;
        private Integer provincialDemonstration;
        private Integer provincialQualityCourse;
        private Integer undergraduateTotal;
        private Integer newThisYear;
        private Integer discontinued;
    }

    @Data
    public static class DegreePointsData {
        private Integer postdoctoral;
        private Integer doctoralFirst;
        private Integer masterFirst;
        private Integer masterSecond;
        private Integer professional;
        private Integer total;
    }
}
