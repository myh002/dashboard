package com.dashboard.controller;

import com.dashboard.common.ResultData;
import com.dashboard.pojo.dto.DisciplineData;
import com.dashboard.pojo.dto.YearlyDisciplineData;
import com.dashboard.service.DisciplineService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/discipline")
@RequiredArgsConstructor
@Validated
public class DisciplineController {

    private final DisciplineService disciplineService;

    @GetMapping
    public ResultData<DisciplineData> getDiscipline(
            @RequestParam(defaultValue = "2025")
            @Min(value = 2000, message = "年份不能小于2000")
            @Max(value = 2100, message = "年份不能大于2100") Integer year) {
        return ResultData.success(disciplineService.getDisciplineData(year));
    }

    @GetMapping("/yearly")
    public ResultData<List<YearlyDisciplineData>> getYearlyData() {
        return ResultData.success(disciplineService.getYearlyData());
    }
}
