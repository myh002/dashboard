<template>
  <BaseChart :option="chartOption" :height="height" />
</template>

<script setup lang="ts">
import { computed } from 'vue'
import BaseChart from '@/components/common/BaseChart.vue'
import { useChartTheme } from '@/composables/useChartTheme'

const props = withDefaults(defineProps<{
  xData: string[]
  series: Array<{ name: string; data: number[]; type?: 'line' | 'bar' }>
  height?: string
  hasDualYAxis?: boolean
}>(), {
  height: '220px',
  hasDualYAxis: false
})

const { baseOption, chartColors } = useChartTheme()

const chartOption = computed(() => {
  const hasBar = props.series.some(s => s.type === 'bar')
  const hasLine = props.series.some(s => s.type !== 'bar')

  return {
    ...baseOption,
    tooltip: {
      ...baseOption.tooltip,
      trigger: 'axis' as const,
      axisPointer: {
        type: 'cross' as const,
        crossStyle: {
          color: '#999'
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
      data: props.xData,
      axisLabel: { color: '#a0aec0', fontSize: 11 },
      axisLine: { lineStyle: { color: '#2d4a7c' } },
      axisPointer: {
        type: 'shadow' as const
      }
    },
    yAxis: props.hasDualYAxis ? [
      {
        type: 'value' as const,
        name: props.series.find(s => s.type === 'bar')?.name || '',
        axisLabel: { color: '#a0aec0', fontSize: 11 },
        splitLine: { lineStyle: { color: 'rgba(45,74,124,0.3)' } }
      },
      {
        type: 'value' as const,
        name: props.series.find(s => s.type !== 'bar')?.name || '',
        axisLabel: { color: '#a0aec0', fontSize: 11 },
        splitLine: { show: false }
      }
    ] : {
      type: 'value' as const,
      axisLabel: { color: '#a0aec0', fontSize: 11 },
      splitLine: { lineStyle: { color: 'rgba(45,74,124,0.3)' } }
    },
    series: props.series.map((s, i) => {
      const isBar = s.type === 'bar'
      const yAxisIndex = isBar ? 0 : (hasBar ? 1 : 0)

      if (isBar) {
        return {
          name: s.name,
          type: 'bar' as const,
          data: s.data,
          yAxisIndex,
          itemStyle: {
            color: {
              type: 'linear' as const,
              x: 0, y: 0, x2: 0, y2: 1,
              colorStops: [
                { offset: 0, color: chartColors[i % chartColors.length] },
                { offset: 1, color: 'rgba(0,102,255,0.3)' }
              ]
            },
            borderRadius: [4, 4, 0, 0]
          },
          emphasis: {
            itemStyle: {
              color: {
                type: 'linear' as const,
                x: 0, y: 0, x2: 0, y2: 1,
                colorStops: [
                  { offset: 0, color: '#00ff88' },
                  { offset: 1, color: 'rgba(0,255,136,0.3)' }
                ]
              }
            }
          }
        }
      }

      return {
        name: s.name,
        type: 'line' as const,
        data: s.data,
        yAxisIndex,
        smooth: 0.6,
        symbol: 'circle',
        symbolSize: 8,
        lineStyle: {
          color: chartColors[i % chartColors.length],
          width: 3,
          shadowBlur: 10,
          shadowColor: 'rgba(0,102,255,0.3)'
        },
        itemStyle: {
          color: chartColors[i % chartColors.length],
          borderWidth: 2,
          borderColor: '#fff'
        },
        emphasis: {
          scale: true,
          scaleSize: 12
        }
      }
    })
  }
})
</script>
