<script setup lang="ts">
import { onMounted, ref } from 'vue'
import DashboardView from '@/modules/dashboard/DashboardView.vue'
import { fetchApplicationSession, logOut, type AdminSessionView } from '@/app/session'

const session = ref<AdminSessionView | null>(null)
const isLoading = ref(true)
const errorMessage = ref('')

async function loadSession() {
  isLoading.value = true
  errorMessage.value = ''

  try {
    session.value = await fetchApplicationSession()
  } catch (error) {
    errorMessage.value =
      error instanceof Error ? error.message : 'Unable to load the Application Session.'
  } finally {
    isLoading.value = false
  }
}

async function handleLogOut() {
  errorMessage.value = ''

  try {
    await logOut()
    session.value = null
  } catch (error) {
    errorMessage.value =
      error instanceof Error ? error.message : 'Unable to end the Application Session.'
  }
}

onMounted(loadSession)
</script>

<template>
  <main class="app-shell">
    <section v-if="isLoading" class="auth-panel" aria-live="polite">
      <p class="eyebrow">Application Session</p>
      <p>Loading</p>
    </section>

    <section v-else-if="!session" class="auth-panel" aria-labelledby="sign-in-title">
      <p class="eyebrow">Streamer Entry</p>
      <h1 id="sign-in-title">Sign in with Twitch</h1>
      <a class="primary-action" href="/api/auth/twitch/start">Sign in with Twitch</a>
      <p v-if="errorMessage" class="error-text" role="alert">{{ errorMessage }}</p>
    </section>

    <DashboardView v-else :session="session" @log-out="handleLogOut" />

    <p v-if="session && errorMessage" class="shell-error" role="alert">{{ errorMessage }}</p>
  </main>
</template>
