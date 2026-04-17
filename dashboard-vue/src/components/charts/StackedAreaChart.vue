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

const { baseOption } = useChartTheme()

const areaColors = [
  ['rgba(0,212,255,0.9)', 'rgba(0,212,255,0.1)'],
  ['rgba(0,102,255,0.9)', 'rgba(0,102,255,0.1)'],
  ['rgba(0,255,136,0.9)', 'rgba(0,255,136,0.1)'],
  ['rgba(255,165,0,0.9)', 'rgba(255,165,0,0.1)'],
  ['rgba(138,43,226,0.9)', 'rgba(138,43,226,0.1)']
]

const chartOption = computed(() => {
  if (!props.xData || props.xData.length === 0 || !props.series || props.series.length === 0) {
    return { series: [] }
  }
  
  return {
    ...baseOption,
    tooltip: {
      ...baseOption.tooltip,
      trigger: 'axis' as const,
      axisPointer: {
        type: 'cross' as const,
        label: {
          backgroundColor: '#6a7985'
        }
      }
    },
    legend: {
      ...baseOption.legend,
      top: 0,
      data: props.series.map(s => s.name)
    },
    xAxis: {
      type: 'category' as const,
      boundaryGap: false,
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
      stack: '总量',
      data: s.data,
      smooth: true,
      symbol: 'circle',
      symbolSize: 6,
      lineStyle: { width: 0 },
      itemStyle: { color: areaColors[i]?.[0] || 'rgba(64,158,255,0.8)' },
      areaStyle: {
        color: {
          type: 'linear' as const,
          x: 0, y: 0, x2: 0, y2: 1,
          colorStops: [
            { offset: 0, color: areaColors[i]?.[0] || 'rgba(64,158,255,0.9)' },
            { offset: 1, color: areaColors[i]?.[1] || 'rgba(64,158,255,0.1)' }
          ]
        }
      },
      emphasis: {
        focus: 'series' as const
      }
    }))
  }
})
</script>
