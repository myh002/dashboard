import { defineStore } from 'pinia'
import type { ThemeState } from '@/types'

export const useThemeStore = defineStore('theme', {
  state: (): ThemeState => ({
    primaryColor: '#0066ff',
    backgroundColor: '#0a1628',
    textColor: '#ffffff',
    chartColors: ['#00d4ff', '#0066ff', '#00ff88', '#ff6b6b', '#ffd93d']
  })
})
