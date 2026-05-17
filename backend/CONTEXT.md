# Backend

Server-side Twitch integration, auction rules, persistence, authentication, and administrative behavior.

## Language

**Streamer**:
A Twitch broadcaster who signs in to the application and manages their channel's auction.
_Avoid_: User, account, broadcaster account

**Twitch Connection**:
The streamer's Twitch-based sign-in and authorization of the application to manage the auction module for their Twitch channel.
_Avoid_: Partial connection, separate app account

**Twitch Token Custody**:
The backend's responsibility for securely holding Twitch authorization needed for server-side auction actions.
_Avoid_: Browser token storage, frontend token custody

**Log Out**:
The streamer ending their current application session without revoking Twitch authorization.
_Avoid_: Disconnect Twitch, revoke access

**Application Session**:
The browser session that lets a signed-in streamer access admin workflows.
_Avoid_: Twitch token, frontend bearer token

**Viewer**:
A Twitch chat participant who can place a bid through a channel points reward but does not sign in to the application.
_Avoid_: Application user, customer

**Viewer Display Name**:
The viewer-facing Twitch name recorded on a bid.
_Avoid_: Viewer id, user id, login

**Auction List**:
The single current list of auction lots managed by a streamer for their channel.
_Avoid_: Auction session, auction round, archive

**Auction Title**:
The streamer-defined public name of the auction list.
_Avoid_: Streamer name, channel title, reward set title

**Active Auction List**:
An auction list whose bid rewards are enabled on Twitch so viewers can place new bids.
_Avoid_: Open auction, running auction

**Inactive Auction List**:
An auction list whose bid rewards are disabled on Twitch while its lots and incoming bids remain unchanged.
_Avoid_: Closed auction, deleted auction, paused archive

**Public Auction Page**:
A viewer-facing page that shows the current auction list without requiring application sign-in.
_Avoid_: Admin page, overlay

**Public Auction View**:
The unauthenticated read-only auction data exposed for the public auction page.
_Avoid_: Admin auction data, full auction record

**Public Auction Updates**:
The unauthenticated near-real-time public updates for the public auction page.
_Avoid_: Admin stream, bid queue stream

**Admin Auction Updates**:
The authenticated near-real-time updates for the point auction workbench.
_Avoid_: Public stream, command channel

**Twitch-dependent Command**:
An admin command whose final outcome depends on a Twitch API operation.
_Avoid_: Local edit, immediate command

**Public Auction Slug**:
The Twitch login-based public identifier used in the auction page URL.
_Avoid_: Internal id, channel id

**Auction Lot**:
A proposed film, game, or other text item in the auction list.
_Avoid_: Item, position, entry

**Lot Amount**:
The total channel points value currently assigned to an auction lot.
_Avoid_: Weight, cost, price

**Lot Alias**:
An alternate lot name that allows future bids with that text to match an existing auction lot.
_Avoid_: Synonym, tag, translation

**Incoming Bid**:
A bid received from Twitch that has not yet been applied or rejected by the streamer.
_Avoid_: Redemption, request, pending reward

**Bid Reward Set**:
A streamer-defined group of Twitch channel points rewards used for placing bids.
_Avoid_: Reward group, reward campaign

**Bid Reward Set Title**:
The required non-empty name shared by the bid reward options in a set.
_Avoid_: Reward prefix, campaign name

**Bid Reward Description**:
The optional viewer-facing instruction shared by the bid reward options in a set.
_Avoid_: Help text, admin note

**Bid Reward Option**:
One Twitch channel points reward within a bid reward set, with its own bid amount and color.
_Avoid_: Tier, level, stake

**Reward Color**:
The optional Twitch display color configured for a bid reward option.
_Avoid_: Theme color, lot color

**Failed Reward Sync**:
A bid reward set synchronization that did not complete for all managed Twitch rewards.
_Avoid_: Partial success, saved rewards

**EventSub Subscription**:
A Twitch event subscription used by the application to receive redemptions for managed bid reward options.
_Avoid_: Webhook, listener, polling

**Bid Amount**:
The channel points value of the Twitch reward used to place a bid.
_Avoid_: Bid weight, custom amount

**Bid Text**:
The viewer-provided text naming what the bid is meant to support.
_Avoid_: Comment, message, description

**Applied Bid**:
A bid that has been applied to an auction lot and increased its lot amount.
_Avoid_: Accepted bid, completed bid

**Applying Bid**:
A bid that the streamer chose to apply to an auction lot while the application is waiting for the action to complete.
_Avoid_: Applied bid, accepted bid

**Rejected Bid**:
A bid that the streamer declined instead of treating it as support for any auction lot.
_Avoid_: Deleted bid, ignored bid

**Refunding Bid**:
A rejected bid whose channel points return is in progress.
_Avoid_: Refunded bid, cancelled bid

**Refunded Bid**:
A rejected bid whose channel points were confirmed as returned to the viewer through Twitch.
_Avoid_: Cancelled bid, voided bid

**Failed Bid Action**:
A bid action that did not complete and needs retry or attention from the streamer.
_Avoid_: Final bid state, rejected bid, applied bid

**Externally Resolved Bid**:
A bid whose Twitch redemption was already finalized outside the application before the application could complete its action.
_Avoid_: Applied bid, refunded bid, rejected bid

**External Twitch Change**:
A change made directly in Twitch to a reward or redemption managed by the application.
_Avoid_: Supported edit, application sync

**Bid Queue**:
The admin-facing working list of bids that still need attention.
_Avoid_: Bid history, public bid list

**Invalid Bid**:
A bid whose text is empty after trimming or longer than the allowed lot name length.
_Avoid_: Rejected bid, malformed request

**Supporter**:
A short name recorded as having supported an auction lot, either from a viewer's bid or entered manually by the streamer.
_Avoid_: Investor, backer, shareholder

## Relationships

- A **Streamer** manages one channel's auction.
- Only the **Streamer** who owns the channel can access that channel's admin workflows.
- A **Streamer** uses one **Twitch Connection** that grants the auction module all permissions it needs.
- The application login is only through **Twitch Connection**.
- **Twitch Connection** requests only the identity and channel points management access needed by the auction module, without email access.
- **Twitch Token Custody** allows server-side auction actions when the streamer browser is not online.
- Twitch access and refresh tokens are not exposed to the frontend.
- The frontend accesses admin workflows through an **Application Session**, not Twitch tokens.
- One **Streamer** manages exactly one Twitch channel.
- **Log Out** ends the current application session without deleting auction data or revoking Twitch authorization.
- **Twitch Connection** problems do not automatically change whether the **Auction List** is active or inactive.
- **Twitch Connection** recovery happens through signing in with Twitch again.
- First Twitch sign-in creates the **Streamer** record, current **Auction List**, and **Public Auction Slug** if they do not exist.
- A **Viewer** can place bids without becoming a **Streamer** or an application user.
- A **Viewer** can view a **Public Auction Page** without signing in.
- A **Public Auction View** is unauthenticated, read-only, rate limited, and contains only public auction fields.
- **Public Auction Updates** are unauthenticated, connection limited, use server-sent events, and contain only **Public Auction View** fields.
- A bid records a **Viewer Display Name**, not a stable viewer identity.
- A **Streamer** has exactly one current **Auction List**.
- An **Auction List** can have one **Auction Title**, defaulting to `Point Auction` when blank.
- An **Auction List** is either **Active Auction List** or **Inactive Auction List**.
- A new **Auction List** starts inactive.
- An **Auction List** can become active only when a **Bid Reward Set** has at least one active **Bid Reward Option**.
- A **Failed Reward Sync** does not by itself prevent making the **Auction List** active when at least one managed active **Bid Reward Option** exists.
- Removing the last active **Bid Reward Option** makes the **Auction List** inactive.
- An **Auction List** contains zero or more **Auction Lots**.
- A **Streamer** can manually manage **Auction Lots** regardless of whether bidding is active or bid rewards are configured.
- An **Auction List** is displayed by **Lot Amount** descending, then by lot name ascending.
- A **Public Auction Page** shows each current **Auction Lot's** name, **Lot Amount**, and **Supporters**.
- A **Public Auction Page** shows the **Auction Title** instead of streamer identity as its page title.
- A **Public Auction Page** shows whether bidding is open or closed.
- A **Public Auction Page** updates near real time as the **Auction List** changes.
- A **Public Auction Page** is addressed by the streamer's **Public Auction Slug**.
- A **Public Auction Slug** follows the streamer's current Twitch login and does not keep old slug history.
- Successful Twitch sign-in updates the **Public Auction Slug** to the current Twitch login.
- A **Public Auction Slug** conflict between different streamers is a data integrity error, not a user-resolvable flow.
- A **Public Auction Page** exists before bid rewards are configured and then shows an empty list with closed bidding.
- Pending **Incoming Bids** are visible only in the admin workflows.
- The **Bid Queue** shows pending **Incoming Bids** and **Failed Bid Actions**, not completed applied or refunded bids.
- **Admin Auction Updates** are authenticated, use server-sent events, and can include bid queue state.
- **Twitch-dependent Commands** return accepted or in-progress before final completion, and final outcomes arrive through **Admin Auction Updates**.
- Completed bid history is not part of the first admin workflow.
- An **Inactive Auction List** remains visible on the **Public Auction Page**.
- An **Auction Lot** has one **Lot Amount**.
- **Lot Amount** is an integer channel points amount.
- An **Auction Lot's** name must be 1 to 250 characters after trimming.
- An **Auction Lot** has zero or more **Lot Aliases**.
- An **Auction Lot** has zero or more unique **Supporters** by trimmed, case-insensitive supporter name.
- Within one **Auction List**, normalized lot names and **Lot Aliases** must not overlap across lots.
- A **Streamer** can edit an **Auction Lot's** **Lot Amount** directly.
- Manual **Lot Amount** must be greater than or equal to zero.
- A **Streamer** can manually add or remove **Supporters** on an **Auction Lot**.
- A manually created **Auction Lot** can start with zero or one **Supporter** entered by the **Streamer**.
- Manual and bid-derived **Supporters** follow the same rules and are not distinguished.
- A removed **Supporter** can appear again later if a matching bid is applied to the same **Auction Lot**.
- A **Streamer** can view and remove **Lot Aliases**.
- Removing an **Auction Lot** from the **Auction List** does not refund already applied bids.
- Clearing the **Auction List** removes its current lots without refunding already applied bids.
- Clearing the **Auction List** does not remove pending **Incoming Bids**.
- An incoming bid automatically matches an **Auction Lot** when its text equals the lot name or one of its **Lot Aliases** after trimming, collapsing repeated spaces, and case-insensitive comparison.
- An automatically matched **Incoming Bid** starts the apply flow without streamer confirmation.
- Existing pending **Incoming Bids** can be automatically matched when the **Auction List's** names or aliases change.
- Retroactive automatic matching runs after creating an **Auction Lot**, renaming an **Auction Lot**, or creating a new **Lot Alias** from a manual bid attachment.
- Retroactive automatic matching applies all matching pending **Incoming Bids**.
- Local lot changes that trigger retroactive automatic matching become visible immediately, while matched bid applications continue asynchronously.
- A **Lot Alias** learned from manually applying one bid can automatically match other pending **Incoming Bids**.
- Creating a manual **Auction Lot** with an initial **Supporter** still triggers retroactive automatic matching by lot name.
- Renaming an **Auction Lot** can automatically apply matching pending **Incoming Bids** without preview or confirmation.
- When the **Streamer** manually attaches an incoming bid to an **Auction Lot**, the bid text becomes a **Lot Alias** only if it does not already match the lot name or an existing alias after normalization.
- A **Lot Alias** learned from manual bid attachment is created only after Twitch confirms fulfillment for that bid.
- Creating a **Lot Alias** after successful manual bid fulfillment starts retroactive automatic matching.
- If alias creation conflicts after successful manual bid fulfillment, the bid remains applied and the alias is skipped.
- Renaming an **Auction Lot** clears its **Lot Aliases**.
- After renaming an **Auction Lot**, retroactive automatic matching uses the new lot name and the cleared alias set.
- A **Streamer** can define one active **Bid Reward Set** for placing bids.
- A **Bid Reward Set** has one trimmed, non-empty **Bid Reward Set Title**.
- A **Bid Reward Set** can have a **Bid Reward Description**, otherwise it uses a default viewer instruction.
- A **Bid Reward Set** contains one or more **Bid Reward Options**.
- A **Bid Reward Set's** **Bid Reward Options** must have unique positive **Bid Amounts**.
- A **Bid Reward Option** has one **Bid Amount**.
- **Bid Amount** is an integer channel points amount.
- The auction domain does not define its own maximum point amount beyond Twitch or technical limits.
- A **Bid Reward Option** can have a **Reward Color**, otherwise it uses a default color.
- A **Bid Reward Option's** Twitch reward title is formed from the reward set title and bid amount.
- Updating a **Bid Reward Set Title** or **Bid Reward Description** synchronizes all managed **Bid Reward Options**.
- Updating a **Bid Reward Set Title** rebuilds managed Twitch reward titles from the new title and each option's **Bid Amount**.
- A partial Twitch synchronization of a **Bid Reward Set** is a **Failed Reward Sync** until retry completes the desired set state.
- Failed **EventSub Subscription** cleanup is part of **Failed Reward Sync**.
- Creating a **Bid Reward Set** does not automatically make the **Auction List** active.
- Editing a **Bid Reward Set** synchronizes its existing Twitch rewards instead of creating duplicate rewards.
- **EventSub Subscriptions** are created and synchronized when managed **Bid Reward Options** are created or changed, not at first login.
- Each managed **Bid Reward Option** has its own **EventSub Subscription** scoped to that Twitch reward.
- Removing a **Bid Reward Option** disables its related Twitch reward.
- Successfully disabling or removing a **Bid Reward Option** removes its related **EventSub Subscription**.
- A removed or inactive **Bid Reward Option** is treated as removed or inactive only after Twitch confirms the disable.
- Saving **Bid Reward Set** changes while the **Auction List** is inactive keeps managed Twitch rewards disabled.
- Saving **Bid Reward Set** changes while the **Auction List** is active enables active managed Twitch rewards.
- Pending **Incoming Bids** do not block editing **Bid Reward Set** settings.
- **External Twitch Changes** are unsupported, but the application tolerates them by processing still-managed redemptions and retrying desired reward synchronization.
- Making the **Auction List** inactive disables its related Twitch rewards.
- Making the **Auction List** active enables its active **Bid Reward Options** on Twitch.
- Changing the **Auction List** between active and inactive becomes final only after Twitch reward enablement or disablement succeeds.
- An **Inactive Auction List** still allows the **Streamer** to process existing **Incoming Bids**.
- During pending or failed deactivation, redemptions from still-managed Twitch rewards become **Incoming Bids**.
- Updating a **Bid Reward Option** does not change the **Bid Amount** already recorded on existing bids.
- A **Bid Reward Option** requires viewer input.
- Only redemptions for the application's **Bid Reward Options** become **Incoming Bids**.
- A redemption for an inactive or removed **Bid Reward Option** is automatically refunded.
- During a **Failed Reward Sync**, a redemption for a still-managed Twitch reward becomes an **Incoming Bid** even if the desired state was to disable that option.
- An **Incoming Bid** can become an **Applying Bid**, **Applied Bid**, **Refunding Bid**, **Refunded Bid**, or **Failed Bid Action**.
- An **Incoming Bid** has one **Bid Amount**.
- An **Incoming Bid** has one **Bid Text**.
- **Bid Text** must be 1 to 250 characters after trimming.
- An **Invalid Bid** is automatically sent through the refund flow.
- A regular **Incoming Bid** can be handled by creating a new **Auction Lot**, applying it to an existing **Auction Lot**, or rejecting and refunding it.
- A **Failed Bid Action** can be retried but not manually forced into a final state.
- If a **Failed Bid Action's** target **Auction Lot** no longer exists at retry time, the bid returns to pending **Incoming Bid** state.
- If a failed create-lot action can no longer safely create its original lot at retry time, the bid returns to pending **Incoming Bid** state.
- Bids returned to pending **Incoming Bid** state participate in normal automatic matching.
- An **Externally Resolved Bid** is removed from the **Bid Queue** without changing auction state.
- An **Applied Bid** belongs to exactly one **Auction Lot**.
- An **Applied Bid** increases the **Auction Lot's** **Lot Amount** by its **Bid Amount**.
- One Twitch reward redemption can increase **Lot Amount** at most once, including retries.
- An **Applying Bid** becomes an **Applied Bid** and changes **Lot Amount** only after Twitch confirms fulfillment.
- A **Rejected Bid** is expected to become a **Refunded Bid** through Twitch.
- Applying an **Incoming Bid** to an **Auction Lot** fulfills the related Twitch reward redemption.
- Creating a new **Auction Lot** from an **Incoming Bid** fulfills the related Twitch reward redemption.
- Creating a new **Auction Lot** from an **Incoming Bid** uses the bid text trimmed of surrounding whitespace as the initial lot name.
- Creating a new **Auction Lot** from an **Incoming Bid** becomes final only after Twitch confirms fulfillment.
- A **Failed Bid Action** is not treated as an **Applied Bid** or **Refunded Bid** until the action later succeeds.

## Example dialogue

> **Dev:** "Do we need a user account for the viewer who placed this bid?"
> **Domain expert:** "No - only the Streamer signs in; the bid only needs the viewer's display name."

> **Dev:** "Can viewers see the current auction outside Twitch rewards?"
> **Domain expert:** "Yes - they can view the Public Auction Page without signing in."

> **Dev:** "Can public page requests see admin-only auction data?"
> **Domain expert:** "No - Public Auction View is read-only, rate limited, and contains only public fields."

> **Dev:** "Can public realtime updates include bid queue details?"
> **Domain expert:** "No - Public Auction Updates contain only Public Auction View fields."

> **Dev:** "Should the public page title be the Streamer's Twitch name?"
> **Domain expert:** "No - the public page shows the streamer-defined Auction Title."

> **Dev:** "Does the Streamer need to set an auction title before sharing the page?"
> **Domain expert:** "No - Auction Title defaults to `Point Auction`."

> **Dev:** "What happens if the Streamer clears the auction title?"
> **Domain expert:** "A blank Auction Title falls back to `Point Auction`."

> **Dev:** "Do we need a separate OBS overlay for the first release?"
> **Domain expert:** "No - the Public Auction Page is the viewer-facing surface."

> **Dev:** "Should viewers see bids that the Streamer has not handled yet?"
> **Domain expert:** "No - pending Incoming Bids are visible only in admin workflows."

> **Dev:** "Should completed bids stay in the working queue?"
> **Domain expert:** "No - the Bid Queue only shows pending Incoming Bids and Failed Bid Actions."

> **Dev:** "How does the admin workbench receive new bids without refresh?"
> **Domain expert:** "It uses authenticated Admin Auction Updates over server-sent events."

> **Dev:** "Should a bid action HTTP request wait for every Twitch result?"
> **Domain expert:** "No - Twitch-dependent Commands return in-progress and final outcomes arrive through Admin Auction Updates."

> **Dev:** "Does the first release need a completed bid history screen?"
> **Domain expert:** "No - completed bid history is not part of the first admin workflow."

> **Dev:** "What URL should the Streamer share with viewers?"
> **Domain expert:** "A URL based on the streamer's Public Auction Slug."

> **Dev:** "Does the public page exist before the Streamer creates bid rewards?"
> **Domain expert:** "Yes - it shows an empty list and closed bidding."

> **Dev:** "If the Streamer changes Twitch login, should the old public URL keep working?"
> **Domain expert:** "No - the Public Auction Slug follows the current Twitch login."

> **Dev:** "When does the Public Auction Slug update?"
> **Domain expert:** "Successful Twitch sign-in updates it to the current Twitch login."

> **Dev:** "What if two streamers appear to have the same Public Auction Slug?"
> **Domain expert:** "That is a data integrity error, not a user-resolvable flow."

> **Dev:** "Should viewers see Lot Aliases?"
> **Domain expert:** "No - the Public Auction Page shows lot names, Lot Amounts, and Supporters."

> **Dev:** "Should viewers know whether bids are currently accepted?"
> **Domain expert:** "Yes - the Public Auction Page shows a small open or closed bidding status."

> **Dev:** "Should viewers refresh the page to see new bids?"
> **Domain expert:** "No - the Public Auction Page should update near real time."

> **Dev:** "Does inactive hide the public auction?"
> **Domain expert:** "No - inactive only prevents new Twitch bids; the Public Auction Page remains visible."

> **Dev:** "Can a Twitch moderator manage the auction in this application?"
> **Domain expert:** "No - only the Streamer who owns the channel can access the admin workflows."

> **Dev:** "Can the Streamer connect Twitch for login first and grant auction permissions later?"
> **Domain expert:** "No - the Twitch Connection grants all permissions needed by the auction module."

> **Dev:** "Do we need the Streamer's Twitch email?"
> **Domain expert:** "No - Twitch Connection does not request email access."

> **Dev:** "Can the browser be responsible for Twitch tokens used to fulfill bids?"
> **Domain expert:** "No - Twitch Token Custody belongs to the backend so server-side actions can run offline."

> **Dev:** "Should the frontend ever receive Twitch access or refresh tokens?"
> **Domain expert:** "No - the frontend uses the application session, and Twitch tokens stay in the backend."

> **Dev:** "What authenticates admin browser requests?"
> **Domain expert:** "An Application Session authenticates admin requests, not frontend-held Twitch tokens."

> **Dev:** "Can the Streamer sign in with an application account separate from Twitch?"
> **Domain expert:** "No - application login is only through Twitch Connection."

> **Dev:** "What exists after the Streamer's first Twitch login?"
> **Domain expert:** "The Streamer record, current Auction List, and Public Auction Slug exist immediately."

> **Dev:** "Can one Streamer manage multiple Twitch channels here?"
> **Domain expert:** "No - one Streamer manages exactly one Twitch channel."

> **Dev:** "Does logging out delete the streamer's lots or revoke Twitch access?"
> **Domain expert:** "No - Log Out only ends the current application session."

> **Dev:** "If Twitch connection breaks, does the Auction List become inactive?"
> **Domain expert:** "No - active or inactive is the Streamer's chosen auction state, separate from connection health."

> **Dev:** "How does the Streamer recover from expired Twitch authorization?"
> **Domain expert:** "They sign in with Twitch again; there is no separate connection management screen."

> **Dev:** "If enabling or disabling rewards fails, does the Auction List state still change?"
> **Domain expert:** "No - changing active or inactive becomes final only after Twitch reward enablement or disablement succeeds."

> **Dev:** "When the streamer clears the auction, do we close a round and create an archive?"
> **Domain expert:** "No - there is only one current Auction List, and clearing it removes its current lots."

> **Dev:** "What changes when the Auction List becomes inactive?"
> **Domain expert:** "The lots and incoming bids remain unchanged, but Twitch rewards are disabled so viewers cannot place new bids."

> **Dev:** "Can the Streamer handle bids that arrived before the Auction List became inactive?"
> **Domain expert:** "Yes - inactive prevents new viewer bids but does not block processing existing Incoming Bids."

> **Dev:** "What if a viewer redeems while Twitch rewards are still enabled during deactivation?"
> **Domain expert:** "The redemption becomes an Incoming Bid because it came from a still-managed Twitch reward."

> **Dev:** "Can a brand-new Auction List accept bids immediately?"
> **Domain expert:** "No - a new Auction List starts inactive."

> **Dev:** "Can the Streamer open bidding before creating reward options?"
> **Domain expert:** "No - an Auction List can become active only with at least one active Bid Reward Option."

> **Dev:** "Does a failed reward sync block opening bidding?"
> **Domain expert:** "No - if at least one managed active option exists, bidding can open and bids use the reward data received from Twitch."

> **Dev:** "What happens if the Streamer removes the last active bid option?"
> **Domain expert:** "The Auction List becomes inactive because bidding can no longer be opened."

> **Dev:** "Can the Streamer prepare lots before opening bidding?"
> **Domain expert:** "Yes - manual lot management is available regardless of active state or reward configuration."

> **Dev:** "Which lot appears first?"
> **Domain expert:** "Lots are displayed by Lot Amount descending, then by lot name ascending."

> **Dev:** "Is this lot's weight a ranking multiplier?"
> **Domain expert:** "No - use Lot Amount; it is the channel points total assigned to the Auction Lot."

> **Dev:** "Can point amounts contain decimals?"
> **Domain expert:** "No - Lot Amount and Bid Amount are integer channel points amounts."

> **Dev:** "Does the auction define its own maximum point amount?"
> **Domain expert:** "No - maximums come from Twitch or technical limits, not the auction domain."

> **Dev:** "Can a lot have an empty name?"
> **Domain expert:** "No - an Auction Lot name must be 1 to 250 characters after trimming."

> **Dev:** "Must Lot Amount always equal the sum of applied bids?"
> **Domain expert:** "No - the Streamer can edit Lot Amount directly, so it is the current displayed amount rather than a strict calculated total."

> **Dev:** "Can the Streamer set Lot Amount below the applied bid total?"
> **Domain expert:** "Yes, as long as the manual Lot Amount is not negative."

> **Dev:** "Can the Streamer change a bid's amount while applying it?"
> **Domain expert:** "No - an Applied Bid increases Lot Amount by the fixed Bid Amount from the Twitch reward."

> **Dev:** "If retry runs after a Twitch error, can the same bid add its amount again?"
> **Domain expert:** "No - one Twitch reward redemption can affect Lot Amount at most once."

> **Dev:** "Should Lot Amount change before Twitch confirms fulfillment?"
> **Domain expert:** "No - the bid is Applying until Twitch confirms fulfillment, then it becomes Applied and changes Lot Amount."

> **Dev:** "Should a new lot from a bid appear before Twitch confirms fulfillment?"
> **Domain expert:** "No - creating a lot from a bid becomes final only after Twitch confirms fulfillment."

> **Dev:** "If a bid says `Shrek`, does it match ` shrek `?"
> **Domain expert:** "Yes - lot matching trims, collapses repeated spaces, and ignores case."

> **Dev:** "If the Streamer manually attaches `Шрек` to the `Shrek` lot, what happens next time a viewer bids `Шрек`?"
> **Domain expert:** "The bid matches the existing Auction Lot through its Lot Alias."

> **Dev:** "Does every manual attachment create a Lot Alias?"
> **Domain expert:** "No - only new normalized text becomes a Lot Alias."

> **Dev:** "When does a manually learned Lot Alias become active?"
> **Domain expert:** "Only after Twitch confirms fulfillment for the manually applied bid."

> **Dev:** "What happens after a manual apply successfully creates a Lot Alias?"
> **Domain expert:** "Retroactive automatic matching starts for pending Incoming Bids that match the new alias."

> **Dev:** "What if the learned alias conflicts after the bid was already fulfilled?"
> **Domain expert:** "The bid remains applied, and the conflicting alias is skipped."

> **Dev:** "Can two lots both match the same bid text?"
> **Domain expert:** "No - normalized lot names and aliases are unique across one Auction List."

> **Dev:** "Does the Streamer need to confirm an automatic match?"
> **Domain expert:** "No - an automatic match starts the apply flow immediately."

> **Dev:** "If a lot is created after a matching bid is already pending, does the bid stay pending?"
> **Domain expert:** "No - pending Incoming Bids can be automatically matched when lot names or aliases change."

> **Dev:** "Does changing Lot Amount rerun matching?"
> **Domain expert:** "No - retroactive matching runs only after lot names or aliases change."

> **Dev:** "If five pending bids now match a new lot, does only one apply?"
> **Domain expert:** "No - all matching pending Incoming Bids start the apply flow."

> **Dev:** "Does creating or renaming a lot wait for all retroactive Twitch fulfillments?"
> **Domain expert:** "No - the local lot change becomes visible immediately, and matched bid applications continue asynchronously."

> **Dev:** "If applying `Шрек` to `Shrek` learns an alias, what happens to other pending `Шрек` bids?"
> **Domain expert:** "They automatically match the same Auction Lot and start the apply flow."

> **Dev:** "Does adding an initial Supporter change matching for a new manual lot?"
> **Domain expert:** "No - the new lot name still triggers retroactive automatic matching."

> **Dev:** "When a lot is renamed, should the old name keep matching?"
> **Domain expert:** "No - renaming clears Lot Aliases because the Streamer may be changing the lot's meaning."

> **Dev:** "After rename, do pending bids match old aliases before they are cleared?"
> **Domain expert:** "No - matching uses the new lot name and the cleared alias set."

> **Dev:** "Should rename show a preview before applying matching pending bids?"
> **Domain expert:** "No - rename-triggered matches start the apply flow without preview or confirmation."

> **Dev:** "Can the Streamer type arbitrary aliases into a lot?"
> **Domain expert:** "No - aliases are learned by manually applying bids, but the Streamer can remove them."

> **Dev:** "Is `Ставка 1000` a separate feature from `Ставка 2000`?"
> **Domain expert:** "No - they are Bid Reward Options inside the same Bid Reward Set."

> **Dev:** "Can a reward set have no title?"
> **Domain expert:** "No - a Bid Reward Set Title is required and non-empty after trimming."

> **Dev:** "Does the Streamer need to write instructions for viewers?"
> **Domain expert:** "No - Bid Reward Description is optional and falls back to a default instruction."

> **Dev:** "If the Streamer changes the set title or description, do existing Twitch rewards update?"
> **Domain expert:** "Yes - shared set fields synchronize across all managed Bid Reward Options."

> **Dev:** "If `Ставка` becomes `Movie bid`, what happens to `Ставка: 1000`?"
> **Domain expert:** "The managed Twitch reward title becomes `Movie bid: 1000`."

> **Dev:** "If some Twitch rewards update but one fails, is the set saved?"
> **Domain expert:** "No - it is a Failed Reward Sync until retry completes the desired set state."

> **Dev:** "If a reward is disabled but subscription cleanup fails, is sync complete?"
> **Domain expert:** "No - failed EventSub Subscription cleanup is part of Failed Reward Sync."

> **Dev:** "Can the same reward set contain two 1000-point options?"
> **Domain expert:** "No - Bid Reward Option amounts are unique and positive within a set."

> **Dev:** "Must the Streamer choose a color for every option?"
> **Domain expert:** "No - Reward Color is optional and falls back to a default."

> **Dev:** "What should viewers see as the Twitch reward title?"
> **Domain expert:** "A Bid Reward Option title combines the set title and amount, such as `Ставка: 1000`."

> **Dev:** "Does creating rewards immediately open bidding?"
> **Domain expert:** "No - the Streamer must explicitly make the Auction List active."

> **Dev:** "Do we create EventSub subscriptions immediately after Twitch login?"
> **Domain expert:** "No - EventSub Subscriptions are created and synchronized with managed Bid Reward Options."

> **Dev:** "Do we subscribe to all channel point redemptions for a channel?"
> **Domain expert:** "No - each managed Bid Reward Option has its own EventSub Subscription scoped to that Twitch reward."

> **Dev:** "What happens to a reward's EventSub Subscription when its option is removed?"
> **Domain expert:** "After the option is successfully disabled or removed, its EventSub Subscription is removed."

> **Dev:** "When an option is removed, should the Twitch reward disappear entirely?"
> **Domain expert:** "No - its Twitch reward is disabled so existing history remains understandable."

> **Dev:** "If rewards are edited while bidding is closed, do they become visible on Twitch?"
> **Domain expert:** "No - saving reward changes while the Auction List is inactive keeps managed Twitch rewards disabled."

> **Dev:** "If rewards are edited while bidding is open, should active options stay available?"
> **Domain expert:** "Yes - saving reward changes while the Auction List is active enables active managed Twitch rewards."

> **Dev:** "If the Streamer changes an option from 1000 to 1500, do old bids become 1500?"
> **Domain expert:** "No - each bid keeps the Bid Amount captured when it was placed."

> **Dev:** "Can reward settings change while bids are waiting in the queue?"
> **Domain expert:** "Yes - pending Incoming Bids keep their captured Bid Amount and do not block reward edits."

> **Dev:** "Should the application support editing managed rewards directly in Twitch?"
> **Domain expert:** "No - External Twitch Changes are unsupported, but the application tolerates them where possible."

> **Dev:** "Can a viewer place a bid without naming anything?"
> **Domain expert:** "No - Bid Reward Options require viewer input, and that input becomes the Bid Text."

> **Dev:** "Do all channel points rewards become Incoming Bids?"
> **Domain expert:** "No - only redemptions for Bid Reward Options managed by the application become Incoming Bids."

> **Dev:** "What if Twitch sends a redemption for an option the Streamer just removed?"
> **Domain expert:** "It is automatically refunded because the option is no longer active for bidding."

> **Dev:** "What if disabling an option failed and Twitch still lets a viewer redeem it?"
> **Domain expert:** "If the reward is still managed by the application, the redemption becomes an Incoming Bid."

> **Dev:** "When does a removed option stop being valid for bids?"
> **Domain expert:** "Only after Twitch confirms that its reward was disabled."

> **Dev:** "Should the Streamer decide what to do with an empty bid?"
> **Domain expert:** "No - invalid Bid Text is automatically refunded."

> **Dev:** "What choices does the Streamer have for an unmatched Incoming Bid?"
> **Domain expert:** "Create a new Auction Lot, apply it to an existing Auction Lot, or reject and refund it."

> **Dev:** "Is a rejected bid the same thing as a refunded bid?"
> **Domain expert:** "No - rejection is the streamer's domain decision; refunded means Twitch confirmed that channel points were returned."

> **Dev:** "When the Streamer applies a bid to a lot, does the reward stay pending in Twitch?"
> **Domain expert:** "No - applying a bid or creating a lot from it fulfills the Twitch reward redemption."

> **Dev:** "Does creating a lot from a bid open an edit form first?"
> **Domain expert:** "No - the new lot uses the trimmed bid text as its initial name and can be renamed later."

> **Dev:** "If Twitch does not confirm fulfill or refund, should the bid look final?"
> **Domain expert:** "No - it should stay in an in-progress or failed action state until it can be retried or resolved."

> **Dev:** "Can the Streamer force a failed action to look applied or refunded?"
> **Domain expert:** "No - a Failed Bid Action can only be retried."

> **Dev:** "What if a failed apply targets a lot that was deleted before retry?"
> **Domain expert:** "The bid returns to pending Incoming Bid state for a new decision."

> **Dev:** "What if a failed create-lot retry now conflicts with a lot that appeared later?"
> **Domain expert:** "The bid returns to pending Incoming Bid state for a new decision."

> **Dev:** "Can a bid returned to pending auto-match like other pending bids?"
> **Domain expert:** "Yes - returned pending bids participate in normal automatic matching."

> **Dev:** "What if Twitch says the redemption was already finalized outside the application?"
> **Domain expert:** "The bid is treated as Externally Resolved and removed from the Bid Queue without changing auction state."

> **Dev:** "Should we call viewers who contributed to a lot investors?"
> **Domain expert:** "No - they are Supporters; they do not own anything, we only record their display names."

> **Dev:** "If the same viewer supports Shrek twice, do they appear twice?"
> **Domain expert:** "No - the Lot Amount increases again, but the viewer appears once as a Supporter."

> **Dev:** "Are `Viewer`, `viewer`, and ` viewer ` three Supporters?"
> **Domain expert:** "No - Supporter uniqueness is trimmed and case-insensitive, while the displayed spelling can be preserved."

> **Dev:** "Can the Streamer correct the Supporters list manually?"
> **Domain expert:** "Yes - the Streamer can add or remove Supporters on a lot."

> **Dev:** "If a lot is deleted, do its applied bids get refunded?"
> **Domain expert:** "No - deleting a lot only removes it from the current Auction List."

> **Dev:** "Does clearing the Auction List refund viewers?"
> **Domain expert:** "No - clearing removes current lots and does not change already fulfilled bids."

> **Dev:** "Does clearing the Auction List discard incoming bids waiting for a decision?"
> **Domain expert:** "No - pending Incoming Bids remain for the Streamer to handle."

> **Dev:** "Can a Supporter only come from Twitch?"
> **Domain expert:** "No - the Streamer can enter one Supporter manually when creating a lot."

> **Dev:** "Do manual Supporters behave differently from bid-derived Supporters?"
> **Domain expert:** "No - all Supporters follow the same rules and are not distinguished."

> **Dev:** "If the Streamer removes a Supporter, can a later bid add that name again?"
> **Domain expert:** "Yes - a later applied bid can add the Supporter again."

## Flagged ambiguities

- "User" was used broadly; resolved: the application's signed-in user is the **Streamer**, while the **Viewer** is external to the application.
- "Investor" was considered for viewers attached to a lot; resolved: use **Supporter** because no ownership or financial investment is implied.
- "Supporter" was first defined as only a viewer display name from Twitch; resolved: a **Supporter** can also be entered manually by the Streamer.
- "Bid" was overloaded; resolved: use **Incoming Bid**, **Applied Bid**, **Rejected Bid**, and **Refunded Bid** for the bid lifecycle states.
- "Bit" conflicts with Twitch Bits; resolved: use **Bid** for auction stakes.
