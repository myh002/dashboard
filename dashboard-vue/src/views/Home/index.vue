<template>
  <div class="home-page">
    <!-- 左栏：师资队伍 + 科学研究 -->
    <div class="home-column home-column--left">
      <dv-border-box-1 :color="['#0066ff', '#00d4ff']" class="column-panel">
        <div class="section-content">
          <div class="section-header">
            <div class="section-title section-title--compact-left">师资队伍</div>
            <div class="section-decoration">
              <span class="deco-bar" style="width: 40%"></span>
              <span class="deco-bar" style="width: 25%"></span>
              <span class="deco-bar" style="width: 55%"></span>
            </div>
          </div>
          <div class="card-row">
            <div class="metric-card">
              <div class="metric-label">在职教职工</div>
              <div class="metric-value">{{ formatNumber(faculty.totalStaff) }}<span class="metric-unit">人</span></div>
            </div>
            <div class="metric-card">
              <div class="metric-label">专任教师</div>
              <div class="metric-value">{{ formatNumber(faculty.totalTeachers) }}<span class="metric-unit">人</span></div>
            </div>
            <div class="metric-card">
              <div class="metric-label">研究生导师数</div>
              <div class="metric-value">{{ formatNumber(faculty.graduateSupervisors) }}<span class="metric-unit">人</span></div>
            </div>
          </div>
          <div class="dual-panel">
            <div class="dual-left">
              <div class="chart-title">专任教师职级</div>
              <RoseChart :data="facultyTitleChartData" height="100%" />
            </div>
            <div class="dual-right">
              <div class="chart-title">国家高层次人才</div>
              <div class="talent-list">
                <div v-for="item in faculty.topTalents" :key="item.category" class="talent-item">
                  <div class="talent-icon">
                    <component :is="talentIconMap[item.icon || '']" />
                  </div>
                  <div class="talent-info">
                    <span class="talent-category">{{ item.category }}</span>
                    <div class="talent-data">
                      <span class="talent-count">{{ formatNumber(item.count) }} {{ item.unit }}</span>
                      <span class="talent-increment">
                        +{{ item.newIncrement || 0 }}
                        <span class="arrow-up">↑</span>
                      </span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </dv-border-box-1>

      <dv-border-box-10 :color="['#0066ff', '#00d4ff']" class="column-panel">
        <div class="section-content">
          <div class="section-header">
            <div class="section-title">科学研究</div>
            <div class="section-decoration">
              <span class="deco-bar" style="width: 40%"></span>
              <span class="deco-bar" style="width: 60%"></span>
            </div>
          </div>
          <div class="card-row card-row--4">
            <DataCard mode="dual" title="公开发布论文" :lastYear="research.papers.lastYear" :newThisYear="research.papers.newThisYear" unit="篇" />
            <DataCard mode="dual" title="出版著作" :lastYear="research.publications.lastYear" :newThisYear="research.publications.newThisYear" unit="部" />
            <DataCard mode="dual" title="专利" :lastYear="research.patents.lastYear" :newThisYear="research.patents.newThisYear" unit="项" />
            <DataCard mode="dual" title="科研平台" :lastYear="research.bases.lastYear" :newThisYear="research.bases.newThisYear" unit="个" />
          </div>
          <div class="dual-panel">
            <div class="dual-left">
              <div class="chart-title chart-title--center">科研经费情况</div>
              <LineChart
                :xData="fundingYears"
                :series="fundingSeries"
                height="100%"
              />
            </div>
            <div class="dual-right">
              <div class="chart-title">科研项目情况</div>
              <div class="stats-list">
                <div class="stats-item">
                  <div class="stats-icon">📊</div>
                  <div class="stats-info">
                    <span class="stats-label">科研项目总数</span>
                    <span class="stats-value">{{ formatNumber(research.projectStats.total) }} 个</span>
                  </div>
                </div>
                <div class="stats-item">
                  <div class="stats-icon">📊</div>
                  <div class="stats-info">
                    <span class="stats-label">省级项目</span>
                    <span class="stats-value">{{ formatNumber(research.projectStats.provincial) }} 个</span>
                  </div>
                </div>
                <div class="stats-item">
                  <div class="stats-icon">📊</div>
                  <div class="stats-info">
                    <span class="stats-label">厅局级项目</span>
                    <span class="stats-value">{{ formatNumber(research.projectStats.prefectural) }} 个</span>
                  </div>
                </div>
                <div class="stats-item">
                  <div class="stats-icon">📊</div>
                  <div class="stats-info">
                    <span class="stats-label">校级项目</span>
                    <span class="stats-value">{{ formatNumber(research.projectStats.institutional) }} 个</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </dv-border-box-10>
    </div>

    <!-- 中栏：中央导航 + 学科建设 -->
    <div class="home-column home-column--center">
      <div class="column-panel nav-panel">
        <Navigation />
      </div>

      <dv-border-box-10 :color="['#0066ff', '#00d4ff']" class="column-panel">
        <div class="section-content">
          <div class="section-header section-header--center">
            <div class="section-title">科学建设</div>
          </div>
          <div class="card-row card-row--4">
            <DataCard mode="simple" title="硕博学位点" :value="discipline.degreePoints" />
            <DataCard mode="simple" title="省一流专业" :value="discipline.firstClassMajors" />
            <DataCard mode="simple" title="省级教学成果奖" :value="discipline.esiDisciplines" />
            <DataCard mode="simple" title="教改工程项目" :value="discipline.firstClassDisciplines" />
          </div>
          <div class="chart-title chart-title--center">论文发表分布</div>
          <BarChart :data="evaluationChartData" height="100%" />
        </div>
      </dv-border-box-10>
    </div>

    <!-- 右栏：人才培养 + 办学条件 -->
    <div class="home-column home-column--right">
      <dv-border-box-1 :color="['#0066ff', '#00d4ff']" class="column-panel">
        <div class="section-content">
          <div class="section-header">
            <div class="section-title section-title--compact-right">人才培养</div>
            <div class="section-decoration">
              <span class="deco-bar" style="width: 55%"></span>
              <span class="deco-bar" style="width: 30%"></span>
              <span class="deco-bar" style="width: 45%"></span>
            </div>
          </div>
          <div class="card-row">
            <div class="metric-card metric-card--icon">
              <div class="metric-card-icon">📚</div>
              <div>
                <div class="metric-label">系部数</div>
                <div class="metric-value">{{ talent.departmentCount }}<span class="metric-unit">个</span></div>
              </div>
            </div>
            <div class="metric-card metric-card--icon">
              <div class="metric-card-icon">📖</div>
              <div>
                <div class="metric-label">专业数</div>
                <div class="metric-value">{{ talent.majorCount }}<span class="metric-unit">个</span></div>
              </div>
            </div>
            <div class="metric-card metric-card--icon">
              <div class="metric-card-icon">📝</div>
              <div>
                <div class="metric-label">省级教改工程项目</div>
                <div class="metric-value">{{ talent.courseCount }}<span class="metric-unit">项</span></div>
              </div>
            </div>
          </div>
          <div class="dual-panel">
            <div class="dual-left">
              <div class="chart-title">培养层次学生数量</div>
              <div class="student-list">
                <div class="student-item">
                  <div class="student-icon">🎓</div>
                  <div class="student-info">
                    <span class="student-label">在校生总数</span>
                    <div class="student-data">
                      <span class="student-value">{{ formatNumber(talent.studentStats.total) }} 人</span>
                      <span class="student-increment">{{ formatIncrement(talent.studentStats.totalIncrement) }} <span :class="getArrowClass(talent.studentStats.totalIncrement)">{{ talent.studentStats.totalIncrement >= 0 ? '↑' : '↓' }}</span></span>
                    </div>
                  </div>
                </div>
                <div class="student-item">
                  <div class="student-icon">🎓</div>
                  <div class="student-info">
                    <span class="student-label">本科生数</span>
                    <div class="student-data">
                      <span class="student-value">{{ formatNumber(talent.studentStats.undergraduate) }} 人</span>
                      <span class="student-increment">{{ formatIncrement(talent.studentStats.undergraduateIncrement) }} <span :class="getArrowClass(talent.studentStats.undergraduateIncrement)">{{ talent.studentStats.undergraduateIncrement >= 0 ? '↑' : '↓' }}</span></span>
                    </div>
                  </div>
                </div>
                <div class="student-item">
                  <div class="student-icon">🎓</div>
                  <div class="student-info">
                    <span class="student-label">硕士研究生数</span>
                    <div class="student-data">
                      <span class="student-value">{{ formatNumber(talent.studentStats.master) }} 人</span>
                      <span class="student-increment">{{ formatIncrement(talent.studentStats.masterIncrement) }} <span :class="getArrowClass(talent.studentStats.masterIncrement)">{{ talent.studentStats.masterIncrement >= 0 ? '↑' : '↓' }}</span></span>
                    </div>
                  </div>
                </div>
                <div class="student-item">
                  <div class="student-icon">🎓</div>
                  <div class="student-info">
                    <span class="student-label">博士研究生数</span>
                    <div class="student-data">
                      <span class="student-value">{{ formatNumber(talent.studentStats.phd) }} 人</span>
                      <span class="student-increment">{{ formatIncrement(talent.studentStats.phdIncrement) }} <span :class="getArrowClass(talent.studentStats.phdIncrement)">{{ talent.studentStats.phdIncrement >= 0 ? '↑' : '↓' }}</span></span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="dual-right">
              <div class="chart-title">在校生培养层次</div>
              <DonutChart
                :data="studentLevelChartData"
                :centerValue="talent.studentLevels.undergraduateTotal"
                centerLabel="本专科生"
                height="100%"
              />
            </div>
          </div>
        </div>
      </dv-border-box-1>

      <dv-border-box-10 :color="['#0066ff', '#00d4ff']" class="column-panel">
        <div class="section-content">
          <div class="section-header">
            <div class="section-title">财政资产</div>
            <div class="section-decoration">
              <span class="deco-bar" style="width: 45%"></span>
              <span class="deco-bar" style="width: 55%"></span>
            </div>
          </div>
          <div class="card-row card-row--4">
            <DataCard mode="dual" title="占地面积" :lastYear="condition.landArea.lastYear" :newThisYear="condition.landArea.newThisYear" unit="亩" />
            <DataCard mode="dual" title="实验室面积" :lastYear="condition.labArea.lastYear" :newThisYear="condition.labArea.newThisYear" unit="万m²" />
            <DataCard mode="dual" title="学生宿舍面积" :lastYear="condition.teachingAdminArea.lastYear" :newThisYear="condition.teachingAdminArea.newThisYear" unit="万m²" />
            <DataCard mode="dual" title="固有资产" :lastYear="condition.fixedAssets.lastYear" :newThisYear="condition.fixedAssets.newThisYear" unit="万元" />
          </div>
          <div class="dual-panel">
            <div class="dual-left">
              <div class="chart-title">资产概况</div>
              <div class="stats-list">
                <div class="stats-item">
                  <div class="stats-icon">🏫</div>
                  <div class="stats-info">
                    <span class="stats-label">教学行政用房</span>
                    <span class="stats-value">{{ formatNumber(condition.assetOverview.teachingResearchArea) }} 万m²</span>
                  </div>
                </div>
                <div class="stats-item">
                  <div class="stats-icon">⚙️</div>
                  <div class="stats-info">
                    <span class="stats-label">设备资产总值</span>
                    <span class="stats-value">{{ formatNumber(condition.assetOverview.equipmentValue) }} 万元</span>
                  </div>
                </div>
                <div class="stats-item">
                  <div class="stats-icon">📚</div>
                  <div class="stats-info">
                    <span class="stats-label">图书数量</span>
                    <span class="stats-value">{{ formatNumber(condition.assetOverview.bookTotal) }} 册</span>
                  </div>
                </div>
                <div class="stats-item">
                  <div class="stats-icon">💾</div>
                  <div class="stats-info">
                    <span class="stats-label">电子期刊</span>
                    <span class="stats-value">{{ formatNumber(condition.assetOverview.databaseCount) }} 种</span>
                  </div>
                </div>
              </div>
            </div>
            <div class="dual-right">
              <div class="chart-title">科研设备总值趋势</div>
              <LineChartClean
                :xData="equipmentYears"
                :series="equipmentSeries"
                :hasDualYAxis="true"
                height="100%"
              />
            </div>
          </div>
        </div>
      </dv-border-box-10>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, markRaw, onMounted, type Component } from 'vue'
import { useDataStore } from '@/stores/data'
import { formatNumber } from '@/utils/format'
import Navigation from '@/layouts/Navigation.vue'
import DataCard from '@/components/common/DataCard.vue'
import DonutChart from '@/components/charts/DonutChart.vue'
import RoseChart from '@/components/charts/RoseChart.vue'
import LineChart from '@/components/charts/LineChart.vue'
import LineChartClean from '@/components/charts/LineChartClean.vue'
import BarChart from '@/components/charts/BarChart.vue'
import { UserFilled, User, Trophy, Star } from '@element-plus/icons-vue'

const dataStore = useDataStore()

onMounted(() => {
  dataStore.fetchAllData()
})

const faculty = computed(() => dataStore.faculty)
const research = computed(() => dataStore.research)
const discipline = computed(() => dataStore.discipline)
const talent = computed(() => dataStore.talent)
const condition = computed(() => dataStore.condition)
const facultyTitleChartData = computed(() => dataStore.facultyTitleChartData)
const studentLevelChartData = computed(() => dataStore.studentLevelChartData)

const talentIconMap: Record<string, Component> = {
  UserFilled: markRaw(UserFilled),
  User: markRaw(User),
  Trophy: markRaw(Trophy),
  Star: markRaw(Star)
}

function formatIncrement(value: number | undefined): string {
  if (value == null) return '0'
  return value >= 0 ? `+${value}` : `${value}`
}

function getArrowClass(value: number | undefined): string {
  if (value == null || value >= 0) return 'arrow-up'
  return 'arrow-down'
}

const evaluationChartData = computed(() =>
  discipline.value.evaluationDistribution.map(e => ({ name: e.name, value: e.count }))
)

const fundingYears = computed(() =>
  research.value.fundingTrend.map(f => String(f.year))
)
const fundingSeries = computed(() => [
  { name: '纵向项目经费', data: research.value.fundingTrend.map(f => f.vertical) },
  { name: '横向项目经费', data: research.value.fundingTrend.map(f => f.horizontal) }
])

const equipmentYears = computed(() =>
  condition.value.equipmentTrend.map(f => String(f.year))
)
const equipmentSeries = computed(() => [
  { name: '教学科研设备数', data: condition.value.equipmentTrend.map(f => f.vertical), type: 'bar' as const },
  { name: '设备资产总值', data: condition.value.equipmentTrend.map(f => f.horizontal), type: 'line' as const }
])
</script>

<style scoped lang="scss">
// 优化的样式变量和mixin
$panel-gap: 6px;
$card-gap: 6px;
$border-radius: var(--radius-sm);
$transition-duration: 0.3s;
$hover-scale: 1.02;

// 增强的发光效果mixin
@mixin enhanced-glow($color: var(--text-accent), $intensity: 0.6) {
  text-shadow: 0 0 8px rgba($color, $intensity), 0 0 16px rgba($color, $intensity * 0.5);
}

// 卡片悬停效果mixin
@mixin card-hover-effect {
  transition: all $transition-duration ease;
  transform-origin: center;
  
  &:hover {
    transform: scale($hover-scale);
    box-shadow: 0 4px 12px rgba(0, 102, 255, 0.3);
    border-color: rgba(0, 102, 255, 0.6);
    background: rgba(18, 38, 74, 0.8);
  }
}

// 数字动画mixin
@mixin number-animation {
  animation: countUp 2s ease-out forwards;
}

// 数字增长动画
@keyframes countUp {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

// 装饰条动画
@keyframes pulse {
  0%, 100% {
    opacity: 0.6;
  }
  50% {
    opacity: 1;
  }
}

// 主布局
.home-page {
  width: 100%;
  height: calc(100vh - 60px);
  display: grid;
  grid-template-columns: 31% 1fr 31%;
  gap: 12px;
  padding: 0 6px 4px;
  box-sizing: border-box;
  background: linear-gradient(135deg, rgba(10, 25, 47, 0.95) 0%, rgba(17, 34, 64, 0.95) 100%);
}

.home-column {
  display: flex;
  flex-direction: column;
  gap: $panel-gap;
  min-height: 0;
  overflow: hidden;
}

.column-panel {
  flex: 1;
  min-height: 0;
  overflow: hidden;
  transition: all $transition-duration ease;

  &:first-child,
  &:last-child {
    flex: 1;
  }

  :deep(.dv-border-box-1),
  :deep(.dv-border-box-10),
  :deep(.border-box-content) {
    height: 100% !important;
    overflow: hidden;
  }
  
  // 面板悬停效果
  &:hover {
    :deep(.dv-border-box-1),
    :deep(.dv-border-box-10) {
      box-shadow: 0 4px 20px rgba(0, 102, 255, 0.25);
    }
  }
}

.nav-panel {
  border: none;
  background: transparent;
}

/* ===== Section 通用 ===== */
.section-content {
  padding: 4px 8px;
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 6px;

  &--center {
    justify-content: center;
  }
}

.section-title {
  font-size: 14px;
  font-weight: bold;
  color: var(--text-accent);
  letter-spacing: 2px;
  @include enhanced-glow;
  white-space: nowrap;
  transition: all $transition-duration ease;
}

.section-title--compact-left {
  @include enhanced-glow(var(--primary-color), 0.5);
  font-size: 13px;
  letter-spacing: 1px;
  line-height: 1;
  padding-top: 6px;
  padding-left: 6px;
  transform: translateX(6px);
}

.section-title--compact-right {
  @include enhanced-glow(var(--primary-color), 0.5);
  font-size: 13px;
  letter-spacing: 1px;
  line-height: 1;
  padding-top: 6px;
  padding-right: 6px;
  transform: translateX(-6px);
}

.section-decoration {
  display: flex;
  flex-direction: column;
  gap: 3px;
  flex: 1;
  margin-left: 12px;
  max-width: 120px;
}

.deco-bar {
  height: 2px;
  border-radius: 1px;
  background: linear-gradient(90deg, var(--primary-color), var(--text-accent));
  opacity: 0.6;
  animation: pulse 2s ease-in-out infinite;
  transition: all $transition-duration ease;
  
  &:hover {
    opacity: 1;
    transform: scaleX(1.1);
  }
}

/* 左栏伸缩条：由内向外（从右往左） */
.home-column--left .section-decoration {
  align-items: flex-end;
}
.home-column--left .deco-bar {
  background: linear-gradient(270deg, var(--primary-color), var(--text-accent));
  animation-delay: 0.3s;
}

/* 右栏伸缩条：由内向外（从左往右） */
.home-column--right .section-decoration {
  margin-left: 0;
  margin-right: 12px;
  order: -1;
  align-items: flex-start;
}
.home-column--right .deco-bar {
  background: linear-gradient(90deg, var(--primary-color), var(--text-accent));
  animation-delay: 0.6s;
}

.chart-title {
  font-size: 14px;
  color: var(--text-secondary);
  margin: 4px 0 2px;
  transition: all $transition-duration ease;
  
  &:hover {
    color: var(--text-accent);
    @include enhanced-glow;
  }

  &--center {
    text-align: center;
  }
}

/* ===== 指标卡片 ===== */
.card-row {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: $card-gap;
  margin-bottom: 4px;

  &--4 { grid-template-columns: repeat(4, 1fr); }
}

.metric-card {
  @include card-style;
  @include card-hover-effect;
  padding: 6px 8px;
  text-align: center;
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 2px;
    background: linear-gradient(90deg, transparent, var(--primary-color), transparent);
    transform: translateX(-100%);
    transition: transform $transition-duration ease;
  }
  
  &:hover::before {
    transform: translateX(100%);
  }

  &--icon {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    text-align: center;
  }
}

.metric-card-icon {
  font-size: 20px;
  flex-shrink: 0;
  transition: all $transition-duration ease;
  text-align: center;
  
  .metric-card:hover & {
    transform: scale(1.2);
    color: var(--primary-color);
  }
}

.metric-label {
  font-size: 13px;
  color: var(--text-secondary);
  transition: all $transition-duration ease;
  
  .metric-card:hover & {
    color: var(--text-primary);
  }
}

.metric-value {
  font-size: 22px;
  font-weight: bold;
  color: var(--text-primary);
  @include number-style;
  @include number-animation;
  transition: all $transition-duration ease;
  
  .metric-card:hover & {
    color: var(--text-accent);
    @include enhanced-glow;
  }
}

.metric-unit {
  font-size: 11px;
  color: var(--text-secondary);
  margin-left: 2px;
  font-weight: normal;
}

/* ===== 双栏面板 ===== */
.dual-panel {
  display: flex;
  gap: 6px;
  flex: 1;
  min-height: 0;
  margin-top: 4px;
}

.dual-left,
.dual-right {
  min-height: 0;
  display: flex;
  flex-direction: column;
  transition: all $transition-duration ease;
}

.dual-left {
  flex: 7;
}

.dual-right {
  flex: 3;
  align-items: center;
}

/* ===== 右栏的反向布局 ===== */
.home-column--right .dual-left {
  flex: 3;
  order: 1;
}

.home-column--right .dual-right {
  flex: 7;
  order: 2;
}

.home-column--right .dual-left .student-list,
.home-column--right .dual-left .stats-list {
  align-items: flex-start;
}

.home-column--right .dual-right .chart-title {
  text-align: center;
}

.home-column--right .dual-left .chart-title {
  text-align: center;
}

/* ===== 人才列表 ===== */
.talent-list {
  display: flex;
  flex-direction: column;
  gap: 4px;
  flex: 1;
  justify-content: space-evenly;
}

.talent-item {
  @include card-hover-effect;
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 4px 8px;
  width: 160px;
  background: rgba(18, 38, 74, 0.6);
  border-radius: $border-radius;
  border: 1px solid rgba(45, 74, 124, 0.3);
  margin-left: auto;
}

.talent-icon {
  width: 28px;
  height: 28px;
  color: var(--text-accent);
  @include flex-center;
  flex-shrink: 0;
  transition: all $transition-duration ease;

  :deep(svg) {
    width: 20px;
    height: 20px;
    transition: all $transition-duration ease;
  }
  
  .talent-item:hover :deep(svg) {
    transform: scale(1.2) rotate(5deg);
    color: var(--primary-color);
  }
}

.talent-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
  min-width: 0;
  flex: 1;
  text-align: center;
  align-items: center;
}

.talent-category {
  font-size: 12px;
  color: var(--text-secondary);
  text-align: center;
  transition: all $transition-duration ease;
  
  .talent-item:hover & {
    color: var(--text-primary);
  }
}

.talent-data {
  display: flex;
  align-items: baseline;
  gap: 4px;
  justify-content: center;
}

.talent-count {
  @include number-style;
  @include number-animation;
  font-size: 16px;
  color: var(--text-accent);
  text-align: center;
  transition: all $transition-duration ease;
  
  .talent-item:hover & {
    @include enhanced-glow;
  }
}

.talent-increment {
  font-size: 10px;
  color: #67C23A;
  white-space: nowrap;
  animation: countUp 2s ease-out forwards 0.3s;
  opacity: 0;
}

.arrow-up {
  font-size: 10px;
  transition: all $transition-duration ease;

  .talent-item:hover & {
    transform: translateY(-3px) scale(1.2);
    color: #85CE61;
  }
}

.arrow-down {
  font-size: 10px;
  color: #F56C6C;
  transition: all $transition-duration ease;

  .talent-item:hover & {
    transform: translateY(3px) scale(1.2);
  }
}

/* ===== 学生列表 ===== */
.student-list {
  display: flex;
  flex-direction: column;
  gap: 4px;
  flex: 1;
  justify-content: space-evenly;
}

.student-item {
  @include card-hover-effect;
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 4px 8px;
  width: 160px;
  background: rgba(18, 38, 74, 0.6);
  border-radius: $border-radius;
  border: 1px solid rgba(45, 74, 124, 0.3);
}

.student-icon {
  font-size: 18px;
  flex-shrink: 0;
  transition: all $transition-duration ease;
  
  .student-item:hover & {
    transform: scale(1.2);
    color: var(--primary-color);
  }
}

.student-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
  min-width: 0;
  flex: 1;
  text-align: center;
  align-items: center;
}

.student-label {
  font-size: 12px;
  color: var(--text-secondary);
  text-align: center;
  transition: all $transition-duration ease;
  
  .student-item:hover & {
    color: var(--text-primary);
  }
}

.student-data {
  display: flex;
  align-items: baseline;
  gap: 4px;
  justify-content: center;
}

.student-value {
  @include number-style;
  @include number-animation;
  font-size: 16px;
  color: var(--text-accent);
  text-align: center;
  transition: all $transition-duration ease;
  
  .student-item:hover & {
    @include enhanced-glow;
  }
}

.student-increment {
  font-size: 10px;
  color: #67C23A;
  white-space: nowrap;
  animation: countUp 2s ease-out forwards 0.3s;
  opacity: 0;
}

/* ===== 统计列表 ===== */
.stats-list {
  display: flex;
  flex-direction: column;
  gap: 4px;
  flex: 1;
  justify-content: space-evenly;
}

.stats-item {
  @include card-hover-effect;
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 4px 8px;
  width: 160px;
  background: rgba(18, 38, 74, 0.6);
  border-radius: $border-radius;
  border: 1px solid rgba(45, 74, 124, 0.3);
}

.stats-icon {
  font-size: 18px;
  flex-shrink: 0;
  transition: all $transition-duration ease;
  
  .stats-item:hover & {
    transform: scale(1.2);
    color: var(--primary-color);
  }
}

.stats-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
  min-width: 0;
  flex: 1;
  text-align: center;
  align-items: center;
}

.stats-label {
  font-size: 12px;
  color: var(--text-secondary);
  text-align: center;
  transition: all $transition-duration ease;
  
  .stats-item:hover & {
    color: var(--text-primary);
  }
}

.stats-value {
  @include number-style;
  @include number-animation;
  font-size: 16px;
  color: var(--text-accent);
  text-align: center;
  transition: all $transition-duration ease;
  
  .stats-item:hover & {
    @include enhanced-glow;
  }
}
</style>
