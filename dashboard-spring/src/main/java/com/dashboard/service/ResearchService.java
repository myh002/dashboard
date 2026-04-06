package com.dashboard.service;

import com.dashboard.pojo.dto.ResearchData;
import com.dashboard.pojo.dto.YearlyResearchData;
import java.util.List;

public interface ResearchService {
    ResearchData getResearchData(Integer year);
    List<ResearchData.FundingTrendItem> getFundingTrend();
    List<YearlyResearchData> getYearlyData();
}
