# Twitch-confirmed bid workflows

Bid actions are processed as durable, idempotent workflows with separate requested, in-progress, failed, and confirmed outcomes. Applying a bid or creating a lot from a bid becomes final only after Twitch confirms reward fulfillment; rejecting a bid becomes refunded only after Twitch confirms the channel points return. One Twitch reward redemption can affect a lot amount at most once, including retries, so failed actions are retried rather than manually forced into final states.

**Consequences**

- Auction state reflects Twitch-confirmed outcomes instead of optimistic local changes.
- Retry handling must be idempotent per Twitch reward redemption.
- The admin UI must show in-progress and failed bid actions instead of treating every click as immediately final.
- Twitch-dependent HTTP commands return accepted or in-progress quickly, with final results delivered through admin updates.
