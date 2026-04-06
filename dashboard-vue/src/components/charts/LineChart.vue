<template>
  <BaseChart :option="chartOption" :height="height" />
</template>

<script setup lang="ts">
import { computed } from 'vue'
import BaseChart from '@/components/common/BaseChart.vue'
import { useChartTheme } from '@/composables/useChartTheme'

const props = withDefaults(defineProps<{
  xData: string[]
  series: Array<{ name: string; data: number[] }>
  height?: string
}>(), {
  height: '220px'
})

const { baseOption, chartColors } = useChartTheme()

const seriesColors = [
  ['rgba(0,212,255,0.8)', 'rgba(0,212,255,0.05)'],
  ['rgba(0,102,255,0.8)', 'rgba(0,102,255,0.05)']
]

const chartOption = computed(() => ({
  ...baseOption,
  tooltip: {
    ...baseOption.tooltip,
    trigger: 'axis' as const
  },
  legend: {
    ...baseOption.legend,
    top: 0,
    data: props.series.map(s => s.name)
  },
  xAxis: {
    type: 'category' as const,
    data: props.xData,
    axisLabel: { color: '#a0aec0', fontSize: 11 },
    axisLine: { lineStyle: { color: '#2d4a7c' } }
  },
  yAxis: {
    type: 'value' as const,
    axisLabel: { color: '#a0aec0', fontSize: 11 },
    splitLine: { lineStyle: { color: 'rgba(45,74,124,0.3)' } }
  },
  series: props.series.map((s, i) => ({
    name: s.name,
    type: 'line' as const,
    data: s.data,
    smooth: true,
    symbol: 'circle',
    symbolSize: 6,
    lineStyle: { color: chartColors[i % chartColors.length], width: 2 },
    itemStyle: { color: chartColors[i % chartColors.length] },
    areaStyle: {
      color: {
        type: 'linear' as const,
        x: 0, y: 0, x2: 0, y2: 1,
        colorStops: [
          { offset: 0, color: seriesColors[i]?.[0] || 'rgba(64,158,255,0.8)' },
          { offset: 1, color: seriesColors[i]?.[1] || 'rgba(64,158,255,0.05)' }
        ]
      }
    }
  }))
}))
</script>
