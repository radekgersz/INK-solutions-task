package app.technical;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Component
public class DocumentsCatalog {
    private final List<Document> documents = new ArrayList<>();

    public DocumentsCatalog() {
        // ---------------------------------------------------------
        // DOC 1: API Authentication
        // ---------------------------------------------------------
        documents.add(new Document(
                "doc_auth_01",
                "API Authentication & Security",
                "Authentication relies on OAuth 2.0. " +
                        "User-facing apps must use the Authorization Code flow to exchange a code for an access token. " +
                        "Once obtained, the token must be sent in the 'Authorization' header with the prefix 'Bearer '. " +
                        "Server-to-server communication should use static x-api-key headers. " +
                        "Tokens expire after 1 hour; use Refresh Tokens (valid for 14 days) to renew them.",
                Arrays.asList("auth", "oauth", "token", "bearer", "login", "key", "refresh", "security")
        ));

        // ---------------------------------------------------------
        // DOC 2: WebSockets
        // ---------------------------------------------------------
        documents.add(new Document(
                "doc_ws_01",
                "WebSocket Protocol",
                "Establish connections via the HTTP handshake at wss://api.example.com/v1/stream with header 'Upgrade: websocket'. " +
                        "To keep connections alive, the server sends a 'ping' frame every 30 seconds; clients must respond with 'pong' within 10 seconds. " +
                        "All payloads must be JSON strings. Binary frames cause immediate disconnection (Code 1003). " +
                        "Reconnect using exponential backoff starting at 1s.",
                Arrays.asList("websocket", "wss", "stream", "real-time", "heartbeat", "ping", "pong", "json")
        ));

        // ---------------------------------------------------------
        // DOC 3: Rate Limits & Errors
        // ---------------------------------------------------------
        documents.add(new Document(
                "doc_limits_01",
                "Rate Limits and Error Codes",
                "The API enforces a limit of 60 requests per minute per client IP. " +
                        "Exceeding this returns a 429 Too Many Requests status. " +
                        "Common errors include: 400 (Bad Request - malformed JSON), " +
                        "401 (Unauthorized - missing/invalid token), " +
                        "403 (Forbidden - valid token but insufficient permissions), " +
                        "and 500 (Internal Server Error - please contact support).",
                Arrays.asList("rate", "limit", "429", "error", "400", "401", "403", "500", "status")
        ));

        // ---------------------------------------------------------
        // DOC 4: SQL Syntax & Database Management
        // ---------------------------------------------------------
        documents.add(new Document(
                "doc_sql_01",
                "SQL Syntax & Database Management",
                "Direct database access is available via the read-only replica endpoint. " +
                        "We support standard ANSI SQL syntax (PostgreSQL dialect). " +
                        "Complex queries must use explicit JOINs; implicit joins are blocked for performance. " +
                        "Full-table scans taking longer than 5 seconds will be automatically terminated. " +
                        "All transactions must be ACID compliant. " +
                        "Always use prepared statements (parameterized queries) to prevent SQL injection vulnerabilities.",
                Arrays.asList("sql", "database", "query", "join", "select", "replica", "performance", "injection", "prepared")
        ));
    }
}