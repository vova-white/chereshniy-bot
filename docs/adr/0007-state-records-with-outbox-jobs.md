# State records with outbox jobs instead of event sourcing

The Point Auction MVP stores current state records for auctions, bids, rewards, subscriptions, and workflow status, and uses durable outbox/job records for Twitch-dependent work. We are not event-sourcing bid workflows in the first release because the product needs idempotent retries and observable states, not full state reconstruction from an append-only event history.

**Consequences**

- Bid and reward workflows persist explicit current states such as Incoming, Applying, Refunding, Failed, Applied, and Refunded.
- Twitch-dependent commands enqueue durable work and return accepted or in-progress before final Twitch results arrive.
- Outbox/job processing must be idempotent around Twitch redemption IDs, EventSub message IDs, reward IDs, and subscription IDs.
- The generic job runner owns leasing, retries, and scheduling mechanics; each bounded context owns its job definitions and business semantics.
- If full audit history becomes a product requirement later, it should be added deliberately rather than inferred from MVP workflow storage.
