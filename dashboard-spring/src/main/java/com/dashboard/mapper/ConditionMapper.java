package com.dashboard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dashboard.pojo.entity.DeptStats;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface ConditionMapper extends BaseMapper<DeptStats> {

    Long getMetricValue(@Param("deptName") String deptName, @Param("metricId") Integer metricId, @Param("year") Integer year);

    Long getMetricValueById(@Param("metricId") Integer metricId, @Param("year") Integer year);

    Long getMetricSum(@Param("deptName") String deptName, @Param("metricIds") List<Integer> metricIds, @Param("year") Integer year);

    List<Map<String, Object>> getEquipmentTrend(@Param("metricId") Integer metricId);
}
