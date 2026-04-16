package com.dashboard.pojo.dto;

import lombok.Data;
import java.util.List;

@Data
public class FacultyData {
    private Integer totalStaff;
    private Integer totalTeachers;
    private Integer graduateSupervisors;
    private TitleDistribution titleDistribution;
    private List<TalentItem> topTalents;

    @Data
    public static class TitleDistribution {
        private Integer seniorHigh;
        private Integer seniorTech;
        private Integer seniorTitle;
        private Integer junior;
        private Integer middle;
    }

    @Data
    public static class TalentItem {
        private String category;
        private Integer count;
        private Integer newIncrement;
        private String icon;
        private String unit;
    }
}
