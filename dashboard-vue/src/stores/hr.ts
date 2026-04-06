import { defineStore } from 'pinia'
import type { HrState, YearlyHrData } from '@/types'
import { hrApi } from '@/api'
import { ElMessage } from 'element-plus'

export const useHrStore = defineStore('hr', {
  state: (): HrState => ({
    yearlyData: [],
    selectedYear: '2025',
    loading: false,
    error: null as string | null
  }),

  getters: {
    years: (state): string[] => state.yearlyData.map(d => d.year),
    currentYearData: (state): YearlyHrData => {
      const found = state.yearlyData.find(d => d.year === state.selectedYear)
        || state.yearlyData[state.yearlyData.length - 1]
      if (!found) {
        return {
          year: state.selectedYear,
          staff: { total: 0, fullTime: 0, management: 0, supporting: 0, external: 0 },
          education: { doctorate: 0, master: 0, bachelor: 0 },
          title: { professor: 0, associate: 0, lecturer: 0, assistant: 0 },
          talents: { nationalMaster: 0, provincialMaster: 0, huangdanianTeam: 0, nationalTeam: 0, provincialTeam: 0 }
        }
      }
      return found
    },
    titleChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      return [
        { name: '正高', value: data.title.professor ?? 0 },
        { name: '副高', value: data.title.associate ?? 0 },
        { name: '中级', value: data.title.lecturer ?? 0 },
        { name: '初级及以下', value: data.title.assistant ?? 0 }
      ]
    },
    staffChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      return [
        { name: '专任教师', value: data.staff.fullTime ?? 0 },
        { name: '管理人员', value: data.staff.management ?? 0 },
        { name: '教辅人员', value: data.staff.supporting ?? 0 },
        { name: '外聘教师', value: data.staff.external ?? 0 }
      ]
    },
    educationChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      return [
        { name: '博士', value: data.education.doctorate ?? 0 },
        { name: '硕士', value: data.education.master ?? 0 },
        { name: '本科及以下', value: data.education.bachelor ?? 0 }
      ]
    },
    talentsChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      return [
        { name: '国家教学团队', value: data.talents.nationalTeam ?? 0 },
        { name: '省级教学团队', value: data.talents.provincialTeam ?? 0 },
        { name: '黄大年团队', value: data.talents.huangdanianTeam ?? 0 }
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
        const res = await hrApi.getData()
        if (Array.isArray(res.data) && res.data.length > 0) {
          this.yearlyData = res.data
          const latest = res.data[res.data.length - 1]
          if (latest?.year) {
            this.selectedYear = latest.year
          }
        } else {
          ElMessage.warning('未获取到人事数据，请检查后端服务')
          this.yearlyData = []
        }
      } catch (error: any) {
        const msg = error?.message || '获取人事数据失败'
        this.error = msg
        ElMessage.error(msg)
        console.error('获取人事数据失败:', error)
      } finally {
        this.loading = false
      }
    }
  }
})
