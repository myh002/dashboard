package com.dashboard.service.impl;

import com.dashboard.mapper.FacultyMapper;
import com.dashboard.pojo.dto.FacultyData;
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

        // 职称分布（人事处 metrics 30-34）
        FacultyData.TitleDistribution title = new FacultyData.TitleDistribution();
        title.setSeniorTitle(intOf(facultyMapper.getTitleCount(year, 30))); // 正高级
        title.setSeniorHigh(intOf(facultyMapper.getTitleCount(year, 31)));   // 副高级
        title.setMiddle(intOf(facultyMapper.getTitleCount(year, 32)));        // 中级
        title.setJunior(intOf(facultyMapper.getTitleCount(year, 33)));        // 初级及其他
        title.setSeniorTech(intOf(facultyMapper.getTitleCount(year, 34)));    // 外聘教师
        data.setTitleDistribution(title);

        // 高层次人才/团队（教务处 metrics 91-96）
        // 新增 = 本年数据 - 上一年数据（若无上一年数据则为0）
        List<FacultyData.TalentItem> topTalents = new ArrayList<>();
        int professorCount = intOf(facultyMapper.getTitleCount(year, 30));
        int professorPrev = intOf(facultyMapper.getTitleCount(year - 1, 30));
        topTalents.add(buildItem("教授", professorCount, professorCount - professorPrev, "UserFilled"));

        int associateProfessorCount = intOf(facultyMapper.getTitleCount(year, 31));
        int associateProfessorPrev = intOf(facultyMapper.getTitleCount(year - 1, 31));
        topTalents.add(buildItem("副教授", associateProfessorCount, associateProfessorCount - associateProfessorPrev, "User"));

        int nationalTeamCount = intOf(facultyMapper.getTitleCount(year, 95));
        int nationalTeamPrev = intOf(facultyMapper.getTitleCount(year - 1, 95));
        topTalents.add(buildItem("国家级教学团队", nationalTeamCount, nationalTeamCount - nationalTeamPrev, "Trophy"));

        int provincialTeamCount = intOf(facultyMapper.getTitleCount(year, 96));
        int provincialTeamPrev = intOf(facultyMapper.getTitleCount(year - 1, 96));
        topTalents.add(buildItem("省级教学团队", provincialTeamCount, provincialTeamCount - provincialTeamPrev, "Star"));

        data.setTopTalents(topTalents);
        return data;
    }

    private int intOf(Long v) {
        return v != null ? v.intValue() : 0;
    }

    private FacultyData.TalentItem buildItem(String category, int count, int increment, String icon) {
        FacultyData.TalentItem item = new FacultyData.TalentItem();
        item.setCategory(category);
        item.setCount(count);
        item.setNewIncrement(increment);
        item.setIcon(icon);
        return item;
    }
}
