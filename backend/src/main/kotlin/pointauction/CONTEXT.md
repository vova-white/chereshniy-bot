# Point Auction

Point Auction is a workspace-scoped module for running channel-points auctions.

## Language

**Point Auction**:
A workspace-scoped module for running channel-points auctions.
_Avoid_: auction, points module

**Workspace**:
The access boundary that owns Point Auction data.
_Avoid_: tenant, channel

## Relationships

- A **Point Auction** belongs to exactly one **Workspace**
- A **Workspace** may have many **Point Auctions**

## Example Dialogue

> **Dev:** "Does a **Point Auction** know who logged in through Twitch?"
> **Domain expert:** "No. The command is already authorized and scoped to a **Workspace**."

## Flagged Ambiguities

- Detailed Point Auction rules are intentionally not defined yet.
