package com.dashboard.controller;

import com.dashboard.common.ResultData;
import com.dashboard.pojo.dto.YearlyHrData;
import com.dashboard.service.HrService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/hr")
@RequiredArgsConstructor
public class HrController {

    private final HrService hrService;

    @GetMapping
    public ResultData<List<YearlyHrData>> getHr() {
        return ResultData.success(hrService.getHrData());
    }
}
