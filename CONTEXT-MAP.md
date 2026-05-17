# Context Map

## Contexts

- [Backend](./backend/CONTEXT.md) - owns Twitch integration, auction rules, persistence, authentication, and server-side behavior.
- [Frontend](./frontend/CONTEXT.md) - owns browser-facing admin workflows and UI language.

## Relationships

- **Frontend -> Backend**: the frontend presents admin workflows for concepts owned by the backend domain.
