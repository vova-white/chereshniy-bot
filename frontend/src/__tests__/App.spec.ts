import { afterEach, describe, expect, it, vi } from 'vite-plus/test'

import { flushPromises, mount } from '@vue/test-utils'
import App from '../App.vue'

const signedInSession = {
  streamer: {
    displayName: 'Cherry Streamer',
    publicAuctionSlug: 'cherry_streamer',
  },
  pointAuction: {
    auctionTitle: 'Point Auction',
    statusLabel: 'Closed Bidding Status',
    lotCount: 0,
    pendingBidCount: 0,
  },
}

describe('App', () => {
  afterEach(() => {
    vi.unstubAllGlobals()
  })

  it('renders twitch sign in as the only entry point when no application session exists', async () => {
    vi.stubGlobal(
      'fetch',
      vi.fn<typeof globalThis.fetch>(
        async () => new Response('{"error":{"code":"not_signed_in"}}', { status: 401 }),
      ),
    )

    const wrapper = mount(App)
    await flushPromises()

    expect(wrapper.text()).toContain('Sign in with Twitch')
    expect(wrapper.find('a[href="/api/auth/twitch/start"]').exists()).toBe(true)
    expect(wrapper.text()).not.toContain('Google')
  })

  it('renders the signed-in dashboard with point auction summary placeholders', async () => {
    vi.stubGlobal(
      'fetch',
      vi.fn<typeof globalThis.fetch>(async () => Response.json(signedInSession)),
    )

    const wrapper = mount(App)
    await flushPromises()

    expect(wrapper.text()).toContain('Dashboard')
    expect(wrapper.text()).toContain('Cherry Streamer')
    expect(wrapper.text()).toContain('Point Auction')
    expect(wrapper.text()).toContain('Closed Bidding Status')
    expect(wrapper.text()).toContain('0 lots')
    expect(wrapper.text()).toContain('0 pending bids')
    expect(wrapper.text()).not.toContain('access-token')
    expect(wrapper.text()).not.toContain('refresh-token')
  })

  it('logs out without offering a twitch disconnect action', async () => {
    const fetchMock = vi.fn<typeof globalThis.fetch>(async (input) => {
      const path =
        typeof input === 'string'
          ? input
          : input instanceof URL
            ? input.pathname
            : new URL(input.url).pathname

      if (path === '/api/admin/session') {
        return Response.json(signedInSession)
      }
      return new Response(null, { status: 204 })
    })
    vi.stubGlobal('fetch', fetchMock)

    const wrapper = mount(App)
    await flushPromises()

    expect(wrapper.text()).toContain('Log Out')
    expect(wrapper.text()).not.toContain('Disconnect Twitch')

    await wrapper.get('button').trigger('click')
    await flushPromises()

    expect(fetchMock).toHaveBeenCalledWith('/api/admin/logout', {
      method: 'POST',
      headers: {
        'X-CSRF-Token': 'logout',
      },
    })
    expect(wrapper.text()).toContain('Sign in with Twitch')
  })
})
