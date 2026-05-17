<script setup lang="ts">
import { pointAuctionSummary } from '@/modules/point-auction/model/pointAuctionSummary'
</script>

<template>
  <section class="dashboard-shell" aria-labelledby="dashboard-title">
    <header class="dashboard-header">
      <p class="eyebrow">Admin Workflows</p>
      <h1 id="dashboard-title">Dashboard</h1>
    </header>

    <article class="module-card" aria-labelledby="point-auction-title">
      <div>
        <p class="eyebrow">Module</p>
        <h2 id="point-auction-title">Point Auction</h2>
      </div>
      <p>{{ pointAuctionSummary.statusLabel }}</p>
    </article>
  </section>
</template>
