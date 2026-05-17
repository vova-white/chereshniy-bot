import { describe, it, expect } from 'vite-plus/test'

import { mount } from '@vue/test-utils'
import App from '../App.vue'

describe('App', () => {
  it('renders the dashboard shell through the app module', () => {
    const wrapper = mount(App)
    expect(wrapper.text()).toContain('Dashboard')
    expect(wrapper.text()).toContain('Point Auction')
  })
})
