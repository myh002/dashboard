package com.dashboard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dashboard.pojo.entity.DeptStats;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface FinanceMapper extends BaseMapper<DeptStats> {

    Long getMetricValue(@Param("deptName") String deptName, @Param("metricId") Integer metricId, @Param("year") Integer year);

    List<Map<String, Object>> getFundingTrend(@Param("metricId") Integer metricId);

    List<Map<String, Object>> getMetricsBatch(
        @Param("deptName") String deptName,
        @Param("years") List<Integer> years,
        @Param("metricIds") List<Integer> metricIds
    );
}
