# Frontend

Browser-facing admin and public auction workflows for streamers and viewers.

## Language

**Admin Workflows**:
The signed-in streamer interface for managing auction lots, bid rewards, auction activity, and incoming bids.
_Avoid_: Control room

**Dashboard**:
The signed-in streamer's module overview page.
_Avoid_: Landing page, home page

**Log Out**:
The streamer action that ends the current application session.
_Avoid_: Disconnect Twitch, revoke access

**Point Auction Module**:
The admin module for managing the auction list, auction activity, bid queue, and bid reward set.
_Avoid_: Poin Auc, auction app

**Point Auction Label**:
The visible menu and dashboard label for the point auction module.
_Avoid_: Poin Auc, points auction, channel points auction

**Point Auction Summary**:
The dashboard-card summary of bidding status, lot count, and pending bid count.
_Avoid_: Analytics, report

**Point Auction Workbench**:
The main point auction screen combining lot management, bid queue handling, and bidding activation.
_Avoid_: Lots tab, auction settings page

**Mobile-first Admin**:
An admin interface designed first for small screens while expanding to denser desktop layouts.
_Avoid_: Desktop-only admin, responsive afterthought

**Rewards Settings**:
The point auction area for managing the bid reward set and bid reward options.
_Avoid_: Main auction screen, bid queue

**Public Auction Page**:
The viewer-facing page that shows the current auction list without requiring application sign-in.
_Avoid_: Admin page, overlay

**Auction Title**:
The public title shown at the top of the public auction page.
_Avoid_: Streamer name, channel title

**Bid Queue**:
The admin-facing working list of pending incoming bids and failed bid actions that need streamer attention.
_Avoid_: Bid history, public bid list

**Lot Picker**:
The mobile-friendly selector used to choose an auction lot when applying an incoming bid.
_Avoid_: Dropdown, autocomplete only

**Open Bidding Status**:
The public indication that viewers can currently place new bids through Twitch rewards.
_Avoid_: Online, live

**Closed Bidding Status**:
The public indication that viewers can see the auction list but cannot currently place new bids through Twitch rewards.
_Avoid_: Offline, hidden

**Empty Auction State**:
The public-page state shown when the current auction list has no lots.
_Avoid_: Setup prompt, onboarding

**English-first UI**:
The first-release interface language for admin and public pages.
_Avoid_: Multilingual UI, Russian-first UI

## Relationships

- The first release uses **English-first UI**.
- After sign-in, the streamer lands on the **Dashboard**.
- Sign-in is only through Twitch.
- The first release has **Log Out**, not a separate disconnect Twitch action.
- Twitch authorization recovery uses repeated Twitch sign-in, not a separate connection screen.
- After first Twitch sign-in, the **Dashboard** is immediately available with a **Point Auction** card.
- The **Dashboard** links to the **Point Auction Module**.
- The **Point Auction Module** is labeled **Point Auction** in navigation and dashboard cards.
- The **Dashboard** card for **Point Auction Module** shows **Point Auction Summary**.
- **Admin Workflows** are available only to the signed-in streamer.
- The **Point Auction Module** contains the **Point Auction Workbench** and **Rewards Settings**.
- **Admin Workflows** are **Mobile-first Admin** workflows.
- The **Public Auction Page** is also mobile-first.
- The **Point Auction Workbench** combines lots, the bid queue, and activation controls.
- The **Point Auction Workbench** shows a simple active/inactive bidding toggle, not separate desired and actual Twitch availability states.
- The **Point Auction Workbench** is where the Streamer edits the **Auction Title**.
- Editing the **Auction Title** does not require confirmation.
- If changing the active/inactive toggle fails, the visible toggle returns to its previous state and shows an error.
- Deactivating bidding from the **Point Auction Workbench** does not require a warning confirmation.
- Clearing the auction list from the **Point Auction Workbench** requires confirmation.
- Removing a single lot from the **Point Auction Workbench** requires confirmation.
- Rejecting and refunding an incoming bid from the **Bid Queue** does not require confirmation.
- Creating a new lot from an incoming bid does not require confirmation.
- Applying an incoming bid to a selected lot does not require confirmation.
- Renaming a lot does not warn about possible automatic matching of pending bids.
- Skipped alias creation after a successful bid apply does not show a separate UI notice.
- On desktop, the **Point Auction Workbench** shows lots and the **Bid Queue** at the same time.
- On mobile, applying an incoming bid uses a searchable **Lot Picker**.
- **Rewards Settings** manage the bid reward set separately from the main workbench.
- Saving **Rewards Settings** immediately synchronizes the managed Twitch rewards.
- The **Public Auction Page** is available to viewers without sign-in.
- The **Public Auction Page** shows the **Auction Title** as its page title, defaulting to `Point Auction` when blank.
- The **Public Auction Page** shows **Empty Auction State** when there are no lots.
- The **Public Auction Page** does not show separate instructions for placing bids.
- The **Bid Queue** is part of **Admin Workflows**, not the **Public Auction Page**.
- The **Public Auction Page** shows either **Open Bidding Status** or **Closed Bidding Status**.

## Example dialogue

> **Dev:** "Should pending bids appear on the public page?"
> **Domain expert:** "No - pending bids belong in the Bid Queue inside Admin Workflows."

> **Dev:** "Should the public page title use the streamer's Twitch identity?"
> **Domain expert:** "No - it shows the Auction Title."

> **Dev:** "What title appears before the Streamer customizes it?"
> **Domain expert:** "Auction Title defaults to `Point Auction`."

> **Dev:** "What if the Streamer clears the Auction Title field?"
> **Domain expert:** "The public page falls back to `Point Auction`."

> **Dev:** "Where does the Streamer go after login?"
> **Domain expert:** "The Streamer lands on the Dashboard and opens the Point Auction Module from there."

> **Dev:** "Does the Streamer need a setup wizard before seeing Dashboard?"
> **Domain expert:** "No - after first Twitch sign-in, Dashboard is immediately available."

> **Dev:** "Can a Streamer reach the Dashboard without Twitch login?"
> **Domain expert:** "No - sign-in is only through Twitch."

> **Dev:** "Does the first release need a Disconnect Twitch button?"
> **Domain expert:** "No - it has Log Out only."

> **Dev:** "Where does the Streamer fix expired Twitch authorization?"
> **Domain expert:** "They sign in with Twitch again; there is no separate connection screen."

> **Dev:** "Should the Point Auction dashboard card be only a link?"
> **Domain expert:** "No - it shows bidding status, lot count, and pending bid count."

> **Dev:** "Should the menu shorten the module name to Poin Auc?"
> **Domain expert:** "No - the visible label is Point Auction."

> **Dev:** "Should lots and bid queue be separate tabs?"
> **Domain expert:** "No - they belong together in the Point Auction Workbench with activation controls."

> **Dev:** "Should closing bidding warn about pending bids?"
> **Domain expert:** "No - deactivation does not require a warning confirmation."

> **Dev:** "Should the UI show separate desired and actual Twitch reward availability?"
> **Domain expert:** "No - the workbench shows a simple active/inactive bidding toggle."

> **Dev:** "Where does the Streamer edit the Auction Title?"
> **Domain expert:** "On the Point Auction Workbench, not in Rewards Settings."

> **Dev:** "Does changing the Auction Title require confirmation?"
> **Domain expert:** "No - editing the Auction Title is a regular edit."

> **Dev:** "What should the toggle show if Twitch enable or disable fails?"
> **Domain expert:** "It returns to the previous visible state and shows an error."

> **Dev:** "Can the Streamer clear all lots with one unconfirmed tap?"
> **Domain expert:** "No - clearing the auction list requires confirmation."

> **Dev:** "Can the Streamer delete a lot with one unconfirmed tap?"
> **Domain expert:** "No - removing a single lot requires confirmation."

> **Dev:** "Should rejecting a bid ask for confirmation?"
> **Domain expert:** "No - rejecting and refunding an incoming bid does not require confirmation."

> **Dev:** "Should creating a lot from a bid ask for confirmation?"
> **Domain expert:** "No - creating a new lot from an incoming bid does not require confirmation."

> **Dev:** "Should applying a bid to a selected lot ask for confirmation?"
> **Domain expert:** "No - applying an incoming bid to a selected lot does not require confirmation."

> **Dev:** "Should renaming a lot warn that pending bids may auto-apply?"
> **Domain expert:** "No - renaming a lot does not show that warning."

> **Dev:** "Should skipped alias creation after a successful apply show a notice?"
> **Domain expert:** "No - skipped alias creation does not show a separate UI notice."

> **Dev:** "Can the Streamer see the bid and target lots at the same time?"
> **Domain expert:** "Yes - on desktop, lots and the Bid Queue are visible together."

> **Dev:** "Is the admin UI desktop-first?"
> **Domain expert:** "No - Admin Workflows are mobile-first, then expand to denser desktop layouts."

> **Dev:** "How does the Streamer apply a bid to a lot on mobile?"
> **Domain expert:** "They open a searchable Lot Picker from the bid action."

> **Dev:** "Do reward edits need a separate publish step?"
> **Domain expert:** "No - saving Rewards Settings immediately synchronizes Twitch rewards."

> **Dev:** "Does closed bidding hide the public page?"
> **Domain expert:** "No - the Public Auction Page remains visible and shows Closed Bidding Status."

> **Dev:** "What should viewers see when there are no lots?"
> **Domain expert:** "The Public Auction Page shows Empty Auction State and the current bidding status."

> **Dev:** "Should the public page explain how to place bids?"
> **Domain expert:** "No - bid instructions belong in the Twitch reward description."

> **Dev:** "Is the public page optimized for desktop first?"
> **Domain expert:** "No - the Public Auction Page is mobile-first too."

> **Dev:** "Should the first release support multiple UI languages?"
> **Domain expert:** "No - the first release uses English-first UI."

## Flagged ambiguities

- "Overlay" was considered for the viewer-facing surface; resolved: the first release uses **Public Auction Page** rather than a separate OBS overlay.
