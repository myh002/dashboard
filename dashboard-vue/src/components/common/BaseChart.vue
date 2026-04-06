<template>
  <div
    ref="chartRef"
    class="base-chart"
    :style="{ width, height }"
  />
</template>

<script setup lang="ts">
import { ref, watch, onMounted, onUnmounted, shallowRef } from 'vue'
import * as echarts from 'echarts'
import type { EChartsOption } from 'echarts'

const props = withDefaults(defineProps<{
  option: EChartsOption
  width?: string
  height?: string
  autoResize?: boolean
}>(), {
  width: '100%',
  height: '300px',
  autoResize: true
})

const emit = defineEmits<{
  (e: 'chart-ready', instance: echarts.ECharts): void
}>()

const chartRef = ref<HTMLDivElement>()
const chartInstance = shallowRef<echarts.ECharts>()
let resizeObserver: ResizeObserver | null = null

function initChart() {
  if (!chartRef.value) return
  try {
    chartInstance.value = echarts.init(chartRef.value)
    chartInstance.value.setOption(props.option)
    emit('chart-ready', chartInstance.value)
  } catch {
    // Fail silently — render empty area instead of crashing
  }
}

watch(
  () => props.option,
  (newOption) => {
    if (chartInstance.value && newOption) {
      chartInstance.value.setOption(newOption, true)
    }
  },
  { deep: true }
)

onMounted(() => {
  initChart()

  if (props.autoResize && chartRef.value) {
    resizeObserver = new ResizeObserver(() => {
      chartInstance.value?.resize()
    })
    resizeObserver.observe(chartRef.value)
  }
})

onUnmounted(() => {
  resizeObserver?.disconnect()
  resizeObserver = null
  chartInstance.value?.dispose()
  chartInstance.value = undefined
})
</script>

<style scoped>
.base-chart {
  min-height: 0;
}
</style>
