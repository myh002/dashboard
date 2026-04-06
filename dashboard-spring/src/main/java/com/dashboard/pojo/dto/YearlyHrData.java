package com.dashboard.pojo.dto;

import lombok.Data;

@Data
public class YearlyHrData {
    private String year;
    private StaffData staff;
    private EducationData education;
    private TitleData title;
    private TalentsData talents;

    @Data
    public static class StaffData {
        private Integer total;
        private Integer fullTime;
        private Integer management;
        private Integer supporting;
        private Integer external;
    }

    @Data
    public static class EducationData {
        private Integer doctorate;
        private Integer master;
        private Integer bachelor;
    }

    @Data
    public static class TitleData {
        private Integer professor;
        private Integer associate;
        private Integer lecturer;
        private Integer assistant;
    }

    @Data
    public static class TalentsData {
        private Integer nationalMaster;
        private Integer provincialMaster;
        private Integer huangdanianTeam;
        private Integer nationalTeam;
        private Integer provincialTeam;
    }
}
