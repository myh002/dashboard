<template>
  <BaseChart :option="chartOption" :height="height" />
</template>

<script setup lang="ts">
import { computed } from 'vue'
import BaseChart from '@/components/common/BaseChart.vue'

const props = withDefaults(defineProps<{
  data: {
    undergraduates: { total: number; rate: number; degreeGranted: number }
    postgraduates: { total: number; rate: number; degreeGranted: number }
  }
  height?: string
}>(), {
  height: '320px'
})

const chartOption = computed(() => {
  const { undergraduates, postgraduates } = props.data

  const underGradRate = undergraduates.total > 0 ? (undergraduates.rate * 100).toFixed(1) : '0.0'
  const postGradRate = postgraduates.total > 0 ? (postgraduates.rate * 100).toFixed(1) : '0.0'

  const underDegreeCount = undergraduates.degreeGranted
  const postDegreeCount = postgraduates.degreeGranted
  const totalGraduates = undergraduates.total + postgraduates.total
  const totalDegrees = underDegreeCount + postDegreeCount
  const totalDegreeRate = totalGraduates > 0 ? ((totalDegrees / totalGraduates) * 100).toFixed(1) : '0.0'

  return {
    tooltip: {
      trigger: 'item' as const,
      backgroundColor: 'rgba(12, 30, 60, 0.95)',
      borderColor: '#00d4ff',
      borderWidth: 1,
      textStyle: { color: '#ffffff', fontSize: 13 },
      formatter: (params: any) => {
        if (params.seriesIndex === 0) {
          // Inner ring: graduation count
          const rate = params.name === '本科生' ? underGradRate : postGradRate
          return `<b>${params.name}</b><br/>毕业人数: <b>${params.value}</b> 人<br/>毕业率: <b>${rate}%</b>`
        } else {
          // Outer ring: degree granted
          return `<b>${params.name}</b><br/>已授学位: <b>${params.value}</b> 人`
        }
      }
    },
    legend: {
      data: ['本科生', '研究生'],
      orient: 'vertical' as const,
      right: 10,
      top: 'center',
      textStyle: { color: '#a0aec0', fontSize: 12 },
      itemWidth: 12,
      itemHeight: 12,
      icon: 'circle'
    },
    series: [
      {
        type: 'pie' as const,
        radius: ['40%', '60%'],
        center: ['50%', '50%'],
        data: [
          { name: '本科生', value: undergraduates.total, itemStyle: { color: '#00d4ff' } },
          { name: '研究生', value: postgraduates.total, itemStyle: { color: '#a855f7' } }
        ],
        label: {
          show: true,
          position: 'inside' as const,
          formatter: '{b}\n{c}人',
          fontSize: 12,
          fontWeight: 'bold' as const,
          color: '#ffffff',
          lineHeight: 18
        },
        emphasis: {
          scaleSize: 8,
          itemStyle: {
            shadowBlur: 15,
            shadowColor: 'rgba(0, 212, 255, 0.5)'
          }
        }
      },
      {
        type: 'pie' as const,
        radius: ['65%', '85%'],
        center: ['50%', '50%'],
        data: [
          { name: '本科生已授学位', value: underDegreeCount, itemStyle: { color: '#00a8cc', opacity: 0.8 } },
          { name: '研究生已授学位', value: postDegreeCount, itemStyle: { color: '#8b3fcf', opacity: 0.8 } }
        ],
        label: {
          show: true,
          position: 'outside' as const,
          formatter: '{b}\n{c}人',
          fontSize: 10,
          color: '#c0c8d8',
          lineHeight: 14
        },
        labelLine: {
          length: 10,
          length2: 15,
          lineStyle: { color: 'rgba(0, 212, 255, 0.4)' }
        },
        emphasis: {
          scaleSize: 5,
          itemStyle: {
            shadowBlur: 10,
            shadowColor: 'rgba(0, 212, 255, 0.4)'
          }
        }
      }
    ]
  }
})
</script>
