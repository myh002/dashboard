<template>
  <DashboardLayout
    title="人事人才总览"
    :years="years"
    :selectedYear="selectedYear"
    @yearChange="handleYearChange"
  >
    <template #left-top>
      <SectionPanel :title="`${selectedYear}年职称结构分布`" border-type="box-10">
        <BarChart :data="titleChartData" height="100%" />
      </SectionPanel>
    </template>

    <template #left-bottom>
      <SectionPanel :title="`${selectedYear}年师资类别构成`" border-type="box-1">
        <DonutChart
          :data="staffChartData"
          :centerValue="currentYearData.staff.total"
          centerLabel="教职工总数"
          height="100%"
        />
      </SectionPanel>
    </template>

    <template #center-top>
      <div class="indicator-grid">
        <div class="indicator-card">
          <div class="indicator-icon">👥</div>
          <div class="indicator-value">{{ currentYearData.staff.total }}</div>
          <div class="indicator-label">教职工总数</div>
        </div>
        <div class="indicator-card">
          <div class="indicator-icon">🎓</div>
          <div class="indicator-value">{{ currentYearData.staff.fullTime }}</div>
          <div class="indicator-label">专任教师</div>
        </div>
        <div class="indicator-card">
          <div class="indicator-icon">📊</div>
          <div class="indicator-value">{{ graduateRatio }}%</div>
          <div class="indicator-label">硕博学历占比</div>
        </div>
        <div class="indicator-card">
          <div class="indicator-icon">🏆</div>
          <div class="indicator-value">{{ highLevelTalents }}</div>
          <div class="indicator-label">高层次人才</div>
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
            <p>师资队伍方面，教职工总数达 <span class="highlight">{{ currentYearData.staff.total }}</span> 人，其中专任教师 <span class="highlight">{{ currentYearData.staff.fullTime }}</span> 人、管理人员 <span class="highlight">{{ currentYearData.staff.management }}</span> 人、教辅人员 <span class="highlight">{{ currentYearData.staff.supporting }}</span> 人、外聘教师 <span class="highlight">{{ currentYearData.staff.external }}</span> 人，师资力量持续壮大。</p>
          </div>
          <div class="analysis-item">
            <p>学历结构方面，拥有博士学位教师 <span class="highlight">{{ currentYearData.education.doctorate }}</span> 人、硕士学位教师 <span class="highlight">{{ currentYearData.education.master }}</span> 人，硕博学历占比达 <span class="highlight">{{ graduateRatio }}%</span>，高层次学历人才比例逐年提升。</p>
          </div>
          <div class="analysis-item">
            <p>职称结构方面，正高级职称 <span class="highlight">{{ currentYearData.title.professor }}</span> 人、副高级职称 <span class="highlight">{{ currentYearData.title.associate }}</span> 人、中级职称 <span class="highlight">{{ currentYearData.title.lecturer }}</span> 人，职称结构合理分布。</p>
          </div>
          <div class="analysis-item">
            <p>高层次人才方面，拥有国家教学名师 <span class="highlight">{{ currentYearData.talents.nationalMaster }}</span> 人、省级教学名师 <span class="highlight">{{ currentYearData.talents.provincialMaster }}</span> 人、黄大年教师团队 <span class="highlight">{{ currentYearData.talents.huangdanianTeam }}</span> 个，国家级教学团队 <span class="highlight">{{ currentYearData.talents.nationalTeam }}</span> 个、省部级教学团队 <span class="highlight">{{ currentYearData.talents.provincialTeam }}</span> 个，人才梯队建设成效显著。</p>
          </div>
        </div>
      </div>
    </template>

    <template #right-top>
      <SectionPanel :title="`${selectedYear}年学历结构占比`" border-type="box-10">
        <PieChart :data="educationChartData" height="100%" />
      </SectionPanel>
    </template>

    <template #right-bottom>
      <SectionPanel :title="`${selectedYear}年教学团队构成`" border-type="box-1">
        <DonutChart
          :data="talentsChartData"
          :centerValue="currentYearData.talents.provincialTeam"
          centerLabel="省部级团队"
          height="100%"
        />
      </SectionPanel>
    </template>
  </DashboardLayout>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useHrStore } from '@/stores/hr'
import DashboardLayout from '@/components/layout/DashboardLayout.vue'
import SectionPanel from '@/components/common/SectionPanel.vue'
import BarChart from '@/components/charts/BarChart.vue'
import DonutChart from '@/components/charts/DonutChart.vue'
import PieChart from '@/components/charts/PieChart.vue'

const hrStore = useHrStore()

onMounted(() => {
  hrStore.fetchYearlyData()
})

const selectedYear = computed(() => hrStore.selectedYear)
const years = computed(() => hrStore.years)
const currentYearData = computed(() => hrStore.currentYearData)

const titleChartData = computed(() => hrStore.titleChartData)
const staffChartData = computed(() => hrStore.staffChartData)
const educationChartData = computed(() => hrStore.educationChartData)
const talentsChartData = computed(() => hrStore.talentsChartData)

const graduateRatio = computed(() => {
  const { education } = currentYearData.value
  const total = education.doctorate + education.master + education.bachelor
  return ((education.doctorate + education.master) / total * 100).toFixed(1)
})

const highLevelTalents = computed(() => {
  const { talents } = currentYearData.value
  return talents.nationalMaster + talents.provincialMaster + talents.huangdanianTeam + talents.nationalTeam + talents.provincialTeam
})

const handleYearChange = (year: string) => {
  hrStore.setYear(year)
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
