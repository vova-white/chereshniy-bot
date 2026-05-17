# Twitch EventSub over webhooks

The first release receives Twitch EventSub notifications through webhooks rather than a long-lived EventSub WebSocket connection. Webhooks fit the production backend shape better because Twitch can deliver events to a stable HTTPS callback across process restarts and retry delivery, while WebSocket transport would require reliable connection/session management inside the application runtime.

For channel points custom reward redemptions, the application creates EventSub subscriptions scoped to each managed reward ID when Twitch supports the `reward_id` condition for that subscription type. This keeps unrelated channel rewards out of the bid pipeline.

**Consequences**

- The backend must expose a public HTTPS EventSub callback and verify Twitch webhook signatures.
- Event processing must deduplicate Twitch EventSub message IDs because delivery is at least once.
- Local development simulates EventSub events with Twitch CLI or tests.
- Real EventSub delivery is verified against a deployed HTTPS dev, staging, or production environment.
- Manual local end-to-end testing may use a tunnel that exposes localhost through a public HTTPS URL.
