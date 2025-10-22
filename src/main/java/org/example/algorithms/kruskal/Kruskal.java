package org.example.algorithms.kruskal;

import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.metrics.Metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Kruskal {
    private final Graph g;
    private final Metrics metrics = new Metrics();
    private final List<Edge> mst = new ArrayList<>();
    private long totalCost = 0;
    private long timeMs = 0;

    public Kruskal(Graph g) {
        this.g = g;
    }

    public void runKruskal() {
        long startTime = System.nanoTime();

        List<Edge> edges = new ArrayList<>(g.getEdges());
        // sort edges in asc
        Collections.sort(edges);
        metrics.addComparisons(edges.size());

        UnionFind unionFind = new UnionFind(g.getVertices(), metrics);

        for (Edge e : edges) {
            metrics.incComparisons();

            String u = e.getFrom();
            String v = e.getTo();

            if (!unionFind.find(u).equals(unionFind.find(v))) {
                boolean merged = unionFind.union(u, v);
                if (merged) {
                    mst.add(e);
                    totalCost += e.getWeight();

                    metrics.incEdgeAdditions();

                    if (mst.size() == g.vertexCount() - 1) {
                        break;
                    }
                }
            }
        }

        long endTime = System.nanoTime();
        timeMs = (endTime - startTime) / 1_000_000;
    }
    public List<Edge> getMstEdges() {
        return List.copyOf(mst);
    }
    public long getTotalCost() {
        return totalCost;
    }
    public Metrics getMetrics() {
        return metrics;
    }
    public long getExecTimeMs() {
        return timeMs;
    }
}
