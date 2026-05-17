# Architecture

The MVP is a modular monolith with context-first backend packages and workflow-first frontend modules.

## Backend Target Structure

```text
backend/src/main/kotlin/com/chereshniy/
  app/
    # Ktor composition root: configuration, route registration, sessions,
    # serialization, error handling, dependency wiring, and job startup.

  identity/
    domain/
    application/
    web/
    persistence/

  pointauction/
    auction/
      domain/
      application/
      web/
      persistence/

    bid/
      domain/
      application/
      web/
      persistence/

    rewards/
      domain/
      application/
      web/
      persistence/

    publicview/
      application/
      web/

  twitch/
    oauth/
    helix/
    eventsub/
    tokens/

  updates/
    admin/
    public/

  shared/
    # Boring technical primitives only: jobs, clock, result, db helpers,
    # generic IDs infrastructure. No shared domain models.
```

## Backend Rules

- `app` is the only composition root. Domain modules must not depend on `app`.
- Admin routes are a delivery surface and live inside the owning module/subcontext.
- `pointauction/auction` owns Auction List, Auction Lot, matching, aliases, Supporters, and Lot Amount rules.
- `pointauction/bid` owns Incoming Bid lifecycle, Bid Queue, apply/refund workflows, failed actions, and externally resolved bids.
- `pointauction/rewards` owns Bid Reward Set/Options, reward sync, and EventSub Subscription records for managed rewards.
- `pointauction/publicview` owns public read model payload boundaries, not auction state mutation.
- Public auction payloads are built on demand from current Auction List/Lot state in the MVP; no separate persisted public projection is introduced until load or lifecycle requirements justify it.
- Admin Workbench payloads are built on demand from `pointauction/auction`, `pointauction/bid`, and `pointauction/rewards` state in the MVP; no separate persisted admin projection is introduced until query cost or lifecycle requirements justify it.
- `twitch` is an anti-corruption layer around Twitch OAuth, Helix, EventSub, token refresh, and Twitch DTOs.
- `updates` owns SSE delivery mechanics, not auction or bid decisions.
- Public SSE events are invalidation hints; public clients reload the Public Auction View snapshot after updates.
- Admin SSE events are authenticated typed patch events with compact payloads; admin clients refetch snapshots only for initial load/recovery.
- Cross-context writes go through application interfaces, not another context's persistence code.
- Application command handlers own transaction boundaries.
- Domain models are framework-free and use typed IDs/value objects.
- Cross-context application interfaces are named as narrow `...UseCases` from the providing context.
- Outbound dependencies required by a slice are named as `...Port` from the consuming context's perspective.
- `Commands` and `Queries` files define request/response models for application handlers; they are not persistence DTOs.
- Exposed table definitions and row mappings live in the owning context's `persistence` package.
- The app database module aggregates context schema objects and controls dev/test setup or production migration execution order.
- Cross-context foreign keys are allowed only for stable identity relationships and do not replace application interfaces.

## Backend Testing Rules

- The main test boundary is the application command/query handler for each vertical slice.
- Twitch-dependent behavior uses fake/stub ports in normal tests.
- Repository and database tests use a real database where constraints, transactions, idempotency, unique normalized name/alias rules, or durable job state matter.
- Domain unit tests are focused on dense rules such as matching, aliases, supporter uniqueness, bid workflow transitions, and reward sync planning.
- HTTP route tests stay thin and cover auth/session behavior, validation envelopes, status codes, and explicit error mapping.
- End-to-end tests are few and cover representative seams such as OAuth callback, EventSub webhook to Bid Queue, and apply/reject happy paths with fake Twitch adapters.

## Frontend Target Structure

```text
frontend/src/
  app/
    router/
    layout/
    session/

  modules/
    dashboard/

    point-auction/
      workbench/
      rewards-settings/
      public-page/
      api/
      realtime/
      model/

  shared/
    ui/
      # shadcn-vue source components
    lib/
    http/
    formatting/
    composables/
```

## Frontend Rules

- Frontend modules are organized by user workflow, not by mirroring backend subcontexts.
- shadcn-vue components live under `src/shared/ui`.
- Shared helpers live under `src/shared/lib`.
- Module API DTOs are written manually beside module API clients for the MVP.
- No single global store for all app data; use module-local stores/composables where needed.
