<template>
  <DashboardLayout
    title="党建思政总览"
    :years="years"
    :selectedYear="selectedYear"
    @yearChange="handleYearChange"
  >
    <template #left-top>
      <SectionPanel
        :title="studentScaleTitle"
        border-type="box-10"
        clickable
        show-mode-indicator
        :is-trend-mode="studentScaleTrendMode"
        @title-click="toggleStudentScaleTrend"
      >
        <Transition name="chart-fade" mode="out-in">
          <GroupedStudentChart v-if="!studentScaleTrendMode" :data="partyMembersChartData" height="100%" />
          <TrendChart v-else :x-data="studentScaleTrendData.years" :series="studentScaleTrendData.series" height="100%" />
        </Transition>
      </SectionPanel>
    </template>

    <template #left-bottom>
      <SectionPanel
        :title="politicalTitle"
        border-type="box-1"
        clickable
        show-mode-indicator
        :is-trend-mode="politicalTrendMode"
        @title-click="togglePoliticalTrend"
      >
        <Transition name="chart-fade" mode="out-in">
          <DonutChart
            v-if="!politicalTrendMode"
            :data="politicalChartData"
            :centerValue="(currentYearData.graduatePolitical?.partyMember ?? 0) + (currentYearData.graduatePolitical?.youthLeague ?? 0)"
            centerLabel="总人数"
            height="100%"
          />
          <TrendChart v-else :x-data="politicalTrendData.years" :series="politicalTrendData.series" height="100%" />
        </Transition>
      </SectionPanel>
    </template>

    <template #center-top>
      <div class="indicator-grid">
        <div class="indicator-card">
          <div class="indicator-icon">🏛️</div>
          <div class="indicator-value">{{ currentYearData.partyMembers.total }}</div>
          <div class="indicator-label">党员总数</div>
        </div>
        <div class="indicator-card">
          <div class="indicator-icon">🎗️</div>
          <div class="indicator-value">{{ totalYouthLeague }}</div>
          <div class="indicator-label">共青团员</div>
        </div>
        <div class="indicator-card">
          <div class="indicator-icon">⚖️</div>
          <div class="indicator-value">{{ totalDemocraticParties }}</div>
          <div class="indicator-label">民主党派</div>
        </div>
        <div class="indicator-card">
          <div class="indicator-icon">🎓</div>
          <div class="indicator-value">{{ ideologicalTeams }}</div>
          <div class="indicator-label">思政教学团队</div>
        </div>
      </div>
    </template>

    <template #center-bottom>
      <div class="section-content">
        <div class="section-header section-header--center">
          <div class="section-title">{{ selectedYear }}年分析结论</div>
        </div>
        <div class="analysis-section">
          <div class="analysis-item">
            <p>党员队伍建设方面，党员总数达 <span class="highlight">{{ currentYearData.partyMembers.total }}</span> 人，其中本科生党员 <span class="highlight">{{ currentYearData.partyMembers.undergraduate }}</span> 人、研究生党员 <span class="highlight">{{ currentYearData.partyMembers.graduate }}</span> 人，党员队伍持续壮大。</p>
          </div>
          <div class="analysis-item">
            <p>民主党派建设方面，各民主党派党员共计 <span class="highlight">{{ totalDemocraticParties }}</span> 人，涵盖民革，民盟，民建，民进、农工党、致公党、九三学社、台盟等八个民主党派，多党合作事业蓬勃发展。</p>
          </div>
          <div class="analysis-item">
            <p>青年群众方面，共青团员达 <span class="highlight">{{ totalYouthLeague }}</span> 人，其中本科生共青团员 <span class="highlight">{{ currentYearData.youthLeague.undergraduate }}</span> 人、研究生共青团员 <span class="highlight">{{ currentYearData.youthLeague.graduate }}</span> 人，青年群众基础牢固。</p>
          </div>
          <div class="analysis-item">
            <p>特殊群体关怀方面，少数民族学生 <span class="highlight">{{ currentYearData.specialGroups.minority }}</span> 人、残疾学生 <span class="highlight">{{ currentYearData.specialGroups.disabled }}</span> 人，国家级课程思政教学团队 <span class="highlight">{{ currentYearData.ideologicalTeams.nationalTeam }}</span> 个，省部级 <span class="highlight">{{ currentYearData.ideologicalTeams.provincialTeam }}</span> 个，立德树人成效显著。</p>
          </div>
        </div>
      </div>
    </template>

    <template #right-top>
      <SectionPanel
        :title="democraticTitle"
        border-type="box-10"
        clickable
        show-mode-indicator
        :is-trend-mode="democraticTrendMode"
        @title-click="toggleDemocraticTrend"
      >
        <Transition name="chart-fade" mode="out-in">
          <PieChart v-if="!democraticTrendMode" :data="democraticPartiesChartData" height="100%" />
          <TrendChart v-else :x-data="democraticTrendData.years" :series="democraticTrendData.series" height="100%" />
        </Transition>
      </SectionPanel>
    </template>

    <template #right-bottom>
      <SectionPanel
        :title="specialGroupsTitle"
        border-type="box-1"
        clickable
        show-mode-indicator
        :is-trend-mode="specialGroupsTrendMode"
        @title-click="toggleSpecialGroupsTrend"
      >
        <Transition name="chart-fade" mode="out-in">
          <PieChart v-if="!specialGroupsTrendMode" :data="specialGroupsChartData" height="100%" />
          <TrendChart v-else :x-data="specialGroupsTrendData.years" :series="specialGroupsTrendData.series" height="100%" />
        </Transition>
      </SectionPanel>
    </template>
  </DashboardLayout>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { usePartyStore } from '@/stores/party'
import DashboardLayout from '@/components/layout/DashboardLayout.vue'
import SectionPanel from '@/components/common/SectionPanel.vue'
import GroupedStudentChart from '@/components/charts/GroupedStudentChart.vue'
import DonutChart from '@/components/charts/DonutChart.vue'
import PieChart from '@/components/charts/PieChart.vue'
import TrendChart from '@/components/charts/TrendChart.vue'

const partyStore = usePartyStore()

onMounted(() => {
  partyStore.fetchYearlyData()
})

const selectedYear = computed(() => partyStore.selectedYear)
const years = computed(() => partyStore.years)
const currentYearData = computed(() => partyStore.currentYearData)

const partyMembersChartData = computed(() => partyStore.partyMembersChartData)
const politicalChartData = computed(() => partyStore.politicalChartData)
const democraticPartiesChartData = computed(() => partyStore.democraticPartiesChartData)
const specialGroupsChartData = computed(() => partyStore.specialGroupsChartData)

const studentScaleTrendData = computed(() => partyStore.studentScaleTrendData)
const politicalTrendData = computed(() => partyStore.politicalTrendData)
const democraticTrendData = computed(() => partyStore.democraticTrendData)
const specialGroupsTrendData = computed(() => partyStore.specialGroupsTrendData)

const studentScaleTrendMode = ref(false)
const politicalTrendMode = ref(false)
const democraticTrendMode = ref(false)
const specialGroupsTrendMode = ref(false)

const studentScaleTitle = computed(() =>
  studentScaleTrendMode.value ? '学生规模趋势' : `${selectedYear.value}年学生规模`
)
const politicalTitle = computed(() =>
  politicalTrendMode.value ? '硕博政治面貌趋势' : `${selectedYear.value}年硕博政治面貌`
)
const democraticTitle = computed(() =>
  democraticTrendMode.value ? '民主党派趋势' : `${selectedYear.value}年民主党派构成`
)
const specialGroupsTitle = computed(() =>
  specialGroupsTrendMode.value ? '特殊群体趋势' : `${selectedYear.value}年特殊群体构成`
)

const toggleStudentScaleTrend = () => { studentScaleTrendMode.value = !studentScaleTrendMode.value }
const togglePoliticalTrend = () => { politicalTrendMode.value = !politicalTrendMode.value }
const toggleDemocraticTrend = () => { democraticTrendMode.value = !democraticTrendMode.value }
const toggleSpecialGroupsTrend = () => { specialGroupsTrendMode.value = !specialGroupsTrendMode.value }

const totalYouthLeague = computed(() => {
  const { youthLeague } = currentYearData.value
  return youthLeague.undergraduate + youthLeague.graduate
})

const totalDemocraticParties = computed(() => {
  const { democraticParties } = currentYearData.value
  return democraticParties.revolutionary + democraticParties.league + democraticParties.construction +
    democraticParties.progress + democraticParties.farmersWorkers + democraticParties.zhiGong +
    democraticParties.jiuSan + democraticParties.taiwanLeague
})

const ideologicalTeams = computed(() => {
  const { ideologicalTeams } = currentYearData.value
  return ideologicalTeams.nationalTeam + ideologicalTeams.provincialTeam
})

const handleYearChange = (year: string) => {
  partyStore.setYear(year)
}
</script>

<style scoped lang="scss">
.section-content {
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 4px;
  flex-shrink: 0;

  &--center {
    justify-content: center;
  }
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  color: var(--text-accent);
  letter-spacing: 2px;
  text-shadow: 0 0 8px rgba(0, 212, 255, 0.6);
  white-space: nowrap;
}

.indicator-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  flex: 1;
  align-items: center;
}

.indicator-card {
  background: rgba(12, 30, 60, 0.6);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-sm);
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 12px;
  box-shadow: 0 0 16px rgba(0, 102, 255, 0.4);
  transition: all 0.3s ease;

  &:hover {
    transform: scale(1.02);
    box-shadow: 0 0 24px rgba(0, 212, 255, 0.6);
    border-color: var(--text-accent);
  }
}

.indicator-icon {
  font-size: 32px;
}

.indicator-value {
  font-size: 32px;
  font-weight: bold;
  color: var(--text-accent);
  font-family: var(--font-mono);
  text-shadow: 0 0 8px rgba(0, 212, 255, 0.5);
}

.indicator-label {
  font-size: 14px;
  color: var(--text-secondary);
}

.analysis-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 16px;
  padding: 16px 16px 16px 0px;
  background: rgba(12, 30, 60, 0.4);
  border-radius: var(--radius-sm);
  overflow-y: auto;
}

.analysis-item {
  padding: 12px 16px;
  background: rgba(17, 34, 64, 0.6);
  border-left: 3px solid var(--text-accent);
  border-radius: 0 4px 4px 0;
  width: 100%;
  max-width: 100%;

  p {
    font-size: 15px;
    color: #e2e8f0;
    line-height: 1.8;
    margin: 0;
    text-align: left;

    .highlight {
      color: #ffd700;
      font-weight: bold;
      text-shadow: 0 0 8px rgba(255, 215, 0, 0.5);
    }
  }
}

.chart-fade-enter-active,
.chart-fade-leave-active {
  transition: opacity 0.3s ease;
}

.chart-fade-enter-from,
.chart-fade-leave-to {
  opacity: 0;
}
</style>
