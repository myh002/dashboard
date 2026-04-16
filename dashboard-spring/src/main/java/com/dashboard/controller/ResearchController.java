package com.dashboard.controller;

import com.dashboard.common.ResultData;
import com.dashboard.pojo.dto.ResearchData;
import com.dashboard.pojo.dto.YearlyResearchData;
import com.dashboard.service.ResearchService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/research")
@RequiredArgsConstructor
@Validated
public class ResearchController {

    private final ResearchService researchService;

    @GetMapping
    public ResultData<ResearchData> getResearch(
            @RequestParam(defaultValue = "2024")
            @Min(value = 2000, message = "年份不能小于2000")
            @Max(value = 2100, message = "年份不能大于2100") Integer year) {
        return ResultData.success(researchService.getResearchData(year));
    }

    @GetMapping("/yearly")
    public ResultData<List<YearlyResearchData>> getYearlyData() {
        return ResultData.success(researchService.getYearlyData());
    }
}
