<template>
  <DashboardLayout
    title="社会服务总览"
    :years="years"
    :selectedYear="selectedYear"
    @yearChange="handleYearChange"
  >
    <template #left-top>
      <SectionPanel
        :title="cooperationTitle"
        border-type="box-10"
        clickable
        show-mode-indicator
        :is-trend-mode="cooperationTrendMode"
        @title-click="toggleCooperationTrend"
      >
        <Transition name="chart-fade" mode="out-in">
          <BarChart v-if="!cooperationTrendMode" :data="cooperationChartData" height="100%" />
          <TrendChart v-else :x-data="cooperationTrendData.years" :series="cooperationTrendData.series" height="100%" />
        </Transition>
      </SectionPanel>
    </template>

    <template #left-bottom>
      <SectionPanel
        :title="techOutputTitle"
        border-type="box-1"
        clickable
        show-mode-indicator
        :is-trend-mode="techOutputTrendMode"
        @title-click="toggleTechOutputTrend"
      >
        <Transition name="chart-fade" mode="out-in">
          <DonutChart
            v-if="!techOutputTrendMode"
            :data="techOutputChartData"
            centerLabel="技术服务"
            height="100%"
          />
          <TrendChart v-else :x-data="techOutputTrendData.years" :series="techOutputTrendData.series" height="100%" />
        </Transition>
      </SectionPanel>
    </template>

    <template #center-top>
      <div class="indicator-grid">
        <div class="indicator-card">
          <div class="indicator-icon">💰</div>
          <div class="indicator-value">{{ currentYearData.cooperation.horizontalFunding }}</div>
          <div class="indicator-label">横向经费(万)</div>
        </div>
        <div class="indicator-card">
          <div class="indicator-icon">📈</div>
          <div class="indicator-value">{{ (currentYearData.employment.graduateRate * 100).toFixed(1) }}%</div>
          <div class="indicator-label">硕博就业率</div>
        </div>
        <div class="indicator-card">
          <div class="indicator-icon">🌍</div>
          <div class="indicator-value">{{ currentYearData.international.internationalStudents }}</div>
          <div class="indicator-label">留学生数</div>
        </div>
        <div class="indicator-card">
          <div class="indicator-icon">👨‍🏫</div>
          <div class="indicator-value">{{ currentYearData.experts.externalTeachers }}</div>
          <div class="indicator-label">外聘教师</div>
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
            <p>产学研合作方面，横向项目经费达 <span class="highlight">{{ currentYearData.cooperation.horizontalFunding }}</span> 万元，发明专利 <span class="highlight">{{ currentYearData.cooperation.inventionPatents }}</span> 项、实用新型专利 <span class="highlight">{{ currentYearData.cooperation.utilityPatents }}</span> 项，外观设计专利 <span class="highlight">{{ currentYearData.cooperation.designPatents }}</span> 项、动植物新品种 <span class="highlight">{{ currentYearData.cooperation.varieties }}</span> 项，科技成果转化活跃。</p>
          </div>
          <div class="analysis-item">
            <p>技术服务方面，标准发布 <span class="highlight">{{ currentYearData.cooperation.standards }}</span> 项、软件著作权 <span class="highlight">{{ currentYearData.cooperation.software }}</span> 项，技术成果输出能力持续增强。</p>
          </div>
          <div class="analysis-item">
            <p>人才服务社会方面，本科生毕业 <span class="highlight">{{ currentYearData.employment.undergraduate }}</span> 人、研究生毕业 <span class="highlight">{{ currentYearData.employment.master }}</span> 人，人才培养质量稳步提升。</p>
          </div>
          <div class="analysis-item">
            <p>国际交流服务方面，留学生 <span class="highlight">{{ currentYearData.international.internationalStudents }}</span> 人、中外合作办学招生 <span class="highlight">{{ currentYearData.international.cooperativePrograms }}</span> 人，国际化办学成效显著。</p>
          </div>
        </div>
      </div>
    </template>

    <template #right-top>
      <SectionPanel
        :title="internationalTitle"
        border-type="box-10"
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

    <template #right-bottom>
      <SectionPanel
        :title="employmentTitle"
        border-type="box-1"
        clickable
        show-mode-indicator
        :is-trend-mode="employmentTrendMode"
        @title-click="toggleEmploymentTrend"
      >
        <Transition name="chart-fade" mode="out-in">
          <DonutChart
            v-if="!employmentTrendMode"
            :data="employmentChartData"
            :centerValue="totalEmployment"
            centerLabel="毕业总数"
            height="100%"
          />
          <TrendChart v-else :x-data="employmentTrendData.years" :series="employmentTrendData.series" height="100%" />
        </Transition>
      </SectionPanel>
    </template>
  </DashboardLayout>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useServiceStore } from '@/stores/service'
import DashboardLayout from '@/components/layout/DashboardLayout.vue'
import SectionPanel from '@/components/common/SectionPanel.vue'
import BarChart from '@/components/charts/BarChart.vue'
import DonutChart from '@/components/charts/DonutChart.vue'
import PieChart from '@/components/charts/PieChart.vue'
import TrendChart from '@/components/charts/TrendChart.vue'

const serviceStore = useServiceStore()

onMounted(() => {
  serviceStore.fetchYearlyData()
})

const selectedYear = computed(() => serviceStore.selectedYear)
const years = computed(() => serviceStore.years)
const currentYearData = computed(() => serviceStore.currentYearData)

const techOutputChartData = computed(() => serviceStore.techOutputChartData)
const cooperationChartData = computed(() => serviceStore.cooperationChartData)
const internationalChartData = computed(() => serviceStore.internationalChartData)
const employmentChartData = computed(() => serviceStore.employmentChartData)

const cooperationTrendData = computed(() => serviceStore.cooperationTrendData)
const techOutputTrendData = computed(() => serviceStore.techOutputTrendData)
const internationalTrendData = computed(() => serviceStore.internationalTrendData)
const employmentTrendData = computed(() => serviceStore.employmentTrendData)

const cooperationTrendMode = ref(false)
const techOutputTrendMode = ref(false)
const internationalTrendMode = ref(false)
const employmentTrendMode = ref(false)

const cooperationTitle = computed(() =>
  cooperationTrendMode.value ? '产学研合作趋势' : `${selectedYear.value}年产学研合作`
)
const techOutputTitle = computed(() =>
  techOutputTrendMode.value ? '技术服务产出趋势' : `${selectedYear.value}年技术服务产出`
)
const internationalTitle = computed(() =>
  internationalTrendMode.value ? '国际交流服务趋势' : `${selectedYear.value}年国际交流服务`
)
const employmentTitle = computed(() =>
  employmentTrendMode.value ? '人才服务输出趋势' : `${selectedYear.value}年人才服务输出`
)

const toggleCooperationTrend = () => { cooperationTrendMode.value = !cooperationTrendMode.value }
const toggleTechOutputTrend = () => { techOutputTrendMode.value = !techOutputTrendMode.value }
const toggleInternationalTrend = () => { internationalTrendMode.value = !internationalTrendMode.value }
const toggleEmploymentTrend = () => { employmentTrendMode.value = !employmentTrendMode.value }

const totalEmployment = computed(() => {
  const { employment } = currentYearData.value
  return employment.undergraduate + employment.master
})

const handleYearChange = (year: string) => {
  serviceStore.setYear(year)
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
