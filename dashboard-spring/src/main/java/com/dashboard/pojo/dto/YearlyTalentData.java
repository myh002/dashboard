package com.dashboard.pojo.dto;

import lombok.Data;

@Data
public class YearlyTalentData {
    private String year;
    private UndergraduateData undergraduate;
    private MasterData master;
    private PhdData phd;
    private TeachingData teaching;
    private InternationalData international;

    @Data
    public static class UndergraduateData {
        private Integer total;
        private Integer male;
        private Integer female;
        private Integer graduates;
        private Double graduateRate;
        private Integer employment;
        private Double employmentRate;
    }

    @Data
    public static class MasterData {
        private Integer total;
        private Integer male;
        private Integer female;
        private Integer supervisors;
        private Integer graduates;
        private Double graduateRate;
        private Integer employment;
        private Double employmentRate;
    }

    @Data
    public static class PhdData {
        private Integer total;
        private Integer male;
        private Integer female;
        private Integer supervisors;
        private Integer graduates;
        private Double graduateRate;
        private Integer employment;
        private Double employmentRate;
    }

    @Data
    public static class TeachingData {
        private Integer courses;
        private Integer professorCourses;
        private Integer nationalReform;
        private Integer provincialReform;
        private Integer schoolReform;
        private Integer teachingAward;
        private Integer practiceBases;
    }

    @Data
    public static class InternationalData {
        private Integer cooperativePrograms;
        private Integer internationalStudents;
    }
}
