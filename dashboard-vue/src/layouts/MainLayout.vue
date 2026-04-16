<template>
  <div class="main-layout">
    <Header />
    <div class="content">
      <router-view />
    </div>
  </div>
</template>

<script setup lang="ts">
import Header from './Header.vue'
import { watch } from 'vue'
import { useRoute } from 'vue-router'
import { useNavigationStore } from '@/stores/navigation'

const route = useRoute()
const navigationStore = useNavigationStore()

watch(
  () => route.path,
  (newPath) => {
    navigationStore.setActiveRoute(newPath)
  },
  { immediate: true }
)
</script>

<style scoped lang="scss">
.main-layout {
  width: 100%;
  height: 100%;
  background: var(--bg-dark);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.content {
  flex: 1;
  margin-top: -10px;
  position: relative;
  z-index: 11;
  overflow: hidden;
}
</style>
