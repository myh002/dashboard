<template>
  <component :is="borderComponent" :color="['#0066ff', '#00d4ff']" class="section-panel">
    <div class="section-content">
      <div class="section-header section-header--center">
        <div
          class="section-title"
          :class="{ 'section-title--clickable': clickable }"
          @click="handleClick"
        >
          {{ title }}
          <span v-if="showModeIndicator && isTrendMode" class="mode-indicator">趋势</span>
        </div>
      </div>
      <div class="chart-wrapper">
        <slot />
      </div>
    </div>
  </component>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = withDefaults(defineProps<{
  title: string
  borderType?: 'box-1' | 'box-10'
  clickable?: boolean
  showModeIndicator?: boolean
  isTrendMode?: boolean
}>(), {
  borderType: 'box-10',
  clickable: false,
  showModeIndicator: false,
  isTrendMode: false
})

const emit = defineEmits<{
  (e: 'titleClick'): void
}>()

const borderComponent = computed(() => {
  return props.borderType === 'box-1' ? 'dv-border-box-1' : 'dv-border-box-10'
})

const handleClick = () => {
  if (props.clickable) {
    emit('titleClick')
  }
}
</script>

<style scoped lang="scss">
.section-panel {
  width: 100%;
  height: 100%;

  :deep(.dv-border-box-1),
  :deep(.dv-border-box-10) {
    height: 100% !important;
  }

  :deep(.border-box-content) {
    height: 100% !important;
    padding: 8px;
  }
}

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
  transition: all 0.3s ease;

  &--clickable {
    cursor: pointer;
    padding: 4px 8px;
    border-radius: 4px;
    display: inline-flex;
    align-items: center;
    gap: 8px;

    &:hover {
      background: rgba(0, 102, 255, 0.2);
      transform: scale(1.02);
      box-shadow: 0 0 12px rgba(0, 212, 255, 0.4);
    }

    &:active {
      transform: scale(0.98);
    }
  }
}

.mode-indicator {
  font-size: 12px;
  padding: 2px 8px;
  background: linear-gradient(135deg, rgba(0, 212, 255, 0.3), rgba(0, 102, 255, 0.3));
  border: 1px solid var(--text-accent);
  border-radius: 10px;
  letter-spacing: 1px;
  color: #ffd700;
  text-shadow: 0 0 6px rgba(255, 215, 0, 0.5);
  animation: pulse 1.5s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% {
    opacity: 0.8;
  }
  50% {
    opacity: 1;
  }
}

.chart-wrapper {
  flex: 1;
  min-height: 0;
  overflow: hidden;
}
</style>
