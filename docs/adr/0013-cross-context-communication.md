# Cross-context communication in the modular monolith

Bounded contexts communicate through explicit application interfaces when a use case needs immediate consistency, and through in-process events for notifications, projections, update fanout, and other side effects. The MVP does not route every interaction through events.

Internal events for public/admin projections and SSE update fanout are in-process and best-effort after the initiating transaction commits. They are not a durable event log.

Work that must retry, survive process restarts, or coordinate with Twitch uses durable jobs/outbox records instead of best-effort internal events.

**Consequences**

- Bid workflows can call Point Auction application interfaces directly when confirmed bid outcomes must change Auction Lot state.
- Lot and bid changes can publish internal events for public/admin projections and SSE updates.
- Projection and SSE update events can be lost during a process crash; clients must recover by reloading the current read model.
- Twitch-dependent work must be represented by durable job/outbox state when it needs retry or crash recovery.
- The MVP does not persist every domain event.
- Commands are named as requests, events are named as past-tense facts, and jobs are named as work to perform.
- Contexts must not bypass another context's application interface to write its persistence state directly.
- Event handlers should not hide required consistency steps that belong in the initiating command handler.
