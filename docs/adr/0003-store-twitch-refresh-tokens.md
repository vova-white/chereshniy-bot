# Store Twitch refresh tokens for server-side actions

The backend stores Twitch refresh tokens for streamers so it can refresh access tokens and perform server-side auction actions without requiring the streamer browser to be online. EventSub redemptions, reward synchronization, fulfillment, and refunds may happen after the original access token expires, so relying only on browser sessions would make the auction unreliable.

**Consequences**

- Twitch tokens are sensitive backend secrets and must be protected at rest and in operational access.
- Token refresh failures are treated as Twitch authorization failures and recovered through signing in with Twitch again.
- The frontend must not receive or store Twitch refresh tokens.
- Browser admin authentication uses a secure HTTP-only application session cookie rather than exposing Twitch tokens or storing bearer tokens in the frontend.
