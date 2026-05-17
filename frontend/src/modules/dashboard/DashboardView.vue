<script setup lang="ts">
import { LogOut, RadioTower } from 'lucide-vue-next'
import type { AdminSessionView } from '@/app/session'

defineProps<{
  readonly session: AdminSessionView
}>()

defineEmits<{
  logOut: []
}>()
</script>

<template>
  <section class="dashboard-shell" aria-labelledby="dashboard-title">
    <header class="dashboard-header">
      <div>
        <p class="eyebrow">Admin Workflows</p>
        <h1 id="dashboard-title">Dashboard</h1>
      </div>
      <button
        class="icon-action"
        type="button"
        aria-label="Log Out"
        title="Log Out"
        @click="$emit('logOut')"
      >
        <LogOut aria-hidden="true" :size="18" />
        <span>Log Out</span>
      </button>
    </header>

    <article class="module-card" aria-labelledby="point-auction-title">
      <div class="module-card__heading">
        <RadioTower aria-hidden="true" :size="22" />
        <div>
          <p class="eyebrow">{{ session.streamer.displayName }}</p>
          <h2 id="point-auction-title">Point Auction</h2>
        </div>
      </div>
      <dl class="summary-grid" aria-label="Point Auction Summary">
        <div>
          <dt>Status</dt>
          <dd>{{ session.pointAuction.statusLabel }}</dd>
        </div>
        <div>
          <dt>Lots</dt>
          <dd>{{ session.pointAuction.lotCount }} lots</dd>
        </div>
        <div>
          <dt>Bid Queue</dt>
          <dd>{{ session.pointAuction.pendingBidCount }} pending bids</dd>
        </div>
      </dl>
    </article>
  </section>
</template>
