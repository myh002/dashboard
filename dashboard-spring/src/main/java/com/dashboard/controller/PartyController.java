package com.dashboard.controller;

import com.dashboard.common.ResultData;
import com.dashboard.pojo.dto.YearlyPartyData;
import com.dashboard.service.PartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/party")
@RequiredArgsConstructor
public class PartyController {

    private final PartyService partyService;

    @GetMapping
    public ResultData<List<YearlyPartyData>> getParty() {
        return ResultData.success(partyService.getPartyData());
    }
}
