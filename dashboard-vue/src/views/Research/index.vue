<template>
  <DashboardLayout
    title="科研总览"
    :years="years"
    :selectedYear="selectedYear"
    @yearChange="handleYearChange"
  >
    <template #left-top>
      <SectionPanel :title="`${selectedYear}年各级项目数`" border-type="box-10">
        <BarChart :data="projectChartData" height="100%" />
      </SectionPanel>
    </template>

    <template #left-bottom>
      <SectionPanel :title="`${selectedYear}年科研经费构成`" border-type="box-1">
        <DonutChart
          :data="fundingChartData"
          :centerValue="currentYearData.funding.total"
          centerLabel="总经费(万)"
          height="100%"
        />
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
            <p>高水平论文方面，在 Nature/Science/Cell 等顶刊发表论文 <span class="highlight">{{ currentYearData.papers.topTierJournals }}</span> 篇，SCI 一区论文 <span class="highlight">{{ currentYearData.papers.sciQ1 }}</span> 篇、二区 <span class="highlight">{{ currentYearData.papers.sciQ2 }}</span> 篇，EI 论文 <span class="highlight">{{ currentYearData.papers.ei }}</span> 篇，科研影响力显著增强。</p>
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
      <SectionPanel :title="`${selectedYear}年论文分布`" border-type="box-10">
        <HorizontalBarChart :data="paperChartData" height="100%" />
      </SectionPanel>
    </template>

    <template #right-bottom>
      <SectionPanel :title="`${selectedYear}年知识产权构成`" border-type="box-1">
        <PieChart :data="patentChartData" height="100%" />
      </SectionPanel>
    </template>
  </DashboardLayout>
</template>

<script setup lang="ts">
import { computed, watch, onMounted } from 'vue'
import { useResearchStore } from '@/stores/research'
import { formatNumber } from '@/utils/format'
import DashboardLayout from '@/components/layout/DashboardLayout.vue'
import SectionPanel from '@/components/common/SectionPanel.vue'
import BarChart from '@/components/charts/BarChart.vue'
import DonutChart from '@/components/charts/DonutChart.vue'
import HorizontalBarChart from '@/components/charts/HorizontalBarChart.vue'
import PieChart from '@/components/charts/PieChart.vue'

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
