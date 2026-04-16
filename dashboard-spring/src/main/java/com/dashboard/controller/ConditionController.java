package com.dashboard.controller;

import com.dashboard.common.ResultData;
import com.dashboard.pojo.dto.ConditionData;
import com.dashboard.service.ConditionService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/condition")
@RequiredArgsConstructor
@Validated
public class ConditionController {

    private final ConditionService conditionService;

    @GetMapping
    public ResultData<ConditionData> getCondition(
            @RequestParam(defaultValue = "2024")
            @Min(value = 2000, message = "年份不能小于2000")
            @Max(value = 2100, message = "年份不能大于2100") Integer year) {
        return ResultData.success(conditionService.getConditionData(year));
    }
}
