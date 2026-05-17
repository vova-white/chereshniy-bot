# Frontend module-local state

Frontend state is organized by module and workflow rather than in one global application store. Point Auction owns its API clients, realtime clients, view models, Workbench state, Rewards Settings state, and Public Auction Page state inside the module.

**Consequences**

- Frontend source is organized product-module-first: app shell/session/router, modules/dashboard, modules/point-auction, and shared generic UI/http/formatting.
- Frontend routes are `/dashboard`, `/point-auction`, `/point-auction/rewards`, and public `/auction/:slug`.
- `app/session` owns session loading and route guards; admin modules assume authenticated context, while the public auction page does not require session.
- Dashboard, Point Auction Workbench, Rewards Settings, and Public Auction Page can evolve independently.
- Shared frontend code should stay generic UI, HTTP, formatting, or session plumbing rather than domain state.
- Shared UI primitives come from shadcn-vue source components styled with TailwindCSS and live under `src/shared/ui`.
- Shared frontend helpers live under `src/shared/lib`.
- Point Auction frontend code is organized by user workflows such as Workbench, Rewards Settings, and Public Auction Page rather than mirroring backend subcontexts one-to-one.
- Frontend API DTOs are written manually beside module API clients for the MVP; generated API clients can be reconsidered when API size or contract churn justifies them.
- Pinia or composables may be used per module/workflow, but there is no single global store for all application data.
