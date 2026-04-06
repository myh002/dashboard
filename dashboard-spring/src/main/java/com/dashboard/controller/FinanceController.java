package com.dashboard.controller;

import com.dashboard.common.ResultData;
import com.dashboard.pojo.dto.YearlyFinanceData;
import com.dashboard.service.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping
    public ResultData<List<YearlyFinanceData>> getFinance() {
        return ResultData.success(financeService.getFinanceData());
    }
}
