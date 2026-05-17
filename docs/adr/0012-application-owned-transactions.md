# Application-owned transaction boundaries

Application command handlers own transaction boundaries. Repositories and persistence adapters participate in the caller's transaction instead of silently opening independent transactions for each operation.

**Consequences**

- A vertical slice can atomically change domain state and enqueue outbox/job work in one transaction.
- Repository interfaces should accept or otherwise use the current transaction/session context provided by the application layer.
- Query handlers may use read-only transactions where useful, but persistence code should not hide transaction boundaries from the use case.
