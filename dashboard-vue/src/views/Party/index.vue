<template>
  <DashboardLayout
    title="党建思政总览"
    :years="years"
    :selectedYear="selectedYear"
    @yearChange="handleYearChange"
  >
    <template #left-top>
      <SectionPanel :title="`${selectedYear}年学生规模（学历×性别）`" border-type="box-10">
        <GroupedStudentChart :data="partyMembersChartData" height="100%" />
      </SectionPanel>
    </template>

    <template #left-bottom>
      <SectionPanel :title="`${selectedYear}年硕博政治面貌`" border-type="box-1">
        <DonutChart
          :data="politicalChartData"
          :centerValue="(currentYearData.graduatePolitical?.partyMember ?? 0) + (currentYearData.graduatePolitical?.youthLeague ?? 0)"
          centerLabel="总人数"
          height="100%"
        />
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
            <p>民主党派建设方面，各民主党派党员共计 <span class="highlight">{{ totalDemocraticParties }}</span> 人，涵盖民革、民盟、民建、民进、农工党、致公党、九三学社、台盟等八个民主党派，多党合作事业蓬勃发展。</p>
          </div>
          <div class="analysis-item">
            <p>青年群众方面，共青团员达 <span class="highlight">{{ totalYouthLeague }}</span> 人，其中本科生共青团员 <span class="highlight">{{ currentYearData.youthLeague.undergraduate }}</span> 人、研究生共青团员 <span class="highlight">{{ currentYearData.youthLeague.graduate }}</span> 人，青年群众基础牢固。</p>
          </div>
          <div class="analysis-item">
            <p>特殊群体关怀方面，少数民族学生 <span class="highlight">{{ currentYearData.specialGroups.minority }}</span> 人、残疾学生 <span class="highlight">{{ currentYearData.specialGroups.disabled }}</span> 人，国家级课程思政教学团队 <span class="highlight">{{ currentYearData.ideologicalTeams.nationalTeam }}</span> 个、省部级 <span class="highlight">{{ currentYearData.ideologicalTeams.provincialTeam }}</span> 个，立德树人成效显著。</p>
          </div>
        </div>
      </div>
    </template>

    <template #right-top>
      <SectionPanel :title="`${selectedYear}年民主党派构成`" border-type="box-10">
        <PieChart :data="democraticPartiesChartData" height="100%" />
      </SectionPanel>
    </template>

    <template #right-bottom>
      <SectionPanel :title="`${selectedYear}年特殊群体构成`" border-type="box-1">
        <PieChart :data="specialGroupsChartData" height="100%" />
      </SectionPanel>
    </template>
  </DashboardLayout>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { usePartyStore } from '@/stores/party'
import DashboardLayout from '@/components/layout/DashboardLayout.vue'
import SectionPanel from '@/components/common/SectionPanel.vue'
import GroupedStudentChart from '@/components/charts/GroupedStudentChart.vue'
import DonutChart from '@/components/charts/DonutChart.vue'
import PieChart from '@/components/charts/PieChart.vue'

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
  padding: 16px;
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
</style>
