<template>
  <DashboardLayout
    title="财政资产总览"
    :years="years"
    :selectedYear="selectedYear"
    @yearChange="handleYearChange"
  >
    <template #left-top>
      <SectionPanel
        :title="libraryTitle"
        border-type="box-10"
        clickable
        show-mode-indicator
        :is-trend-mode="libraryTrendMode"
        @title-click="toggleLibraryTrend"
      >
        <Transition name="chart-fade" mode="out-in">
          <DonutChart v-if="!libraryTrendMode" :data="libraryChartData" height="100%" />
          <TrendChart v-else :x-data="libraryTrendData.years" :series="libraryTrendData.series" height="100%" />
        </Transition>
      </SectionPanel>
    </template>

    <template #left-bottom>
      <SectionPanel
        :title="campusTitle"
        border-type="box-1"
        clickable
        show-mode-indicator
        :is-trend-mode="campusTrendMode"
        @title-click="toggleCampusTrend"
      >
        <Transition name="chart-fade" mode="out-in">
          <DonutChart
            v-if="!campusTrendMode"
            :data="campusChartData"
            :centerValue="currentYearData.campus.schoolArea ?? 0"
            centerLabel="学校面积(亩)"
            unit="万平方米"
            height="100%"
          />
          <TrendChart v-else :x-data="campusTrendData.years" :series="campusTrendData.series" height="100%" />
        </Transition>
      </SectionPanel>
    </template>

    <template #center-top>
      <div class="indicator-grid">
        <div class="indicator-card">
          <div class="indicator-icon">🏢</div>
          <div class="indicator-value">{{ formatNumber(currentYearData.assets.fixedAssets ?? 0) }}</div>
          <div class="indicator-label">固定资产(万)</div>
        </div>
        <div class="indicator-card">
          <div class="indicator-icon">🔬</div>
          <div class="indicator-value">{{ formatNumber(currentYearData.assets.equipmentValue ?? 0) }}</div>
          <div class="indicator-label">设备总值(万)</div>
        </div>
        <div class="indicator-card">
          <div class="indicator-icon">💰</div>
          <div class="indicator-value">{{ formatNumber(currentYearData.campus.horizontalFunding ?? 0) }}</div>
          <div class="indicator-label">横向项目经费(万)</div>
        </div>
        <div class="indicator-card">
          <div class="indicator-icon">📈</div>
          <div class="indicator-value">{{ formatNumber(currentYearData.research.verticalFunding ?? 0) }}</div>
          <div class="indicator-label">纵向经费(万)</div>
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
            <p>校园建设方面，学校面积达 <span class="highlight">{{ currentYearData.campus.schoolArea }}</span> 亩，教学行政用房 <span class="highlight">{{ currentYearData.campus.teachingArea }}</span> 万平方米，实验室用房 <span class="highlight">{{ currentYearData.campus.labArea }}</span> 万平方米，学生宿舍 <span class="highlight">{{ currentYearData.campus.dormitoryArea }}</span> 万平方米，办学条件持续改善。</p>
          </div>
          <div class="analysis-item">
            <p>资产管理方面，固定资产达 <span class="highlight">{{ formatNumber(currentYearData.assets.fixedAssets ?? 0) }}</span> 万元，教学科研设备 <span class="highlight">{{ currentYearData.assets.equipmentCount }}</span> 台（套），设备总值 <span class="highlight">{{ formatNumber(currentYearData.assets.equipmentValue ?? 0) }}</span> 万元，50万元以上大型设备 <span class="highlight">{{ currentYearData.assets.largeEquipmentCount }}</span> 台（套），大型设备原值 <span class="highlight">{{ formatNumber(currentYearData.assets.largeEquipmentValue ?? 0) }}</span> 万元，资产管理规范高效。</p>
          </div>
          <div class="analysis-item">
            <p>科研投入方面，纵向项目经费达 <span class="highlight">{{ formatNumber(currentYearData.research.verticalFunding ?? 0) }}</span> 万元，横向项目经费达 <span class="highlight">{{ formatNumber(currentYearData.campus.horizontalFunding ?? 0) }}</span> 万元，科研支撑条件持续增强，服务社会能力不断提升。</p>
          </div>
        </div>
      </div>
    </template>

    <template #right-top>
      <SectionPanel
        :title="fundingTitle"
        border-type="box-10"
        clickable
        show-mode-indicator
        :is-trend-mode="fundingTrendMode"
        @title-click="toggleFundingTrend"
      >
        <Transition name="chart-fade" mode="out-in">
          <PieChart v-if="!fundingTrendMode" :data="fundingChartData" unit="万元" height="100%" />
          <TrendChart v-else :x-data="fundingTrendData.years" :series="fundingTrendData.series" height="100%" />
        </Transition>
      </SectionPanel>
    </template>

    <template #right-bottom>
      <SectionPanel
        :title="equipmentTitle"
        border-type="box-1"
        clickable
        show-mode-indicator
        :is-trend-mode="equipmentTrendMode"
        @title-click="toggleEquipmentTrend"
      >
        <Transition name="chart-fade" mode="out-in">
          <BarChart v-if="!equipmentTrendMode" :data="equipmentDetailChartData" height="100%" />
          <TrendChart v-else :x-data="equipmentTrendData.years" :series="equipmentTrendData.series" height="100%" />
        </Transition>
      </SectionPanel>
    </template>
  </DashboardLayout>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useFinanceStore } from '@/stores/finance'
import { formatNumber } from '@/utils/format'
import DashboardLayout from '@/components/layout/DashboardLayout.vue'
import SectionPanel from '@/components/common/SectionPanel.vue'
import BarChart from '@/components/charts/BarChart.vue'
import DonutChart from '@/components/charts/DonutChart.vue'
import PieChart from '@/components/charts/PieChart.vue'
import TrendChart from '@/components/charts/TrendChart.vue'

const financeStore = useFinanceStore()

onMounted(() => {
  financeStore.fetchYearlyData()
})

const selectedYear = computed(() => financeStore.selectedYear)
const years = computed(() => financeStore.years)
const currentYearData = computed(() => financeStore.currentYearData)

const assetsChartData = computed(() => financeStore.assetsChartData)
const campusChartData = computed(() => financeStore.campusChartData)
const fundingChartData = computed(() => financeStore.fundingChartData)
const equipmentChartData = computed(() => financeStore.equipmentChartData)
const equipmentDetailChartData = computed(() => financeStore.equipmentDetailChartData)
const libraryChartData = computed(() => financeStore.libraryChartData)

const libraryTrendData = computed(() => financeStore.libraryTrendData)
const campusTrendData = computed(() => financeStore.campusTrendData)
const fundingTrendData = computed(() => financeStore.fundingTrendData)
const equipmentTrendData = computed(() => financeStore.equipmentTrendData)

const libraryTrendMode = ref(false)
const campusTrendMode = ref(false)
const fundingTrendMode = ref(false)
const equipmentTrendMode = ref(false)

const libraryTitle = computed(() =>
  libraryTrendMode.value ? '图书馆资源趋势' : `${selectedYear.value}年图书馆资源构成`
)
const campusTitle = computed(() =>
  campusTrendMode.value ? '校园面积趋势' : `${selectedYear.value}年校园面积构成`
)
const fundingTitle = computed(() =>
  fundingTrendMode.value ? '科研经费趋势' : `${selectedYear.value}年科研经费来源`
)
const equipmentTitle = computed(() =>
  equipmentTrendMode.value ? '仪器设备趋势' : `${selectedYear.value}年仪器设备明细`
)

const toggleLibraryTrend = () => {
  libraryTrendMode.value = !libraryTrendMode.value
}
const toggleCampusTrend = () => {
  campusTrendMode.value = !campusTrendMode.value
}
const toggleFundingTrend = () => {
  fundingTrendMode.value = !fundingTrendMode.value
}
const toggleEquipmentTrend = () => {
  equipmentTrendMode.value = !equipmentTrendMode.value
}

const handleYearChange = (year: string) => {
  financeStore.setYear(year)
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
