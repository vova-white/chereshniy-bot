# Point Auction bounded contexts

The first release is implemented as one application, but it should be designed around clear bounded contexts: pointauction/auction, pointauction/bid, pointauction/rewards, Twitch Integration, Streamer Identity, pointauction/publicview, Admin Experience, and Update Delivery. pointauction/auction and pointauction/bid are the core subdomains and must keep Twitch API language behind a Twitch Integration anti-corruption layer.

**Consequences**

- Core auction code should speak in Auction Lists, Auction Lots, Lot Amounts, Lot Aliases, Supporters, Incoming Bids, and confirmed bid outcomes rather than raw Twitch redemptions.
- Twitch identifiers, token refresh, EventSub verification, reward API calls, and webhook delivery details belong behind Twitch Integration interfaces.
- Reusable Twitch clients do not depend on business contexts; inbound Twitch webhook adapters may call application interfaces after translating and verifying Twitch messages.
- The EventSub webhook route lives in Twitch Integration because it handles Twitch protocol concerns before handing translated events to Point Auction application code.
- pointauction/publicview and Admin Experience must remain separate because public viewers receive only a strict read-only subset of auction state.
- Future modules should reuse Streamer Identity rather than embedding Twitch login/session/token behavior inside Point Auction.
