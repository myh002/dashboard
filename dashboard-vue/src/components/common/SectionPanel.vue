<template>
  <component :is="borderComponent" :color="['#0066ff', '#00d4ff']" class="section-panel">
    <div class="section-content">
      <div class="section-header section-header--center">
        <div class="section-title">{{ title }}</div>
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
}>(), {
  borderType: 'box-10'
})

const borderComponent = computed(() => {
  return props.borderType === 'box-1' ? 'dv-border-box-1' : 'dv-border-box-10'
})
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
}

.chart-wrapper {
  flex: 1;
  min-height: 0;
  overflow: hidden;
}
</style>
