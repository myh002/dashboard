<template>
  <DashboardLayout
    title="人才培养总览"
    :years="years"
    :selectedYear="selectedYear"
    @yearChange="handleYearChange"
  >
    <template #left-top>
      <SectionPanel :title="`${selectedYear}年学生规模分布`" border-type="box-10">
        <BarChart :data="studentScaleChartData" height="100%" />
      </SectionPanel>
    </template>

    <template #left-bottom>
      <SectionPanel :title="`${selectedYear}年学生性别构成`" border-type="box-1">
        <DonutChart
          :data="genderChartData"
          :centerValue="totalStudents"
          centerLabel="在校生总数"
          height="100%"
        />
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
          <div class="indicator-icon">📈</div>
          <div class="indicator-value">{{ averageEmploymentRate }}%</div>
          <div class="indicator-label">平均就业率</div>
        </div>
        <div class="indicator-card">
          <div class="indicator-icon">📚</div>
          <div class="indicator-value">{{ currentYearData.teaching.courses }}</div>
          <div class="indicator-label">开设课程数</div>
        </div>
        <div class="indicator-card">
          <div class="indicator-icon">🏕️</div>
          <div class="indicator-value">{{ currentYearData.teaching.practiceBases }}</div>
          <div class="indicator-label">校外实践基地</div>
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
            <p>就业情况方面，本科就业率 <span class="highlight">{{ currentYearData.undergraduate.employmentRate }}%</span>、硕士就业率 <span class="highlight">{{ currentYearData.master.employmentRate }}%</span>、博士就业率 <span class="highlight">{{ currentYearData.phd.employmentRate }}%</span>，毕业生就业质量稳步提升。</p>
          </div>
          <div class="analysis-item">
            <p>教学改革方面，开设课程 <span class="highlight">{{ currentYearData.teaching.courses }}</span> 门，教授讲授本科课程 <span class="highlight">{{ currentYearData.teaching.professorCourses }}</span> 门次，国家级教改项目 <span class="highlight">{{ currentYearData.teaching.nationalReform }}</span> 项、省级 <span class="highlight">{{ currentYearData.teaching.provincialReform }}</span> 项，教学改革成效显著。</p>
          </div>
          <div class="analysis-item">
            <p>国际化办学方面，留学生 <span class="highlight">{{ currentYearData.international.internationalStudents }}</span> 人、中外合作办学招生 <span class="highlight">{{ currentYearData.international.cooperativePrograms }}</span> 人，校外实践基地 <span class="highlight">{{ currentYearData.teaching.practiceBases }}</span> 个，国际化办学水平持续提升。</p>
          </div>
        </div>
      </div>
    </template>

    <template #right-top>
      <SectionPanel :title="`${selectedYear}年毕业就业情况`" border-type="box-10">
        <BarChart :data="graduateEmploymentChartData" height="100%" />
      </SectionPanel>
    </template>

    <template #right-bottom>
      <SectionPanel :title="`${selectedYear}年国际交流`" border-type="box-1">
        <PieChart :data="internationalChartData" height="100%" />
      </SectionPanel>
    </template>
  </DashboardLayout>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useTalentStore } from '@/stores/talent'
import DashboardLayout from '@/components/layout/DashboardLayout.vue'
import SectionPanel from '@/components/common/SectionPanel.vue'
import BarChart from '@/components/charts/BarChart.vue'
import DonutChart from '@/components/charts/DonutChart.vue'
import PieChart from '@/components/charts/PieChart.vue'

const talentStore = useTalentStore()

onMounted(() => {
  talentStore.fetchYearlyData()
})

const selectedYear = computed(() => talentStore.selectedYear)
const years = computed(() => talentStore.years)
const currentYearData = computed(() => talentStore.currentYearData)

const studentScaleChartData = computed(() => talentStore.studentScaleChartData)
const genderChartData = computed(() => talentStore.genderChartData)
const graduateEmploymentChartData = computed(() => talentStore.graduateEmploymentChartData)
const internationalChartData = computed(() => talentStore.internationalChartData)

const totalStudents = computed(() => {
  const { undergraduate, master, phd } = currentYearData.value
  return undergraduate.total + master.total + phd.total
})

const averageEmploymentRate = computed(() => {
  const { undergraduate, master, phd } = currentYearData.value
  return ((undergraduate.employmentRate + master.employmentRate + phd.employmentRate) / 3).toFixed(1)
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
