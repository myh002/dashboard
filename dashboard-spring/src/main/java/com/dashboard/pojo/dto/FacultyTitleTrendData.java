package com.dashboard.pojo.dto;

import lombok.Data;
import java.util.List;

@Data
public class FacultyTitleTrendData {
    private List<TitleDistribution> yearlyData;
    
    @Data
    public static class TitleDistribution {
        private Integer year;
        private Integer seniorTitle;
        private Integer seniorHigh;
        private Integer middle;
        private Integer junior;
        private Integer seniorTech;
    }
}
