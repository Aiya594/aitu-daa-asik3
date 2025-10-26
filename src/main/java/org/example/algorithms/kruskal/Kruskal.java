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
    private double timeMs = 0;

    public Kruskal(Graph g) {
        this.g = g;
    }

    public void runKruskal() {
        long startTime = System.nanoTime();

        List<Edge> edges = new ArrayList<>(g.getEdges());
        // sort edges in asc
        Collections.sort(edges);
        metrics.addComparisons(edges.size());

        //use union-find to detect cycles
        UnionFind unionFind = new UnionFind(g.getVertices(), metrics);

        for (Edge e : edges) {
            metrics.incComparisons();

            String u = e.getFrom();
            String v = e.getTo();

            //if u and v belong to diff sets adding this edge will not create a cycle
            if (!unionFind.find(u).equals(unionFind.find(v))) {
                boolean merged = unionFind.union(u, v); //merging u and v
                if (merged) {
                    mst.add(e); //then store this edge to the result mst and update totalCost
                    totalCost += e.getWeight();

                    metrics.incEdgeAdditions();

                    //stops when the size of mst will be equal to v-1
                    if (mst.size() == g.vertexCount() - 1) {
                        break;
                    }
                }
            }
        }

        long endTime = System.nanoTime();
        timeMs = (endTime - startTime) / 1_000_000.0;
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
    public double getTimeMs() {
        return timeMs;
    }
}
