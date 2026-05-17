# REST-ish command and query HTTP API

The backend exposes REST-ish HTTP endpoints for admin commands, public queries, and SSE streams. Business actions such as applying, rejecting, activating, or syncing are represented as explicit command endpoints rather than forced into pure CRUD naming.

**Consequences**

- HTTP routes are grouped by bounded context and vertical slice rather than by global controller type.
- Modules expose route registration functions, and the app composition root calls them while controlling URL layout and authentication wrappers.
- MVP API routes do not use a `/v1` prefix; admin routes live under `/api/admin/...` and public routes under `/api/public/...`.
- Point Auction admin routes use user-facing resource names under `/api/admin/point-auction`, such as `/summary`, `/lots`, `/bids`, `/rewards`, and `/events`.
- Public auction data uses `/api/public/auctions/{slug}` while the frontend human page uses `/auction/:slug`.
- Public query endpoints expose Public Auction View fields only.
- Admin command endpoints authenticate with the Application Session and may return accepted or in-progress for Twitch-dependent work.
- HTTP error handling uses a central baseline for unexpected exceptions, auth/session failures, and response envelopes, while module web adapters map their own explicit domain/application result errors to HTTP statuses and messages.
- Successful responses return endpoint-specific DTOs directly; error responses use a lightweight `{ error: { code, message } }` envelope.
- GraphQL, tRPC, and generic RPC frameworks are out of scope for the MVP.
