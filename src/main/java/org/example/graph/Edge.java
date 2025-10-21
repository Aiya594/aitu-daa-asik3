package org.example.graph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Edge implements Comparable<Edge> {
    //for parsing json file
    private final String from;
    private final String to;
    private final long weight;

    @JsonCreator
    public Edge(@JsonProperty("from") String from, @JsonProperty("to") String to, @JsonProperty("weight") long weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public String getFrom() {
        return from;
    }
    public String getTo() {
        return to;
    }
    public long getWeight() {
        return weight;
    }

    public Edge other(String other) {
        if (from.equals(other)) {
            return new Edge(to, from, weight);
        }
        if (to.equals(other)) {
            return new Edge(from, to, weight);
        }
        throw new IllegalArgumentException("vertex not incident to edge");
    }

    @Override
    public int compareTo(Edge o) {
        return Long.compare(this.weight, o.weight);
    }

    @Override
    public String toString() {
        return String.format("(%s,%s)= %d", from, to, weight); //(from,to)=weight
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Edge)){
            return false;
        }
        Edge e = (Edge) o;

        return weight == e.weight && ((from.equals(e.from) && to.equals(e.to)) || (from.equals(e.to) && to.equals(e.from)));
    }


    @Override
    public int hashCode() {
        //(a,b)=(b,a)
        int h1 = from.hashCode() ^ to.hashCode();
        int h2 = Long.hashCode(weight);
        return h1 * 31 + h2;
    }
}
