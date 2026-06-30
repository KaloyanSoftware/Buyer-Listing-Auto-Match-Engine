export type BuyerStatus = 'ACTIVE' | 'PAUSED' | 'CLOSED'
export type PropertyType = 'APARTMENT' | 'HOUSE' | 'COMMERCIAL' | 'LAND' | 'ANY'
export type BuyerTimeline = 'IMMEDIATE' | 'THREE_TO_SIX_MONTHS' | 'SIX_PLUS_MONTHS'

export interface BuyerListItem {
  id: string
  fullName: string
  email: string | null
  phone: string | null
  status: BuyerStatus
  budgetMin: number | null
  budgetMax: number
  propertyType: PropertyType
  preferredLocations: string[]
  lastContactedAt: string | null
  matchCount: number
}

export interface BuyerResponse extends BuyerListItem {
  minSqm: number | null
  minBedrooms: number | null
  requiresElevator: boolean
  requiresParking: boolean
  requiresGarden: boolean
  avoidGroundFloor: boolean
  avoidTopFloor: boolean
  timeline: BuyerTimeline | null
  notes: string | null
  createdAt: string
}

export interface CreateBuyerRequest {
  fullName: string
  email?: string
  phone?: string
  budgetMin?: number
  budgetMax: number
  propertyType: PropertyType
  minSqm?: number
  minBedrooms?: number
  requiresElevator: boolean
  requiresParking: boolean
  requiresGarden: boolean
  avoidGroundFloor: boolean
  avoidTopFloor: boolean
  timeline?: BuyerTimeline
  notes?: string
  preferredLocations: string[]
}
