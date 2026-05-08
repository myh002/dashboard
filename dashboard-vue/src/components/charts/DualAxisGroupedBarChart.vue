<template>
  <BaseChart :option="chartOption" :height="height" />
</template>

<script setup lang="ts">
import { computed } from 'vue'
import BaseChart from '@/components/common/BaseChart.vue'
import { useChartTheme } from '@/composables/useChartTheme'

const props = withDefaults(defineProps<{
  years: string[]
  undergraduate: number[]
  master: number[]
  phd: number[]
  height?: string
}>(), {
  height: '100%'
})

const { baseOption, chartColors } = useChartTheme()

const chartOption = computed(() => {
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
      data: ['本科生', '硕士生', '博士生'],
      textStyle: {
        color: '#a0aec0',
        fontSize: 11
      }
    },
    xAxis: {
      type: 'category' as const,
      data: props.years,
      axisLabel: {
        color: '#a0aec0',
        fontSize: 11
      },
      axisLine: {
        lineStyle: { color: '#2d4a7c' }
      },
      axisPointer: {
        type: 'shadow' as const
      }
    },
    yAxis: [
      {
        type: 'value' as const,
        name: '本科生',
        position: 'left' as const,
        axisLabel: {
          color: '#a0aec0',
          fontSize: 11,
          formatter: (value: number) => {
            return value >= 10000 ? (value / 10000).toFixed(0) + 'w' : value.toString()
          }
        },
        splitLine: {
          lineStyle: { color: 'rgba(45,74,124,0.3)' }
        },
        axisLine: {
          show: true,
          lineStyle: { color: chartColors[0] }
        }
      },
      {
        type: 'value' as const,
        name: '硕博',
        position: 'right' as const,
        axisLabel: {
          color: '#a0aec0',
          fontSize: 11,
          formatter: (value: number) => {
            return value >= 10000 ? (value / 10000).toFixed(0) + 'w' : value.toString()
          }
        },
        splitLine: {
          show: false
        },
        axisLine: {
          show: true,
          lineStyle: { color: chartColors[1] }
        }
      }
    ],
    series: [
      {
        name: '本科生',
        type: 'bar' as const,
        data: props.undergraduate,
        yAxisIndex: 0,
        barWidth: '50%',
        itemStyle: {
          color: {
            type: 'linear' as const,
            x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [
              { offset: 0, color: chartColors[0] },
              { offset: 1, color: 'rgba(0,212,255,0.3)' }
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
        },
        animationDuration: 1500,
        animationEasing: 'elasticOut' as const
      },
      {
        name: '硕士生',
        type: 'line' as const,
        data: props.master,
        yAxisIndex: 1,
        smooth: 0.6,
        symbol: 'circle',
        symbolSize: 8,
        lineStyle: {
          color: chartColors[1],
          width: 3,
          shadowBlur: 10,
          shadowColor: 'rgba(0,102,255,0.3)'
        },
        itemStyle: {
          color: chartColors[1],
          borderWidth: 2,
          borderColor: '#fff'
        },
        emphasis: {
          scale: true,
          scaleSize: 12
        },
        animationDuration: 1500,
        animationDelay: 100,
        animationEasing: 'elasticOut' as const
      },
      {
        name: '博士生',
        type: 'line' as const,
        data: props.phd,
        yAxisIndex: 1,
        smooth: 0.6,
        symbol: 'circle',
        symbolSize: 8,
        lineStyle: {
          color: chartColors[2],
          width: 3,
          shadowBlur: 10,
          shadowColor: 'rgba(0,102,255,0.3)'
        },
        itemStyle: {
          color: chartColors[2],
          borderWidth: 2,
          borderColor: '#fff'
        },
        emphasis: {
          scale: true,
          scaleSize: 12
        },
        animationDuration: 1500,
        animationDelay: 200,
        animationEasing: 'elasticOut' as const
      }
    ]
  }
})
</script>
