import { defineStore } from 'pinia'
import type { FinanceState, YearlyFinanceData } from '@/types'
import { financeApi } from '@/api'
import { ElMessage } from 'element-plus'

export const useFinanceStore = defineStore('finance', {
  state: (): FinanceState => ({
    yearlyData: [],
    selectedYear: '2025',
    loading: false,
    error: null as string | null
  }),

  getters: {
    years: (state): string[] => state.yearlyData.map(d => d.year),
    currentYearData: (state): YearlyFinanceData => {
      const found = state.yearlyData.find(d => d.year === state.selectedYear)
        || state.yearlyData[state.yearlyData.length - 1]
      if (!found) {
        return {
          year: state.selectedYear,
          campus: { schoolArea: null, teachingArea: null, labArea: null, dormitoryArea: null, horizontalFunding: 0 },
          assets: { fixedAssets: null, equipmentCount: 0, equipmentValue: 0, largeEquipmentCount: 0, largeEquipmentValue: 0 },
          research: { verticalFunding: 0 }
        }
      }
      return found
    },
    // DB存万元，前端转换为百万元显示
    assetsChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      return [
        { name: '固定资产', value: (data.assets.fixedAssets ?? 0) / 100 },
        { name: '设备总值', value: (data.assets.equipmentValue ?? 0) / 100 },
        { name: '大型设备', value: (data.assets.largeEquipmentValue ?? 0) / 100 }
      ]
    },
    campusChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      return [
        { name: '教学行政', value: data.campus.teachingArea ?? 0 },
        { name: '实验室', value: data.campus.labArea ?? 0 },
        { name: '学生宿舍', value: data.campus.dormitoryArea ?? 0 }
      ]
    },
    fundingChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      return [
        { name: '纵向经费', value: data.research.verticalFunding ?? 0 },
        { name: '横向经费', value: data.campus.horizontalFunding ?? 0 }
      ]
    },
    equipmentChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      return [
        { name: '大型设备', value: data.assets.largeEquipmentCount ?? 0 },
        { name: '普通设备', value: (data.assets.equipmentCount ?? 0) - (data.assets.largeEquipmentCount ?? 0) }
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
        const res = await financeApi.getData()
        if (Array.isArray(res.data) && res.data.length > 0) {
          this.yearlyData = res.data
          const latest = res.data[res.data.length - 1]
          if (latest?.year) {
            this.selectedYear = latest.year
          }
        } else {
          ElMessage.warning('未获取到财务数据，请检查后端服务')
          this.yearlyData = []
        }
      } catch (error: any) {
        const msg = error?.message || '获取财务数据失败'
        this.error = msg
        ElMessage.error(msg)
        console.error('获取财务数据失败:', error)
      } finally {
        this.loading = false
      }
    }
  }
})
