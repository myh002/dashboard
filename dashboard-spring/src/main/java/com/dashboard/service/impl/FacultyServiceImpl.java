package com.dashboard.service.impl;

import com.dashboard.mapper.FacultyMapper;
import com.dashboard.pojo.dto.FacultyData;
import com.dashboard.pojo.dto.FacultyTitleTrendData;
import com.dashboard.service.FacultyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FacultyServiceImpl implements FacultyService {

    private final FacultyMapper facultyMapper;

    @Override
    public FacultyData getFacultyData(Integer year) {
        FacultyData data = new FacultyData();

        Long totalStaff = facultyMapper.getTotalStaff(year);
        Long totalTeachers = facultyMapper.getTotalTeachers(year);
        // 硕士+博士导师总数（含男女）
        Long graduateSupervisors = facultyMapper.getGraduateSupervisors(year);

        data.setTotalStaff(totalStaff != null ? totalStaff.intValue() : 0);
        data.setTotalTeachers(totalTeachers != null ? totalTeachers.intValue() : 0);
        data.setGraduateSupervisors(graduateSupervisors != null ? graduateSupervisors.intValue() : 0);

        // 职称分布（人事处 metrics 50-54）
        FacultyData.TitleDistribution title = new FacultyData.TitleDistribution();
        title.setSeniorTitle(intOf(facultyMapper.getTitleCount(year, 50))); // 正高级
        title.setSeniorHigh(intOf(facultyMapper.getTitleCount(year, 51)));   // 副高级
        title.setMiddle(intOf(facultyMapper.getTitleCount(year, 52)));        // 中级
        title.setJunior(intOf(facultyMapper.getTitleCount(year, 53)));        // 初级及其他
        title.setSeniorTech(intOf(facultyMapper.getTitleCount(year, 54)));    // 外聘教师
        data.setTitleDistribution(title);

        // 高层次人才/团队（教务处 metrics 100, 102）
        // 新增 = 本年数据 - 上一年数据（若无上一年数据则为0）
        List<FacultyData.TalentItem> topTalents = new ArrayList<>();
        int professorCount = intOf(facultyMapper.getTitleCount(year, 50));
        int professorPrev = intOf(facultyMapper.getTitleCount(year - 1, 50));
        topTalents.add(buildItem("教授", professorCount, professorCount - professorPrev, "UserFilled", "人"));

        int associateProfessorCount = intOf(facultyMapper.getTitleCount(year, 51));
        int associateProfessorPrev = intOf(facultyMapper.getTitleCount(year - 1, 51));
        topTalents.add(buildItem("副教授", associateProfessorCount, associateProfessorCount - associateProfessorPrev, "User", "人"));

        int nationalTeamCount = intOf(facultyMapper.getTitleCount(year, 103));
        int nationalTeamPrev = intOf(facultyMapper.getTitleCount(year - 1, 103));
        topTalents.add(buildItem("省级教学成果奖", nationalTeamCount, nationalTeamCount - nationalTeamPrev, "Trophy", "项"));

        int provincialTeamCount = intOf(facultyMapper.getTitleCount(year, 105));
        int provincialTeamPrev = intOf(facultyMapper.getTitleCount(year - 1, 105));
        topTalents.add(buildItem("省级教改工程项目", provincialTeamCount, provincialTeamCount - provincialTeamPrev, "Star", "项"));

        data.setTopTalents(topTalents);
        return data;
    }
    
    @Override
    public FacultyTitleTrendData getFacultyTitleTrend() {
        FacultyTitleTrendData result = new FacultyTitleTrendData();
        List<FacultyTitleTrendData.TitleDistribution> yearlyData = new ArrayList<>();
        
        int[] years = {2020, 2021, 2022, 2023, 2024};
        
        for (int year : years) {
            FacultyTitleTrendData.TitleDistribution dist = new FacultyTitleTrendData.TitleDistribution();
            dist.setYear(year);
            dist.setSeniorTitle(intOf(facultyMapper.getTitleCount(year, 50)));
            dist.setSeniorHigh(intOf(facultyMapper.getTitleCount(year, 51)));
            dist.setMiddle(intOf(facultyMapper.getTitleCount(year, 52)));
            dist.setJunior(intOf(facultyMapper.getTitleCount(year, 53)));
            dist.setSeniorTech(intOf(facultyMapper.getTitleCount(year, 54)));
            yearlyData.add(dist);
        }
        
        result.setYearlyData(yearlyData);
        return result;
    }

    private int intOf(Long v) {
        return v != null ? v.intValue() : 0;
    }

    private FacultyData.TalentItem buildItem(String category, int count, int increment, String icon, String unit) {
        FacultyData.TalentItem item = new FacultyData.TalentItem();
        item.setCategory(category);
        item.setCount(count);
        item.setNewIncrement(increment);
        item.setIcon(icon);
        item.setUnit(unit);
        return item;
    }
}
