import { defineStore } from 'pinia'
import type { NavigationState } from '@/types'

export const useNavigationStore = defineStore('navigation', {
  state: (): NavigationState => ({
    activeRoute: '/',
    activeButton: '',
    pageTitle: ''
  }),

  actions: {
    setActiveRoute(route: string) {
      this.activeRoute = route
      const navMap: Record<string, string> = {
        '/safety': '校园安全',
        '/research': '科学研究',
        '/discipline': '科学建设',
        '/hr': '人事人才',
        '/party': '党建思政',
        '/talent': '人才培养',
        '/service': '社会服务',
        '/finance': '财政资产'
      }
      const titleMap: Record<string, string> = {
        '/safety': '校园安全分析',
        '/research': '科学研究分析',
        '/discipline': '科学建设分析',
        '/hr': '人事人才分析',
        '/party': '党建思政分析',
        '/talent': '人才培养分析',
        '/service': '社会服务分析',
        '/finance': '财政资产分析'
      }
      this.activeButton = navMap[route] || ''
      this.pageTitle = route === '/' ? '贵州师范大学"一张表"' : (titleMap[route] || navMap[route] || '')
    }
  }
})
