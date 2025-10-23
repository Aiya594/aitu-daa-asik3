package org.example.io;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.example.graph.Edge;

import java.util.List;
import java.util.Map;

public class Output {
    public final List<Result> results;

    public Output(List<Result> results) {
        this.results = results;
    }

    public static class AlgorithmResult {
        public final List<Edge> mst_edges;
        public final long total_cost;
        public final long operations_count;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public final Double execution_time_ms;

        public AlgorithmResult(List<Edge> mst_edges, long total_cost, long operations_count, Double execution_time_ms) {
            this.mst_edges = mst_edges;
            this.total_cost = total_cost;
            this.operations_count = operations_count;
            this.execution_time_ms = execution_time_ms;
        }
    }

    public static class Result {
        public final int graph_id;
        public final Map<String, Integer> input_stats;
        public final AlgorithmResult prim;
        public final AlgorithmResult kruskal;

        public Result(int graph_id, Map<String, Integer> input_stats, AlgorithmResult prim, AlgorithmResult kruskal) {
            this.graph_id = graph_id;
            this.input_stats = input_stats;
            this.prim = prim;
            this.kruskal = kruskal;
        }
    }




}
