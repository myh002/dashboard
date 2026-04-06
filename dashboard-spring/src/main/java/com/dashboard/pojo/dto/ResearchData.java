package com.dashboard.pojo.dto;

import lombok.Data;
import java.util.List;

@Data
public class ResearchData {
    private DualYearData papers;
    private DualYearData publications;
    private DualYearData patents;
    private DualYearData bases;
    private ProjectStats projectStats;
    private List<FundingTrendItem> fundingTrend;

    @Data
    public static class DualYearData {
        private Integer lastYear;
        private Integer newThisYear;
    }

    @Data
    public static class ProjectStats {
        private Integer total;
        private Integer provincial;
        private Integer prefectural;
        private Integer institutional;
    }

    @Data
    public static class FundingTrendItem {
        private Integer year;
        private Double vertical;
        private Double horizontal;
    }
}
