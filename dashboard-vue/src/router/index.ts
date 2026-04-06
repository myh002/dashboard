import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: () => import('@/layouts/MainLayout.vue'),
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('@/views/Home/index.vue')
      },
      {
        path: 'safety',
        name: 'Safety',
        component: () => import('@/views/Safety/index.vue')
      },
      {
        path: 'party',
        name: 'Party',
        component: () => import('@/views/Party/index.vue')
      },
      {
        path: 'research',
        name: 'Research',
        component: () => import('@/views/Research/index.vue')
      },
      {
        path: 'discipline',
        name: 'Discipline',
        component: () => import('@/views/Discipline/index.vue')
      },
      {
        path: 'talent',
        name: 'Talent',
        component: () => import('@/views/Talent/index.vue')
      },
      {
        path: 'service',
        name: 'Service',
        component: () => import('@/views/Service/index.vue')
      },
      {
        path: 'hr',
        name: 'Hr',
        component: () => import('@/views/Hr/index.vue')
      },
      {
        path: 'finance',
        name: 'Finance',
        component: () => import('@/views/Finance/index.vue')
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
