# Design Document

## 1. Problem Statement

Build a REST service capable of loading hierarchical transaction graph data and providing efficient graph traversal operations including:

* Node lookup
* Parent chain retrieval
* Child traversal
* Level calculation
* Transaction aggregation
* Subtree analytics

The solution must operate entirely in memory and provide low-latency responses.

---

# 2. High Level Architecture

```text
                 +------------------+
                 | JSON Data Source |
                 +---------+--------+
                           |
                           v
                +----------------------+
                | GraphDataLoader      |
                +----------+-----------+
                           |
                           v
             +-----------------------------+
             | In-Memory Repository         |
             |                             |
             | nodeById                    |
             | childrenByParentId          |
             +--------------+--------------+
                            |
                            v
                    +---------------+
                    | Graph Service |
                    +-------+-------+
                            |
                            v
                  +-------------------+
                  | REST Controller   |
                  +-------------------+
```

---

# 3. Startup Data Loading

## Step 1

Load JSON file.

```java
transactions-graph-nodes.json
```

## Step 2

Deserialize into:

```java
List<GraphNode>
```

## Step 3

Build lookup maps.

### Node Index

```java
Map<String, GraphNode> nodeById
```

Complexity:

```text
Lookup = O(1)
```

---

### Children Index

```java
Map<String, List<GraphNode>> childrenByParentId
```

Complexity:

```text
Get Children = O(1)
```

---

# 4. API Processing Flow

```text
Request
   |
   v
Controller
   |
   v
GraphService
   |
   +--> Find Node
   |
   +--> Compute Level
   |
   +--> Build Parent Chain
   |
   +--> Find Children
   |
   +--> Fetch Transactions
   |
   +--> Aggregate Statistics
   |
   v
Response
```

---

# 5. Level Calculation

## Algorithm

```java
while(parent exists){
   level++;
   parent = parent.parent;
}
```

### Missing Parent

If parent does not exist:

```java
level = 0
```

Node becomes root-like.

### Complexity

```text
O(height)
```

---

# 6. Parent Chain Construction

Example:

```text
N1
└── N2
    └── N3
```

Request:

```text
N3
```

Response:

```text
[N1, N2]
```

Algorithm:

```java
Traverse upward
Reverse list
```

Complexity:

```text
O(height)
```

---

# 7. Direct Children Retrieval

Lookup:

```java
childrenByParentId.get(nodeId)
```

Complexity:

```text
O(1)
```

excluding output size.

---

# 8. Depth-Limited Tree Traversal

## Objective

Build nested child hierarchy.

Example:

```text
N1
├── N2
│   └── N4
└── N3
```

For:

```text
maxDepth=2
```

Return:

```text
N1
 ├── N2
 │    └── N4
 └── N3
```

---

## Algorithm

DFS recursion

```java
buildTree(node, depth)
```

Stopping conditions:

```java
depth >= maxDepth
```

or

```java
visited.contains(nodeId)
```

---

## Complexity

```text
O(V + E)
```

within requested depth.

---

# 9. Cycle Protection

Potential invalid graph:

```text
N1 -> N2 -> N3 -> N1
```

Protection:

```java
Set<String> visited
```

before processing child.

```java
if(visited.contains(id))
   return;
```

Prevents stack overflow and infinite recursion.

---

# 10. Aggregate Calculation

Aggregate statistics are computed level-by-level for the subtree.

Example:

```text
Level 0 -> N1
Level 1 -> N2,N3
Level 2 -> N4,N5
```

Metrics:

* nodeCount
* transactionCount
* totalAmount

---

## Traversal Strategy

Breadth First Search (BFS)

Reason:

* Natural level grouping
* Single traversal
* Easy aggregation

---

## BFS Flow

```text
Queue
 |
 N1
 |
 N2 N3
 |
 N4 N5
```

For every dequeued node:

```java
aggregate[level].nodeCount++;
aggregate[level].transactionCount+=...
aggregate[level].totalAmount+=...
```

---

## Complexity

```text
Time: O(V + E)

Space: O(W)
```

Where:

```text
W = maximum width of tree
```

---

# 11. Error Handling

## Node Missing

```java
throw NodeNotFoundException
```

Returns:

```http
404 NOT FOUND
```

---

## Invalid Depth

```java
maxDepth < 0
maxDepth > 10
```

Returns:

```http
400 BAD REQUEST
```

---

# 12. Design Decisions

## Why In-Memory Storage?

Advantages:

* Fast lookup
* No database dependency
* Simplified deployment
* Ideal for static dataset

---

## Why Map Indexes?

Without indexes:

```text
Find node = O(N)
```

With indexes:

```text
Find node = O(1)
```

---

## Why BFS For Aggregation?

Aggregation requires grouping by level.

BFS naturally processes nodes level-by-level.

---

## Why DFS For Children Tree?

DFS provides:

* Simple recursive implementation
* Natural nested structure generation
* Easy depth limiting

---

# 13. Scalability Considerations

Current:

```text
Single JVM
In-memory graph
```

Future Enhancements:

* External database
* Redis cache
* Distributed graph storage
* Parallel subtree aggregation
* Graph validation service
* Incremental graph updates

---

# 14. Complexity Summary

| Operation         | Complexity |
| ----------------- | ---------- |
| Node Lookup       | O(1)       |
| Children Lookup   | O(1)       |
| Parent Chain      | O(H)       |
| Level Calculation | O(H)       |
| DFS Tree Build    | O(V+E)     |
| BFS Aggregation   | O(V+E)     |

Where:

```text
H = Tree Height
V = Nodes
E = Edges
```
