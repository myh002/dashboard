<template>
  <DashboardLayout
    title="人才培养总览"
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
          <BarChart v-if="!studentScaleTrendMode" :data="studentScaleChartData" height="100%" />
          <TrendChart v-else :x-data="studentScaleTrendData.years" :series="studentScaleTrendData.series" height="100%" />
        </Transition>
      </SectionPanel>
    </template>

    <template #left-bottom>
      <SectionPanel
        :title="genderTitle"
        border-type="box-1"
        clickable
        show-mode-indicator
        :is-trend-mode="genderTrendMode"
        @title-click="toggleGenderTrend"
      >
        <Transition name="chart-fade" mode="out-in">
          <DonutChart
            v-if="!genderTrendMode"
            :data="genderChartData"
            :centerValue="totalStudents"
            centerLabel="在校生总数"
            height="100%"
          />
          <TrendChart v-else :x-data="genderTrendData.years" :series="genderTrendData.series" height="100%" />
        </Transition>
      </SectionPanel>
    </template>

    <template #center-top>
      <div class="indicator-grid">
        <div class="indicator-card">
          <div class="indicator-icon">👥</div>
          <div class="indicator-value">{{ totalStudents }}</div>
          <div class="indicator-label">在校生总数</div>
        </div>
        <div class="indicator-card">
          <div class="indicator-icon">🎓</div>
          <div class="indicator-value">{{ totalGraduates }}</div>
          <div class="indicator-label">毕业人数</div>
        </div>
        <div class="indicator-card">
          <div class="indicator-icon">🏗️</div>
          <div class="indicator-value">{{ totalEngineeringProjects }}</div>
          <div class="indicator-label">工程项目数</div>
        </div>
        <div class="indicator-card">
          <div class="indicator-icon">🏕️</div>
          <div class="indicator-value">{{ totalTeachingTeams }}</div>
          <div class="indicator-label">教学团队数</div>
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
            <p>学生规模方面，本科生 <span class="highlight">{{ currentYearData.undergraduate.total }}</span> 人、硕士生 <span class="highlight">{{ currentYearData.master.total }}</span> 人、博士生 <span class="highlight">{{ currentYearData.phd.total }}</span> 人，在校生总数达 <span class="highlight">{{ totalStudents }}</span> 人，人才培养层次持续完善。</p>
          </div>
          <div class="analysis-item">
            <p>学位授予方面，本科授予率 <span class="highlight">{{ (currentYearData.undergraduate.grantRate * 100).toFixed(1) }}%</span>、研究生授予率 <span class="highlight">{{ (currentYearData.master.grantRate * 100).toFixed(1) }}%</span>，学位授予质量稳步提升。</p>
          </div>
          <div class="analysis-item">
            <p>教学改革方面，省级教学成果奖 <span class="highlight">{{ currentYearData.teaching.teachingAward }}</span> 项，省级教改工程项目 <span class="highlight">{{ currentYearData.teaching.provincialReform }}</span> 项、校级教改工程项目 <span class="highlight">{{ currentYearData.teaching.schoolReform }}</span> 项，国家级课程思政教学团队 <span class="highlight">{{ currentYearData.teaching.nationalTeams }}</span> 个，省级课程思政教学团队 <span class="highlight">{{ currentYearData.teaching.provincialTeams }}</span> 个，教学改革成效显著。</p>
          </div>
          <div class="analysis-item">
            <p>国际化办学方面，留学生 <span class="highlight">{{ currentYearData.international.internationalStudents }}</span> 人、中外合作办学招生 <span class="highlight">{{ currentYearData.international.cooperativePrograms }}</span> 人，国际化办学水平持续提升。</p>
          </div>
        </div>
      </div>
    </template>

    <template #right-top>
      <SectionPanel
        :title="graduateTitle"
        border-type="box-10"
        clickable
        show-mode-indicator
        :is-trend-mode="graduateTrendMode"
        @title-click="toggleGraduateTrend"
      >
        <Transition name="chart-fade" mode="out-in">
          <SunburstChart v-if="!graduateTrendMode" :data="sunburstChartData" height="100%" />
          <TrendChart v-else :x-data="graduateTrendData.years" :series="graduateTrendData.series" height="100%" />
        </Transition>
      </SectionPanel>
    </template>

    <template #right-bottom>
      <SectionPanel
        :title="internationalTitle"
        border-type="box-1"
        clickable
        show-mode-indicator
        :is-trend-mode="internationalTrendMode"
        @title-click="toggleInternationalTrend"
      >
        <Transition name="chart-fade" mode="out-in">
          <PieChart v-if="!internationalTrendMode" :data="internationalChartData" height="100%" />
          <TrendChart v-else :x-data="internationalTrendData.years" :series="internationalTrendData.series" height="100%" />
        </Transition>
      </SectionPanel>
    </template>
  </DashboardLayout>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useTalentStore } from '@/stores/talent'
import DashboardLayout from '@/components/layout/DashboardLayout.vue'
import SectionPanel from '@/components/common/SectionPanel.vue'
import BarChart from '@/components/charts/BarChart.vue'
import DonutChart from '@/components/charts/DonutChart.vue'
import PieChart from '@/components/charts/PieChart.vue'
import SunburstChart from '@/components/charts/SunburstChart.vue'
import TrendChart from '@/components/charts/TrendChart.vue'

const talentStore = useTalentStore()

onMounted(() => {
  talentStore.fetchYearlyData()
})

const selectedYear = computed(() => talentStore.selectedYear)
const years = computed(() => talentStore.years)
const currentYearData = computed(() => talentStore.currentYearData)

const studentScaleChartData = computed(() => talentStore.studentScaleChartData)
const genderChartData = computed(() => talentStore.genderChartData)
const sunburstChartData = computed(() => talentStore.sunburstChartData)
const internationalChartData = computed(() => talentStore.internationalChartData)

const studentScaleTrendData = computed(() => talentStore.studentScaleTrendData)
const genderTrendData = computed(() => talentStore.genderTrendData)
const graduateTrendData = computed(() => talentStore.graduateTrendData)
const internationalTrendData = computed(() => talentStore.internationalTrendData)

const studentScaleTrendMode = ref(false)
const genderTrendMode = ref(false)
const graduateTrendMode = ref(false)
const internationalTrendMode = ref(false)

const studentScaleTitle = computed(() =>
  studentScaleTrendMode.value ? '学生规模趋势' : `${selectedYear.value}年学生规模分布`
)
const genderTitle = computed(() =>
  genderTrendMode.value ? '学生性别趋势' : `${selectedYear.value}年学生性别构成`
)
const graduateTitle = computed(() =>
  graduateTrendMode.value ? '毕业情况趋势' : `${selectedYear.value}年毕业情况`
)
const internationalTitle = computed(() =>
  internationalTrendMode.value ? '国际交流趋势' : `${selectedYear.value}年国际交流`
)

const toggleStudentScaleTrend = () => { studentScaleTrendMode.value = !studentScaleTrendMode.value }
const toggleGenderTrend = () => { genderTrendMode.value = !genderTrendMode.value }
const toggleGraduateTrend = () => { graduateTrendMode.value = !graduateTrendMode.value }
const toggleInternationalTrend = () => { internationalTrendMode.value = !internationalTrendMode.value }

const totalStudents = computed(() => {
  const { undergraduate, master, phd } = currentYearData.value
  return undergraduate.total + master.total + phd.total
})

const totalGraduates = computed(() => {
  const { undergraduate, master } = currentYearData.value
  return undergraduate.graduates + master.graduates
})

const totalEngineeringProjects = computed(() => {
  const teaching = currentYearData.value.teaching
  return (teaching.provincialReform || 0) + (teaching.schoolReform || 0)
})

const totalTeachingTeams = computed(() => {
  const teaching = currentYearData.value.teaching
  return (teaching.nationalTeams || 0) + (teaching.provincialTeams || 0)
})

const handleYearChange = (year: string) => {
  talentStore.setYear(year)
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
