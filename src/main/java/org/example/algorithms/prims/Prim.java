package org.example.algorithms.prims;

import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.metrics.Metrics;

import java.util.*;

public class Prim {
    private final Graph g;
    private final Metrics metrics = new Metrics();
    private final List<Edge> mst = new ArrayList<>();
    private long totalCost = 0;
    private double timeMs = 0;

    public Prim(Graph g) {
        this.g = g;
    }

    public void runPrim(){
        long startTime = System.nanoTime();

        if (g.vertexCount() == 0) {
            timeMs = 0;
            return;
        }

        Set<String> visited = new HashSet<>(); //to tarck vertices that alreaady in mst
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingLong(Edge::getWeight)); //keeps the smallest weight at the top

        //start from first vertex and mark it as visited
        String startV = g.getVertices().iterator().next();
        visited.add(startV);

        //push neighbors
        for (Edge e : g.getAdjacency().get(startV)) {
            pq.add(e);
            metrics.incHeapOpers();
        }

        while (!pq.isEmpty() && mst.size() < g.vertexCount() - 1) {
            metrics.incHeapOpers();

            //gives the smallest edge that connects a visited vertex to unvisited one
            Edge e = pq.poll();

            metrics.incComparisons();

            String to = e.getTo();
            if (visited.contains(to)) {
                continue; //skip if already in tree
            }

            //adding edge to mst
            visited.add(to);
            mst.add(e);

            totalCost += e.getWeight();
            metrics.incEdgeAdditions();
            //add neighbors of "to"
            for (Edge ne : g.getAdjacency().get(to)) {
                if (!visited.contains(ne.getTo())) {
                    pq.add(ne);
                    metrics.incHeapOpers();
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
