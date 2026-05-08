import { defineStore } from 'pinia'
import type { DisciplineState, YearlyDisciplineData } from '@/types'
import { disciplineApi } from '@/api'
import { ElMessage } from 'element-plus'

export const useDisciplineStore = defineStore('discipline', {
  state: (): DisciplineState => ({
    yearlyData: [],
    selectedYear: '2025',
    loading: false,
    error: null as string | null
  }),

  getters: {
    years: (state): string[] => state.yearlyData.map(d => d.year),
    currentYearData: (state): YearlyDisciplineData => {
      const found = state.yearlyData.find(d => d.year === state.selectedYear)
        || state.yearlyData[state.yearlyData.length - 1]
      if (!found) {
        return {
          year: state.selectedYear,
          disciplines: { provincialKey: 0, provincialEmphasis: 0, firstClassConstruction: 0, firstClass: 0 },
          majors: { undergraduateMale: 0, undergraduateFemale: 0, undergraduateTotal: 0, newThisYear: 0, discontinued: 0 },
          teachingAchievements: { provincialTeachingAward: 0, provincialReformProject: 0, schoolReformProject: 0 },
          titleDistribution: { professorCount: 0, associateProfessorCount: 0, lecturerCount: 0, juniorCount: 0 },
          degreePoints: { postdoctoral: 0, doctoralFirst: 0, masterFirst: 0, masterSecond: 0, professional: 0, total: 0 },
          colleges: 0
        }
      }
      return {
        year: found.year,
        disciplines: found.disciplines ?? { provincialKey: 0, provincialEmphasis: 0, firstClassConstruction: 0, firstClass: 0 },
        majors: found.majors ?? { undergraduateMale: 0, undergraduateFemale: 0, undergraduateTotal: 0, newThisYear: 0, discontinued: 0 },
        teachingAchievements: found.teachingAchievements ?? { provincialTeachingAward: 0, provincialReformProject: 0, schoolReformProject: 0 },
        titleDistribution: found.titleDistribution ?? { professorCount: 0, associateProfessorCount: 0, lecturerCount: 0, juniorCount: 0 },
        degreePoints: found.degreePoints ?? { postdoctoral: 0, doctoralFirst: 0, masterFirst: 0, masterSecond: 0, professional: 0, total: 0 },
        colleges: found.colleges ?? 0
      }
    },
    disciplineChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      return [
        { name: '一级博点', value: data.disciplines.provincialKey ?? 0 },
        { name: '一级硕点', value: data.disciplines.provincialEmphasis ?? 0 },
        { name: '专业硕点', value: data.disciplines.firstClassConstruction ?? 0 },
        { name: '二级硕点', value: data.disciplines.firstClass ?? 0 }
      ]
    },
    majorChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      return [
        { name: '本科生（男）', value: data.majors.undergraduateMale ?? 0 },
        { name: '本科生（女）', value: data.majors.undergraduateFemale ?? 0 }
      ]
    },
    titleDistributionChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data || !data.titleDistribution) return []
      const td = data.titleDistribution
      return [
        { name: '正高级职称', value: td.professorCount ?? 0 },
        { name: '副高级职称', value: td.associateProfessorCount ?? 0 },
        { name: '中级职称', value: td.lecturerCount ?? 0 },
        { name: '初级及其他', value: td.juniorCount ?? 0 }
      ]
    },
    teachingAchievementsChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      const ta = data.teachingAchievements
      return [
        { name: '省级教学成果奖', value: ta?.provincialTeachingAward ?? 0 },
        { name: '省级教改工程项目', value: ta?.provincialReformProject ?? 0 },
        { name: '校级教改工程项目', value: ta?.schoolReformProject ?? 0 }
      ]
    },
    disciplineTrendData: (state) => {
      const years = state.yearlyData.map(d => d.year)
      return {
        years,
        series: [
          {
            name: '省级特色重点学科',
            data: state.yearlyData.map(d => d.disciplines?.provincialKey ?? 0)
          },
          {
            name: '省级重点学科',
            data: state.yearlyData.map(d => d.disciplines?.provincialEmphasis ?? 0)
          },
          {
            name: '一流建设学科',
            data: state.yearlyData.map(d => d.disciplines?.firstClassConstruction ?? 0)
          },
          {
            name: '一流学科',
            data: state.yearlyData.map(d => d.disciplines?.firstClass ?? 0)
          }
        ]
      }
    },
    majorTrendData: (state) => {
      const years = state.yearlyData.map(d => d.year)
      return {
        years,
        series: [
          {
            name: '本科生（男）',
            data: state.yearlyData.map(d => d.majors?.undergraduateMale ?? 0)
          },
          {
            name: '本科生（女）',
            data: state.yearlyData.map(d => d.majors?.undergraduateFemale ?? 0)
          },
          {
            name: '本科生总数',
            data: state.yearlyData.map(d => d.majors?.undergraduateTotal ?? 0)
          }
        ]
      }
    },
    titleTrendData: (state) => {
      const years = state.yearlyData.map(d => d.year)
      return {
        years,
        series: [
          {
            name: '正高级职称',
            data: state.yearlyData.map(d => d.titleDistribution?.professorCount ?? 0)
          },
          {
            name: '副高级职称',
            data: state.yearlyData.map(d => d.titleDistribution?.associateProfessorCount ?? 0)
          },
          {
            name: '中级职称',
            data: state.yearlyData.map(d => d.titleDistribution?.lecturerCount ?? 0)
          },
          {
            name: '初级及其他',
            data: state.yearlyData.map(d => d.titleDistribution?.juniorCount ?? 0)
          }
        ]
      }
    },
    teachingTrendData: (state) => {
      const years = state.yearlyData.map(d => d.year)
      return {
        years,
        series: [
          {
            name: '省级教学成果奖',
            data: state.yearlyData.map(d => d.teachingAchievements?.provincialTeachingAward ?? 0)
          },
          {
            name: '省级教改工程项目',
            data: state.yearlyData.map(d => d.teachingAchievements?.provincialReformProject ?? 0)
          },
          {
            name: '校级教改工程项目',
            data: state.yearlyData.map(d => d.teachingAchievements?.schoolReformProject ?? 0)
          }
        ]
      }
    }
  },

  actions: {
    setYear(year: string) {
      this.selectedYear = year
    },
    async fetchYearlyData() {
      this.loading = true
      this.error = null
      try {
        const res = await disciplineApi.getYearlyData()
        if (Array.isArray(res.data) && res.data.length > 0) {
          this.yearlyData = res.data
          const latest = res.data[res.data.length - 1]
          if (latest?.year) {
            this.selectedYear = latest.year
          }
        }
      } catch (error: any) {
        const msg = error?.message || '获取学科数据失败'
        this.error = msg
        ElMessage.error(msg)
        console.error('获取学科数据失败:', error)
      } finally {
        this.loading = false
      }
    }
  }
})
