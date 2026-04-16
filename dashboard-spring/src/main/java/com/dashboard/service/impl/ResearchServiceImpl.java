package com.dashboard.service.impl;

import com.dashboard.pojo.dto.ResearchData;
import com.dashboard.pojo.dto.YearlyResearchData;
import com.dashboard.mapper.ResearchMapper;
import com.dashboard.service.ResearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ResearchServiceImpl implements ResearchService {

    private final ResearchMapper researchMapper;

    private static final int[] YEARS = {2020, 2021, 2022, 2023, 2024};

    private static final List<Integer> PAPER_IDS = Arrays.asList(27, 28, 29, 30, 31, 32, 33, 34);
    private static final List<Integer> PROJECT_IDS = Arrays.asList(12, 13, 14, 15, 16);
    private static final List<Integer> PLATFORM_IDS = Arrays.asList(17, 18, 19, 20, 21);
    private static final List<Integer> PATENT_IDS = Arrays.asList(36, 37, 38);
    private static final List<Integer> IP_IDS = Arrays.asList(36, 37, 38, 39, 40, 41);

    private static final List<Integer> KEJICHU_METRIC_IDS = Arrays.asList(12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41);
    private static final List<Integer> KEJICHU_PAPER_IDS = Arrays.asList(27, 28, 29, 30, 31, 32, 33, 34);

    @Override
    @Cacheable(value = "research", key = "'single-' + #year")
    public ResearchData getResearchData(Integer year) {
        ResearchData data = new ResearchData();

        Long papersThisYear = researchMapper.getMetricSumByIds(PAPER_IDS, year);
        Long papersLastYear = researchMapper.getMetricSumByIds(PAPER_IDS, year - 1);
        data.setPapers(buildDualYear(intOf(papersLastYear), intOf(papersThisYear)));

        Long pubThisYear = researchMapper.getMetricSumByIds(IP_IDS, year);
        Long pubLastYear = researchMapper.getMetricSumByIds(IP_IDS, year - 1);
        data.setPublications(buildDualYear(intOf(pubLastYear), intOf(pubThisYear)));

        Long patentThisYear = researchMapper.getMetricSumByIds(PATENT_IDS, year);
        Long patentLastYear = researchMapper.getMetricSumByIds(PATENT_IDS, year - 1);
        data.setPatents(buildDualYear(intOf(patentLastYear), intOf(patentThisYear)));

        Long baseThisYear = researchMapper.getMetricSumByIds(PLATFORM_IDS, year);
        Long baseLastYear = researchMapper.getMetricSumByIds(PLATFORM_IDS, year - 1);
        data.setBases(buildDualYear(intOf(baseLastYear), intOf(baseThisYear)));

        ResearchData.ProjectStats stats = new ResearchData.ProjectStats();
        stats.setTotal(intOf(researchMapper.getMetricSumByIds(PROJECT_IDS, year)));
        stats.setProvincial(intOf(researchMapper.getMetricValue("科技处", 14, year)));
        stats.setPrefectural(intOf(researchMapper.getMetricValue("科技处", 15, year)));
        stats.setInstitutional(intOf(researchMapper.getMetricValue("科技处", 16, year)));
        data.setProjectStats(stats);

        data.setFundingTrend(getFundingTrend());
        return data;
    }

    @Override
    public List<ResearchData.FundingTrendItem> getFundingTrend() {
        List<Map<String, Object>> vRows = researchMapper.getFundingTrend(22);
        List<Map<String, Object>> hRows = researchMapper.getFundingTrend(23);

        Map<Integer, Double> vMap = new HashMap<>();
        for (Map<String, Object> row : vRows) {
            vMap.put(((Number) row.get("year")).intValue(), ((Number) row.get("value")).doubleValue());
        }
        Map<Integer, Double> hMap = new HashMap<>();
        for (Map<String, Object> row : hRows) {
            hMap.put(((Number) row.get("year")).intValue(), ((Number) row.get("value")).doubleValue());
        }

        Set<Integer> years = new TreeSet<>(vMap.keySet());
        years.addAll(hMap.keySet());

        List<ResearchData.FundingTrendItem> trend = new ArrayList<>();
        for (Integer y : years) {
            ResearchData.FundingTrendItem item = new ResearchData.FundingTrendItem();
            item.setYear(y);
            item.setVertical(vMap.getOrDefault(y, 0.0));
            item.setHorizontal(hMap.getOrDefault(y, 0.0));
            trend.add(item);
        }
        return trend;
    }

    @Override
    @Cacheable(value = "research", key = "'yearly'")
    public List<YearlyResearchData> getYearlyData() {
        Map<String, Map<Integer, Map<Integer, Long>>> allMetrics = loadAllMetrics();

        List<YearlyResearchData> result = new ArrayList<>();
        for (int year : YEARS) {
            result.add(buildYearData(year, allMetrics));
        }
        return result;
    }

    private Map<String, Map<Integer, Map<Integer, Long>>> loadAllMetrics() {
        Map<String, Map<Integer, Map<Integer, Long>>> result = new HashMap<>();

        result.put("科技处", loadDeptMetrics("科技处", KEJICHU_METRIC_IDS));

        return result;
    }

    private Map<Integer, Map<Integer, Long>> loadDeptMetrics(String deptName, List<Integer> metricIds) {
        List<Integer> yearList = new ArrayList<>();
        for (int y : YEARS) yearList.add(y);
        List<Map<String, Object>> rows = researchMapper.getMetricsBatch(deptName, yearList, metricIds);
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

    private YearlyResearchData buildYearData(int year, Map<String, Map<Integer, Map<Integer, Long>>> allMetrics) {
        YearlyResearchData data = new YearlyResearchData();
        data.setYear(String.valueOf(year));

        Map<Integer, Long> kejichuMetrics = allMetrics.get("科技处").get(year);

        YearlyResearchData.ProjectsData projects = new YearlyResearchData.ProjectsData();
        projects.setNational(intOf(getMetricValue(kejichuMetrics, 12)));
        projects.setMinisterial(intOf(getMetricValue(kejichuMetrics, 13)));
        projects.setProvincial(intOf(getMetricValue(kejichuMetrics, 14)));
        projects.setPrefectural(intOf(getMetricValue(kejichuMetrics, 15)));
        projects.setSchool(intOf(getMetricValue(kejichuMetrics, 16)));
        projects.setTotal(intOf(researchMapper.getMetricSumByIds(PROJECT_IDS, year)));
        data.setProjects(projects);

        YearlyResearchData.PlatformsData platforms = new YearlyResearchData.PlatformsData();
        platforms.setNational(intOf(getMetricValue(kejichuMetrics, 17)));
        platforms.setProvincial(intOf(getMetricValue(kejichuMetrics, 19)));
        platforms.setOther(intOf(getMetricValue(kejichuMetrics, 18))
                + intOf(getMetricValue(kejichuMetrics, 20))
                + intOf(getMetricValue(kejichuMetrics, 21)));
        data.setPlatforms(platforms);

        YearlyResearchData.FundingData funding = new YearlyResearchData.FundingData();
        funding.setVertical(getMetricValue(kejichuMetrics, 22) / 1.0);
        funding.setHorizontal(getMetricValue(kejichuMetrics, 23) / 1.0);
        funding.setTotal((getMetricValue(kejichuMetrics, 22) + getMetricValue(kejichuMetrics, 23)) / 1.0);
        data.setFunding(funding);

        YearlyResearchData.AwardsData awards = new YearlyResearchData.AwardsData();
        awards.setFirst(intOf(getMetricValue(kejichuMetrics, 24)));
        awards.setSecond(intOf(getMetricValue(kejichuMetrics, 25)));
        awards.setThird(intOf(getMetricValue(kejichuMetrics, 26)));
        data.setAwards(awards);

        YearlyResearchData.PublicationsData publications = new YearlyResearchData.PublicationsData();
        publications.setBooks(intOf(getMetricValue(kejichuMetrics, 35)));
        YearlyResearchData.PatentsData patents = new YearlyResearchData.PatentsData();
        patents.setInvention(intOf(getMetricValue(kejichuMetrics, 36)));
        patents.setUtility(intOf(getMetricValue(kejichuMetrics, 37)));
        patents.setDesign(intOf(getMetricValue(kejichuMetrics, 38)));
        publications.setPatents(patents);
        publications.setVarieties(intOf(getMetricValue(kejichuMetrics, 39)));
        publications.setStandards(intOf(getMetricValue(kejichuMetrics, 40)));
        publications.setSoftware(intOf(getMetricValue(kejichuMetrics, 41)));
        publications.setTotal(intOf(researchMapper.getMetricSumByIds(IP_IDS, year)));
        data.setPublications(publications);

        YearlyResearchData.PapersData papers = new YearlyResearchData.PapersData();
        papers.setSciQ1(intOf(getMetricValue(kejichuMetrics, 27)));
        papers.setSciQ2(intOf(getMetricValue(kejichuMetrics, 28)));
        papers.setSciQ3(intOf(getMetricValue(kejichuMetrics, 29)));
        papers.setSciQ4(intOf(getMetricValue(kejichuMetrics, 30)));
        papers.setEi(intOf(getMetricValue(kejichuMetrics, 31)));
        papers.setCscd(intOf(getMetricValue(kejichuMetrics, 32)));
        papers.setCore(intOf(getMetricValue(kejichuMetrics, 33)));
        papers.setOther(intOf(getMetricValue(kejichuMetrics, 34)));
        papers.setTotal(intOf(researchMapper.getMetricSumByIds(PAPER_IDS, year)));
        data.setPapers(papers);

        return data;
    }

    private Long getMetricValue(Map<Integer, Long> metricMap, int metricId) {
        return metricMap.getOrDefault(metricId, 0L);
    }

    private ResearchData.DualYearData buildDualYear(int last, int current) {
        ResearchData.DualYearData d = new ResearchData.DualYearData();
        d.setLastYear(last);
        d.setNewThisYear(current - last);
        return d;
    }

    private int intOf(Long v) {
        return v != null ? v.intValue() : 0;
    }
}
