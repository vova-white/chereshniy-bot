# Backend

The Backend hosts Chereshniy Bot's server-side bounded contexts and composes them into one Ktor application.

## Language

**Identity & Access**:
The backend context that identifies the current User and controls access to Workspaces.
_Avoid_: auth

**Point Auction**:
A workspace-scoped module for running channel-points auctions.
_Avoid_: auction, points module

**Twitch Integration**:
The backend area that translates between Chereshniy Bot and Twitch accounts, authorization, and APIs.
_Avoid_: twitch auth, twitch client

## Relationships

- **Identity & Access** uses **Twitch Integration** to link Users with Twitch accounts
- **Point Auction** is scoped to a Workspace owned and protected by **Identity & Access**

## Example Dialogue

> **Dev:** "Should a **Point Auction** command check Twitch OAuth data?"
> **Domain expert:** "No. It should be scoped to an authorized Workspace."

## Flagged Ambiguities

- **Workspace** currently belongs to **Identity & Access** because it acts as an access boundary, not as an independent business context.
