import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useQuery } from '@tanstack/react-query'
import { Plus, User } from 'lucide-react'
import { buyersApi } from '@/api/buyers'
import type { BuyerStatus, BuyerListItem } from '@/types/buyer'

const STATUS_TABS: { label: string; value: BuyerStatus | undefined }[] = [
  { label: 'All', value: undefined },
  { label: 'Active', value: 'ACTIVE' },
  { label: 'Paused', value: 'PAUSED' },
  { label: 'Closed', value: 'CLOSED' },
]

const STATUS_BADGE: Record<BuyerStatus, string> = {
  ACTIVE: 'bg-green-100 text-green-700',
  PAUSED: 'bg-yellow-100 text-yellow-700',
  CLOSED: 'bg-gray-100 text-gray-500',
}

function formatBudget(min: number | null, max: number): string {
  const fmt = (n: number) => '€' + n.toLocaleString()
  return min ? `${fmt(min)} – ${fmt(max)}` : `Up to ${fmt(max)}`
}

function formatLastContacted(date: string | null): string {
  if (!date) return 'Never'
  const days = Math.floor((Date.now() - new Date(date).getTime()) / 86_400_000)
  if (days === 0) return 'Today'
  if (days === 1) return 'Yesterday'
  return `${days} days ago`
}

export default function BuyersPage() {
  const navigate = useNavigate()
  const [activeTab, setActiveTab] = useState<BuyerStatus | undefined>(undefined)

  const { data: buyers = [], isLoading } = useQuery({
    queryKey: ['buyers', activeTab],
    queryFn: () => buyersApi.list(activeTab),
  })

  return (
    <div className="p-8">
      {/* Header */}
      <div className="flex items-center justify-between mb-6">
        <div>
          <h2 className="text-2xl font-bold text-gray-900">Buyers</h2>
          <p className="text-sm text-gray-500 mt-1">
            {buyers.length} buyer{buyers.length !== 1 ? 's' : ''} in your agency
          </p>
        </div>
        <button
          onClick={() => navigate('/buyers/new')}
          className="flex items-center gap-2 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white text-sm font-semibold rounded-lg transition-colors"
        >
          <Plus size={16} />
          Add Buyer
        </button>
      </div>

      {/* Status tabs */}
      <div className="flex gap-1 mb-6 border-b border-gray-200">
        {STATUS_TABS.map(tab => (
          <button
            key={tab.label}
            onClick={() => setActiveTab(tab.value)}
            className={`px-4 py-2 text-sm font-medium border-b-2 transition-colors ${
              activeTab === tab.value
                ? 'border-blue-600 text-blue-600'
                : 'border-transparent text-gray-500 hover:text-gray-700'
            }`}
          >
            {tab.label}
          </button>
        ))}
      </div>

      {/* Table */}
      <div className="bg-white rounded-xl border border-gray-200 overflow-hidden">
        {isLoading ? (
          <div className="p-12 text-center text-gray-400">Loading buyers…</div>
        ) : buyers.length === 0 ? (
          <div className="p-12 text-center">
            <User size={40} className="mx-auto text-gray-300 mb-3" />
            <p className="text-gray-500 font-medium">No buyers yet</p>
            <p className="text-gray-400 text-sm mt-1">Add your first buyer to start matching</p>
            <button
              onClick={() => navigate('/buyers/new')}
              className="mt-4 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white text-sm font-semibold rounded-lg transition-colors"
            >
              Add Buyer
            </button>
          </div>
        ) : (
          <table className="w-full">
            <thead className="bg-gray-50 border-b border-gray-200">
              <tr>
                {['Name', 'Status', 'Budget', 'Locations', 'Last Contacted', 'Matches'].map(h => (
                  <th key={h} className="text-left text-xs font-semibold text-gray-500 uppercase tracking-wide px-4 py-3">
                    {h}
                  </th>
                ))}
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {buyers.map((buyer: BuyerListItem) => (
                <tr
                  key={buyer.id}
                  onClick={() => navigate(`/buyers/${buyer.id}`)}
                  className="hover:bg-gray-50 cursor-pointer transition-colors"
                >
                  <td className="px-4 py-3">
                    <p className="text-sm font-semibold text-gray-900">{buyer.fullName}</p>
                    {buyer.email && <p className="text-xs text-gray-400 mt-0.5">{buyer.email}</p>}
                  </td>
                  <td className="px-4 py-3">
                    <span className={`inline-flex items-center px-2 py-0.5 rounded-full text-xs font-medium ${STATUS_BADGE[buyer.status]}`}>
                      {buyer.status.charAt(0) + buyer.status.slice(1).toLowerCase()}
                    </span>
                  </td>
                  <td className="px-4 py-3 text-sm text-gray-700">
                    {formatBudget(buyer.budgetMin, buyer.budgetMax)}
                  </td>
                  <td className="px-4 py-3">
                    <div className="flex flex-wrap gap-1">
                      {buyer.preferredLocations.slice(0, 3).map(loc => (
                        <span key={loc} className="inline-block bg-gray-100 text-gray-600 text-xs px-2 py-0.5 rounded-full">
                          {loc}
                        </span>
                      ))}
                      {buyer.preferredLocations.length > 3 && (
                        <span className="text-xs text-gray-400">+{buyer.preferredLocations.length - 3}</span>
                      )}
                    </div>
                  </td>
                  <td className="px-4 py-3 text-sm text-gray-500">
                    {formatLastContacted(buyer.lastContactedAt)}
                  </td>
                  <td className="px-4 py-3">
                    {buyer.matchCount > 0 ? (
                      <span className="inline-flex items-center px-2 py-0.5 rounded-full text-xs font-semibold bg-blue-100 text-blue-700">
                        {buyer.matchCount}
                      </span>
                    ) : (
                      <span className="text-xs text-gray-400">—</span>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  )
}
