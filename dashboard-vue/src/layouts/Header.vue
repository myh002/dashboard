<template>
  <div class="header">
    <div class="header-content">
      <div class="decoration-left">
        <dv-decoration-1 :color="['#0066ff', '#00d4ff']" style="width: 200px; height: 50px" />
      </div>
      <div class="title">{{ navigationStore.pageTitle || '贵州师范大学"一张表"' }}</div>
      <div class="decoration-right">
        <dv-decoration-1 :color="['#0066ff', '#00d4ff']" style="width: 200px; height: 50px" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useNavigationStore } from '@/stores/navigation'

const navigationStore = useNavigationStore()
</script>

<style scoped lang="scss">
.header {
  width: 100%;
  height: 60px;
  background: linear-gradient(90deg, #0a1628, #0d2347, #0a1628);
  display: flex;
  flex-direction: column;
  position: relative;
  z-index: 10;
}

.header-content {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 20px;
  padding: 0 40px;
}

.title {
  font-size: 24px;
  font-weight: bold;
  color: var(--text-primary);
  font-family: var(--font-title);
  letter-spacing: 4px;
  @include text-glow(#00d4ff);
  white-space: nowrap;
  flex-shrink: 0;
}

.decoration-left,
.decoration-right {
  flex: 1;
  display: flex;
  align-items: center;
  min-width: 0;

  :deep(.dv-decoration-1) {
    width: 100% !important;
    height: 50px;
  }
}

/* 左侧装饰由内向外（从右往左，水平翻转） */
.decoration-left {
  justify-content: flex-end;
  transform: scaleX(-1);
}

/* 右侧装饰由内向外（从左往右，默认方向） */
.decoration-right {
  justify-content: flex-start;
}
</style>
