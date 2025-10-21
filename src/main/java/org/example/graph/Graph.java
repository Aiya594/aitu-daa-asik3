package org.example.graph;

import java.util.*;

public class Graph {

    private final Set<String> vertices;
    private final List<Edge> edges;
    private final Map<String, List<Edge>> adj; //vertex: list of connected edges

    public Graph(Collection<String> vertices, Collection<Edge> edges) {
        this.vertices = new LinkedHashSet<>(vertices);
        this.edges = new ArrayList<>(edges);
        this.adj = new HashMap<>();

        for (String v : this.vertices){
            adj.put(v, new ArrayList<>());
        }
        for (Edge e : this.edges) {
            // ensure vertices exist
            if (!this.vertices.contains(e.getFrom()) || !this.vertices.contains(e.getTo())) {
                throw new IllegalArgumentException("edge references unknown vertex: " + e);
            }

            adj.get(e.getFrom()).add(e);
            adj.get(e.getTo()).add(new Edge(e.getTo(), e.getFrom(), e.getWeight()));
        }
    }

    public Set<String> getVertices() {
        return Collections.unmodifiableSet(vertices);
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    public Map<String, List<Edge>> getAdjacency() {
        Map<String, List<Edge>> copy = new HashMap<>();

        adj.forEach((k, v) -> copy.put(k, Collections.unmodifiableList(v)));

        return Collections.unmodifiableMap(copy);
    }

    public int vertexCount() {
        return vertices.size();
    }
    public int edgeCount() {
        return edges.size();
    }
}
