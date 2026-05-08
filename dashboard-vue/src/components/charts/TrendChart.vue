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

const seriesGradients = [
  ['rgba(0,212,255,0.8)', 'rgba(0,212,255,0.05)'],
  ['rgba(0,102,255,0.8)', 'rgba(0,102,255,0.05)'],
  ['rgba(168,85,247,0.8)', 'rgba(168,85,247,0.05)'],
  ['rgba(34,197,94,0.8)', 'rgba(34,197,94,0.05)']
]

const chartOption = computed(() => ({
  ...baseOption,
  tooltip: {
    ...baseOption.tooltip,
    trigger: 'axis' as const,
    backgroundColor: 'rgba(12, 30, 60, 0.9)',
    borderColor: '#00d4ff',
    textStyle: { color: '#ffffff' }
  },
  legend: {
    show: true,
    bottom: 0,
    textStyle: { color: '#a0aec0', fontSize: 11 },
    icon: 'roundRect',
    itemWidth: 14,
    itemHeight: 8
  },
  grid: {
    top: 10,
    left: 50,
    right: 20,
    bottom: 50
  },
  xAxis: {
    type: 'category' as const,
    data: props.xData,
    axisLabel: {
      color: '#a0aec0',
      fontSize: 11,
      formatter: (value: string) => value.replace('20', '')
    },
    axisLine: {
      lineStyle: { color: '#2d4a7c' }
    },
    axisTick: { show: false }
  },
  yAxis: {
    type: 'value' as const,
    axisLabel: {
      color: '#a0aec0',
      fontSize: 10,
      formatter: (value: number) => {
        if (value >= 10000) return (value / 10000).toFixed(1) + 'w'
        if (value >= 1000) return (value / 1000).toFixed(1) + 'k'
        return value.toString()
      }
    },
    splitLine: {
      lineStyle: { color: 'rgba(45,74,124,0.3)' }
    }
  },
  series: props.series.map((s, i) => ({
    name: s.name,
    type: 'line' as const,
    data: s.data,
    smooth: 0.4,
    symbol: 'circle',
    symbolSize: 6,
    lineStyle: {
      color: chartColors[i % chartColors.length],
      width: 2
    },
    itemStyle: {
      color: chartColors[i % chartColors.length],
      borderWidth: 2,
      borderColor: '#fff'
    },
    areaStyle: {
      color: {
        type: 'linear' as const,
        x: 0, y: 0, x2: 0, y2: 1,
        colorStops: [
          { offset: 0, color: seriesGradients[i % seriesGradients.length][0] },
          { offset: 1, color: seriesGradients[i % seriesGradients.length][1] }
        ]
      }
    },
    emphasis: {
      scale: true,
      scaleSize: 10
    }
  }))
}))
</script>
