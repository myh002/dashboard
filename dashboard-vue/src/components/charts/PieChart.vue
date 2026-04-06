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
  const colors = [
    '#00d4ff',
    '#0066ff',
    '#a855f7',
    '#22c55e',
    '#f59e0b',
    '#ef4444'
  ]

  // 安全处理：空数据时显示提示
  const safeData = Array.isArray(props.data) ? props.data : []
  const chartData = safeData.length > 0
    ? safeData.map((item, index) => ({
        name: item.name,
        value: item.value,
        itemStyle: {
          color: colors[index % colors.length]
        }
      }))
    : [{ name: '暂无数据', value: 1, itemStyle: { color: '#4a5568' } }]

  return {
    tooltip: {
      trigger: 'item' as const,
      backgroundColor: 'rgba(12, 30, 60, 0.9)',
      borderColor: '#00d4ff',
      textStyle: { color: '#ffffff' },
      formatter: safeData.length > 0 ? '{b}: {c} ({d}%)' : '暂无数据'
    },
    legend: {
      orient: 'vertical' as const,
      right: '5%',
      top: 'center',
      textStyle: {
        color: '#a0aec0',
        fontSize: 11
      },
      itemWidth: 12,
      itemHeight: 12,
      itemGap: 8
    },
    series: [{
      type: 'pie' as const,
      radius: ['40%', '65%'],
      center: ['35%', '50%'],
      avoidLabelOverlap: true,
      itemStyle: {
        borderRadius: 6,
        borderColor: 'rgba(10, 25, 47, 0.8)',
        borderWidth: 2
      },
      label: {
        show: true,
        position: 'outside' as const,
        formatter: safeData.length > 0 ? '{b}\n{d}%' : '',
        color: '#e2e8f0',
        fontSize: 11,
        lineHeight: 16
      },
      labelLine: {
        show: true,
        lineStyle: {
          color: 'rgba(0, 212, 255, 0.5)'
        }
      },
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowColor: 'rgba(0, 212, 255, 0.5)'
        },
        label: {
          show: true,
          fontSize: 13,
          fontWeight: 'bold' as const
        }
      },
      data: chartData
    }]
  }
})
</script>
