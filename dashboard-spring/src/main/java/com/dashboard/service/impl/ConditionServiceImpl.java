package com.dashboard.service.impl;

import com.dashboard.pojo.dto.ConditionData;
import com.dashboard.mapper.ConditionMapper;
import com.dashboard.service.ConditionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ConditionServiceImpl implements ConditionService {

    private final ConditionMapper conditionMapper;

    @Override
    public ConditionData getConditionData(Integer year) {
        ConditionData data = new ConditionData();

        // 学校面积 (国资处 metric 134)
        data.setLandArea(buildDualYear(
                intOf(conditionMapper.getMetricValue("国资处", 134, year - 1)),
                intOf(conditionMapper.getMetricValue("国资处", 134, year))
        ));

        // 实验室面积 (国资处 metric 136)
        data.setLabArea(buildDualYear(
                intOf(conditionMapper.getMetricValue("国资处", 136, year - 1)),
                intOf(conditionMapper.getMetricValue("国资处", 136, year))
        ));

        // 教学行政用房 (国资处 metric 135)
        data.setTeachingAdminArea(buildDualYear(
                intOf(conditionMapper.getMetricValue("国资处", 135, year - 1)),
                intOf(conditionMapper.getMetricValue("国资处", 135, year))
        ));

        // 固定资产 (国资处 metric 138, 万元)
        data.setFixedAssets(buildDualYear(
                intOf(conditionMapper.getMetricValue("国资处", 138, year - 1)),
                intOf(conditionMapper.getMetricValue("国资处", 138, year))
        ));

        // 资产概况
        ConditionData.AssetOverview overview = new ConditionData.AssetOverview();
        overview.setEquipmentValue(
                conditionMapper.getMetricValue("国资处", 140, year) / 100.0);
        overview.setBookTotal(
                conditionMapper.getMetricValue("图书馆", 49, year) / 1.0);
        overview.setTeachingResearchArea(
                conditionMapper.getMetricValue("国资处", 135, year) / 1.0);
        overview.setDatabaseCount(
                intOf(conditionMapper.getMetricValue("图书馆", 51, year)));
        data.setAssetOverview(overview);

        // 设备趋势
        data.setEquipmentTrend(getEquipmentTrend());

        return data;
    }

    public List<ConditionData.EquipmentTrendItem> getEquipmentTrend() {
        List<Map<String, Object>> countRows = conditionMapper.getEquipmentTrend(139);
        List<Map<String, Object>> valueRows = conditionMapper.getEquipmentTrend(140);

        Map<Integer, Double> countMap = new HashMap<>();
        for (Map<String, Object> row : countRows) {
            countMap.put(((Number) row.get("year")).intValue(),
                    ((Number) row.get("value")).doubleValue());
        }
        Map<Integer, Double> valueMap = new HashMap<>();
        for (Map<String, Object> row : valueRows) {
            valueMap.put(((Number) row.get("year")).intValue(),
                    ((Number) row.get("value")).doubleValue());
        }

        Set<Integer> years = new TreeSet<>(countMap.keySet());
        years.addAll(valueMap.keySet());

        List<ConditionData.EquipmentTrendItem> trend = new ArrayList<>();
        for (Integer y : years) {
            ConditionData.EquipmentTrendItem item = new ConditionData.EquipmentTrendItem();
            item.setYear(y);
            item.setVertical(countMap.getOrDefault(y, 0.0));
            item.setHorizontal(valueMap.getOrDefault(y, 0.0));
            trend.add(item);
        }
        return trend;
    }

    private ConditionData.DualYearData buildDualYear(int last, int current) {
        ConditionData.DualYearData d = new ConditionData.DualYearData();
        d.setLastYear(last);
        d.setNewThisYear(current - last);     // 改为增量语义
        return d;
    }

    private int intOf(Long v) {
        return v != null ? v.intValue() : 0;
    }
}
