import { useThemeStore } from '@/stores/theme'

export function useChartTheme() {
  const themeStore = useThemeStore()

  const baseOption = {
    backgroundColor: 'transparent',
    textStyle: {
      color: '#ffffff',
      fontFamily: 'Microsoft YaHei'
    },
    tooltip: {
      backgroundColor: 'rgba(18, 38, 74, 0.95)',
      borderColor: '#2d4a7c',
      textStyle: { color: '#ffffff' }
    },
    legend: {
      textStyle: { color: '#ffffff' }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    }
  }

  const createGradient = (color1: string, color2: string) => ({
    type: 'linear' as const,
    x: 0,
    y: 0,
    x2: 0,
    y2: 1,
    colorStops: [
      { offset: 0, color: color1 },
      { offset: 1, color: color2 }
    ]
  })

  return { baseOption, createGradient, chartColors: themeStore.chartColors }
}
