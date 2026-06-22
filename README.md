# Transaction Graph Project

## Overview

Transaction Graph Project is a Spring Boot application that loads a hierarchical graph of transaction nodes from a JSON file and exposes REST APIs to retrieve node information, hierarchy details, transactions, and aggregate statistics.

The application maintains the graph entirely in memory for fast traversal and lookup operations.

---

## Features

### Core Features

* Load graph data from `transactions-graph-nodes.json`
* In-memory graph representation
* Retrieve graph node details by node ID
* Calculate node hierarchy level
* Return complete parent chain
* Return direct child nodes
* Return node transactions
* Return next-level transactions (transactions from direct children)
* Detect root and leaf nodes
* Gracefully handle orphan nodes (missing parent)
* Depth-limited subtree traversal
* Recursive children tree generation
* Cycle detection using visited nodes
* Aggregate information by level
* Subtree statistics

---

## Technology Stack

* Java 17+
* Spring Boot 3.x
* Maven

---

## Project Structure

```text
src
├── controller
│   └── GraphController
├── service
│   ├── GraphService
│   └── GraphLoaderService
├── repository
│   └── GraphRepository
├── model
│   ├── GraphNode
│   ├── Transaction
│   └── DTOs
├── validator
├── exception
├── constant
└── resources
    └── transactions-graph-nodes.json
```

---

## Data Loading

During application startup:

1. Read `transactions-graph-nodes.json`
2. Deserialize using Jackson
3. Build in-memory indexes

### Primary Indexes

```java
Map<String, GraphNode> nodeById
Map<String, List<GraphNode>> childrenByParentId
```

These indexes allow O(1) node lookup and efficient hierarchy traversal.

---

## API

### Get Node Details

```http
GET /api/graph/nodes/{id}
```

Example:

```http
GET /api/graph/nodes/N1
```

Response:

```json
{
  "id": "N1",
  "name": "Parent Node",
  "accountNumber": "123456",
  "level": 0,
  "isRoot": true,
  "isLeaf": false,
  "parentChain": [],
  "children": [],
  "transactions": [],
  "nextLevelTransactions": []
}
```

---

### Get Node Details With Depth

```http
GET /api/graph/nodes/{id}?maxDepth=3
```

Returns nested child hierarchy up to the requested depth.

---

## Level Calculation

Level definition:

| Condition      | Level            |
| -------------- | ---------------- |
| Parent is null | 0                |
| Parent missing | 0                |
| Otherwise      | Parent Level + 1 |

Example:

```text
N1
├── N2
│   └── N4
└── N3
```

| Node | Level |
| ---- | ----- |
| N1   | 0     |
| N2   | 1     |
| N3   | 1     |
| N4   | 2     |

---

## Root Node Rules

A node is considered root when:

```java
parentId == null
```

or

```java
parent node does not exist
```

Example:

```json
{
  "id": "N10",
  "parentId": "UNKNOWN"
}
```

Result:

```json
{
  "isRoot": true,
  "level": 0,
  "parentChain": []
}
```

---

## Leaf Node Rules

A node is a leaf node when:

```java
childrenByParentId.get(nodeId).isEmpty()
```

or no children exist.

---

## Aggregate Information

Aggregate information is calculated for the subtree rooted at the requested node.

Each level contains:

```json
{
  "level": 0,
  "nodeCount": 1,
  "transactionCount": 5,
  "totalTxAmount": 12500.00
}
```

### Total Amount Definition

Total amount is calculated as:

```text
Sum of absolute transaction amounts
```

for all transactions belonging to nodes at the same subtree level.

Example:

```text
100 + (-50) + 200 = 350
```

---

## Error Handling

### Node Not Found

HTTP 404

```json
{
  "error": "NODE_NOT_FOUND",
  "message": "Graph node N999 does not exist"
}
```

### Invalid Depth

HTTP 400

```json
{
  "error": "INVALID_DEPTH",
  "message": "maxDepth must be between 0 and 10"
}
```

---

## Cycle Protection

Although the graph is expected to be a tree structure, cycle protection is implemented.

Example:

```text
N1 -> N2 -> N3 -> N1
```

Traversal uses a visited set to avoid infinite recursion.

---

## Running the Application

### Clone Repository

```bash
git clone <repository-url>
```

### Build

```bash
mvn clean install
```

### Run

```bash
mvn spring-boot:run
```

Application starts on:

```text
http://localhost:8080
```

---

## Testing

Run all tests:

```bash
mvn test
```

---

## Future Improvements

* Cache aggregate calculations
* Graph validation during startup
* H2 persistence option
* Graph visualization endpoint
* OpenAPI / Swagger integration
* Distributed cache support
