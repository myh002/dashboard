import { defineStore } from 'pinia'
import type { PartyState, YearlyPartyData } from '@/types'
import { partyApi } from '@/api'
import { ElMessage } from 'element-plus'

export const usePartyStore = defineStore('party', {
  state: (): PartyState => ({
    yearlyData: [],
    selectedYear: '2025',
    loading: false,
    error: null as string | null
  }),

  getters: {
    years: (state): string[] => state.yearlyData.map(d => d.year),
    currentYearData: (state): YearlyPartyData => {
      const found = state.yearlyData.find(d => d.year === state.selectedYear)
        || state.yearlyData[state.yearlyData.length - 1]
      if (!found) {
        return {
          year: state.selectedYear,
          partyMembers: { total: 0, undergraduate: 0, graduate: 0 },
          democraticParties: { revolutionary: 0, league: 0, construction: 0, progress: 0, farmersWorkers: 0, zhiGong: 0, jiuSan: 0, taiwanLeague: 0 },
          youthLeague: { undergraduate: 0, graduate: 0 },
          specialGroups: { minority: 0, disabled: 0 },
          ideologicalTeams: { nationalTeam: 0, provincialTeam: 0 },
          studentScale: { undergraduateMale: 0, undergraduateFemale: 0, masterMale: 0, masterFemale: 0, phdMale: 0, phdFemale: 0 },
          graduatePolitical: { partyMember: 0, youthLeague: 0 }
        }
      }
      return found
    },
    partyMembersChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data || !data.studentScale) return []
      return [
        { name: '本科生（男）', value: data.studentScale.undergraduateMale ?? 0 },
        { name: '本科生（女）', value: data.studentScale.undergraduateFemale ?? 0 },
        { name: '硕士生（男）', value: data.studentScale.masterMale ?? 0 },
        { name: '硕士生（女）', value: data.studentScale.masterFemale ?? 0 },
        { name: '博士生（男）', value: data.studentScale.phdMale ?? 0 },
        { name: '博士生（女）', value: data.studentScale.phdFemale ?? 0 }
      ]
    },
    politicalChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data || !data.graduatePolitical) return []
      return [
        { name: '中共党员', value: data.graduatePolitical.partyMember ?? 0 },
        { name: '共青团员', value: data.graduatePolitical.youthLeague ?? 0 }
      ]
    },
    democraticPartiesChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      return [
        { name: '民革', value: data.democraticParties.revolutionary ?? 0 },
        { name: '民盟', value: data.democraticParties.league ?? 0 },
        { name: '民建', value: data.democraticParties.construction ?? 0 },
        { name: '民进', value: data.democraticParties.progress ?? 0 },
        { name: '农工党', value: data.democraticParties.farmersWorkers ?? 0 },
        { name: '致公党', value: data.democraticParties.zhiGong ?? 0 },
        { name: '九三学社', value: data.democraticParties.jiuSan ?? 0 },
        { name: '台盟', value: data.democraticParties.taiwanLeague ?? 0 }
      ]
    },
    specialGroupsChartData: (state) => {
      const data = state.yearlyData.find(d => d.year === state.selectedYear) || state.yearlyData[state.yearlyData.length - 1]
      if (!data) return []
      return [
        { name: '少数民族', value: data.specialGroups.minority ?? 0 },
        { name: '残疾人', value: data.specialGroups.disabled ?? 0 }
      ]
    },
    studentScaleTrendData: (state) => {
      const years = state.yearlyData.map(d => d.year)
      return {
        years,
        series: [
          { name: '本科生（男）', data: state.yearlyData.map(d => d.studentScale?.undergraduateMale ?? 0) },
          { name: '本科生（女）', data: state.yearlyData.map(d => d.studentScale?.undergraduateFemale ?? 0) },
          { name: '硕士生（男）', data: state.yearlyData.map(d => d.studentScale?.masterMale ?? 0) },
          { name: '硕士生（女）', data: state.yearlyData.map(d => d.studentScale?.masterFemale ?? 0) },
          { name: '博士生（男）', data: state.yearlyData.map(d => d.studentScale?.phdMale ?? 0) },
          { name: '博士生（女）', data: state.yearlyData.map(d => d.studentScale?.phdFemale ?? 0) }
        ]
      }
    },
    politicalTrendData: (state) => {
      const years = state.yearlyData.map(d => d.year)
      return {
        years,
        series: [
          { name: '中共党员', data: state.yearlyData.map(d => d.graduatePolitical?.partyMember ?? 0) },
          { name: '共青团员', data: state.yearlyData.map(d => d.graduatePolitical?.youthLeague ?? 0) }
        ]
      }
    },
    democraticTrendData: (state) => {
      const years = state.yearlyData.map(d => d.year)
      return {
        years,
        series: [
          { name: '民革', data: state.yearlyData.map(d => d.democraticParties?.revolutionary ?? 0) },
          { name: '民盟', data: state.yearlyData.map(d => d.democraticParties?.league ?? 0) },
          { name: '民建', data: state.yearlyData.map(d => d.democraticParties?.construction ?? 0) },
          { name: '民进', data: state.yearlyData.map(d => d.democraticParties?.progress ?? 0) },
          { name: '农工党', data: state.yearlyData.map(d => d.democraticParties?.farmersWorkers ?? 0) },
          { name: '致公党', data: state.yearlyData.map(d => d.democraticParties?.zhiGong ?? 0) },
          { name: '九三学社', data: state.yearlyData.map(d => d.democraticParties?.jiuSan ?? 0) },
          { name: '台盟', data: state.yearlyData.map(d => d.democraticParties?.taiwanLeague ?? 0) }
        ]
      }
    },
    specialGroupsTrendData: (state) => {
      const years = state.yearlyData.map(d => d.year)
      return {
        years,
        series: [
          { name: '少数民族', data: state.yearlyData.map(d => d.specialGroups?.minority ?? 0) },
          { name: '残疾人', data: state.yearlyData.map(d => d.specialGroups?.disabled ?? 0) }
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
        const res = await partyApi.getData()
        if (Array.isArray(res.data) && res.data.length > 0) {
          this.yearlyData = res.data
          const latest = res.data[res.data.length - 1]
          if (latest?.year) {
            this.selectedYear = latest.year
          }
        } else {
          ElMessage.warning('未获取到党建数据，请检查后端服务')
          this.yearlyData = []
        }
      } catch (error: any) {
        const msg = error?.message || '获取党建数据失败'
        this.error = msg
        ElMessage.error(msg)
        console.error('获取党建数据失败:', error)
      } finally {
        this.loading = false
      }
    }
  }
})
