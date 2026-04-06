package com.dashboard.service;

import com.dashboard.pojo.dto.YearlyPartyData;
import java.util.List;

public interface PartyService {
    List<YearlyPartyData> getPartyData();
}
