package com.dashboard.service;

import com.dashboard.pojo.dto.YearlyFinanceData;
import java.util.List;

public interface FinanceService {
    List<YearlyFinanceData> getFinanceData();
}
