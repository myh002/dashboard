package com.dashboard.controller;

import com.dashboard.common.ResultData;
import com.dashboard.pojo.dto.TalentData;
import com.dashboard.pojo.dto.YearlyTalentData;
import com.dashboard.service.TalentService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/talent")
@RequiredArgsConstructor
@Validated
public class TalentController {

    private final TalentService talentService;

    @GetMapping
    public ResultData<TalentData> getTalent(
            @RequestParam(defaultValue = "2024")
            @Min(value = 2000, message = "年份不能小于2000")
            @Max(value = 2100, message = "年份不能大于2100") Integer year) {
        return ResultData.success(talentService.getTalentData(year));
    }

    @GetMapping("/yearly")
    public ResultData<List<YearlyTalentData>> getYearlyData() {
        return ResultData.success(talentService.getYearlyData());
    }
}
