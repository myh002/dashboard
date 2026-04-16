package com.dashboard.service.impl;

import com.dashboard.pojo.dto.TalentData;
import com.dashboard.pojo.dto.YearlyTalentData;
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

    private static final List<Integer> JIAOWUCHU_METRIC_IDS = Arrays.asList(67, 71, 72, 78, 81, 82, 83, 84, 87, 88, 97, 98, 103, 104, 105, 106, 107);
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
        Map<String, Map<Integer, Map<Integer, Long>>> allMetrics = loadAllMetrics();

        List<YearlyTalentData> result = new ArrayList<>();
        for (int year : YEARS) {
            result.add(buildYearData(year, allMetrics));
        }
        return result;
    }

    private Map<String, Map<Integer, Map<Integer, Long>>> loadAllMetrics() {
        Map<String, Map<Integer, Map<Integer, Long>>> result = new HashMap<>();

        result.put("教务处", loadDeptMetrics("教务处", JIAOWUCHU_METRIC_IDS));
        result.put("研究生院", loadDeptMetrics("研究生院", YANJIUSHENGYUAN_METRIC_IDS));
        result.put("国际教育学院", loadDeptMetrics("国际教育学院", GUOJIJIAOYU_METRIC_IDS));

        return result;
    }

    private Map<Integer, Map<Integer, Long>> loadDeptMetrics(String deptName, List<Integer> metricIds) {
        List<Integer> yearList = new ArrayList<>();
        for (int y : YEARS) yearList.add(y);
        List<Map<String, Object>> rows = talentMapper.getMetricsBatch(deptName, yearList, metricIds);
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

    private YearlyTalentData buildYearData(int year, Map<String, Map<Integer, Map<Integer, Long>>> allMetrics) {
        YearlyTalentData data = new YearlyTalentData();
        data.setYear(String.valueOf(year));

        Map<Integer, Long> jiaowuchuMetrics = allMetrics.get("教务处").get(year);
        Map<Integer, Long> yanjiushengyuanMetrics = allMetrics.get("研究生院").get(year);
        Map<Integer, Long> guojijiaoyuMetrics = allMetrics.get("国际教育学院").get(year);

        YearlyTalentData.UndergraduateData under = new YearlyTalentData.UndergraduateData();
        under.setMale(intOf(getMetricValue(jiaowuchuMetrics, 71)));
        under.setFemale(intOf(getMetricValue(jiaowuchuMetrics, 72)));
        under.setTotal(under.getMale() + under.getFemale());
        under.setGraduates(intOf(getMetricValue(jiaowuchuMetrics, 83)));
        under.setGraduateRate(getMetricValue(jiaowuchuMetrics, 84) / 1.0);
        under.setEmployment(intOf(getMetricValue(jiaowuchuMetrics, 87)));
        under.setEmploymentRate(getMetricValue(jiaowuchuMetrics, 88) / 1.0);
        data.setUndergraduate(under);

        YearlyTalentData.MasterData master = new YearlyTalentData.MasterData();
        master.setMale(intOf(getMetricValue(yanjiushengyuanMetrics, 110)));
        master.setFemale(intOf(getMetricValue(yanjiushengyuanMetrics, 111)));
        master.setTotal(master.getMale() + master.getFemale());
        master.setSupervisors(intOf(getMetricValue(yanjiushengyuanMetrics, 108)));
        master.setGraduates(intOf(getMetricValue(yanjiushengyuanMetrics, 124)));
        master.setGraduateRate(getMetricValue(yanjiushengyuanMetrics, 125) / 1.0);
        master.setEmployment(intOf(getMetricValue(yanjiushengyuanMetrics, 128)));
        master.setEmploymentRate(getMetricValue(yanjiushengyuanMetrics, 129) / 1.0);
        data.setMaster(master);

        YearlyTalentData.PhdData phd = new YearlyTalentData.PhdData();
        phd.setMale(intOf(getMetricValue(yanjiushengyuanMetrics, 112)));
        phd.setFemale(intOf(getMetricValue(yanjiushengyuanMetrics, 113)));
        phd.setTotal(phd.getMale() + phd.getFemale());
        phd.setSupervisors(intOf(getMetricValue(yanjiushengyuanMetrics, 109)));
        phd.setGraduates(intOf(getMetricValue(yanjiushengyuanMetrics, 124)));
        phd.setGraduateRate(getMetricValue(yanjiushengyuanMetrics, 125) / 1.0);
        phd.setEmployment(intOf(getMetricValue(yanjiushengyuanMetrics, 128)));
        phd.setEmploymentRate(getMetricValue(yanjiushengyuanMetrics, 129) / 1.0);
        data.setPhd(phd);

        YearlyTalentData.TeachingData teaching = new YearlyTalentData.TeachingData();
        teaching.setCourses(intOf(getMetricValue(jiaowuchuMetrics, 81)));
        teaching.setProfessorCourses(intOf(getMetricValue(jiaowuchuMetrics, 82)));
        teaching.setNationalReform(intOf(getMetricValue(jiaowuchuMetrics, 104)));
        teaching.setProvincialReform(intOf(getMetricValue(jiaowuchuMetrics, 105)));
        teaching.setSchoolReform(intOf(getMetricValue(jiaowuchuMetrics, 106)));
        teaching.setTeachingAward(intOf(getMetricValue(jiaowuchuMetrics, 103)));
        teaching.setPracticeBases(intOf(getMetricValue(jiaowuchuMetrics, 107)));
        data.setTeaching(teaching);

        YearlyTalentData.InternationalData intl = new YearlyTalentData.InternationalData();
        intl.setCooperativePrograms(intOf(getMetricValue(guojijiaoyuMetrics, 1)));
        intl.setInternationalStudents(intOf(getMetricValue(guojijiaoyuMetrics, 2)));
        data.setInternational(intl);

        return data;
    }

    private Long getMetricValue(Map<Integer, Long> metricMap, int metricId) {
        return metricMap.getOrDefault(metricId, 0L);
    }

    private int intOf(Long v) {
        return v != null ? v.intValue() : 0;
    }
}
