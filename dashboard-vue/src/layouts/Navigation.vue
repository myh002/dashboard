<template>
  <div class="center-navigation">
    <div class="nav-ring">
      <div
        v-for="(item, index) in allItems"
        :key="item.path"
        class="nav-button active"
        :class="[
          { 'nav-button--right': index >= 4 },
          `nav-pos-${index}`
        ]"
        @click="handleNavigate(item.path)"
      >
        <div class="nav-icon-circle">
          <component :is="iconMap[item.icon]" />
        </div>
        <div class="nav-label">{{ item.label }}</div>
      </div>
    </div>

    <!-- 中心时钟仪表盘 -->
    <div class="clock-wrapper">
      <div class="clock-ring">
        <div class="clock-tick" v-for="n in 60" :key="n" :style="{ transform: `rotate(${n * 6}deg)` }">
          <span :class="['tick-mark', n % 5 === 0 ? 'tick-major' : 'tick-minor']"></span>
        </div>
        <div class="glow-dot" v-for="n in 10" :key="'g'+n" :style="{ transform: `rotate(${n * 36}deg) translateY(-80px)` }"></div>
      </div>
      <div class="clock-center">
        <div class="clock-time">{{ currentTime }}</div>
        <div class="clock-date">{{ currentDate }}</div>
      </div>
      <div class="clock-label-bottom">综合校情</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, markRaw, type Component } from 'vue'
import { useRouter } from 'vue-router'
import { useNavigationStore } from '@/stores/navigation'
import { formatTime, formatDate } from '@/utils/format'
import type { NavItem } from '@/types'
import {
  Lock,
  Flag,
  DataAnalysis,
  Reading,
  UserFilled,
  Service,
  User,
  Money
} from '@element-plus/icons-vue'

const router = useRouter()
const navigationStore = useNavigationStore()

const iconMap: Record<string, Component> = {
  Shield: markRaw(Lock),
  Flag: markRaw(Flag),
  DataAnalysis: markRaw(DataAnalysis),
  Reading: markRaw(Reading),
  UserFilled: markRaw(UserFilled),
  Service: markRaw(Service),
  User: markRaw(User),
  Money: markRaw(Money)
}

const leftItems: NavItem[] = [
  { label: '财政资产', path: '/finance', icon: 'Money' },
  { label: '科学研究', path: '/research', icon: 'DataAnalysis' },
  { label: '科学建设', path: '/discipline', icon: 'Reading' },
  { label: '人事人才', path: '/hr', icon: 'User' }
]

const rightItems: NavItem[] = [
  { label: '党建思政', path: '/party', icon: 'Flag' },
  { label: '人才培养', path: '/talent', icon: 'UserFilled' },
  { label: '社会服务', path: '/service', icon: 'Service' },
  { label: '校园安全', path: '/safety', icon: 'Shield' }
]

// 8个导航项围成一圈：左侧4个(0-3) + 右侧4个(4-7)
const allItems: NavItem[] = [
  ...leftItems,
  ...rightItems
]

const currentTime = ref(formatTime(new Date()))
const currentDate = ref(formatDate(new Date()))
let timer: ReturnType<typeof setInterval> | null = null

function handleNavigate(path: string) {
  router.push(path)
  navigationStore.setActiveRoute(path)
}

onMounted(() => {
  timer = setInterval(() => {
    const now = new Date()
    currentTime.value = formatTime(now)
    currentDate.value = formatDate(now)
  }, 1000)
})

onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
    timer = null
  }
})
</script>

<style scoped lang="scss">
.center-navigation {
  width: 100%;
  height: 100%;
  position: relative;
  @include flex-center;
}

/* 导航环 - 10个项目围绕中心 */
.nav-ring {
  position: absolute;
  width: 100%;
  height: 100%;
}

.nav-button {
  position: absolute;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  white-space: nowrap;
  width: 150px;
  height: 50px;
  background: rgba(12, 30, 60, 0.6);
  border: 1px solid var(--border-color);
  border-radius: 25px;
  gap: 10px;
  padding: 0 12px;
  box-sizing: border-box;

  &:hover {
    background: rgba(0, 102, 255, 0.2);
    border-color: var(--primary-color);
    box-shadow: 0 0 16px rgba(0, 102, 255, 0.4);
    transform: scale(1.05);
    .nav-icon-circle {
      background: rgba(0, 102, 255, 0.4);
      border-color: var(--primary-color);
    }
    .nav-label { color: var(--text-primary); }
  }

  &.active {
    background: rgba(0, 102, 255, 0.25);
    border-color: var(--text-accent);
    box-shadow: 0 0 12px rgba(0, 212, 255, 0.4);
    .nav-icon-circle {
      background: rgba(0, 212, 255, 0.3);
      border-color: var(--text-accent);
      box-shadow: 0 0 8px rgba(0, 212, 255, 0.5);
    }
    .nav-label { color: var(--text-accent); }
  }
}

.nav-icon-circle {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: rgba(12, 30, 60, 0.8);
  border: 1px solid var(--border-color);
  @include flex-center;
  flex-shrink: 0;
  transition: all 0.3s ease;

  :deep(svg) {
    width: 18px;
    height: 18px;
    color: var(--text-secondary);
    transition: color 0.3s ease;
  }
}

.nav-label {
  font-size: 14px;
  color: var(--text-secondary);
  white-space: nowrap;
  transition: color 0.3s ease;
  letter-spacing: 1px;
  text-align: center;
  flex: 1;
}

/* 右侧按钮：图标在前 */
.nav-button--right {
  flex-direction: row;
}

/* 左侧按钮：标签在前（靠近时钟） */
.nav-button:not(.nav-button--right) {
  flex-direction: row-reverse;
}

/* 左侧4个: 水平排列，分散布局 */
.nav-pos-0 { top: 18%; left: 12%; }
.nav-pos-1 { top: 36%; left: 8%; }
.nav-pos-2 { top: 54%; left: 8%; }
.nav-pos-3 { top: 72%; left: 12%; }

/* 右侧4个: 水平排列，分散布局 */
.nav-pos-4 { top: 18%; right: 12%; }
.nav-pos-5 { top: 36%; right: 8%; }
.nav-pos-6 { top: 54%; right: 8%; }
.nav-pos-7 { top: 72%; right: 12%; }

/* Clock */
.clock-wrapper {
  position: relative;
  width: 200px;
  height: 200px;
  flex-shrink: 0;
  @include flex-center;
  flex-direction: column;
  z-index: 2;
}

.clock-ring {
  position: absolute;
  width: 190px;
  height: 190px;
  border-radius: 50%;
  border: 2px solid rgba(0, 102, 255, 0.4);
  box-shadow: 0 0 25px rgba(0, 102, 255, 0.3), inset 0 0 25px rgba(0, 102, 255, 0.08);
}

.clock-tick {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
}

.tick-mark {
  position: absolute;
  top: 3px;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(0, 102, 255, 0.5);
}

.tick-major {
  width: 3px;
  height: 14px;
  background: var(--text-accent);
  box-shadow: 0 0 4px var(--text-accent);
}

.tick-minor {
  width: 2px;
  height: 8px;
}

.glow-dot {
  position: absolute;
  width: 5px;
  height: 5px;
  border-radius: 50%;
  background: var(--text-accent);
  box-shadow: 0 0 8px var(--text-accent);
  top: 50%;
  left: 50%;
  margin: -2.5px 0 0 -2.5px;
}

.clock-center {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  background: radial-gradient(circle, #0d2347, #0a1628);
  border: 2px solid var(--border-color);
  box-shadow: 0 0 20px rgba(0, 102, 255, 0.4), inset 0 0 15px rgba(0, 102, 255, 0.15);
  @include flex-center;
  flex-direction: column;
  z-index: 2;
}

.clock-time {
  font-size: 22px;
  font-weight: bold;
  color: var(--text-accent);
  @include number-style;
  letter-spacing: 1px;
}

.clock-date {
  font-size: 11px;
  color: var(--text-secondary);
  margin-top: 2px;
}

.clock-label-bottom {
  position: absolute;
  bottom: 5px;
  font-size: 12px;
  color: var(--text-accent);
  letter-spacing: 3px;
  @include text-glow(var(--text-accent));
}
</style>
