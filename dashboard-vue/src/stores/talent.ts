import { defineStore } from 'pinia'
import type { TalentState, YearlyTalentData } from '@/types'
import { talentApi } from '@/api'
import { ElMessage } from 'element-plus'

export const useTalentStore = defineStore('talent', {
  state: (): TalentState => ({
    yearlyData: [],
    selectedYear: '2025',
    loading: false,
    error: null as string | null
  }),

  getters: {
    years: (state): string[] => state.yearlyData.map(d => d.year),
    currentYearData: (state): YearlyTalentData => {
      const found = state.yearlyData.find(d => d.year === state.selectedYear)
        || state.yearlyData[state.yearlyData.length - 1]
      if (!found) {
        return {
          year: state.selectedYear,
          undergraduate: { total: 0, male: 0, female: 0, graduates: 0, graduateRate: 0, employment: 0, employmentRate: 0 },
          master: { total: 0, male: 0, female: 0, supervisors: 0, graduates: 0, graduateRate: 0, employment: 0, employmentRate: 0 },
          phd: { total: 0, male: 0, female: 0, supervisors: 0, graduates: 0, graduateRate: 0, employment: 0, employmentRate: 0 },
          teaching: { courses: 0, professorCourses: 0, nationalReform: 0, provincialReform: 0, schoolReform: 0, teachingAward: 0, practiceBases: 0 },
          international: { cooperativePrograms: 0, internationalStudents: 0 }
        }
      }
      return found
    },
    studentScaleChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      return [
        { name: '本科生', value: data.undergraduate.total ?? 0 },
        { name: '硕士生', value: data.master.total ?? 0 },
        { name: '博士生', value: data.phd.total ?? 0 }
      ]
    },
    genderChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      return [
        { name: '男生', value: (data.undergraduate.male ?? 0) + (data.master.male ?? 0) + (data.phd.male ?? 0) },
        { name: '女生', value: (data.undergraduate.female ?? 0) + (data.master.female ?? 0) + (data.phd.female ?? 0) }
      ]
    },
    graduateEmploymentChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      return [
        { name: '本科就业率', value: data.undergraduate.employmentRate ?? 0 },
        { name: '硕士就业率', value: data.master.employmentRate ?? 0 },
        { name: '博士就业率', value: data.phd.employmentRate ?? 0 }
      ]
    },
    internationalChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      return [
        { name: '留学生', value: data.international.internationalStudents ?? 0 },
        { name: '中外合作', value: data.international.cooperativePrograms ?? 0 }
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
        const res = await talentApi.getYearlyData()
        if (Array.isArray(res.data) && res.data.length > 0) {
          this.yearlyData = res.data
          const latest = res.data[res.data.length - 1]
          if (latest?.year) {
            this.selectedYear = latest.year
          }
        }
      } catch (error: any) {
        const msg = error?.message || '获取人才数据失败'
        this.error = msg
        ElMessage.error(msg)
        console.error('获取人才数据失败:', error)
      } finally {
        this.loading = false
      }
    }
  }
})
