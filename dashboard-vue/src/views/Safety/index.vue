<template>
  <div class="sub-page">
    <div class="sub-page__icon">🛡️</div>
    <h1 class="sub-page__title">校办</h1>
    <p class="sub-page__hint">内容建设中...</p>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped lang="scss">
.sub-page {
  width: 100%;
  height: 100%;
  background: var(--bg-dark);
  color: var(--text-primary);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 16px;

  &__icon {
    font-size: 48px;
  }

  &__title {
    font-size: 28px;
    font-family: var(--font-title);
    color: var(--text-accent, #00d4ff);
    text-shadow: 0 0 10px rgba(0, 102, 255, 0.5);
    margin: 0;
  }

  &__hint {
    font-size: 16px;
    color: var(--text-secondary);
    margin: 0;
  }
}
</style>
