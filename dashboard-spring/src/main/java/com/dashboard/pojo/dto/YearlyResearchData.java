package com.dashboard.pojo.dto;

import lombok.Data;
import java.util.List;

@Data
public class YearlyResearchData {
    private String year;
    private ProjectsData projects;
    private PlatformsData platforms;
    private FundingData funding;
    private AwardsData awards;
    private PublicationsData publications;
    private PapersData papers;

    @Data
    public static class ProjectsData {
        private Integer national;
        private Integer ministerial;
        private Integer provincial;
        private Integer prefectural;
        private Integer school;
        private Integer total;
    }

    @Data
    public static class PlatformsData {
        private Integer national;
        private Integer provincial;
        private Integer other;
    }

    @Data
    public static class FundingData {
        private Double vertical;
        private Double horizontal;
        private Double total;
    }

    @Data
    public static class AwardsData {
        private Integer first;
        private Integer second;
        private Integer third;
    }

    @Data
    public static class PublicationsData {
        private Integer books;
        private PatentsData patents;
        private Integer varieties;
        private Integer standards;
        private Integer software;
        private Integer total;
    }

    @Data
    public static class PatentsData {
        private Integer invention;
        private Integer utility;
        private Integer design;
    }

    @Data
    public static class PapersData {
        private Integer topTierJournals;
        private Integer sciQ1;
        private Integer sciQ2;
        private Integer sciQ3;
        private Integer sciQ4;
        private Integer ei;
        private Integer cscd;
        private Integer core;
        private Integer other;
        private Integer total;
    }
}
