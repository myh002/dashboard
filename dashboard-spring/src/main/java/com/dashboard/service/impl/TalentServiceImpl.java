package com.dashboard.service.impl;

import com.dashboard.pojo.dto.TalentData;
import com.dashboard.pojo.dto.YearlyTalentData;
import com.dashboard.pojo.dto.StudentLevelTrendData;
import com.dashboard.mapper.TalentMapper;
import com.dashboard.service.TalentService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TalentServiceImpl implements TalentService {

    private final TalentMapper talentMapper;

    private static final int[] YEARS = {2020, 2021, 2022, 2023, 2024};

    private static final List<Integer> JIAOWUCHU_METRIC_IDS = Arrays.asList(67, 71, 72, 78, 81, 82, 83, 84, 85, 86, 87, 88, 97, 98, 101, 102, 103, 104, 105, 106, 107);
    private static final List<Integer> YANJIUSHENGYUAN_METRIC_IDS = Arrays.asList(108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 124, 125, 126, 127, 128, 129);
    private static final List<Integer> GUOJIJIAOYU_METRIC_IDS = Arrays.asList(1, 2);

    @Override
    @Cacheable(value = "talent", key = "'single-' + #year")
    public TalentData getTalentData(Integer year) {
        TalentData data = new TalentData();

        data.setDepartmentCount(intOf(talentMapper.getMetricValue("校办", 67, year)));
        data.setMajorCount(intOf(talentMapper.getMetricValue("教务处", 78, year)));
        data.setCourseCount(intOf(talentMapper.getMetricValue("教务处", 105, year)));

        TalentData.StudentStats stats = new TalentData.StudentStats();
        int underMale = intOf(talentMapper.getMetricValue("教务处", 71, year));
        int underFemale = intOf(talentMapper.getMetricValue("教务处", 72, year));
        int masterMale = intOf(talentMapper.getMetricValue("研究生院", 110, year));
        int masterFemale = intOf(talentMapper.getMetricValue("研究生院", 111, year));
        int phdMale = intOf(talentMapper.getMetricValue("研究生院", 112, year));
        int phdFemale = intOf(talentMapper.getMetricValue("研究生院", 113, year));

        int underMaleLast = intOf(talentMapper.getMetricValue("教务处", 71, year - 1));
        int underFemaleLast = intOf(talentMapper.getMetricValue("教务处", 72, year - 1));
        int masterMaleLast = intOf(talentMapper.getMetricValue("研究生院", 110, year - 1));
        int masterFemaleLast = intOf(talentMapper.getMetricValue("研究生院", 111, year - 1));
        int phdMaleLast = intOf(talentMapper.getMetricValue("研究生院", 112, year - 1));
        int phdFemaleLast = intOf(talentMapper.getMetricValue("研究生院", 113, year - 1));

        int underTotal = underMale + underFemale;
        int underTotalLast = underMaleLast + underFemaleLast;
        int masterTotal = masterMale + masterFemale;
        int masterTotalLast = masterMaleLast + masterFemaleLast;
        int phdTotal = phdMale + phdFemale;
        int phdTotalLast = phdMaleLast + phdFemaleLast;

        stats.setTotal(underTotal + masterTotal + phdTotal);
        stats.setUndergraduate(underTotal);
        stats.setMaster(masterTotal);
        stats.setPhd(phdTotal);
        stats.setTotalIncrement(stats.getTotal() - (underTotalLast + masterTotalLast + phdTotalLast));
        stats.setUndergraduateIncrement(underTotal - underTotalLast);
        stats.setMasterIncrement(masterTotal - masterTotalLast);
        stats.setPhdIncrement(phdTotal - phdTotalLast);
        data.setStudentStats(stats);

        TalentData.StudentLevels levels = new TalentData.StudentLevels();
        levels.setUndergraduateTotal(underTotal);
        levels.setMasterTotal(masterTotal);
        levels.setPhdTotal(phdTotal);
        data.setStudentLevels(levels);

        return data;
    }

    @Override
    @Cacheable(value = "talent", key = "'yearly'")
    public List<YearlyTalentData> getYearlyData() {
        Map<String, Map<Integer, Map<Integer, Double>>> allMetrics = loadAllMetrics();

        List<YearlyTalentData> result = new ArrayList<>();
        for (int year : YEARS) {
            result.add(buildYearData(year, allMetrics));
        }
        return result;
    }

    @Override
    @Cacheable(value = "talent", key = "'student-level-trend'")
    public List<StudentLevelTrendData> getStudentLevelTrend() {
        Map<String, Map<Integer, Map<Integer, Double>>> allMetrics = loadAllMetrics();

        List<StudentLevelTrendData> result = new ArrayList<>();
        for (int year : YEARS) {
            Map<Integer, Double> jiaowuchuMetrics = allMetrics.get("教务处").get(year);
            Map<Integer, Double> yanjiushengyuanMetrics = allMetrics.get("研究生院").get(year);

            int underMale = intOfDouble(getMetricValueDouble(jiaowuchuMetrics, 71));
            int underFemale = intOfDouble(getMetricValueDouble(jiaowuchuMetrics, 72));
            int masterMale = intOfDouble(getMetricValueDouble(yanjiushengyuanMetrics, 110));
            int masterFemale = intOfDouble(getMetricValueDouble(yanjiushengyuanMetrics, 111));
            int phdMale = intOfDouble(getMetricValueDouble(yanjiushengyuanMetrics, 112));
            int phdFemale = intOfDouble(getMetricValueDouble(yanjiushengyuanMetrics, 113));

            StudentLevelTrendData trendData = new StudentLevelTrendData(
                String.valueOf(year),
                underMale + underFemale,
                masterMale + masterFemale,
                phdMale + phdFemale
            );
            result.add(trendData);
        }
        return result;
    }

    private Map<String, Map<Integer, Map<Integer, Double>>> loadAllMetrics() {
        Map<String, Map<Integer, Map<Integer, Double>>> result = new HashMap<>();

        result.put("教务处", loadDeptMetricsDouble("教务处", JIAOWUCHU_METRIC_IDS));
        result.put("研究生院", loadDeptMetricsDouble("研究生院", YANJIUSHENGYUAN_METRIC_IDS));
        result.put("国际教育学院", loadDeptMetricsDouble("国际教育学院", GUOJIJIAOYU_METRIC_IDS));

        return result;
    }

    private Map<Integer, Map<Integer, Double>> loadDeptMetricsDouble(String deptName, List<Integer> metricIds) {
        List<Integer> yearList = new ArrayList<>();
        for (int y : YEARS) yearList.add(y);
        List<Map<String, Object>> rows = talentMapper.getMetricsBatch(deptName, yearList, metricIds);
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

    private YearlyTalentData buildYearData(int year, Map<String, Map<Integer, Map<Integer, Double>>> allMetrics) {
        YearlyTalentData data = new YearlyTalentData();
        data.setYear(String.valueOf(year));

        Map<Integer, Double> jiaowuchuMetrics = allMetrics.get("教务处").get(year);
        Map<Integer, Double> yanjiushengyuanMetrics = allMetrics.get("研究生院").get(year);
        Map<Integer, Double> guojijiaoyuMetrics = allMetrics.get("国际教育学院").get(year);

        YearlyTalentData.UndergraduateData under = new YearlyTalentData.UndergraduateData();
        under.setMale(intOfDouble(getMetricValueDouble(jiaowuchuMetrics, 71)));
        under.setFemale(intOfDouble(getMetricValueDouble(jiaowuchuMetrics, 72)));
        under.setTotal(under.getMale() + under.getFemale());
        under.setGraduates(intOfDouble(getMetricValueDouble(jiaowuchuMetrics, 83)));
        under.setGraduateRate(getMetricValueDouble(jiaowuchuMetrics, 84));
        under.setGrantRate(getMetricValueDouble(jiaowuchuMetrics, 86));
        under.setDegreeGranted(intOfDouble(getMetricValueDouble(jiaowuchuMetrics, 85)));
        under.setEmployment(intOfDouble(getMetricValueDouble(jiaowuchuMetrics, 87)));
        under.setEmploymentRate(getMetricValueDouble(jiaowuchuMetrics, 88));
        data.setUndergraduate(under);

        YearlyTalentData.MasterData master = new YearlyTalentData.MasterData();
        master.setMale(intOfDouble(getMetricValueDouble(yanjiushengyuanMetrics, 110)));
        master.setFemale(intOfDouble(getMetricValueDouble(yanjiushengyuanMetrics, 111)));
        master.setTotal(master.getMale() + master.getFemale());
        master.setSupervisors(intOfDouble(getMetricValueDouble(yanjiushengyuanMetrics, 108)));
        master.setGraduates(intOfDouble(getMetricValueDouble(yanjiushengyuanMetrics, 124)));
        master.setGraduateRate(getMetricValueDouble(yanjiushengyuanMetrics, 125));
        master.setGrantRate(getMetricValueDouble(yanjiushengyuanMetrics, 127));
        master.setDegreeGranted(intOfDouble(getMetricValueDouble(yanjiushengyuanMetrics, 126)));
        master.setEmployment(intOfDouble(getMetricValueDouble(yanjiushengyuanMetrics, 128)));
        master.setEmploymentRate(getMetricValueDouble(yanjiushengyuanMetrics, 129));
        data.setMaster(master);

        YearlyTalentData.PhdData phd = new YearlyTalentData.PhdData();
        phd.setMale(intOfDouble(getMetricValueDouble(yanjiushengyuanMetrics, 112)));
        phd.setFemale(intOfDouble(getMetricValueDouble(yanjiushengyuanMetrics, 113)));
        phd.setTotal(phd.getMale() + phd.getFemale());
        phd.setSupervisors(intOfDouble(getMetricValueDouble(yanjiushengyuanMetrics, 109)));
        phd.setGraduates(intOfDouble(getMetricValueDouble(yanjiushengyuanMetrics, 124)));
        phd.setGraduateRate(getMetricValueDouble(yanjiushengyuanMetrics, 125));
        phd.setEmployment(intOfDouble(getMetricValueDouble(yanjiushengyuanMetrics, 128)));
        phd.setEmploymentRate(getMetricValueDouble(yanjiushengyuanMetrics, 129));
        data.setPhd(phd);

        YearlyTalentData.TeachingData teaching = new YearlyTalentData.TeachingData();
        teaching.setCourses(intOfDouble(getMetricValueDouble(jiaowuchuMetrics, 81)));
        teaching.setProfessorCourses(intOfDouble(getMetricValueDouble(jiaowuchuMetrics, 82)));
        teaching.setNationalReform(intOfDouble(getMetricValueDouble(jiaowuchuMetrics, 104)));
        teaching.setProvincialReform(intOfDouble(getMetricValueDouble(jiaowuchuMetrics, 105)));
        teaching.setSchoolReform(intOfDouble(getMetricValueDouble(jiaowuchuMetrics, 106)));
        teaching.setTeachingAward(intOfDouble(getMetricValueDouble(jiaowuchuMetrics, 103)));
        teaching.setPracticeBases(intOfDouble(getMetricValueDouble(jiaowuchuMetrics, 107)));
        teaching.setNationalTeams(intOfDouble(getMetricValueDouble(jiaowuchuMetrics, 101)));
        teaching.setProvincialTeams(intOfDouble(getMetricValueDouble(jiaowuchuMetrics, 102)));
        data.setTeaching(teaching);

        YearlyTalentData.InternationalData intl = new YearlyTalentData.InternationalData();
        intl.setCooperativePrograms(intOfDouble(getMetricValueDouble(guojijiaoyuMetrics, 1)));
        intl.setInternationalStudents(intOfDouble(getMetricValueDouble(guojijiaoyuMetrics, 2)));
        data.setInternational(intl);

        return data;
    }

    private Double getMetricValueDouble(Map<Integer, Double> metricMap, int metricId) {
        return metricMap.getOrDefault(metricId, 0.0);
    }

    private int intOfDouble(Double v) {
        return v != null ? v.intValue() : 0;
    }

    private int intOf(Long v) {
        return v != null ? v.intValue() : 0;
    }
}
