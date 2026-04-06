package com.dashboard.pojo.dto;

import lombok.Data;
import java.util.List;

@Data
public class ConditionData {
    private DualYearData landArea;
    private DualYearData labArea;
    private DualYearData teachingAdminArea;
    private DualYearData fixedAssets;
    private AssetOverview assetOverview;
    private List<EquipmentTrendItem> equipmentTrend;

    @Data
    public static class DualYearData {
        private Integer lastYear;
        private Integer newThisYear;
    }

    @Data
    public static class AssetOverview {
        private Double teachingResearchArea;
        private Double equipmentValue;
        private Double bookTotal;
        private Integer databaseCount;
    }

    @Data
    public static class EquipmentTrendItem {
        private Integer year;
        private Double vertical;
        private Double horizontal;
    }
}
