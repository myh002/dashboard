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

    private static final List<Integer> KEJICHU_METRIC_IDS = Arrays.asList(23, 36, 37, 40, 41);
    private static final List<Integer> JIAOWUCHU_METRIC_IDS = Arrays.asList(87, 88, 107);
    private static final List<Integer> YANJIUSHENGYUAN_METRIC_IDS = Arrays.asList(128, 129);
    private static final List<Integer> GUOJIJIAOYU_METRIC_IDS = Arrays.asList(1, 2);
    private static final List<Integer> RENSHICHU_METRIC_IDS = Arrays.asList(54);

    @Override
    @Cacheable(value = "service", key = "'yearly'")
    public List<YearlyServiceData> getServiceData() {
        Map<String, Map<Integer, Map<Integer, Long>>> allMetrics = loadAllMetrics();

        List<YearlyServiceData> result = new ArrayList<>();
        for (int year : YEARS) {
            result.add(buildYearData(year, allMetrics));
        }
        return result;
    }

    private Map<String, Map<Integer, Map<Integer, Long>>> loadAllMetrics() {
        Map<String, Map<Integer, Map<Integer, Long>>> result = new HashMap<>();

        result.put("科技处", loadDeptMetrics("科技处", KEJICHU_METRIC_IDS));
        result.put("教务处", loadDeptMetrics("教务处", JIAOWUCHU_METRIC_IDS));
        result.put("研究生院", loadDeptMetrics("研究生院", YANJIUSHENGYUAN_METRIC_IDS));
        result.put("国际教育学院", loadDeptMetrics("国际教育学院", GUOJIJIAOYU_METRIC_IDS));
        result.put("人事处", loadDeptMetrics("人事处", RENSHICHU_METRIC_IDS));

        return result;
    }

    private Map<Integer, Map<Integer, Long>> loadDeptMetrics(String deptName, List<Integer> metricIds) {
        List<Integer> yearList = new ArrayList<>();
        for (int y : YEARS) yearList.add(y);
        List<Map<String, Object>> rows = serviceMapper.getMetricsBatch(deptName, yearList, metricIds);
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

    private YearlyServiceData buildYearData(int year, Map<String, Map<Integer, Map<Integer, Long>>> allMetrics) {
        YearlyServiceData data = new YearlyServiceData();
        data.setYear(String.valueOf(year));

        Map<Integer, Long> kejichuMetrics = allMetrics.get("科技处").get(year);
        Map<Integer, Long> jiaowuchuMetrics = allMetrics.get("教务处").get(year);
        Map<Integer, Long> yanjiushengyuanMetrics = allMetrics.get("研究生院").get(year);
        Map<Integer, Long> guojijiaoyuMetrics = allMetrics.get("国际教育学院").get(year);
        Map<Integer, Long> renshichuMetrics = allMetrics.get("人事处").get(year);

        YearlyServiceData.CooperationData coop = new YearlyServiceData.CooperationData();
        coop.setHorizontalFunding(getMetricValue(kejichuMetrics, 23) / 1.0);
        coop.setInventionPatents(intOf(getMetricValue(kejichuMetrics, 36)));
        coop.setUtilityPatents(intOf(getMetricValue(kejichuMetrics, 37)));
        coop.setStandards(intOf(getMetricValue(kejichuMetrics, 40)));
        coop.setSoftware(intOf(getMetricValue(kejichuMetrics, 41)));
        data.setCooperation(coop);

        YearlyServiceData.EmploymentData emp = new YearlyServiceData.EmploymentData();
        emp.setUndergraduate(intOf(getMetricValue(jiaowuchuMetrics, 87)));
        emp.setUndergraduateRate(getMetricValue(jiaowuchuMetrics, 88) / 1.0);
        emp.setMaster(intOf(getMetricValue(yanjiushengyuanMetrics, 128)));
        emp.setMasterRate(getMetricValue(yanjiushengyuanMetrics, 129) / 1.0);
        emp.setPhd(intOf(getMetricValue(yanjiushengyuanMetrics, 128)));
        emp.setPhdRate(getMetricValue(yanjiushengyuanMetrics, 129) / 1.0);
        data.setEmployment(emp);

        YearlyServiceData.InternationalData intl = new YearlyServiceData.InternationalData();
        intl.setInternationalStudents(intOf(getMetricValue(guojijiaoyuMetrics, 2)));
        intl.setCooperativePrograms(intOf(getMetricValue(guojijiaoyuMetrics, 1)));
        intl.setPracticeBases(intOf(getMetricValue(jiaowuchuMetrics, 107)));
        data.setInternational(intl);

        YearlyServiceData.ExpertsData experts = new YearlyServiceData.ExpertsData();
        experts.setExternalTeachers(intOf(getMetricValue(renshichuMetrics, 54)));
        data.setExperts(experts);

        return data;
    }

    private Long getMetricValue(Map<Integer, Long> metricMap, int metricId) {
        return metricMap.getOrDefault(metricId, 0L);
    }

    private int intOf(Long v) {
        return v != null ? v.intValue() : 0;
    }
}
