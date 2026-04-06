import axios from 'axios'
import type { AxiosError, AxiosInstance, AxiosResponse } from 'axios'

/** 通用 API 响应结构 */
export interface ApiResponse<T = unknown> {
  code: number
  message: string
  data: T
}

const request: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    // 可在此注入 token
    // const token = localStorage.getItem('token')
    // if (token && config.headers) {
    //   config.headers.Authorization = `Bearer ${token}`
    // }
    return config
  },
  (error: AxiosError) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
// Axios interceptors typed with onFulfilled: (value: T) => T require returning the same type.
// Here we intentionally return response.data (ApiResponse) instead of the full AxiosResponse.
// Axios resolves at runtime correctly; TypeScript cannot express this via the interceptor API.
const onResponseFulfilled = (
  response: AxiosResponse<ApiResponse>
): ApiResponse => {
  if (response.data.code !== 0) {
    console.error(`[API Error] ${response.config.url}: ${response.data.message}`)
    return Promise.reject(new Error(response.data.message)) as unknown as ApiResponse
  }
  return response.data
}
const onResponseRejected = (error: AxiosError): Promise<never> => {
  if (error.response) {
    console.error(`[HTTP Error] ${error.response.status}: ${error.response.statusText}`)
  } else if (error.request) {
    console.error('[Network Error] No response received')
  } else {
    console.error('[Request Error]', error.message)
  }
  return Promise.reject(error)
}
// @ts-expect-error — Axios interceptors don't support response body transformation natively
request.interceptors.response.use(onResponseFulfilled, onResponseRejected)

export default request
