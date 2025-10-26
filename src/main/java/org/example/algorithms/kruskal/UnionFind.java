package org.example.algorithms.kruskal;

import org.example.metrics.Metrics;

import java.util.HashMap;
import java.util.Map;

public class UnionFind {
    private final Map<String, String> parent = new HashMap<>();
    private final Map<String, Integer> rank = new HashMap<>();
    private final Metrics metrics;

    public UnionFind(Iterable<String> elements, Metrics metrics) {
        this.metrics = metrics;
        for (String e : elements) {
            parent.put(e, e);
            rank.put(e, 0);
        }
    }

    //returns root of x vertex
    public String find(String x) {
        metrics.incFinds();
        String p = parent.get(x);
        if (p == null) throw new IllegalArgumentException("unknown element: " + x);
        if (!p.equals(x)) {
            String root = find(p);
            parent.put(x, root);
            return root;
        }
        return p;
    }

    //union by rank
    public boolean union(String a, String b) {
        metrics.incUnions();

        //fina roots of a and b
        String ra = find(a);
        String rb = find(b);

        //if they share same root trey are already connected
        if (ra.equals(rb)) {
            return false;
        }
        //then need to find the greatest rank and then attachthe smaller tree under the greater one
        int rA = rank.get(ra);
        int rB = rank.get(rb);

        if (rA < rB) {
            parent.put(ra, rb);
        } else if (rA > rB) {
            parent.put(rb, ra);
        } else {
            parent.put(rb, ra);
            rank.put(ra, rA + 1);
        }
        return true;
    }



}
