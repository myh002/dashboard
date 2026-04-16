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

    private static final int[] YEARS = {2020, 2021, 2022, 2023, 2024};

    private static final List<Integer> JIAOWUCHU_METRIC_IDS = Arrays.asList(71, 72, 78, 79, 80, 103, 105, 106);
    private static final List<Integer> YANJIUSHENGYUAN_METRIC_IDS = Arrays.asList(119, 120, 121, 122, 123);
    private static final List<Integer> XIAOBAN_METRIC_IDS = Arrays.asList(67);
    private static final List<Integer> KEJICHU_METRIC_IDS = Arrays.asList(27, 28, 29, 30, 31, 32);
    private static final List<Integer> RENSHICHU_METRIC_IDS = Arrays.asList(50, 51, 52, 53);

    @Override
    @Cacheable(value = "discipline", key = "'single-' + #year")
    public DisciplineData getDisciplineData(Integer year) {
        DisciplineData data = new DisciplineData();

        int total = 0;
        for (int id = 119; id <= 123; id++) {
            total += intOf(disciplineMapper.getMetricValue("研究生院", id, year));
        }
        data.setDegreePoints(total);

        data.setFirstClassMajors(intOf(disciplineMapper.getMetricValue("教务处", 78, year)));
        data.setEsiDisciplines(intOf(disciplineMapper.getMetricValue("教务处", 103, year)));

        int fc = intOf(disciplineMapper.getMetricValue("教务处", 105, year))
                + intOf(disciplineMapper.getMetricValue("教务处", 106, year));
        data.setFirstClassDisciplines(fc);

        List<DisciplineData.EvaluationItem> evalDist = new ArrayList<>();
        evalDist.add(new DisciplineData.EvaluationItem("SCI一区", intOf(disciplineMapper.getMetricValue("科技处", 27, year))));
        evalDist.add(new DisciplineData.EvaluationItem("SCI二区", intOf(disciplineMapper.getMetricValue("科技处", 28, year))));
        evalDist.add(new DisciplineData.EvaluationItem("SCI三区", intOf(disciplineMapper.getMetricValue("科技处", 29, year))));
        evalDist.add(new DisciplineData.EvaluationItem("SCI四区", intOf(disciplineMapper.getMetricValue("科技处", 30, year))));
        evalDist.add(new DisciplineData.EvaluationItem("EI", intOf(disciplineMapper.getMetricValue("科技处", 31, year))));
        evalDist.add(new DisciplineData.EvaluationItem("CSCD", intOf(disciplineMapper.getMetricValue("科技处", 32, year))));
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
        result.put("科技处", loadDeptMetrics("科技处", KEJICHU_METRIC_IDS));
        result.put("人事处", loadDeptMetrics("人事处", RENSHICHU_METRIC_IDS));

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
        Map<Integer, Long> renshichuMetrics = Optional.ofNullable(allMetrics.get("人事处")).map(m -> m.getOrDefault(year, new HashMap<>())).orElse(new HashMap<>());

        YearlyDisciplineData.DisciplinesData disciplines = new YearlyDisciplineData.DisciplinesData();
        disciplines.setProvincialKey(intOf(getMetricValue(yanjiushengyuanMetrics, 120)));
        disciplines.setProvincialEmphasis(intOf(getMetricValue(yanjiushengyuanMetrics, 121)));
        disciplines.setFirstClassConstruction(intOf(getMetricValue(yanjiushengyuanMetrics, 123)));
        disciplines.setFirstClass(intOf(getMetricValue(yanjiushengyuanMetrics, 122)));
        data.setDisciplines(disciplines);

        YearlyDisciplineData.MajorsData majors = new YearlyDisciplineData.MajorsData();
        majors.setUndergraduateMale(intOf(getMetricValue(jiaowuchuMetrics, 71)));
        majors.setUndergraduateFemale(intOf(getMetricValue(jiaowuchuMetrics, 72)));
        majors.setUndergraduateTotal(intOf(getMetricValue(jiaowuchuMetrics, 78)));
        majors.setNewThisYear(intOf(getMetricValue(jiaowuchuMetrics, 79)));
        majors.setDiscontinued(intOf(getMetricValue(jiaowuchuMetrics, 80)));
        data.setMajors(majors);

        YearlyDisciplineData.TeachingAchievementsData teachingAchievements = new YearlyDisciplineData.TeachingAchievementsData();
        teachingAchievements.setProvincialTeachingAward(intOf(getMetricValue(jiaowuchuMetrics, 103)));
        teachingAchievements.setProvincialReformProject(intOf(getMetricValue(jiaowuchuMetrics, 105)));
        teachingAchievements.setSchoolReformProject(intOf(getMetricValue(jiaowuchuMetrics, 106)));
        data.setTeachingAchievements(teachingAchievements);

        YearlyDisciplineData.TitleDistributionData titleDistribution = new YearlyDisciplineData.TitleDistributionData();
        titleDistribution.setProfessorCount(intOf(getMetricValue(renshichuMetrics, 50)));
        titleDistribution.setAssociateProfessorCount(intOf(getMetricValue(renshichuMetrics, 51)));
        titleDistribution.setLecturerCount(intOf(getMetricValue(renshichuMetrics, 52)));
        titleDistribution.setJuniorCount(intOf(getMetricValue(renshichuMetrics, 53)));
        data.setTitleDistribution(titleDistribution);

        YearlyDisciplineData.DegreePointsData degreePoints = new YearlyDisciplineData.DegreePointsData();
        degreePoints.setPostdoctoral(intOf(getMetricValue(yanjiushengyuanMetrics, 119)));
        degreePoints.setDoctoralFirst(intOf(getMetricValue(yanjiushengyuanMetrics, 120)));
        degreePoints.setMasterFirst(intOf(getMetricValue(yanjiushengyuanMetrics, 121)));
        degreePoints.setMasterSecond(intOf(getMetricValue(yanjiushengyuanMetrics, 122)));
        degreePoints.setProfessional(intOf(getMetricValue(yanjiushengyuanMetrics, 123)));
        int dpTotal = degreePoints.getPostdoctoral() + degreePoints.getDoctoralFirst()
                + degreePoints.getMasterFirst() + degreePoints.getMasterSecond()
                + degreePoints.getProfessional();
        degreePoints.setTotal(dpTotal);
        data.setDegreePoints(degreePoints);

        data.setColleges(intOf(disciplineMapper.getMetricValue("校办", 67, year)));

        return data;
    }

    private Long getMetricValue(Map<Integer, Long> metricMap, int metricId) {
        return metricMap.getOrDefault(metricId, 0L);
    }

    private int intOf(Long v) {
        return v != null ? v.intValue() : 0;
    }
}
