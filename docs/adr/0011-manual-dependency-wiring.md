# Manual dependency wiring

The MVP uses explicit manual dependency wiring in the app composition root rather than a dependency injection framework. The project is still small enough that clear constructor dependencies and context interfaces are more valuable than introducing a container.

**Consequences**

- Dependencies are assembled in the app package and passed into modules explicitly.
- Domain and application code should declare dependencies through interfaces where boundaries matter.
- A DI framework can be reconsidered later if wiring complexity becomes a real maintenance problem.
