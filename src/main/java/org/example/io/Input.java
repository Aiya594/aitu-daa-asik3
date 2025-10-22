package org.example.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.graph.Edge;

import java.util.List;

public class Input {
    public static class Graphs {
        public final int id;
        public final List<String> nodes;
        public final List<Edge> edges;

        @JsonCreator
        public Graphs(@JsonProperty("id") int id, @JsonProperty("nodes") List<String> nodes, @JsonProperty("edges") List<Edge> edges) {
            this.id = id;
            this.nodes = nodes;
            this.edges = edges;
        }
    }

    public final List<Graphs> graphs;
    @JsonCreator
    public Input(@JsonProperty("graphs") List<Graphs> graphs) {
        this.graphs = graphs;
    }

}
