import type { BuyerListItem, BuyerResponse, CreateBuyerRequest, BuyerStatus } from '@/types/buyer'
import apiClient from './client'

export const buyersApi = {
  list: (status?: BuyerStatus) =>
    apiClient.get<BuyerListItem[]>('/buyers', { params: status ? { status } : {} }).then(r => r.data),

  create: (data: CreateBuyerRequest) =>
    apiClient.post<BuyerResponse>('/buyers', data).then(r => r.data),
}
