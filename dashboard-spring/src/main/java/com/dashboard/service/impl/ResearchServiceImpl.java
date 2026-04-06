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

    private static final int[] YEARS = {2020, 2021, 2022, 2023, 2024, 2025};

    private static final List<Integer> PAPER_IDS = Arrays.asList(52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62);
    private static final List<Integer> PROJECT_IDS = Arrays.asList(1, 2, 3, 4, 5);
    private static final List<Integer> PLATFORM_IDS = Arrays.asList(6, 7, 8, 9, 10);
    private static final List<Integer> PATENT_IDS = Arrays.asList(17, 18, 19);
    private static final List<Integer> IP_IDS = Arrays.asList(17, 18, 19, 20, 21, 22);

    private static final List<Integer> KEJICHU_METRIC_IDS = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22);
    private static final List<Integer> TUSHUGUAN_METRIC_IDS = Arrays.asList(52, 55, 56, 57, 58, 59, 60, 61, 62);

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
        stats.setProvincial(intOf(researchMapper.getMetricValue("科技处", 3, year)));
        stats.setPrefectural(intOf(researchMapper.getMetricValue("科技处", 4, year)));
        stats.setInstitutional(intOf(researchMapper.getMetricValue("科技处", 5, year)));
        data.setProjectStats(stats);

        data.setFundingTrend(getFundingTrend());
        return data;
    }

    @Override
    public List<ResearchData.FundingTrendItem> getFundingTrend() {
        List<Map<String, Object>> vRows = researchMapper.getFundingTrend(11);
        List<Map<String, Object>> hRows = researchMapper.getFundingTrend(12);

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
        result.put("图书馆", loadDeptMetrics("图书馆", TUSHUGUAN_METRIC_IDS));

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
        Map<Integer, Long> tushuguanMetrics = allMetrics.get("图书馆").get(year);

        YearlyResearchData.ProjectsData projects = new YearlyResearchData.ProjectsData();
        projects.setNational(intOf(getMetricValue(kejichuMetrics, 1)));
        projects.setMinisterial(intOf(getMetricValue(kejichuMetrics, 2)));
        projects.setProvincial(intOf(getMetricValue(kejichuMetrics, 3)));
        projects.setPrefectural(intOf(getMetricValue(kejichuMetrics, 4)));
        projects.setSchool(intOf(getMetricValue(kejichuMetrics, 5)));
        projects.setTotal(intOf(researchMapper.getMetricSumByIds(PROJECT_IDS, year)));
        data.setProjects(projects);

        YearlyResearchData.PlatformsData platforms = new YearlyResearchData.PlatformsData();
        platforms.setNational(intOf(getMetricValue(kejichuMetrics, 6)));
        platforms.setProvincial(intOf(getMetricValue(kejichuMetrics, 8)));
        platforms.setOther(intOf(getMetricValue(kejichuMetrics, 7))
                + intOf(getMetricValue(kejichuMetrics, 9))
                + intOf(getMetricValue(kejichuMetrics, 10)));
        data.setPlatforms(platforms);

        YearlyResearchData.FundingData funding = new YearlyResearchData.FundingData();
        funding.setVertical(getMetricValue(kejichuMetrics, 11) / 1.0);
        funding.setHorizontal(getMetricValue(kejichuMetrics, 12) / 1.0);
        funding.setTotal((getMetricValue(kejichuMetrics, 11) + getMetricValue(kejichuMetrics, 12)) / 1.0);
        data.setFunding(funding);

        YearlyResearchData.AwardsData awards = new YearlyResearchData.AwardsData();
        awards.setFirst(intOf(getMetricValue(kejichuMetrics, 13)));
        awards.setSecond(intOf(getMetricValue(kejichuMetrics, 14)));
        awards.setThird(intOf(getMetricValue(kejichuMetrics, 15)));
        data.setAwards(awards);

        YearlyResearchData.PublicationsData publications = new YearlyResearchData.PublicationsData();
        publications.setBooks(intOf(getMetricValue(kejichuMetrics, 16)));
        YearlyResearchData.PatentsData patents = new YearlyResearchData.PatentsData();
        patents.setInvention(intOf(getMetricValue(kejichuMetrics, 17)));
        patents.setUtility(intOf(getMetricValue(kejichuMetrics, 18)));
        patents.setDesign(intOf(getMetricValue(kejichuMetrics, 19)));
        publications.setPatents(patents);
        publications.setVarieties(intOf(getMetricValue(kejichuMetrics, 20)));
        publications.setStandards(intOf(getMetricValue(kejichuMetrics, 21)));
        publications.setSoftware(intOf(getMetricValue(kejichuMetrics, 22)));
        publications.setTotal(intOf(researchMapper.getMetricSumByIds(IP_IDS, year)));
        data.setPublications(publications);

        YearlyResearchData.PapersData papers = new YearlyResearchData.PapersData();
        papers.setTopTierJournals(intOf(getMetricValue(tushuguanMetrics, 52)));
        papers.setSciQ1(intOf(getMetricValue(tushuguanMetrics, 55)));
        papers.setSciQ2(intOf(getMetricValue(tushuguanMetrics, 56)));
        papers.setSciQ3(intOf(getMetricValue(tushuguanMetrics, 57)));
        papers.setSciQ4(intOf(getMetricValue(tushuguanMetrics, 58)));
        papers.setEi(intOf(getMetricValue(tushuguanMetrics, 59)));
        papers.setCscd(intOf(getMetricValue(tushuguanMetrics, 60)));
        papers.setCore(intOf(getMetricValue(tushuguanMetrics, 61)));
        papers.setOther(intOf(getMetricValue(tushuguanMetrics, 62)));
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
