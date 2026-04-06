package com.dashboard.service.impl;

import com.dashboard.pojo.dto.YearlyPartyData;
import com.dashboard.mapper.PartyMapper;
import com.dashboard.service.PartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PartyServiceImpl implements PartyService {

    private final PartyMapper partyMapper;

    private static final int[] YEARS = {2020, 2021, 2022, 2023, 2024, 2025};

    private static final List<Integer> ZUZHIBU_METRIC_IDS = Arrays.asList(48);
    private static final List<Integer> TONGZHANBU_METRIC_IDS = Arrays.asList(39, 40, 41, 42, 43, 44, 45, 46);
    private static final List<Integer> JIAOWUCHU_METRIC_IDS = Arrays.asList(67, 68, 70, 71, 93, 95, 96);
    private static final List<Integer> YANJIUSHENGYUAN_METRIC_IDS = Arrays.asList(110, 111, 113, 114);

    @Override
    @Cacheable(value = "party", key = "'yearly'")
    public List<YearlyPartyData> getPartyData() {
        Map<String, Map<Integer, Map<Integer, Long>>> allMetrics = loadAllMetrics();

        List<YearlyPartyData> result = new ArrayList<>();
        for (int year : YEARS) {
            result.add(buildYearData(year, allMetrics));
        }
        return result;
    }

    private Map<String, Map<Integer, Map<Integer, Long>>> loadAllMetrics() {
        Map<String, Map<Integer, Map<Integer, Long>>> result = new HashMap<>();

        result.put("组织部", loadDeptMetrics("组织部", ZUZHIBU_METRIC_IDS));
        result.put("统战部", loadDeptMetrics("统战部", TONGZHANBU_METRIC_IDS));
        result.put("教务处", loadDeptMetrics("教务处", JIAOWUCHU_METRIC_IDS));
        result.put("研究生院", loadDeptMetrics("研究生院", YANJIUSHENGYUAN_METRIC_IDS));

        return result;
    }

    private Map<Integer, Map<Integer, Long>> loadDeptMetrics(String deptName, List<Integer> metricIds) {
        List<Integer> yearList = new ArrayList<>();
        for (int y : YEARS) yearList.add(y);
        List<Map<String, Object>> rows = partyMapper.getMetricsBatch(deptName, yearList, metricIds);
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

    private YearlyPartyData buildYearData(int year, Map<String, Map<Integer, Map<Integer, Long>>> allMetrics) {
        YearlyPartyData data = new YearlyPartyData();
        data.setYear(String.valueOf(year));

        Map<Integer, Long> zuzhibuMetrics = allMetrics.get("组织部").get(year);
        Map<Integer, Long> tongzhanbuMetrics = allMetrics.get("统战部").get(year);
        Map<Integer, Long> jiaowuchuMetrics = allMetrics.get("教务处").get(year);
        Map<Integer, Long> yanjiushengyuanMetrics = allMetrics.get("研究生院").get(year);

        YearlyPartyData.PartyMembersData members = new YearlyPartyData.PartyMembersData();
        members.setTotal(intOf(getMetricValue(zuzhibuMetrics, 48)));
        members.setUndergraduate(intOf(getMetricValue(jiaowuchuMetrics, 67)));
        members.setGraduate(intOf(getMetricValue(yanjiushengyuanMetrics, 110)));
        data.setPartyMembers(members);

        YearlyPartyData.DemocraticPartiesData dp = new YearlyPartyData.DemocraticPartiesData();
        dp.setRevolutionary(intOf(getMetricValue(tongzhanbuMetrics, 39)));
        dp.setLeague(intOf(getMetricValue(tongzhanbuMetrics, 40)));
        dp.setConstruction(intOf(getMetricValue(tongzhanbuMetrics, 41)));
        dp.setProgress(intOf(getMetricValue(tongzhanbuMetrics, 42)));
        dp.setFarmersWorkers(intOf(getMetricValue(tongzhanbuMetrics, 43)));
        dp.setZhiGong(intOf(getMetricValue(tongzhanbuMetrics, 44)));
        dp.setJiuSan(intOf(getMetricValue(tongzhanbuMetrics, 45)));
        dp.setTaiwanLeague(intOf(getMetricValue(tongzhanbuMetrics, 46)));
        data.setDemocraticParties(dp);

        YearlyPartyData.YouthLeagueData yl = new YearlyPartyData.YouthLeagueData();
        yl.setUndergraduate(intOf(getMetricValue(jiaowuchuMetrics, 68)));
        yl.setGraduate(intOf(getMetricValue(yanjiushengyuanMetrics, 111)));
        data.setYouthLeague(yl);

        YearlyPartyData.SpecialGroupsData sg = new YearlyPartyData.SpecialGroupsData();
        sg.setMinority(intOf(getMetricValue(jiaowuchuMetrics, 70))
                + intOf(getMetricValue(yanjiushengyuanMetrics, 113)));
        sg.setDisabled(intOf(getMetricValue(jiaowuchuMetrics, 71))
                + intOf(getMetricValue(yanjiushengyuanMetrics, 114)));
        data.setSpecialGroups(sg);

        YearlyPartyData.IdeologicalTeamsData it = new YearlyPartyData.IdeologicalTeamsData();
        it.setNationalTeam(intOf(getMetricValue(jiaowuchuMetrics, 95)));
        it.setProvincialTeam(intOf(getMetricValue(jiaowuchuMetrics, 96)));
        data.setIdeologicalTeams(it);

        return data;
    }

    private Long getMetricValue(Map<Integer, Long> metricMap, int metricId) {
        return metricMap.getOrDefault(metricId, 0L);
    }

    private int intOf(Long v) {
        return v != null ? v.intValue() : 0;
    }
}
