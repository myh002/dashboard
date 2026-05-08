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

const chartOption = computed(() => {
  const data = props.data.map(d => d.value)
  
  return {
    ...baseOption,
    tooltip: {
      ...baseOption.tooltip,
      trigger: 'axis' as const,
      axisPointer: {
        type: 'shadow' as const
      }
    },
    xAxis: {
      type: 'category' as const,
      data: props.data.map(d => d.name),
      axisLabel: {
        interval: 0,
        rotate: 30,
        color: '#a0aec0',
        fontSize: 11
      },
      axisTick: { show: false },
      axisLine: { show: false },
      z: 10
    },
    yAxis: {
      type: 'value' as const,
      axisLine: { show: false },
      axisTick: { show: false },
      axisLabel: { color: '#a0aec0' },
      splitLine: {
        lineStyle: { color: 'rgba(45,74,124,0.3)' }
      }
    },
    dataZoom: [
      {
        type: 'inside' as const,
        start: 0,
        end: 100
      }
    ],
    series: [
      {
        type: 'bar' as const,
        data: data,
        barWidth: '50%',
        barMaxWidth: 30,
        itemStyle: {
          color: createGradient('#00d4ff', '#0066ff'),
          borderRadius: [4, 4, 0, 0]
        },
        emphasis: {
          itemStyle: {
            color: createGradient('#00ff88', '#00d4ff'),
            shadowBlur: 20,
            shadowColor: 'rgba(0,212,255,0.5)'
          }
        },
        animationDuration: 1500,
        animationEasing: 'elasticOut' as const
      }
    ]
  }
})
</script>
