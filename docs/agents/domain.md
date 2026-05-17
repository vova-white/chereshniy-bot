# Domain Docs

How the engineering skills should consume this repo's domain documentation when exploring the codebase.

This repo uses a multi-context domain documentation layout.

## Before exploring, read these

- **`CONTEXT-MAP.md`** at the repo root. It routes each task to the relevant context docs.
- **`backend/CONTEXT.md`** for backend bot logic, integrations, persistence, scheduling, and server-side behavior.
- **`frontend/CONTEXT.md`** for frontend UI, Vite+ tooling, browser-facing workflows, and client-side behavior.
- **`docs/adr/`** for system-wide architectural decisions.
- **`backend/docs/adr/`** for backend-specific decisions.
- **`frontend/docs/adr/`** for frontend-specific decisions.

For cross-cutting changes, read every relevant context.

If any of these files don't exist, **proceed silently**. Don't flag their absence; don't suggest creating them upfront. The producer skill (`/grill-with-docs`) creates them lazily when terms or decisions actually get resolved.

## File structure

Multi-context repo:

```text
/
|-- CONTEXT-MAP.md
|-- docs/
|   `-- adr/
|-- backend/
|   |-- CONTEXT.md
|   `-- docs/
|       `-- adr/
`-- frontend/
    |-- CONTEXT.md
    `-- docs/
        `-- adr/
```

## Use the glossary's vocabulary

When your output names a domain concept in an issue title, refactor proposal, hypothesis, or test name, use the term as defined in the relevant `CONTEXT.md`. Don't drift to synonyms the glossary explicitly avoids.

If the concept you need isn't in the glossary yet, that's a signal: either you're inventing language the project doesn't use, or there's a real gap to note for `/grill-with-docs`.

## Flag ADR conflicts

If your output contradicts an existing ADR, surface it explicitly rather than silently overriding:

> Contradicts ADR-0007 (event-sourced orders), but worth reopening because...
