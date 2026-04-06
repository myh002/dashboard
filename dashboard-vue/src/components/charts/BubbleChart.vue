<template>
  <BaseChart :option="chartOption" :height="height" />
</template>

<script setup lang="ts">
import { computed } from 'vue'
import BaseChart from '@/components/common/BaseChart.vue'

const props = withDefaults(defineProps<{
  data: Array<{ name: string; value: number }>
  height?: string
}>(), {
  height: '220px'
})

const chartOption = computed(() => {
  const maxValue = Math.max(...props.data.map(d => d.value))
  const minValue = Math.min(...props.data.map(d => d.value))

  const bubbleData = props.data.map((d, index) => {
    const normalizedValue = (d.value - minValue) / (maxValue - minValue || 1)
    const size = 20 + normalizedValue * 60
    return {
      name: d.name,
      value: [index, d.value / 100, size]
    }
  })

  const colorStart = '#00d4ff'
  const colorEnd = '#a855f7'

  return {
    grid: {
      left: '3%',
      right: '8%',
      bottom: '15%',
      top: '10%',
      containLabel: true
    },
    xAxis: {
      type: 'category' as const,
      data: props.data.map(d => d.name),
      axisLabel: {
        color: '#a0aec0',
        fontSize: 11,
        rotate: 30,
        interval: 0
      },
      axisLine: { show: false },
      axisTick: { show: false }
    },
    yAxis: {
      type: 'value' as const,
      axisLabel: { show: false },
      axisLine: { show: false },
      axisTick: { show: false },
      splitLine: { show: false }
    },
    series: [{
      type: 'scatter' as const,
      data: bubbleData,
      symbolSize: (data: number[]) => data[2],
      itemStyle: {
        color: (params: { dataIndex: number }) => {
          const ratio = params.dataIndex / (props.data.length - 1 || 1)
          const r = Math.round(0 + ratio * (168 - 0))
          const g = Math.round(212 + ratio * (85 - 212))
          const b = Math.round(255 + ratio * (247 - 255))
          return `rgba(${r}, ${g}, ${b}, 0.8)`
        },
        shadowBlur: 10,
        shadowColor: 'rgba(0, 212, 255, 0.5)'
      },
      label: {
        show: true,
        position: 'inside' as const,
        formatter: (params: any) => {
          return `{name|${params.data.name}}\n{value|${params.data.value[1]}}`
        },
        rich: {
          name: {
            color: '#ffffff',
            fontSize: 11,
            fontWeight: 'bold' as const
          },
          value: {
            color: '#ffd700',
            fontSize: 10
          }
        }
      },
      emphasis: {
        itemStyle: {
          shadowBlur: 20,
          shadowColor: 'rgba(0, 212, 255, 0.8)'
        }
      }
    }],
    tooltip: {
      trigger: 'item' as const,
      backgroundColor: 'rgba(12, 30, 60, 0.9)',
      borderColor: '#00d4ff',
      textStyle: { color: '#ffffff' },
      formatter: (params: any) => {
        return `${params.data.name}<br/>数量: ${params.data.value[1]}`
      }
    }
  }
})
</script>
