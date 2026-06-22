
# Transaction Graph Explorer

Run:
mvn spring-boot:run

Endpoint:
GET /api/graph/nodes/{id}

Features:
- JSON loading on startup
- nodeById and childrenByParentId maps
- level calculation
- parent chain
- direct children
- next-level transactions
- orphan parent handling
- cycle detection
- 404 error response
