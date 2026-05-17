# backend

This project was created using the [Ktor Project Generator](https://start.ktor.io).

Here are some useful links to get you started:

* [Ktor Documentation](https://ktor.io/docs/home.html)
* [Ktor GitHub page](https://github.com/ktorio/ktor)
* [Ktor Slack chat](https://app.slack.com/client/T09229ZC6/C0A974TJ9). [Request an invite](https://surveys.jetbrains.com/s3/kotlin-slack-sign-up).

## Features

Here's a list of features included in this project:

| Name                                                                                  | Description                                                                        |
|---------------------------------------------------------------------------------------|------------------------------------------------------------------------------------|
| [AsyncAPI](https://start.ktor.io/p/com.asyncapi/server-asyncapi)                      | Generates and serves AsyncAPI documentation                                        |
| [CORS](https://start.ktor.io/p/io.ktor/server-cors)                                   | Enables Cross-Origin Resource Sharing (CORS)                                       |
| [Compression](https://start.ktor.io/p/io.ktor/server-compression)                     | Compresses responses using encoding algorithms like GZIP                           |
| [Default Headers](https://start.ktor.io/p/io.ktor/server-default-headers)             | Adds a default set of headers to HTTP responses                                    |
| [Forwarded Headers](https://start.ktor.io/p/io.ktor/server-forwarded-header-support)  | Allows handling proxied headers (X-Forwarded-*)                                    |
| [HttpsRedirect](https://start.ktor.io/p/io.ktor/server-https-redirect)                | Redirects insecure HTTP requests to the respective HTTPS endpoint                  |
| [Swagger](https://start.ktor.io/p/io.ktor/server-swagger)                             | Serves Swagger UI for your project                                                 |
| [Authentication](https://start.ktor.io/p/io.ktor/server-auth)                         | Provides extension point for handling the Authorization header                     |
| [Authentication OAuth](https://start.ktor.io/p/io.ktor/server-auth-oauth)             | Handles OAuth Bearer authentication scheme                                         |
| [CSRF](https://start.ktor.io/p/io.ktor/server-csrf)                                   | Cross-site request forgery mitigation                                              |
| [Content Negotiation](https://start.ktor.io/p/io.ktor/server-content-negotiation)     | Provides automatic content conversion according to Content-Type and Accept headers |
| [kotlinx.serialization](https://start.ktor.io/p/io.ktor/server-kotlinx-serialization) | Handles JSON serialization using kotlinx.serialization library                     |
| [Sessions](https://start.ktor.io/p/io.ktor/server-sessions)                           | Adds support for persistent sessions through cookies or headers                    |
| [AutoHeadResponse](https://start.ktor.io/p/io.ktor/server-auto-head-response)         | Provides automatic responses for HEAD requests                                     |
| [Request Validation](https://start.ktor.io/p/io.ktor/server-request-validation)       | Adds validation for incoming requests                                              |
| [Server-Sent Events (SSE)](https://start.ktor.io/p/io.ktor/server-sse)                | Support for server push events                                                     |
| [Status Pages](https://start.ktor.io/p/io.ktor/server-status-pages)                   | Provides exception handling for routes                                             |
| [Call Logging](https://start.ktor.io/p/io.ktor/server-call-logging)                   | Logs client requests                                                               |
| [Call ID](https://start.ktor.io/p/io.ktor/server-callid)                              | Allows to identify a request/call.                                                 |
| [Exposed](https://start.ktor.io/p/org.jetbrains/server-exposed)                       | Adds Exposed database to your application                                          |
| [Rate Limiting](https://start.ktor.io/p/io.github.flaxoos/server-rate-limiting)       | Manage request rate limiting as you see fit                                        |
| [WebSockets](https://start.ktor.io/p/io.ktor/server-websockets)                       | Adds WebSocket protocol support for bidirectional client connections               |

## Building & Running

To build or run the project, use one of the following tasks:

| Task | Description |
|------|-------------|

If the server starts successfully, you'll see the following output:

```
2024-12-04 14:32:45.584 [main] INFO  Application - Application started in 0.303 seconds.
2024-12-04 14:32:45.682 [main] INFO  Application - Responding at http://0.0.0.0:8080
```
