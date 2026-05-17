export interface StreamerSessionView {
  readonly displayName: string
  readonly publicAuctionSlug: string
}

export interface PointAuctionSummaryView {
  readonly auctionTitle: string
  readonly statusLabel: string
  readonly lotCount: number
  readonly pendingBidCount: number
}

export interface AdminSessionView {
  readonly streamer: StreamerSessionView
  readonly pointAuction: PointAuctionSummaryView
}

export async function fetchApplicationSession(): Promise<AdminSessionView | null> {
  const response = await fetch('/api/admin/session')

  if (response.status === 401) {
    return null
  }

  if (!response.ok) {
    throw new Error('Unable to load the Application Session.')
  }

  return response.json() as Promise<AdminSessionView>
}

export async function logOut(): Promise<void> {
  const response = await fetch('/api/admin/logout', {
    method: 'POST',
    headers: {
      'X-CSRF-Token': 'logout',
    },
  })

  if (!response.ok) {
    throw new Error('Unable to end the Application Session.')
  }
}
