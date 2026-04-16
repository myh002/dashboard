<template>
  <div class="data-card">
    <div class="data-card__title">{{ title }}</div>
    <template v-if="mode === 'simple'">
      <div class="data-card__value">
        <span class="data-card__number">{{ formattedValue }}</span>
        <span v-if="unit" class="data-card__unit">{{ unit }}</span>
      </div>
    </template>
    <template v-else>
      <div class="data-card__dual">
        <div class="data-card__row">
          <span class="data-card__label">上年度</span>
          <div class="data-card__value">
            <span class="data-card__number">{{ formattedLastYear }}</span>
            <span v-if="unit" class="data-card__unit">{{ unit }}</span>
          </div>
        </div>
        <div class="data-card__row">
          <span class="data-card__label">今年新增</span>
          <div class="data-card__value">
            <span :class="['data-card__number', 'data-card__number--accent', getIncrementClass(newThisYear)]">
              {{ formatIncrementValue(newThisYear) }}<span :class="getArrowClass(newThisYear)">{{ (newThisYear ?? 0) >= 0 ? '↑' : '↓' }}</span>
            </span>
            <span v-if="unit" class="data-card__unit">{{ unit }}</span>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { formatNumber } from '@/utils/format'

const props = withDefaults(defineProps<{
  mode?: 'simple' | 'dual'
  title: string
  value?: number
  unit?: string
  lastYear?: number
  newThisYear?: number
}>(), {
  mode: 'simple'
})

const formattedValue = computed(() =>
  props.value != null ? formatNumber(props.value) : '--'
)
const formattedLastYear = computed(() =>
  props.lastYear != null ? formatNumber(props.lastYear) : '--'
)
const formattedNewThisYear = computed(() =>
  props.newThisYear != null ? formatNumber(props.newThisYear) : '--'
)

function formatIncrementValue(value: number | undefined): string {
  if (value == null) return '--'
  return value >= 0 ? `+${formatNumber(value)}` : `${formatNumber(value)}`
}

function getIncrementClass(value: number | undefined): string {
  if (value == null) return ''
  return value >= 0 ? 'increment-positive' : 'increment-negative'
}

function getArrowClass(value: number | undefined): string {
  if (value == null || value >= 0) return 'arrow-up'
  return 'arrow-down'
}
</script>

<style scoped lang="scss">
.data-card {
  @include card-style;
  min-width: 0;
  padding: 8px 3px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;

  &__title {
    font-size: 13px;
    color: var(--text-secondary);
    margin-bottom: 4px;
  }

  &__value {
    display: flex;
    align-items: baseline;
    gap: 4px;
  }

  &__number {
    @include number-style;
    font-size: 14px;

    &--accent {
      color: var(--text-accent);
    }
  }

  &__unit {
    font-size: 12px;
    color: var(--text-secondary);
  }

  &__dual {
    display: flex;
    flex-direction: column;
    gap: 4px;
  }

  &__row {
    display: flex;
    align-items: baseline;
    justify-content: space-between;
  }

  &__label {
    font-size: 12px;
    color: var(--text-secondary);
  }
}

.increment-positive {
  color: #67C23A;
}

.increment-negative {
  color: #F56C6C;
}

.arrow-up {
  font-size: 12px;
  margin-left: 2px;
}

.arrow-down {
  font-size: 12px;
  margin-left: 2px;
}
</style>
