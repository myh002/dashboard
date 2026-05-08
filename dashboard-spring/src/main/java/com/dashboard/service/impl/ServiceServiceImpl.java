package com.dashboard.service.impl;

import com.dashboard.pojo.dto.YearlyServiceData;
import com.dashboard.mapper.ServiceMapper;
import com.dashboard.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final ServiceMapper serviceMapper;

    private static final int[] YEARS = {2020, 2021, 2022, 2023, 2024};

    private static final List<Integer> KEJICHU_METRIC_IDS = Arrays.asList(23, 36, 37, 38, 39, 40, 41);
    private static final List<Integer> JIAOWUCHU_METRIC_IDS = Arrays.asList(83, 87, 88, 107);
    private static final List<Integer> YANJIUSHENGYUAN_METRIC_IDS = Arrays.asList(124, 128, 129);
    private static final List<Integer> GUOJIJIAOYU_METRIC_IDS = Arrays.asList(1, 2);
    private static final List<Integer> RENSHICHU_METRIC_IDS = Arrays.asList(54);

    @Override
    @Cacheable(value = "service", key = "'yearly'")
    public List<YearlyServiceData> getServiceData() {
        Map<String, Map<Integer, Map<Integer, Double>>> allMetrics = loadAllMetrics();

        List<YearlyServiceData> result = new ArrayList<>();
        for (int year : YEARS) {
            result.add(buildYearData(year, allMetrics));
        }
        return result;
    }

    private Map<String, Map<Integer, Map<Integer, Double>>> loadAllMetrics() {
        Map<String, Map<Integer, Map<Integer, Double>>> result = new HashMap<>();

        result.put("科技处", loadDeptMetrics("科技处", KEJICHU_METRIC_IDS));
        result.put("教务处", loadDeptMetrics("教务处", JIAOWUCHU_METRIC_IDS));
        result.put("研究生院", loadDeptMetrics("研究生院", YANJIUSHENGYUAN_METRIC_IDS));
        result.put("国际教育学院", loadDeptMetrics("国际教育学院", GUOJIJIAOYU_METRIC_IDS));
        result.put("人事处", loadDeptMetrics("人事处", RENSHICHU_METRIC_IDS));

        return result;
    }

    private Map<Integer, Map<Integer, Double>> loadDeptMetrics(String deptName, List<Integer> metricIds) {
        List<Integer> yearList = new ArrayList<>();
        for (int y : YEARS) yearList.add(y);
        List<Map<String, Object>> rows = serviceMapper.getMetricsBatch(deptName, yearList, metricIds);
        Map<Integer, Map<Integer, Double>> yearMap = new HashMap<>();

        for (int year : YEARS) {
            yearMap.put(year, new HashMap<>());
        }

        for (Map<String, Object> row : rows) {
            Integer year = ((Number) row.get("year")).intValue();
            Integer metricId = ((Number) row.get("metric_id")).intValue();
            Double value = ((Number) row.get("value")).doubleValue();
            yearMap.get(year).put(metricId, value);
        }

        return yearMap;
    }

    private YearlyServiceData buildYearData(int year, Map<String, Map<Integer, Map<Integer, Double>>> allMetrics) {
        YearlyServiceData data = new YearlyServiceData();
        data.setYear(String.valueOf(year));

        Map<Integer, Double> kejichuMetrics = allMetrics.get("科技处").get(year);
        Map<Integer, Double> jiaowuchuMetrics = allMetrics.get("教务处").get(year);
        Map<Integer, Double> yanjiushengyuanMetrics = allMetrics.get("研究生院").get(year);
        Map<Integer, Double> guojijiaoyuMetrics = allMetrics.get("国际教育学院").get(year);
        Map<Integer, Double> renshichuMetrics = allMetrics.get("人事处").get(year);

        YearlyServiceData.CooperationData coop = new YearlyServiceData.CooperationData();
        coop.setHorizontalFunding(getMetricValueDouble(kejichuMetrics, 23));
        coop.setInventionPatents(intOfDouble(getMetricValueDouble(kejichuMetrics, 36)));
        coop.setUtilityPatents(intOfDouble(getMetricValueDouble(kejichuMetrics, 37)));
        coop.setDesignPatents(intOfDouble(getMetricValueDouble(kejichuMetrics, 38)));
        coop.setVarieties(intOfDouble(getMetricValueDouble(kejichuMetrics, 39)));
        coop.setStandards(intOfDouble(getMetricValueDouble(kejichuMetrics, 40)));
        coop.setSoftware(intOfDouble(getMetricValueDouble(kejichuMetrics, 41)));
        data.setCooperation(coop);

        YearlyServiceData.EmploymentData emp = new YearlyServiceData.EmploymentData();
        emp.setUndergraduate(intOfDouble(getMetricValueDouble(jiaowuchuMetrics, 83)));
        emp.setUndergraduateRate(getMetricValueDouble(jiaowuchuMetrics, 88));
        emp.setMaster(intOfDouble(getMetricValueDouble(yanjiushengyuanMetrics, 124)));
        emp.setPhd(intOfDouble(getMetricValueDouble(yanjiushengyuanMetrics, 124)));
        emp.setGraduateRate(getMetricValueDouble(yanjiushengyuanMetrics, 129));
        data.setEmployment(emp);

        YearlyServiceData.InternationalData intl = new YearlyServiceData.InternationalData();
        intl.setInternationalStudents(intOfDouble(getMetricValueDouble(guojijiaoyuMetrics, 2)));
        intl.setCooperativePrograms(intOfDouble(getMetricValueDouble(guojijiaoyuMetrics, 1)));
        intl.setPracticeBases(intOfDouble(getMetricValueDouble(jiaowuchuMetrics, 107)));
        data.setInternational(intl);

        YearlyServiceData.ExpertsData experts = new YearlyServiceData.ExpertsData();
        experts.setExternalTeachers(intOfDouble(getMetricValueDouble(renshichuMetrics, 54)));
        data.setExperts(experts);

        return data;
    }

    private Double getMetricValueDouble(Map<Integer, Double> metricMap, int metricId) {
        return metricMap.getOrDefault(metricId, 0.0);
    }

    private int intOfDouble(Double v) {
        return v != null ? v.intValue() : 0;
    }
}
