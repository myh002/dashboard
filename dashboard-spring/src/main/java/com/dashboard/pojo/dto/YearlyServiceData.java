package com.dashboard.pojo.dto;

import lombok.Data;

@Data
public class YearlyServiceData {
    private String year;
    private CooperationData cooperation;
    private EmploymentData employment;
    private InternationalData international;
    private ExpertsData experts;

    @Data
    public static class CooperationData {
        private Double horizontalFunding;
        private Integer inventionPatents;
        private Integer utilityPatents;
        private Integer standards;
        private Integer software;
    }

    @Data
    public static class EmploymentData {
        private Integer undergraduate;
        private Double undergraduateRate;
        private Integer master;
        private Double masterRate;
        private Integer phd;
        private Double phdRate;
    }

    @Data
    public static class InternationalData {
        private Integer internationalStudents;
        private Integer cooperativePrograms;
        private Integer practiceBases;
    }

    @Data
    public static class ExpertsData {
        private Integer externalTeachers;
    }
}
