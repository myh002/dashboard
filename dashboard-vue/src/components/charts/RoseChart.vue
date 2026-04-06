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

const { baseOption, chartColors } = useChartTheme()

const chartOption = computed(() => ({
  ...baseOption,
  tooltip: {
    ...baseOption.tooltip,
    trigger: 'item' as const,
    formatter: '{b}: {c} ({d}%)'
  },
  legend: {
    show: false
  },
  series: [{
    name: 'Nightingale Chart',
    type: 'pie' as const,
    radius: ['20%', '70%'],
    center: ['50%', '50%'],
    roseType: 'area' as const,
    itemStyle: {
      borderRadius: 6,
      shadowBlur: 8,
      shadowColor: 'rgba(0,102,255,0.3)'
    },
    data: props.data.map((d, i) => ({
      ...d,
      itemStyle: {
        color: chartColors[i % chartColors.length],
        borderRadius: 6
      }
    })),
    label: {
      show: true,
      color: '#ffffff',
      fontSize: 11,
      formatter: '{b}\n{d}%'
    },
    labelLine: {
      show: true,
      length: 5,
      length2: 8,
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
