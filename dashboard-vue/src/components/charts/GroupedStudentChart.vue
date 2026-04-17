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

const { baseOption, createGradient } = useChartTheme()

const groupedData = computed(() => {
  const groups = [
    { name: '本科生', male: 0, female: 0 },
    { name: '硕士生', male: 0, female: 0 },
    { name: '博士生', male: 0, female: 0 }
  ]

  props.data.forEach(item => {
    if (item.name.includes('本科')) {
      if (item.name.includes('男')) {
        groups[0].male = item.value
      } else {
        groups[0].female = item.value
      }
    } else if (item.name.includes('硕士')) {
      if (item.name.includes('男')) {
        groups[1].male = item.value
      } else {
        groups[1].female = item.value
      }
    } else if (item.name.includes('博士')) {
      if (item.name.includes('男')) {
        groups[2].male = item.value
      } else {
        groups[2].female = item.value
      }
    }
  })

  return groups
})

const chartOption = computed(() => {
  const xData = groupedData.value.map(g => g.name)

  return {
    ...baseOption,
    tooltip: {
      ...baseOption.tooltip,
      trigger: 'axis' as const,
      axisPointer: {
        type: 'shadow' as const
      }
    },
    legend: {
      ...baseOption.legend,
      top: 0,
      data: ['男生', '女生'],
      textStyle: {
        color: '#a0aec0'
      }
    },
    xAxis: {
      type: 'category' as const,
      data: xData,
      axisLabel: {
        color: '#a0aec0',
        fontSize: 12
      },
      axisTick: { show: false },
      axisLine: {
        lineStyle: { color: '#2d4a7c' }
      },
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
    series: [
      {
        name: '男生',
        type: 'bar' as const,
        data: groupedData.value.map(g => g.male),
        barWidth: '35%',
        barGap: '10%',
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
      },
      {
        name: '女生',
        type: 'bar' as const,
        data: groupedData.value.map(g => g.female),
        barWidth: '35%',
        barGap: '10%',
        itemStyle: {
          color: createGradient('#ff6b9d', '#c44569'),
          borderRadius: [4, 4, 0, 0]
        },
        emphasis: {
          itemStyle: {
            color: createGradient('#ff8fab', '#ff6b9d'),
            shadowBlur: 20,
            shadowColor: 'rgba(255,107,157,0.5)'
          }
        },
        animationDuration: 1500,
        animationDelay: 100,
        animationEasing: 'elasticOut' as const
      }
    ]
  }
})
</script>
