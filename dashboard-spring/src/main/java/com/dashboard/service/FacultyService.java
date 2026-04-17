package com.dashboard.service;

import com.dashboard.pojo.dto.FacultyData;
import com.dashboard.pojo.dto.FacultyTitleTrendData;

public interface FacultyService {
    FacultyData getFacultyData(Integer year);
    FacultyTitleTrendData getFacultyTitleTrend();
}
