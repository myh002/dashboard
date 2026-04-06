package com.dashboard.controller;

import com.dashboard.common.ResultData;
import com.dashboard.pojo.dto.YearlyServiceData;
import com.dashboard.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/service")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;

    @GetMapping
    public ResultData<List<YearlyServiceData>> getService() {
        return ResultData.success(serviceService.getServiceData());
    }
}
