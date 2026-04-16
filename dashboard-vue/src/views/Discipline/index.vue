<template>
  <DashboardLayout
    title="科学建设总览"
    :years="years"
    :selectedYear="selectedYear"
    @yearChange="handleYearChange"
  >
    <template #left-top>
      <SectionPanel :title="`${selectedYear}年学科层次分布`" border-type="box-10">
        <BarChart :data="disciplineChartData" height="100%" />
      </SectionPanel>
    </template>

    <template #left-bottom>
      <SectionPanel :title="`${selectedYear}年本科生构成`" border-type="box-1">
        <DonutChart
          :data="majorChartData"
          :centerValue="(currentYearData.majors?.undergraduateMale || 0) + (currentYearData.majors?.undergraduateFemale || 0)"
          centerLabel="本科生总数"
          height="100%"
        />
      </SectionPanel>
    </template>

    <template #center-top>
      <div class="indicator-grid">
        <div class="indicator-card">
          <div class="indicator-icon">🏛️</div>
          <div class="indicator-value">{{ currentYearData.colleges || 0 }}</div>
          <div class="indicator-label">学院数</div>
        </div>
        <div class="indicator-card">
          <div class="indicator-icon">🎓</div>
          <div class="indicator-value">{{ currentYearData.degreePoints?.doctoralFirst || 0 }}</div>
          <div class="indicator-label">一级博点</div>
        </div>
        <div class="indicator-card">
          <div class="indicator-icon">📚</div>
          <div class="indicator-value">{{ doctoralAndMasterTotal || 0 }}</div>
          <div class="indicator-label">硕博点总数</div>
        </div>
        <div class="indicator-card">
          <div class="indicator-icon">📖</div>
          <div class="indicator-value">{{ currentYearData.majors?.undergraduateTotal || 0 }}</div>
          <div class="indicator-label">本科专业</div>
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
            <p>学科建设方面，拥有省级特色重点学科 <span class="highlight">{{ currentYearData.disciplines?.provincialKey || 0 }}</span> 个、省级重点学科 <span class="highlight">{{ currentYearData.disciplines?.provincialEmphasis || 0 }}</span> 个，一流建设学科 <span class="highlight">{{ currentYearData.disciplines?.firstClassConstruction || 0 }}</span> 个、一流学科 <span class="highlight">{{ currentYearData.disciplines?.firstClass || 0 }}</span> 个，学科竞争力持续提升。</p>
          </div>
          <div class="analysis-item">
            <p>学位点布局方面，博士后流动站 <span class="highlight">{{ currentYearData.degreePoints?.postdoctoral || 0 }}</span> 个、一级博点 <span class="highlight">{{ currentYearData.degreePoints?.doctoralFirst || 0 }}</span> 个、一级硕点 <span class="highlight">{{ currentYearData.degreePoints?.masterFirst || 0 }}</span> 个、二级硕点 <span class="highlight">{{ currentYearData.degreePoints?.masterSecond || 0 }}</span> 个、专业硕点 <span class="highlight">{{ currentYearData.degreePoints?.professional || 0 }}</span> 个，学位层次结构完善。</p>
          </div>
          <div class="analysis-item">
            <p>学生构成方面，本科男生 <span class="highlight">{{ currentYearData.majors?.undergraduateMale || 0 }}</span> 人、本科女生 <span class="highlight">{{ currentYearData.majors?.undergraduateFemale || 0 }}</span> 人，形成良好的学科专业协同发展格局。</p>
          </div>
          <div class="analysis-item">
            <p>教学成果方面，拥有省级教学成果奖 <span class="highlight">{{ currentYearData.teachingAchievements?.provincialTeachingAward || 0 }}</span> 个、省级教改工程项目 <span class="highlight">{{ currentYearData.teachingAchievements?.provincialReformProject || 0 }}</span> 个、校级教改工程项目 <span class="highlight">{{ currentYearData.teachingAchievements?.schoolReformProject || 0 }}</span> 个，形成 <span class="highlight">{{ currentYearData.colleges || 0 }}</span> 个学院的学科专业协同发展格局。</p>
          </div>
        </div>
      </div>
    </template>

    <template #right-top>
      <SectionPanel :title="`${selectedYear}年职称分布`" border-type="box-10">
        <BarChart :data="titleDistributionChartData" height="100%" />
      </SectionPanel>
    </template>

    <template #right-bottom>
      <SectionPanel :title="`${selectedYear}年教学成果分布`" border-type="box-1">
        <PieChart :data="teachingAchievementsChartData" height="100%" />
      </SectionPanel>
    </template>
  </DashboardLayout>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useDisciplineStore } from '@/stores/discipline'
import DashboardLayout from '@/components/layout/DashboardLayout.vue'
import SectionPanel from '@/components/common/SectionPanel.vue'
import BarChart from '@/components/charts/BarChart.vue'
import DonutChart from '@/components/charts/DonutChart.vue'
import PieChart from '@/components/charts/PieChart.vue'

const disciplineStore = useDisciplineStore()

onMounted(() => {
  disciplineStore.fetchYearlyData()
})

const selectedYear = computed(() => disciplineStore.selectedYear)
const years = computed(() => disciplineStore.years)
const currentYearData = computed(() => disciplineStore.currentYearData)

const disciplineChartData = computed(() => disciplineStore.disciplineChartData)
const majorChartData = computed(() => disciplineStore.majorChartData)
const titleDistributionChartData = computed(() => disciplineStore.titleDistributionChartData)
const teachingAchievementsChartData = computed(() => disciplineStore.teachingAchievementsChartData)

const doctoralAndMasterTotal = computed(() => {
  const dp = currentYearData.value.degreePoints
  if (!dp) return 0
  return (dp.doctoralFirst ?? 0) + (dp.masterFirst ?? 0) + (dp.masterSecond ?? 0) + (dp.professional ?? 0)
})

const handleYearChange = (year: string) => {
  disciplineStore.setYear(year)
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
