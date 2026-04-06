package com.dashboard.service.impl;

import com.dashboard.mapper.DisciplineMapper;
import com.dashboard.pojo.dto.DisciplineData;
import com.dashboard.pojo.dto.YearlyDisciplineData;
import com.dashboard.service.DisciplineService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DisciplineServiceImpl implements DisciplineService {

    private final DisciplineMapper disciplineMapper;

    private static final int[] YEARS = {2020, 2021, 2022, 2023, 2024, 2025};

    private static final List<Integer> JIAOWUCHU_METRIC_IDS = Arrays.asList(72, 73, 74, 83, 84, 85, 86, 87, 88, 89, 90);
    private static final List<Integer> YANJIUSHENGYUAN_METRIC_IDS = Arrays.asList(115, 116, 117, 118, 119);
    private static final List<Integer> XIAOBAN_METRIC_IDS = Arrays.asList(47);

    @Override
    @Cacheable(value = "discipline", key = "'single-' + #year")
    public DisciplineData getDisciplineData(Integer year) {
        DisciplineData data = new DisciplineData();

        Long total = 0L;
        for (int id = 115; id <= 119; id++) {
            total += disciplineMapper.getMetricValue("研究生院", id, year);
        }
        data.setDegreePoints(total.intValue());

        data.setFirstClassMajors(intOf(disciplineMapper.getMetricValue("教务处", 72, year)));
        data.setEsiDisciplines(intOf(disciplineMapper.getMetricValue("教务处", 86, year)));

        int fc = intOf(disciplineMapper.getMetricValue("教务处", 86, year))
                + intOf(disciplineMapper.getMetricValue("教务处", 90, year));
        data.setFirstClassDisciplines(fc);

        List<DisciplineData.EvaluationItem> evalDist = new ArrayList<>();
        evalDist.add(new DisciplineData.EvaluationItem("省级特色重点学科", intOf(disciplineMapper.getMetricValue("教务处", 84, year))));
        evalDist.add(new DisciplineData.EvaluationItem("省级重点学科", intOf(disciplineMapper.getMetricValue("教务处", 85, year))));
        evalDist.add(new DisciplineData.EvaluationItem("一流建设学科", intOf(disciplineMapper.getMetricValue("教务处", 86, year))));
        evalDist.add(new DisciplineData.EvaluationItem("教育部特色专业", intOf(disciplineMapper.getMetricValue("教务处", 87, year))));
        evalDist.add(new DisciplineData.EvaluationItem("省级特色专业", intOf(disciplineMapper.getMetricValue("教务处", 88, year))));
        evalDist.add(new DisciplineData.EvaluationItem("省级示范专业", intOf(disciplineMapper.getMetricValue("教务处", 89, year))));
        evalDist.add(new DisciplineData.EvaluationItem("一流学科", intOf(disciplineMapper.getMetricValue("教务处", 90, year))));
        data.setEvaluationDistribution(evalDist);

        return data;
    }

    @Override
    @Cacheable(value = "discipline", key = "'yearly'")
    public List<YearlyDisciplineData> getYearlyData() {
        Map<String, Map<Integer, Map<Integer, Long>>> allMetrics = loadAllMetrics();

        List<YearlyDisciplineData> result = new ArrayList<>();
        for (int year : YEARS) {
            result.add(buildYearData(year, allMetrics));
        }
        return result;
    }

    private Map<String, Map<Integer, Map<Integer, Long>>> loadAllMetrics() {
        Map<String, Map<Integer, Map<Integer, Long>>> result = new HashMap<>();

        result.put("教务处", loadDeptMetrics("教务处", JIAOWUCHU_METRIC_IDS));
        result.put("研究生院", loadDeptMetrics("研究生院", YANJIUSHENGYUAN_METRIC_IDS));
        result.put("校办", loadDeptMetrics("校办", XIAOBAN_METRIC_IDS));

        return result;
    }

    private Map<Integer, Map<Integer, Long>> loadDeptMetrics(String deptName, List<Integer> metricIds) {
        List<Integer> yearList = new ArrayList<>();
        for (int y : YEARS) yearList.add(y);
        List<Map<String, Object>> rows = disciplineMapper.getMetricsBatch(deptName, yearList, metricIds);
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

    private YearlyDisciplineData buildYearData(int year, Map<String, Map<Integer, Map<Integer, Long>>> allMetrics) {
        YearlyDisciplineData data = new YearlyDisciplineData();
        data.setYear(String.valueOf(year));

        Map<Integer, Long> jiaowuchuMetrics = Optional.ofNullable(allMetrics.get("教务处")).map(m -> m.getOrDefault(year, new HashMap<>())).orElse(new HashMap<>());
        Map<Integer, Long> yanjiushengyuanMetrics = Optional.ofNullable(allMetrics.get("研究生院")).map(m -> m.getOrDefault(year, new HashMap<>())).orElse(new HashMap<>());

        YearlyDisciplineData.DisciplinesData disciplines = new YearlyDisciplineData.DisciplinesData();
        disciplines.setProvincialKey(intOf(getMetricValue(jiaowuchuMetrics, 84)));
        disciplines.setProvincialEmphasis(intOf(getMetricValue(jiaowuchuMetrics, 85)));
        disciplines.setFirstClassConstruction(intOf(getMetricValue(jiaowuchuMetrics, 86)));
        disciplines.setFirstClass(intOf(getMetricValue(jiaowuchuMetrics, 90)));
        data.setDisciplines(disciplines);

        YearlyDisciplineData.MajorsData majors = new YearlyDisciplineData.MajorsData();
        majors.setMinistryFeature(intOf(getMetricValue(jiaowuchuMetrics, 87)));
        majors.setProvincialFeature(intOf(getMetricValue(jiaowuchuMetrics, 88)));
        majors.setProvincialDemonstration(intOf(getMetricValue(jiaowuchuMetrics, 89)));
        majors.setProvincialQualityCourse(intOf(getMetricValue(jiaowuchuMetrics, 83)));
        majors.setUndergraduateTotal(intOf(getMetricValue(jiaowuchuMetrics, 72)));
        majors.setNewThisYear(intOf(getMetricValue(jiaowuchuMetrics, 73)));
        majors.setDiscontinued(intOf(getMetricValue(jiaowuchuMetrics, 74)));
        data.setMajors(majors);

        YearlyDisciplineData.DegreePointsData degreePoints = new YearlyDisciplineData.DegreePointsData();
        degreePoints.setPostdoctoral(intOf(getMetricValue(yanjiushengyuanMetrics, 115)));
        degreePoints.setDoctoralFirst(intOf(getMetricValue(yanjiushengyuanMetrics, 116)));
        degreePoints.setMasterFirst(intOf(getMetricValue(yanjiushengyuanMetrics, 117)));
        degreePoints.setMasterSecond(intOf(getMetricValue(yanjiushengyuanMetrics, 118)));
        degreePoints.setProfessional(intOf(getMetricValue(yanjiushengyuanMetrics, 119)));
        int dpTotal = degreePoints.getPostdoctoral() + degreePoints.getDoctoralFirst()
                + degreePoints.getMasterFirst() + degreePoints.getMasterSecond()
                + degreePoints.getProfessional();
        degreePoints.setTotal(dpTotal);
        data.setDegreePoints(degreePoints);

        data.setColleges(intOf(disciplineMapper.getMetricValue("校办", 47, year)));

        return data;
    }

    private Long getMetricValue(Map<Integer, Long> metricMap, int metricId) {
        return metricMap.getOrDefault(metricId, 0L);
    }

    private int intOf(Long v) {
        return v != null ? v.intValue() : 0;
    }
}
