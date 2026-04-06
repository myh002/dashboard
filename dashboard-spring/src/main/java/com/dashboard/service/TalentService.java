package com.dashboard.service;

import com.dashboard.pojo.dto.TalentData;
import com.dashboard.pojo.dto.YearlyTalentData;
import java.util.List;

public interface TalentService {
    TalentData getTalentData(Integer year);
    List<YearlyTalentData> getYearlyData();
}
