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
          majors: { ministryFeature: 0, provincialFeature: 0, provincialDemonstration: 0, provincialQualityCourse: 0, undergraduateTotal: 0, newThisYear: 0, discontinued: 0 },
          degreePoints: { postdoctoral: 0, doctoralFirst: 0, masterFirst: 0, masterSecond: 0, professional: 0, total: 0 },
          colleges: 0
        }
      }
      return {
        year: found.year,
        disciplines: found.disciplines ?? { provincialKey: 0, provincialEmphasis: 0, firstClassConstruction: 0, firstClass: 0 },
        majors: found.majors ?? { ministryFeature: 0, provincialFeature: 0, provincialDemonstration: 0, provincialQualityCourse: 0, undergraduateTotal: 0, newThisYear: 0, discontinued: 0 },
        degreePoints: found.degreePoints ?? { postdoctoral: 0, doctoralFirst: 0, masterFirst: 0, masterSecond: 0, professional: 0, total: 0 },
        colleges: found.colleges ?? 0
      }
    },
    disciplineChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      return [
        { name: '省级特色重点学科', value: data.disciplines.provincialKey ?? 0 },
        { name: '省级重点学科', value: data.disciplines.provincialEmphasis ?? 0 },
        { name: '一流建设学科', value: data.disciplines.firstClassConstruction ?? 0 },
        { name: '一流学科', value: data.disciplines.firstClass ?? 0 }
      ]
    },
    majorChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      return [
        { name: '教育部特色专业', value: data.majors.ministryFeature ?? 0 },
        { name: '省级特色专业', value: data.majors.provincialFeature ?? 0 },
        { name: '省级示范专业', value: data.majors.provincialDemonstration ?? 0 },
        { name: '省级精品课程', value: data.majors.provincialQualityCourse ?? 0 }
      ]
    },
    degreeRadarData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear)
        || state.yearlyData[state.yearlyData.length - 1]
      if (!data || !data.degreePoints) return { indicators: [], values: [] }
      const dp = data.degreePoints
      return {
        indicators: [
          { name: '博士后流动站', max: Math.max(15, (dp?.postdoctoral ?? 0) * 1.5) },
          { name: '一级博点', max: Math.max(35, (dp?.doctoralFirst ?? 0) * 1.5) },
          { name: '一级硕点', max: Math.max(65, (dp?.masterFirst ?? 0) * 1.5) },
          { name: '二级硕点', max: Math.max(120, (dp?.masterSecond ?? 0) * 1.5) },
          { name: '专业硕点', max: Math.max(60, (dp?.professional ?? 0) * 1.5) }
        ],
        values: [
          dp?.postdoctoral ?? 0,
          dp?.doctoralFirst ?? 0,
          dp?.masterFirst ?? 0,
          dp?.masterSecond ?? 0,
          dp?.professional ?? 0
        ]
      }
    },
    featureMajorChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      return [
        { name: '教育部特色专业', value: data.majors.ministryFeature ?? 0 },
        { name: '省级特色专业', value: data.majors.provincialFeature ?? 0 },
        { name: '省级示范专业', value: data.majors.provincialDemonstration ?? 0 }
      ]
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
