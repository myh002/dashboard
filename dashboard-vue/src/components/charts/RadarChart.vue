<template>
  <BaseChart :option="chartOption" :height="height" />
</template>

<script setup lang="ts">
import { computed } from 'vue'
import BaseChart from '@/components/common/BaseChart.vue'
import { useChartTheme } from '@/composables/useChartTheme'

interface Indicator {
  name: string
  max: number
}

const props = withDefaults(defineProps<{
  indicators: Indicator[]
  values: number[]
  height?: string
}>(), {
  height: '220px'
})

const { baseOption, createGradient, chartColors } = useChartTheme()

const safeIndicators = computed(() => {
  if (!props.indicators || !Array.isArray(props.indicators)) {
    return []
  }
  return props.indicators
})

const safeValues = computed(() => {
  if (!props.values || !Array.isArray(props.values)) {
    return []
  }
  return props.values.map(v => (v === null || v === undefined || isNaN(v)) ? 0 : v)
})

const chartOption = computed(() => ({
  ...baseOption,
  tooltip: {
    ...baseOption.tooltip,
    trigger: 'item' as const
  },
  legend: {
    show: true,
    orient: 'vertical' as const,
    right: 10,
    top: 'middle',
    textStyle: {
      color: '#a0aec0'
    },
    data: ['学位点结构']
  },
  radar: {
    indicator: safeIndicators.value.map(ind => ({
      ...ind,
      axisLabel: {
        show: false
      }
    })),
    shape: 'polygon' as const,
    splitNumber: 5,
    axisName: {
      color: '#00d4ff',
      fontSize: 12,
      padding: [3, 8]
    },
    splitLine: {
      lineStyle: {
        color: 'rgba(0, 102, 255, 0.2)'
      }
    },
    splitArea: {
      show: true,
      areaStyle: {
        color: ['rgba(0, 40, 100, 0.1)', 'rgba(0, 60, 140, 0.2)', 'rgba(0, 80, 180, 0.3)', 'rgba(0, 102, 255, 0.4)']
      }
    },
    axisLine: {
      lineStyle: {
        color: 'rgba(0, 102, 255, 0.3)'
      }
    }
  },
  series: [{
    type: 'radar' as const,
    data: safeIndicators.value.length > 0 && safeValues.value.length > 0 ? [{
      value: safeValues.value,
      name: '学位点结构',
      symbol: 'circle',
      symbolSize: 6,
      lineStyle: {
        width: 2,
        color: '#00d4ff'
      },
      areaStyle: {
        color: {
          type: 'linear' as const,
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [
            { offset: 0, color: 'rgba(0, 102, 255, 0.4)' },
            { offset: 1, color: 'rgba(0, 212, 255, 0.4)' }
          ]
        }
      },
      itemStyle: {
        color: '#00d4ff',
        borderColor: '#fff',
        borderWidth: 2
      },
      emphasis: {
        lineStyle: {
          width: 3,
          color: '#00ff88'
        },
        areaStyle: {
          color: {
            type: 'linear' as const,
            x: 0,
            y: 0,
            x2: 0,
            y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(0, 255, 136, 0.5)' },
              { offset: 1, color: 'rgba(0, 212, 255, 0.5)' }
            ]
          }
        },
        itemStyle: {
          color: '#00ff88',
          borderColor: '#fff',
          borderWidth: 2
        }
      }
    }] : [],
    animationDuration: 1500,
    animationEasing: 'elasticOut' as const
  }]
}))
</script>
