# Modular monolith for the Point Auction MVP

The first release is built as a modular monolith: one backend deployable and one frontend application, organized around bounded contexts and vertical slices. This keeps delivery simple while preserving clear boundaries for pointauction/auction, pointauction/bid, pointauction/rewards, Twitch Integration, Streamer Identity, pointauction/publicview, Admin Experience, and Update Delivery.

**Consequences**

- Context boundaries are enforced by package/module structure and interfaces, not by network calls between services.
- The top-level app package is the composition root for Ktor configuration, routing registration, dependency wiring, sessions, serialization, error handling, and job startup.
- Domain modules may not depend on the app package.
- Backend packages are organized context-first, then by domain/application/web/persistence concerns inside each context.
- Admin is a delivery surface, not a top-level backend package; admin routes and projections live inside the owning module or subcontext.
- pointauction/auction and pointauction/bid are separate backend packages because current-list rules and Twitch redemption lifecycle rules have different consistency centers.
- The Point Auction backend subpackage names are exactly `pointauction/auction`, `pointauction/bid`, `pointauction/rewards`, and `pointauction/publicview`.
- Lot matching belongs to pointauction/auction; pointauction/bid requests matching through an explicit interface instead of owning name, alias, or normalization rules.
- pointauction/bid applies confirmed bid outcomes through Point Auction application interfaces and never writes Auction Lot state directly.
- The application uses one physical database, but persistence code and table ownership are organized by bounded context rather than through a global repository layer.
- Cross-context persistence access is not allowed for writes; cross-context reads are allowed only through explicit projection/read-model boundaries.
- The shared package is limited to boring technical primitives; shared domain models and business utilities are not allowed.
- Vertical slices live inside their owning context and cut through command/query handling, persistence, HTTP/SSE adapters, and tests where needed.
- Simple command/query slices may be single files; complex slices get their own folder only when the use case needs several collaborating files.
- Backend tests live in the standard test source set but mirror the same context-first package structure as production code.
- Cross-context calls should use explicit application interfaces rather than reaching into another context's persistence or domain internals.
- Services can be extracted later only if operational pressure justifies the extra deployment and integration complexity.
