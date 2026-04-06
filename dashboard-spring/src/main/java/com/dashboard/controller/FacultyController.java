package com.dashboard.controller;

import com.dashboard.common.ResultData;
import com.dashboard.pojo.dto.FacultyData;
import com.dashboard.service.FacultyService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/faculty")
@RequiredArgsConstructor
@Validated
public class FacultyController {

    private final FacultyService facultyService;

    @GetMapping
    public ResultData<FacultyData> getFaculty(
            @RequestParam(defaultValue = "2025")
            @Min(value = 2000, message = "年份不能小于2000")
            @Max(value = 2100, message = "年份不能大于2100") Integer year) {
        return ResultData.success(facultyService.getFacultyData(year));
    }
}
