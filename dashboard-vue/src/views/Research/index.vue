<template>
  <DashboardLayout
    title="科研总览"
    :years="years"
    :selectedYear="selectedYear"
    @yearChange="handleYearChange"
  >
    <template #left-top>
      <SectionPanel
        :title="projectTitle"
        border-type="box-10"
        clickable
        show-mode-indicator
        :is-trend-mode="projectTrendMode"
        @title-click="toggleProjectTrend"
      >
        <Transition name="chart-fade" mode="out-in">
          <BarChart v-if="!projectTrendMode" :data="projectChartData" height="100%" />
          <TrendChart v-else :x-data="projectTrendData.years" :series="projectTrendData.series" height="100%" />
        </Transition>
      </SectionPanel>
    </template>

    <template #left-bottom>
      <SectionPanel
        :title="fundingTitle"
        border-type="box-1"
        clickable
        show-mode-indicator
        :is-trend-mode="fundingTrendMode"
        @title-click="toggleFundingTrend"
      >
        <Transition name="chart-fade" mode="out-in">
          <DonutChart
            v-if="!fundingTrendMode"
            :data="fundingChartData"
            :centerValue="currentYearData.funding.total"
            centerLabel="总经费(万)"
            unit="万元"
            height="100%"
          />
          <TrendChart v-else :x-data="fundingTrendData.years" :series="fundingTrendData.series" height="100%" />
        </Transition>
      </SectionPanel>
    </template>

    <template #center-top>
      <div class="indicator-grid">
        <div class="indicator-card">
          <div class="indicator-icon">📊</div>
          <div class="indicator-value">{{ currentYearData.projects.total }}</div>
          <div class="indicator-label">项目总数</div>
        </div>
        <div class="indicator-card">
          <div class="indicator-icon">💰</div>
          <div class="indicator-value">{{ formatNumber(currentYearData.funding.total) }}</div>
          <div class="indicator-label">科研经费(万)</div>
        </div>
        <div class="indicator-card">
          <div class="indicator-icon">📄</div>
          <div class="indicator-value">{{ formatNumber(currentYearData.papers.total) }}</div>
          <div class="indicator-label">论文总数</div>
        </div>
        <div class="indicator-card">
          <div class="indicator-icon">🔐</div>
          <div class="indicator-value">{{ currentYearData.publications.total }}</div>
          <div class="indicator-label">知识产权</div>
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
            <p>在科研经费方面，纵向经费占比达 <span class="highlight">{{ fundingRatio }}%</span>，体现学校对基础研究的重视。国家级和部级高层次项目共 <span class="highlight">{{ highLevelProjects }}</span> 项，厅局级项目 <span class="highlight">{{ currentYearData.projects.prefectural }}</span> 项，校级项目 <span class="highlight">{{ currentYearData.projects.school }}</span> 项，科研竞争力持续提升。</p>
          </div>
          <div class="analysis-item">
            <p>高水平论文方面，SCI 一区论文 <span class="highlight">{{ currentYearData.papers.sciQ1 }}</span> 篇、二区 <span class="highlight">{{ currentYearData.papers.sciQ2 }}</span> 篇，EI 论文 <span class="highlight">{{ currentYearData.papers.ei }}</span> 篇，科研影响力显著增强。</p>
          </div>
          <div class="analysis-item">
            <p>知识产权转化活跃，共获批知识产权 <span class="highlight">{{ currentYearData.publications.total }}</span> 项，其中发明专利 <span class="highlight">{{ currentYearData.publications.patents.invention }}</span> 项、实用新型 <span class="highlight">{{ currentYearData.publications.patents.utility }}</span> 项，软件著作权 <span class="highlight">{{ currentYearData.publications.software }}</span> 项，产学研合作成效显著。</p>
          </div>
          <div class="analysis-item">
            <p>平台建设方面，拥有国家级平台 <span class="highlight">{{ currentYearData.platforms.national }}</span> 个、省级平台 <span class="highlight">{{ currentYearData.platforms.provincial }}</span> 个，省部级奖项累计 <span class="highlight">{{ awardsTotal }}</span> 项，其中一等奖 <span class="highlight">{{ currentYearData.awards.first }}</span> 项，科研实力稳步增强。</p>
          </div>
        </div>
      </div>
    </template>

    <template #right-top>
      <SectionPanel
        :title="paperTitle"
        border-type="box-10"
        clickable
        show-mode-indicator
        :is-trend-mode="paperTrendMode"
        @title-click="togglePaperTrend"
      >
        <Transition name="chart-fade" mode="out-in">
          <HorizontalBarChart v-if="!paperTrendMode" :data="paperChartData" height="100%" />
          <TrendChart v-else :x-data="paperTrendData.years" :series="paperTrendData.series" height="100%" />
        </Transition>
      </SectionPanel>
    </template>

    <template #right-bottom>
      <SectionPanel
        :title="patentTitle"
        border-type="box-1"
        clickable
        show-mode-indicator
        :is-trend-mode="patentTrendMode"
        @title-click="togglePatentTrend"
      >
        <Transition name="chart-fade" mode="out-in">
          <PieChart v-if="!patentTrendMode" :data="patentChartData" unit="项" height="100%" />
          <TrendChart v-else :x-data="patentTrendData.years" :series="patentTrendData.series" height="100%" />
        </Transition>
      </SectionPanel>
    </template>
  </DashboardLayout>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useResearchStore } from '@/stores/research'
import { formatNumber } from '@/utils/format'
import DashboardLayout from '@/components/layout/DashboardLayout.vue'
import SectionPanel from '@/components/common/SectionPanel.vue'
import BarChart from '@/components/charts/BarChart.vue'
import DonutChart from '@/components/charts/DonutChart.vue'
import HorizontalBarChart from '@/components/charts/HorizontalBarChart.vue'
import PieChart from '@/components/charts/PieChart.vue'
import TrendChart from '@/components/charts/TrendChart.vue'

const researchStore = useResearchStore()

onMounted(() => {
  researchStore.fetchYearlyData()
})

const selectedYear = computed(() => researchStore.selectedYear)
const years = computed(() => researchStore.years)
const currentYearData = computed(() => researchStore.currentYearData)

const projectChartData = computed(() => researchStore.projectChartData)
const fundingChartData = computed(() => researchStore.fundingChartData)
const paperChartData = computed(() => researchStore.paperChartData)
const patentChartData = computed(() => researchStore.patentChartData)

const projectTrendData = computed(() => researchStore.projectTrendData)
const fundingTrendData = computed(() => researchStore.fundingTrendData)
const paperTrendData = computed(() => researchStore.paperTrendData)
const patentTrendData = computed(() => researchStore.patentTrendData)

const projectTrendMode = ref(false)
const fundingTrendMode = ref(false)
const paperTrendMode = ref(false)
const patentTrendMode = ref(false)

const projectTitle = computed(() =>
  projectTrendMode.value ? '各级项目趋势' : `${selectedYear.value}年各级项目数`
)
const fundingTitle = computed(() =>
  fundingTrendMode.value ? '科研经费趋势' : `${selectedYear.value}年科研经费构成`
)
const paperTitle = computed(() =>
  paperTrendMode.value ? '论文分布趋势' : `${selectedYear.value}年论文分布`
)
const patentTitle = computed(() =>
  patentTrendMode.value ? '知识产权趋势' : `${selectedYear.value}年知识产权构成`
)

const toggleProjectTrend = () => {
  projectTrendMode.value = !projectTrendMode.value
}
const toggleFundingTrend = () => {
  fundingTrendMode.value = !fundingTrendMode.value
}
const togglePaperTrend = () => {
  paperTrendMode.value = !paperTrendMode.value
}
const togglePatentTrend = () => {
  patentTrendMode.value = !patentTrendMode.value
}

const fundingRatio = computed(() => {
  const { funding } = currentYearData.value
  return (funding.vertical / funding.total * 100).toFixed(1)
})

const highLevelProjects = computed(() => {
  const { projects } = currentYearData.value
  return projects.national + projects.ministerial
})

const awardsTotal = computed(() => {
  const { awards } = currentYearData.value
  return awards.first + awards.second + awards.third
})

const handleYearChange = (year: string) => {
  researchStore.setYear(year)
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
