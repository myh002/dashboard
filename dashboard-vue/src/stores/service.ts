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
          cooperation: { horizontalFunding: 0, inventionPatents: 0, utilityPatents: 0, standards: 0, software: 0 },
          employment: { undergraduate: 0, undergraduateRate: 0, master: 0, masterRate: 0, phd: 0, phdRate: 0 },
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
        { name: '发明专利', value: data.cooperation.inventionPatents ?? 0 },
        { name: '实用新型', value: data.cooperation.utilityPatents ?? 0 },
        { name: '标准发布', value: data.cooperation.standards ?? 0 },
        { name: '软件著作权', value: data.cooperation.software ?? 0 }
      ]
    },
    cooperationChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      return [
        { name: '发明专利', value: (data.cooperation.inventionPatents ?? 0) * 10 },
        { name: '实用新型', value: (data.cooperation.utilityPatents ?? 0) * 5 },
        { name: '标准发布', value: (data.cooperation.standards ?? 0) * 50 },
        { name: '软著成果', value: (data.cooperation.software ?? 0) * 8 }
      ]
    },
    internationalChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      return [
        { name: '留学生', value: data.international.internationalStudents ?? 0 },
        { name: '中外合作', value: data.international.cooperativePrograms ?? 0 },
        { name: '实践基地', value: data.international.practiceBases ?? 0 }
      ]
    },
    employmentChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      return [
        { name: '本科就业', value: data.employment.undergraduate ?? 0 },
        { name: '硕士就业', value: data.employment.master ?? 0 },
        { name: '博士就业', value: data.employment.phd ?? 0 }
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
