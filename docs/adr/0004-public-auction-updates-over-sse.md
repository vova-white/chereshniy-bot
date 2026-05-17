# Auction updates over SSE

The public auction page and authenticated admin workbench receive near-real-time updates over Server-Sent Events. These update streams are one-way from backend to browser, while admin mutations use regular HTTP commands, so SSE fits the interaction model with less connection and protocol complexity than WebSockets.

**Consequences**

- Public update payloads are lightweight invalidation hints. On public update or reconnect, the browser reloads the current Public Auction View through the public query endpoint.
- Public update events must not contain admin-only data.
- The backend must apply connection limits for unauthenticated public SSE streams.
- Admin update streams must be authenticated.
- Admin update events are typed patch events with compact payloads that let the Workbench update affected UI state without a required refetch after every event.
- Admin clients refetch snapshots for recovery cases: initial connection, reconnect, version gap, unknown event type, failed optimistic state, or an event explicitly marked as requiring refresh.
- WebSockets remain unnecessary for the first release because browsers do not send commands through the realtime channel.
