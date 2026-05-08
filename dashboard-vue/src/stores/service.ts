import { defineStore } from 'pinia'
import type { ServiceState, YearlyServiceData } from '@/types'
import { serviceApi } from '@/api'
import { ElMessage } from 'element-plus'

export const useServiceStore = defineStore('service', {
  state: (): ServiceState => ({
    yearlyData: [],
    selectedYear: '2025',
    loading: false,
    error: null as string | null
  }),

  getters: {
    years: (state): string[] => state.yearlyData.map(d => d.year),
    currentYearData: (state): YearlyServiceData => {
      const found = state.yearlyData.find(d => d.year === state.selectedYear)
        || state.yearlyData[state.yearlyData.length - 1]
      if (!found) {
        return {
          year: state.selectedYear,
          cooperation: { horizontalFunding: 0, inventionPatents: 0, utilityPatents: 0, designPatents: 0, varieties: 0, standards: 0, software: 0 },
          employment: { undergraduate: 0, undergraduateRate: 0, master: 0, phd: 0, graduateRate: 0 },
          international: { internationalStudents: 0, cooperativePrograms: 0, practiceBases: 0 },
          experts: { externalTeachers: 0 }
        }
      }
      return found
    },
    techOutputChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      return [
        { name: '标准发布', value: data.cooperation.standards ?? 0 },
        { name: '软件著作权', value: data.cooperation.software ?? 0 }
      ]
    },
    cooperationChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      return [
        { name: '发明专利', value: data.cooperation.inventionPatents ?? 0 },
        { name: '实用新型专利', value: data.cooperation.utilityPatents ?? 0 },
        { name: '外观设计专利', value: data.cooperation.designPatents ?? 0 },
        { name: '动植物新品种', value: data.cooperation.varieties ?? 0 }
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
    employmentChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      return [
        { name: '本科生', value: data.employment.undergraduate ?? 0 },
        { name: '研究生', value: data.employment.master ?? 0 }
      ]
    },
    cooperationTrendData: (state) => {
      const years = state.yearlyData.map(d => d.year)
      return {
        years,
        series: [
          { name: '发明专利', data: state.yearlyData.map(d => d.cooperation?.inventionPatents ?? 0) },
          { name: '实用新型专利', data: state.yearlyData.map(d => d.cooperation?.utilityPatents ?? 0) },
          { name: '外观设计专利', data: state.yearlyData.map(d => d.cooperation?.designPatents ?? 0) },
          { name: '动植物新品种', data: state.yearlyData.map(d => d.cooperation?.varieties ?? 0) }
        ]
      }
    },
    techOutputTrendData: (state) => {
      const years = state.yearlyData.map(d => d.year)
      return {
        years,
        series: [
          { name: '标准发布', data: state.yearlyData.map(d => d.cooperation?.standards ?? 0) },
          { name: '软件著作权', data: state.yearlyData.map(d => d.cooperation?.software ?? 0) }
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
    },
    employmentTrendData: (state) => {
      const years = state.yearlyData.map(d => d.year)
      return {
        years,
        series: [
          { name: '本科生', data: state.yearlyData.map(d => d.employment?.undergraduate ?? 0) },
          { name: '研究生', data: state.yearlyData.map(d => d.employment?.master ?? 0) }
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
        const res = await serviceApi.getData()
        if (Array.isArray(res.data) && res.data.length > 0) {
          this.yearlyData = res.data
          const latest = res.data[res.data.length - 1]
          if (latest?.year) {
            this.selectedYear = latest.year
          }
        } else {
          ElMessage.warning('未获取到服务数据，请检查后端服务')
          this.yearlyData = []
        }
      } catch (error: any) {
        const msg = error?.message || '获取服务数据失败'
        this.error = msg
        ElMessage.error(msg)
        console.error('获取服务数据失败:', error)
      } finally {
        this.loading = false
      }
    }
  }
})
