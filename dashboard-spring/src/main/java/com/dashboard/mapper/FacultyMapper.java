package com.dashboard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dashboard.pojo.entity.DeptStats;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface FacultyMapper extends BaseMapper<DeptStats> {

    Long getTotalStaff(@Param("year") Integer year);

    Long getTotalTeachers(@Param("year") Integer year);

    Long getGraduateSupervisors(@Param("year") Integer year);

    Long getTitleCount(@Param("year") Integer year, @Param("metricId") Integer metricId);

    Long getSupervisorById(@Param("year") Integer year, @Param("metricId") Integer metricId);
}
