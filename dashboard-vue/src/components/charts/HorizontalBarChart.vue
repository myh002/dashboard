<template>
  <BaseChart :option="chartOption" :height="height" />
</template>

<script setup lang="ts">
import { computed } from 'vue'
import BaseChart from '@/components/common/BaseChart.vue'
import { useChartTheme } from '@/composables/useChartTheme'

const props = withDefaults(defineProps<{
  data: Array<{ name: string; value: number }>
  height?: string
}>(), {
  height: '220px'
})

const { baseOption, createGradient, chartColors } = useChartTheme()

const chartOption = computed(() => ({
  ...baseOption,
  xAxis: {
    type: 'value' as const,
    axisLabel: { color: '#a0aec0', fontSize: 11 },
    splitLine: { lineStyle: { color: 'rgba(45,74,124,0.3)' } }
  },
  yAxis: {
    type: 'category' as const,
    data: props.data.map(d => d.name),
    axisLabel: { color: '#ffffff', fontSize: 12 },
    axisLine: { show: false },
    axisTick: { show: false }
  },
  series: [{
    type: 'bar' as const,
    data: props.data.map(d => d.value),
    barWidth: 14,
    itemStyle: {
      color: createGradient(chartColors[0], 'rgba(64,158,255,0.3)'),
      borderRadius: [0, 4, 4, 0]
    },
    label: {
      show: true,
      position: 'right' as const,
      color: '#ffffff',
      fontSize: 11
    }
  }]
}))
</script>
