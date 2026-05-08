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
          undergraduate: { total: 0, male: 0, female: 0, graduates: 0, graduateRate: 0, grantRate: 0, degreeGranted: 0, employment: 0, employmentRate: 0 },
          master: { total: 0, male: 0, female: 0, supervisors: 0, graduates: 0, graduateRate: 0, grantRate: 0, degreeGranted: 0, employment: 0, employmentRate: 0 },
          phd: { total: 0, male: 0, female: 0, supervisors: 0, graduates: 0, graduateRate: 0, employment: 0, employmentRate: 0 },
          teaching: { courses: 0, professorCourses: 0, nationalReform: 0, provincialReform: 0, schoolReform: 0, teachingAward: 0, practiceBases: 0, nationalTeams: 0, provincialTeams: 0 },
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
    },
    sunburstChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) {
        return {
          undergraduates: { total: 0, rate: 0, degreeGranted: 0 },
          postgraduates: { total: 0, rate: 0, degreeGranted: 0 }
        }
      }
      const underGrad = data.undergraduate.graduates ?? 0
      const underRate = data.undergraduate.graduateRate ?? 0
      const underDegree = data.undergraduate.degreeGranted ?? 0

      const postGrad = data.master.graduates ?? 0
      const postRate = data.master.graduateRate ?? 0
      const postDegree = data.master.degreeGranted ?? 0

      return {
        undergraduates: { total: underGrad, rate: underRate, degreeGranted: underDegree },
        postgraduates: { total: postGrad, rate: postRate, degreeGranted: postDegree }
      }
    },
    studentScaleTrendData: (state) => {
      const years = state.yearlyData.map(d => d.year)
      return {
        years,
        series: [
          { name: '本科生', data: state.yearlyData.map(d => d.undergraduate?.total ?? 0) },
          { name: '硕士生', data: state.yearlyData.map(d => d.master?.total ?? 0) },
          { name: '博士生', data: state.yearlyData.map(d => d.phd?.total ?? 0) }
        ]
      }
    },
    genderTrendData: (state) => {
      const years = state.yearlyData.map(d => d.year)
      return {
        years,
        series: [
          { name: '男生', data: state.yearlyData.map(d => (d.undergraduate?.male ?? 0) + (d.master?.male ?? 0) + (d.phd?.male ?? 0)) },
          { name: '女生', data: state.yearlyData.map(d => (d.undergraduate?.female ?? 0) + (d.master?.female ?? 0) + (d.phd?.female ?? 0)) }
        ]
      }
    },
    graduateTrendData: (state) => {
      const years = state.yearlyData.map(d => d.year)
      return {
        years,
        series: [
          { name: '本科毕业人数', data: state.yearlyData.map(d => d.undergraduate?.graduates ?? 0) },
          { name: '硕士毕业人数', data: state.yearlyData.map(d => d.master?.graduates ?? 0) },
          { name: '博士毕业人数', data: state.yearlyData.map(d => d.phd?.graduates ?? 0) }
        ]
      }
    },
    internationalTrendData: (state) => {
      const years = state.yearlyData.map(d => d.year)
      return {
        years,
        series: [
          { name: '留学生', data: state.yearlyData.map(d => d.international?.internationalStudents ?? 0) },
          { name: '中外合作', data: state.yearlyData.map(d => d.international?.cooperativePrograms ?? 0) }
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
