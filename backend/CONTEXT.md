# Backend

The Backend manages user identity, Twitch-linked workspaces, integrations, and server-side modules for Chereshniy Bot.

## Language

**User**:
A person who authenticates through Twitch to use Chereshniy Bot.
_Avoid_: streamer, account

**User ID**:
The app-owned stable identifier for a User.
_Avoid_: Twitch user id

**Twitch Account**:
The external Twitch account linked to a User after Twitch authentication, including its Twitch user id, login, display name, profile image, and email when available.
_Avoid_: user, workspace

**Workspace**:
The private app space owned by one User.
_Avoid_: tenant, organization, team

**Workspace Owner**:
The User who owns a Workspace.
_Avoid_: member

**Workspace Member**:
A User with delegated access to another User's Workspace.
_Avoid_: viewer, anonymous viewer

**Workspace Access Grant**:
An access record that gives a User a Workspace Role in a Workspace.
_Avoid_: membership, permission row

**Optional Workspace Integration**:
An additional external provider connection attached to a Workspace.
_Avoid_: channel, required integration

**External Account**:
The provider-side account connected through an Optional Workspace Integration.
_Avoid_: streamer, workspace

**Anonymous Viewer**:
A person who can access public parts of the app without authenticating.
_Avoid_: user, member

**Identity & Access**:
The backend area that identifies the current User and controls access to Workspaces.
_Avoid_: auth

**Workspace Role**:
The access level granted to a Workspace Member.
_Avoid_: permission, group

**Twitch Integration**:
The backend area that translates between Chereshniy Bot and Twitch accounts, authorization, and APIs.
_Avoid_: twitch auth, twitch client

## Relationships

- A **User** has exactly one **User ID**
- A **User** is linked to exactly one **Twitch Account**
- A **User** may own exactly one **Workspace**
- A **Workspace** belongs to exactly one **Workspace Owner**
- A **Workspace** may have many **Optional Workspace Integrations**
- An **Optional Workspace Integration** connects exactly one **External Account**
- A **Workspace Owner** has a **Workspace Access Grant** with the Owner role
- A **User** may be a **Workspace Member** of another **User**'s **Workspace**
- A **Workspace Member** has a **Workspace Access Grant** with the Editor role
- An **Anonymous Viewer** is not a **Workspace Member**
- **Identity & Access** uses **Twitch Integration** to link a **User** with a **Twitch Account**

## Example Dialogue

> **Dev:** "When a **User** signs in with Twitch for the first time, do they choose a workspace?"
> **Domain expert:** "No. Their **Workspace** is created automatically and they receive the Owner role."

## Flagged Ambiguities

- "user" means a Twitch-authenticated **User** of Chereshniy Bot; anonymous public visitors are **Anonymous Viewers**.
- **Workspace** is part of **Identity & Access** for now because it currently acts as an access boundary, not as an independent business context.
- **Twitch Account** is not treated as an **Optional Workspace Integration** because registration requires Twitch.
