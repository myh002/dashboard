<template>
  <div class="dashboard-layout">
    <div class="panel panel--left-top">
      <slot name="left-top" />
    </div>

    <div class="panel panel--left-bottom">
      <slot name="left-bottom" />
    </div>

    <div class="panel panel--center-top">
      <div class="panel--center-top-header">
        <div class="section-header">
          <div class="section-title" style="padding-left: 100px;">
          <span class="year-badge">{{ selectedYear }}年</span>
          {{ title }}
        </div>
        </div>
        <div class="year-selector">
          <button class="year-change-btn" @click="handleNextYear">
            更换年份 ▼
          </button>
          <div class="year-dropdown" v-if="showYearDropdown">
            <div
              v-for="year in years"
              :key="year"
              class="year-option"
              :class="{ active: year === selectedYear }"
              @click="selectYear(year)"
            >
              {{ year }}年
            </div>
          </div>
        </div>
      </div>
      <slot name="center-top" />
    </div>

    <div class="panel panel--center-bottom">
      <slot name="center-bottom" />
    </div>

    <div class="panel panel--right-top">
      <slot name="right-top" />
    </div>

    <div class="panel panel--right-bottom">
      <slot name="right-bottom" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'

const props = defineProps<{
  title: string
  years: string[]
  selectedYear: string
}>()

const emit = defineEmits<{
  (e: 'yearChange', year: string): void
}>()

const showYearDropdown = ref(false)

let autoPlayInterval: ReturnType<typeof setInterval> | null = null
let pauseTimeout: ReturnType<typeof setTimeout> | null = null

const startAutoPlay = () => {
  if (autoPlayInterval) {
    clearInterval(autoPlayInterval)
  }
  autoPlayInterval = setInterval(() => {
    const currentIndex = props.years.indexOf(props.selectedYear)
    const nextIndex = (currentIndex + 1) % props.years.length
    emit('yearChange', props.years[nextIndex])
  }, 5000)
}

const stopAutoPlay = () => {
  if (autoPlayInterval) {
    clearInterval(autoPlayInterval)
    autoPlayInterval = null
  }
}

const selectYear = (year: string) => {
  emit('yearChange', year)
  showYearDropdown.value = false
  stopAutoPlay()
  if (pauseTimeout) {
    clearTimeout(pauseTimeout)
  }
  pauseTimeout = setTimeout(() => {
    startAutoPlay()
  }, 5 * 60 * 1000)
}

const handleNextYear = () => {
  showYearDropdown.value = !showYearDropdown.value
}

onMounted(() => {
  startAutoPlay()
})

onUnmounted(() => {
  stopAutoPlay()
  if (pauseTimeout) {
    clearTimeout(pauseTimeout)
  }
})
</script>

<style scoped lang="scss">
.dashboard-layout {
  width: 100%;
  height: calc(100vh - 60px);
  display: grid;
  grid-template-columns: 30% 1fr 30%;
  grid-template-rows: 1fr 1fr;
  gap: 40px;
  padding: 6px;
  box-sizing: border-box;
  background: url('/bg/1.jpeg') center center / cover no-repeat;
  background-color: rgba(10, 25, 47, 0.85);
}

.panel {
  min-height: 0;
  overflow: hidden;

  &--left-top {
    grid-column: 1;
    grid-row: 1;
  }

  &--left-bottom {
    grid-column: 1;
    grid-row: 2;
  }

  &--center-top {
    grid-column: 2;
    grid-row: 1;
    display: flex;
    flex-direction: column;
  }

  &--center-bottom {
    grid-column: 2;
    grid-row: 2;
    display: flex;
    flex-direction: column;
  }

  &--right-top {
    grid-column: 3;
    grid-row: 1;
  }

  &--right-bottom {
    grid-column: 3;
    grid-row: 2;
  }

  &--center-top-header {
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 8px;
    padding-right: 120px;
    flex-shrink: 0;
  }
}

.section-header {
  display: flex;
  align-items: center;
  flex-shrink: 0;
}

.section-title {
  font-size: 18px;
  font-weight: bold;
  color: var(--text-accent);
  letter-spacing: 2px;
  text-shadow: 0 0 8px rgba(0, 212, 255, 0.6);
  white-space: nowrap;
  display: flex;
  align-items: center;
}

.year-badge {
  font-size: 18px;
  font-weight: bold;
  color: var(--text-accent);
  letter-spacing: 2px;
  text-shadow: 0 0 8px rgba(0, 212, 255, 0.6);
  margin-right: 16px;
}

.year-change-btn {
  background: rgba(12, 30, 60, 0.8);
  border: 1px solid var(--text-accent);
  border-radius: var(--radius-sm);
  color: var(--text-accent);
  padding: 6px 12px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 0 10px rgba(0, 102, 255, 0.3);

  &:hover {
    background: rgba(0, 102, 255, 0.3);
    box-shadow: 0 0 16px rgba(0, 212, 255, 0.6);
    transform: scale(1.02);
  }
}

.year-selector {
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
}

.year-dropdown {
  position: absolute;
  top: 100%;
  right: 0;
  margin-top: 4px;
  background: rgba(12, 30, 60, 0.95);
  border: 1px solid var(--text-accent);
  border-radius: var(--radius-sm);
  box-shadow: 0 0 16px rgba(0, 212, 255, 0.6);
  z-index: 100;
  min-width: 100px;
}

.year-option {
  padding: 8px 16px;
  color: var(--text-accent);
  font-size: 12px;
  cursor: pointer;
  transition: all 0.2s ease;
  text-align: center;

  &:hover {
    background: rgba(0, 102, 255, 0.3);
  }

  &.active {
    background: rgba(0, 102, 255, 0.5);
    color: #ffd700;
  }

  &:first-child {
    border-radius: var(--radius-sm) var(--radius-sm) 0 0;
  }

  &:last-child {
    border-radius: 0 0 var(--radius-sm) var(--radius-sm);
  }
}
</style>
