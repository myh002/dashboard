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
  unit?: string
}>(), {
  height: '220px',
  unit: '篇'
})

const { baseOption, createGradient, chartColors } = useChartTheme()

const safeData = computed(() => Array.isArray(props.data) ? props.data : [])

const chartOption = computed(() => ({
  ...baseOption,
  tooltip: {
    trigger: 'axis' as const,
    axisPointer: { type: 'shadow' as const },
    backgroundColor: 'rgba(12, 30, 60, 0.9)',
    borderColor: '#00d4ff',
    textStyle: { color: '#ffffff' },
    formatter: safeData.value.length > 0 
      ? (params: any) => `${params[0].name}: ${params[0].value}${props.unit}`
      : '暂无数据'
  },
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
