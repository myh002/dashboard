<template>
  <BaseChart :option="chartOption" :height="height" />
</template>

<script setup lang="ts">
import { computed } from 'vue'
import BaseChart from '@/components/common/BaseChart.vue'
import { useChartTheme } from '@/composables/useChartTheme'
import { formatNumber } from '@/utils/format'

const props = withDefaults(defineProps<{
  data: Array<{ name: string; value: number }>
  centerValue?: number
  centerLabel?: string
  height?: string
}>(), {
  height: '220px'
})

const { baseOption, chartColors } = useChartTheme()

// 自动计算：如果没传 centerValue，取数据中最大项
// 安全处理：确保数组存在且非空
const safeData = computed(() => Array.isArray(props.data) ? props.data : [])

const maxItem = computed(() => {
  if (safeData.value.length === 0) return null
  return safeData.value.reduce((max, d) => d.value > max.value ? d : max, safeData.value[0])
})

const displayValue = computed(() => props.centerValue ?? maxItem.value?.value ?? 0)
const displayLabel = computed(() => props.centerLabel ?? maxItem.value?.name ?? '')

const chartOption = computed(() => ({
  ...baseOption,
  tooltip: {
    ...baseOption.tooltip,
    trigger: 'item' as const,
    formatter: safeData.value.length > 0 ? '{b}: {c} ({d}%)' : '暂无数据'
  },
  legend: {
    show: false
  },
  series: [{
    name: 'Access From',
    type: 'pie' as const,
    radius: ['40%', '70%'],
    center: ['50%', '50%'],
    avoidLabelOverlap: false,
    padAngle: 5,
    itemStyle: {
      borderRadius: 10,
      shadowBlur: 10,
      shadowColor: 'rgba(0,102,255,0.3)'
    },
    // 安全处理：空数据时显示提示
    data: (safeData.value.length > 0
      ? safeData.value.map((d, i) => ({
          ...d,
          itemStyle: {
            color: chartColors[i % chartColors.length],
            borderRadius: 10
          }
        }))
      : [{ name: '暂无数据', value: 1, itemStyle: { color: '#4a5568', borderRadius: 10 } }]
    ),
    label: {
      show: true,
      color: '#ffffff',
      fontSize: 11,
      formatter: '{b}\n{d}%'
    },
    labelLine: {
      show: true,
      length: 8,
      length2: 10,
      lineStyle: {
        color: 'rgba(255,255,255,0.5)'
      }
    },
    emphasis: {
      label: {
        show: true,
        fontSize: 12,
        fontWeight: 'bold' as const
      },
      itemStyle: {
        shadowBlur: 15,
        shadowColor: 'rgba(0,102,255,0.6)'
      }
    }
  }]
}))
</script>
