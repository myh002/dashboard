/** 数字千分位格式化 */
export function formatNumber(num: number): string {
  if (num == null || Number.isNaN(num)) return '0'
  return num.toLocaleString('zh-CN')
}

/** 格式化时间为 HH:MM */
export function formatTime(date: Date): string {
  return date.toLocaleTimeString('zh-CN', {
    hour: '2-digit',
    minute: '2-digit',
    hour12: false
  })
}

/** 格式化日期为 YYYY/M/D */
export function formatDate(date: Date): string {
  return `${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()}`
}
