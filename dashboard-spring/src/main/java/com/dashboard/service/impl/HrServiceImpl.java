package com.dashboard.service.impl;

import com.dashboard.pojo.dto.YearlyHrData;
import com.dashboard.mapper.HrMapper;
import com.dashboard.service.HrService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class HrServiceImpl implements HrService {

    private final HrMapper hrMapper;

    private static final int[] YEARS = {2020, 2021, 2022, 2023, 2024};

    private static final List<Integer> RENSHICHU_METRIC_IDS = Arrays.asList(42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58);
    private static final List<Integer> JIAOWUCHU_METRIC_IDS = Arrays.asList(103);

    @Override
    @Cacheable(value = "hr", key = "'yearly'")
    public List<YearlyHrData> getHrData() {
        Map<String, Map<Integer, Map<Integer, Long>>> allMetrics = loadAllMetrics();

        List<YearlyHrData> result = new ArrayList<>();
        for (int year : YEARS) {
            result.add(buildYearData(year, allMetrics));
        }
        return result;
    }

    private Map<String, Map<Integer, Map<Integer, Long>>> loadAllMetrics() {
        Map<String, Map<Integer, Map<Integer, Long>>> result = new HashMap<>();

        result.put("人事处", loadDeptMetrics("人事处", RENSHICHU_METRIC_IDS));
        result.put("教务处", loadDeptMetrics("教务处", JIAOWUCHU_METRIC_IDS));

        return result;
    }

    private Map<Integer, Map<Integer, Long>> loadDeptMetrics(String deptName, List<Integer> metricIds) {
        List<Integer> yearList = new ArrayList<>();
        for (int y : YEARS) yearList.add(y);
        List<Map<String, Object>> rows = hrMapper.getMetricsBatch(deptName, yearList, metricIds);
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

    private YearlyHrData buildYearData(int year, Map<String, Map<Integer, Map<Integer, Long>>> allMetrics) {
        YearlyHrData data = new YearlyHrData();
        data.setYear(String.valueOf(year));

        Map<Integer, Long> renshichuMetrics = allMetrics.get("人事处").get(year);
        Map<Integer, Long> jiaowuchuMetrics = allMetrics.get("教务处").get(year);

        YearlyHrData.StaffData staff = new YearlyHrData.StaffData();
        staff.setTotal(intOf(getMetricValue(renshichuMetrics, 42)));
        staff.setFullTime(intOf(getMetricValue(renshichuMetrics, 43)));
        staff.setManagement(intOf(getMetricValue(renshichuMetrics, 44)));
        staff.setSupporting(intOf(getMetricValue(renshichuMetrics, 45)));
        staff.setExternal(intOf(getMetricValue(renshichuMetrics, 54)));
        data.setStaff(staff);

        YearlyHrData.EducationData edu = new YearlyHrData.EducationData();
        edu.setDoctorate(intOf(getMetricValue(renshichuMetrics, 47)));
        edu.setMaster(intOf(getMetricValue(renshichuMetrics, 48)));
        edu.setBachelor(intOf(getMetricValue(renshichuMetrics, 49)));
        data.setEducation(edu);

        YearlyHrData.TitleData title = new YearlyHrData.TitleData();
        title.setProfessor(intOf(getMetricValue(renshichuMetrics, 50)));
        title.setAssociate(intOf(getMetricValue(renshichuMetrics, 51)));
        title.setLecturer(intOf(getMetricValue(renshichuMetrics, 52)));
        title.setAssistant(intOf(getMetricValue(renshichuMetrics, 53)));
        data.setTitle(title);

        YearlyHrData.TalentsData talents = new YearlyHrData.TalentsData();
        talents.setProvincialTeachingAward(intOf(getMetricValue(jiaowuchuMetrics, 103)));
        data.setTalents(talents);
        
        YearlyHrData.AgeDistribution age = new YearlyHrData.AgeDistribution();
        age.setUnder35(intOf(getMetricValue(renshichuMetrics, 55)));
        age.setAge36to45(intOf(getMetricValue(renshichuMetrics, 56)));
        age.setAge46to55(intOf(getMetricValue(renshichuMetrics, 57)));
        age.setOver56(intOf(getMetricValue(renshichuMetrics, 58)));
        data.setAgeDistribution(age);

        return data;
    }

    private Long getMetricValue(Map<Integer, Long> metricMap, int metricId) {
        return metricMap.getOrDefault(metricId, 0L);
    }

    private int intOf(Long v) {
        return v != null ? v.intValue() : 0;
    }
}
