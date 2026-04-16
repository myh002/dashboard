package com.dashboard.pojo.dto;

import lombok.Data;

@Data
public class YearlyFinanceData {
    private String year;
    private CampusData campus;
    private AssetsData assets;
    private ResearchData research;
    private LibraryData library;

    @Data
    public static class CampusData {
        private Double schoolArea;
        private Double teachingArea;
        private Double labArea;
        private Double dormitoryArea;
        private Double horizontalFunding;
    }

    @Data
    public static class AssetsData {
        private Long fixedAssets;
        private Integer equipmentCount;
        private Double equipmentValue;
        private Integer largeEquipmentCount;
        private Double largeEquipmentValue;
    }

    @Data
    public static class ResearchData {
        private Double verticalFunding;
    }

    @Data
    public static class LibraryData {
        private Long bookCount;
        private Long ebookCount;
        private Long ejournalCount;
    }
}
