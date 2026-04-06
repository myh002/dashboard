package com.dashboard.service.impl;

import com.dashboard.pojo.dto.YearlyFinanceData;
import com.dashboard.mapper.FinanceMapper;
import com.dashboard.service.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FinanceServiceImpl implements FinanceService {

    private final FinanceMapper financeMapper;

    private static final int[] YEARS = {2020, 2021, 2022, 2023, 2024, 2025};

    private static final List<Integer> CAMPUS_METRIC_IDS = Arrays.asList(134, 135, 136, 137);
    private static final List<Integer> ASSETS_METRIC_IDS = Arrays.asList(138, 139, 140, 141, 142);
    private static final List<Integer> RESEARCH_METRIC_IDS = Arrays.asList(11, 12);

    @Override
    @Cacheable(value = "finance", key = "'yearly'")
    public List<YearlyFinanceData> getFinanceData() {
        Map<String, Map<Integer, Map<Integer, Long>>> allMetrics = loadAllMetrics();
        
        List<YearlyFinanceData> result = new ArrayList<>();
        for (int year : YEARS) {
            result.add(buildYearData(year, allMetrics));
        }
        return result;
    }

    private Map<String, Map<Integer, Map<Integer, Long>>> loadAllMetrics() {
        Map<String, Map<Integer, Map<Integer, Long>>> result = new HashMap<>();
        
        result.put("国资处", loadDeptMetrics("国资处", Arrays.asList(134, 135, 136, 137, 138, 139, 140, 141, 142)));
        result.put("科技处", loadDeptMetrics("科技处", RESEARCH_METRIC_IDS));
        
        return result;
    }

    private Map<Integer, Map<Integer, Long>> loadDeptMetrics(String deptName, List<Integer> metricIds) {
        List<Integer> yearList = new ArrayList<>();
        for (int y : YEARS) yearList.add(y);
        List<Map<String, Object>> rows = financeMapper.getMetricsBatch(deptName, yearList, metricIds);
        Map<Integer, Map<Integer, Long>> yearMap = new HashMap<>();
        
        for (int year : YEARS) {
            yearMap.put(year, new HashMap<>());
        }
        
        for (Map<String, Object> row : rows) {
            Integer year = ((Number) row.get("year")).intValue();
            Integer metricId = ((Number) row.get("metric_id")).intValue();
            Long value = ((Number) row.get("value")).longValue();
            yearMap.get(year).put(metricId, value);
        }
        
        return yearMap;
    }

    private YearlyFinanceData buildYearData(int year, Map<String, Map<Integer, Map<Integer, Long>>> allMetrics) {
        YearlyFinanceData data = new YearlyFinanceData();
        data.setYear(String.valueOf(year));

        Map<Integer, Long> guoziMetrics = allMetrics.get("国资处").get(year);
        Map<Integer, Long> kejichuMetrics = allMetrics.get("科技处").get(year);

        YearlyFinanceData.CampusData campus = new YearlyFinanceData.CampusData();
        campus.setSchoolArea(getMetricValue(guoziMetrics, 134) / 1.0);
        campus.setTeachingArea(getMetricValue(guoziMetrics, 135) / 1.0);
        campus.setLabArea(getMetricValue(guoziMetrics, 136) / 1.0);
        campus.setDormitoryArea(getMetricValue(guoziMetrics, 137) / 1.0);
        campus.setHorizontalFunding(getMetricValue(kejichuMetrics, 12) / 1.0);
        data.setCampus(campus);

        YearlyFinanceData.AssetsData assets = new YearlyFinanceData.AssetsData();
        assets.setFixedAssets(longOf(getMetricValue(guoziMetrics, 138)));
        assets.setEquipmentCount(intOf(getMetricValue(guoziMetrics, 139)));
        assets.setEquipmentValue(getMetricValue(guoziMetrics, 140) / 1.0);
        assets.setLargeEquipmentCount(intOf(getMetricValue(guoziMetrics, 141)));
        assets.setLargeEquipmentValue(getMetricValue(guoziMetrics, 142) / 1.0);
        data.setAssets(assets);

        YearlyFinanceData.ResearchData research = new YearlyFinanceData.ResearchData();
        research.setVerticalFunding(getMetricValue(kejichuMetrics, 11) / 1.0);
        data.setResearch(research);

        return data;
    }

    private Long getMetricValue(Map<Integer, Long> metricMap, int metricId) {
        return metricMap.getOrDefault(metricId, 0L);
    }

    private int intOf(Long v) {
        return v != null ? v.intValue() : 0;
    }

    private long longOf(Double v) {
        return v != null ? v.longValue() : 0L;
    }

    private long longOf(Long v) {
        return v != null ? v : 0L;
    }
}
