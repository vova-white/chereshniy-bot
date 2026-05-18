# Context Map

## Contexts

- [Backend](./backend/CONTEXT.md) — composes server-side bounded contexts into one Ktor application
- [Identity & Access](./backend/src/main/kotlin/identityaccess/CONTEXT.md) — identifies Users and controls access to Workspaces
- [Point Auction](./backend/src/main/kotlin/pointauction/CONTEXT.md) — runs workspace-scoped channel-points auctions

## Relationships

- **Backend → Identity & Access**: Backend routes authenticated and private API flows through Identity & Access
- **Backend → Point Auction**: Backend routes authorized Point Auction commands to the Point Auction context
- **Identity & Access → Twitch**: Identity & Access uses Twitch Integration to link a User to their Twitch Account
- **Point Auction → Identity & Access**: Point Auction receives commands that are already scoped to an authorized Workspace
