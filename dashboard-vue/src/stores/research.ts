import { defineStore } from 'pinia'
import type { ScienceResearchState, YearlyResearchData } from '@/types'
import { researchApi } from '@/api'
import { ElMessage } from 'element-plus'

export const useResearchStore = defineStore('research', {
  state: (): ScienceResearchState => ({
    yearlyData: [],
    selectedYear: '2025',
    loading: false,
    error: null as string | null
  }),

  getters: {
    years: (state): string[] => state.yearlyData.map(d => d.year),
    currentYearData: (state): YearlyResearchData => {
      const found = state.yearlyData.find(d => d.year === state.selectedYear)
        || state.yearlyData[state.yearlyData.length - 1]
      return found || emptyYearData()
    },
    projectChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      return [
        { name: '国家级', value: data.projects.national ?? 0 },
        { name: '部级', value: data.projects.ministerial ?? 0 },
        { name: '省级', value: data.projects.provincial ?? 0 },
        { name: '厅局级', value: data.projects.prefectural ?? 0 },
        { name: '校级', value: data.projects.school ?? 0 }
      ]
    },
    // DB存万元，前端转换为百万元显示
    fundingChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      return [
        { name: '纵向经费', value: (data.funding.vertical ?? 0) / 100 },
        { name: '横向经费', value: (data.funding.horizontal ?? 0) / 100 }
      ]
    },
    paperChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      return [
        { name: 'SCI一区', value: data.papers.sciQ1 ?? 0 },
        { name: 'SCI二区', value: data.papers.sciQ2 ?? 0 },
        { name: 'SCI三区', value: data.papers.sciQ3 ?? 0 },
        { name: 'SCI四区', value: data.papers.sciQ4 ?? 0 },
        { name: 'EI', value: data.papers.ei ?? 0 },
        { name: 'CSCD', value: data.papers.cscd ?? 0 },
        { name: '核心期刊', value: data.papers.core ?? 0 },
        { name: '其他', value: data.papers.other ?? 0 }
      ]
    },
    patentChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      return [
        { name: '发明专利', value: data.publications.patents.invention ?? 0 },
        { name: '实用新型', value: data.publications.patents.utility ?? 0 },
        { name: '外观设计', value: data.publications.patents.design ?? 0 },
        { name: '动植物新品种', value: data.publications.varieties ?? 0 },
        { name: '标准发布', value: data.publications.standards ?? 0 },
        { name: '软件著作权', value: data.publications.software ?? 0 }
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
        const res = await researchApi.getYearlyData()
        if (Array.isArray(res.data) && res.data.length > 0) {
          this.yearlyData = res.data
          const latest = res.data[res.data.length - 1]
          if (latest?.year) {
            this.selectedYear = latest.year
          }
        }
      } catch (error: any) {
        const msg = error?.message || '获取科研数据失败'
        this.error = msg
        ElMessage.error(msg)
        console.error('获取科研数据失败:', error)
      } finally {
        this.loading = false
      }
    },
  }
})

function emptyYearData(): YearlyResearchData {
  return {
    year: '',
    projects: { national: 0, ministerial: 0, provincial: 0, prefectural: 0, school: 0, total: 0 },
    platforms: { national: 0, provincial: 0, other: 0 },
    funding: { vertical: 0, horizontal: 0, total: 0 },
    awards: { first: 0, second: 0, third: 0 },
    publications: { books: 0, patents: { invention: 0, utility: 0, design: 0 }, varieties: 0, standards: 0, software: 0, total: 0 },
    papers: { sciQ1: 0, sciQ2: 0, sciQ3: 0, sciQ4: 0, ei: 0, cscd: 0, core: 0, other: 0, total: 0 }
  }
}
