import { defineStore } from 'pinia'
import type { DashboardData } from '@/types'
import {
  facultyApi,
  researchApi,
  disciplineApi,
  talentApi,
  conditionApi,
} from '@/api'

/**
 * 数据存储 - 师资/科研/学科/人才/条件
 *
 * 数据流向：后端数据库 -> Service -> Controller -> API -> Store -> 页面组件
 *
 * 页面初始化时调用 fetchAllData() 从后端加载所有数据
 */

export const useDataStore = defineStore('data', {
  state: (): DashboardData => ({
    // ===== 师资数据 =====
    faculty: {
      totalStaff: 0,
      totalTeachers: 0,
      graduateSupervisors: 0,
      titleDistribution: {
        seniorHigh: 0,
        seniorTech: 0,
        seniorTitle: 0,
        junior: 0,
        middle: 0
      },
      topTalents: [],
      titleTrend: []
    },

    // ===== 科研数据 =====
    research: {
      papers: { lastYear: 0, newThisYear: 0 },
      publications: { lastYear: 0, newThisYear: 0 },
      patents: { lastYear: 0, newThisYear: 0 },
      bases: { lastYear: 0, newThisYear: 0 },
      projectStats: {
        total: 0,
        provincial: 0,
        prefectural: 0,
        institutional: 0
      },
      fundingTrend: []
    },

    // ===== 学科数据 =====
    discipline: {
      degreePoints: 0,
      firstClassMajors: 0,
      esiDisciplines: 0,
      firstClassDisciplines: 0,
      evaluationDistribution: []
    },

    // ===== 人才数据 =====
    talent: {
      departmentCount: 0,
      majorCount: 0,
      courseCount: 0,
      studentStats: {
        total: 0,
        undergraduate: 0,
        master: 0,
        phd: 0,
        totalIncrement: 0,
        undergraduateIncrement: 0,
        masterIncrement: 0,
        phdIncrement: 0
      },
      studentLevels: {
        undergraduateTotal: 0,
        masterTotal: 0,
        phdTotal: 0
      }
    },

    // ===== 条件数据 =====
    condition: {
      landArea: { lastYear: 0, newThisYear: 0 },
      labArea: { lastYear: 0, newThisYear: 0 },
      teachingAdminArea: { lastYear: 0, newThisYear: 0 },
      fixedAssets: { lastYear: 0, newThisYear: 0 },
      assetOverview: {
        teachingResearchArea: 0,
        equipmentValue: 0,
        bookTotal: 0,
        databaseCount: 0
      },
      equipmentTrend: []
    }
  }),

  getters: {
    totalStaff: (state) => state.faculty.totalStaff,
    totalStudents: (state) => state.talent.studentStats.total,
    facultyTitleChartData: (state) => {
      const d = state.faculty.titleDistribution
      return [
        { name: '正高级', value: d.seniorTitle },
        { name: '副高级', value: d.seniorHigh },
        { name: '中级', value: d.middle },
        { name: '初级', value: d.junior },
        { name: '专业技术人员', value: d.seniorTech }
      ]
    },
    facultyTitleTrendChartData: (state) => {
      const trend = state.faculty.titleTrend
      return {
        xData: trend.map(item => String(item.year)),
        series: [
          { name: '正高级', data: trend.map(item => item.seniorTitle) },
          { name: '副高级', data: trend.map(item => item.seniorHigh) },
          { name: '中级', data: trend.map(item => item.middle) },
          { name: '初级', data: trend.map(item => item.junior) },
          { name: '外聘教师', data: trend.map(item => item.seniorTech) }
        ]
      }
    },
    studentLevelChartData: (state) => {
      const s = state.talent.studentLevels
      return [
        { name: '本专科生', value: s.undergraduateTotal },
        { name: '硕士生', value: s.masterTotal },
        { name: '博士生', value: s.phdTotal }
      ]
    }
  },

  actions: {
    /**
     * 从后端获取师资数据
     */
    async fetchFacultyData() {
      try {
        const [dataRes, trendRes] = await Promise.all([
          facultyApi.getData(),
          facultyApi.getTitleTrend()
        ])
        this.faculty = {
          ...dataRes.data,
          titleTrend: trendRes.data.yearlyData || []
        }
      } catch (error) {
        console.error('获取师资数据失败:', error)
      }
    },
    /**
     * 从后端获取科研数据
     */
    async fetchResearchData() {
      try {
        const res = await researchApi.getData()
        this.research = res.data
      } catch (error) {
        console.error('获取科研数据失败:', error)
      }
    },
    /**
     * 从后端获取学科数据
     */
    async fetchDisciplineData() {
      try {
        const res = await disciplineApi.getData()
        this.discipline = res.data
      } catch (error) {
        console.error('获取学科数据失败:', error)
      }
    },
    /**
     * 从后端获取人才数据
     */
    async fetchTalentData() {
      try {
        const res = await talentApi.getData()
        this.talent = res.data
      } catch (error) {
        console.error('获取人才数据失败:', error)
      }
    },
    /**
     * 从后端获取条件数据
     */
    async fetchConditionData() {
      try {
        const res = await conditionApi.getData()
        this.condition = res.data
      } catch (error) {
        console.error('获取条件数据失败:', error)
      }
    },
    /**
     * 并行获取所有数据
     */
    async fetchAllData() {
      await Promise.all([
        this.fetchFacultyData(),
        this.fetchResearchData(),
        this.fetchDisciplineData(),
        this.fetchTalentData(),
        this.fetchConditionData(),
      ])
    },
  }
})
