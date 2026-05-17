# Framework-free domain model

Domain models and domain services do not depend on Ktor, Exposed, kotlinx.serialization DTO annotations, Twitch DTOs, or HTTP/database framework types. Framework-specific mapping belongs in web, persistence, and Twitch Integration adapters.

**Consequences**

- Domain rules can be tested without booting Ktor, connecting to a database, or constructing Twitch API payloads.
- Domain and application code use typed IDs and value objects; raw UUIDs and strings are mapped at HTTP, persistence, and Twitch adapter boundaries.
- Expected domain and application outcomes use explicit result/error types; exceptions are reserved for unexpected failures.
- API DTOs, persistence rows, and Twitch DTOs are mapped at context boundaries.
- Domain code should speak in project language such as Auction List, Auction Lot, Lot Amount, Incoming Bid, and Bid Reward Option rather than framework or external API terms.
