package com.dashboard.service;

import com.dashboard.pojo.dto.DisciplineData;
import com.dashboard.pojo.dto.YearlyDisciplineData;
import java.util.List;

public interface DisciplineService {
    DisciplineData getDisciplineData(Integer year);
    List<YearlyDisciplineData> getYearlyData();
}
