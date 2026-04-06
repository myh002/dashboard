package com.dashboard.service;

import com.dashboard.pojo.dto.YearlyServiceData;
import java.util.List;

public interface ServiceService {
    List<YearlyServiceData> getServiceData();
}
