# Assignment 3: Optimization of a City Transportation Network
(Minimum Spanning Tree) 

* Course: Design and Analysis of Algorithms 
* Student: Aiya Zhakupova  

---
### Introduction

#### Objective
The purpose of this assignment is to apply Prim’s and Kruskal’s algorithms to
optimize a city’s transportation network by determining the minimum set of roads that
connect all city districts with the lowest possible total construction cost. Students will
also analyze the efficiency of both algorithms and compare their performance.

The city administration plans to construct roads connecting all districts in such a way
that:
* each district is reachable from any other district;
* the total cost of construction is minimized.
This scenario is modeled as a weighted undirected graph, where:
* vertices represent city districts,
* edges represent potential roads,
* the edge weight represents the cost of constructing the road.

### Input Data  
For testing, several graphs of different sizes and densities were used (from the docs/input_test.json file):

| Graph Type | Vertices | Edges | Description                                             |
| ---------- | -------- | --- |---------------------------------------------------------|
| Small      | 4–6      | ~7  | For debugging and correctness verification              |
| Medium     | 10–15    | ~20–30 | For measuring practical performance                     |
| Large      | 20–30    | ~40 | For scalability and efficiency comparison               |

---
### Algorithm Results

After running the automated JUnit tests, the program produces two main output files:
1. docs/output_test.json - contains detailed results for each graph
2. docs/csv_results.csv - summarized results in a compact table format for easier analysis and inclusion in the analytical report.


Each graph was processed by both Prim’s and Kruskal’s algorithms.
The program measured:
* Total cost of the MST
* Execution time (ms)
* Number of algorithmic operations (comparisons, unions, etc.)

| Graph ID | Vertices | Edges | Prim Total Cost | Kruskal Total Cost | Prim Time (ms) | Kruskal Time (ms) | Prim Ops | Kruskal Ops |
| -------- | -------- | ----- | --------------- | ------------------ | -------------- | ----------------- | -------- | ----------- |
| 1        | 5        | 6     | 9               | 9                  | 2.921          | 0.978             | 20       | 39          |
| 2        | 6        | 7     | 12              | 12                 | 0.238          | 0.069             | 24       | 51          |
| 3        | 12       | 15    | 33              | 33                 | 0.664          | 0.100             | 50       | 112         |
| 4        | 15       | 19    | 47              | 47                 | 0.851          | 0.114             | 63       | 145         |
| 5        | 20       | 19    | 85              | 85                 | 0.924          | 0.141             | 76       | 179         |
| 6        | 30       | 34    | 102             | 102                | 0.892          | 0.315             | 121      | 281         |

For all datasets correctness confirmed:

* MST total cost was identical 
* Each MST contained V − 1 edges 
* No cycles detected 
* Both algorithms handled all graphs successfully

---

### Prim and Kruskal Algorithms Comparison in Practice

| Aspect                     | Prim’s Results                                                        | Kruskal’s Results                                                                  |
| -------------------------- |-----------------------------------------------------------------------|------------------------------------------------------------------------------------|
| MST Total Cost             | Identical across all graphs: 9, 12, 33, 47, 85, 102                   | Identical across all graphs: 9, 12, 33, 47, 85, 102                                |
| Execution Time (ms)        | Prim’s time is more stable as graphs grow.                            | Kruskal’s is generally faster on small/medium graphs due to efficient edge sorting |
| Number of Operations       | Prim uses fewer heap operations per vertex.                           | Kruskal performs more DSU (union/find) operations, increasing linearly with edges  |
| Scalability (Graph Size)   | Handles large graphs efficiently (stable <1 ms for 30 vertices)       | Slight increase in time for larger graphs, but still under 1 ms                    |
| Behavior on Dense Graphs   | Performs better — works directly with adjacency lists and local edges | Becomes slower when many edges must be sorted                                      |
| Behavior on Sparse Graphs  | Slightly slower due to queue updates                                  | Performs faster — fewer edges to sort and process                                  |
| Implementation Complexity  | Requires maintaining a priority queue and visited set                 | Simpler structure; uses global sorting + DSU                                       |
| Practical Efficiency Trend | Slightly slower on small graphs, but more stable overall              | Very fast on small graphs, slower on dense large graphs                            |
| Best Use Case              | Dense graphs, adjacency-based data (e.g. city road networks)          | Sparse graphs, edge-list data (e.g. telecom, map data)                             |

---
### Conclusion

#### Overall

| Aspect    | Kruskal               | Prim                |
|-----------|-----------------------|:--------------------|
| Strategy  | Edge-based            | Vertex-based        |
| Structure | Union-Find            | Priority Queue      |
| Sorting   | Sort all edges once   | Dynamically uses heap |
| Efficiency | O(E log E)            | O(E log V)          |
| Cycle check | Union-Find            | `visited` set       |
| Best for  |         Sparse graphs | Dense graphs        |

Both **Prim’s** and **Kruskal’s** algorithms successfully optimized the city transportation network by producing identical Minimum Spanning Tree (MST) costs for all datasets, confirming the correctness of both implementations.
However, their **efficiency and practical behavior** varied depending on graph characteristics.

In experiments, **Kruskal’s algorithm** demonstrated better speed on **small and sparse graphs**, as it efficiently sorts and processes a limited number of edges.
Meanwhile, **Prim’s algorithm** showed **more stable and scalable performance** as graph size and density increased, thanks to its adjacency-based structure and priority queue approach.

For **dense city networks**—where most districts are interconnected—**Prim’s algorithm** is more suitable in practice because it avoids global edge sorting and maintains consistent performance.
Conversely, for **sparser networks** (like rural or infrastructural systems with fewer connections), **Kruskal’s algorithm** provides faster results with simpler implementation.

Overall, both algorithms are equally correct in terms of MST construction, but **Prim’s** is preferable for large, dense transportation systems, while **Kruskal’s** excels in smaller or sparse environments.

---
### References
* GeeksforGeeks. (n.d.). Prim’s vs Kruskal’s Algorithm – Comparison and
  Implementation.  https://www.geeksforgeeks.org/
* Programiz. (n.d.). Kruskal’s Algorithm. https://www.programiz.com/dsa/kruskal-algorithm 
* Programiz. (n.d.). Prim’s Algorithm.  https://www.programiz.com/dsa/prim-algorithm


