import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.algorithms.kruskal.Kruskal;
import org.example.algorithms.prims.Prim;
import org.example.graph.Graph;
import org.example.io.Input;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.example.io.*;



public class Tests {

    private static final String input_test = "src/test/java/docs/input_test.json";
    private static final String output_test = "src/test/java/docs/output_test.json";
    private static final String csv_results = "src/test/java/docs/csv_results.csv";

    @Test
    public void runTests() throws Exception {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        Input input = mapper.readValue(new File(input_test), Input.class);

        List<Output.Result> results = new ArrayList<>();
        List<String> csvLines = new ArrayList<>();


        csvLines.add("Graph ID,Vertices,Edges,Prim Total Cost,Kruskal Total Cost," +
                "Prim Time (ms),Kruskal Time (ms),Prim Ops,Kruskal Ops");

        for (Input.Graphs gEntry : input.graphs) {
            Graph graph = new Graph(gEntry.nodes, gEntry.edges);

            // prim
            Prim prim = new Prim(graph);
            prim.runPrim();

            //kruskal
            Kruskal kruskal = new Kruskal(graph);
            kruskal.runKruskal();

            // check for correctness
            int vertices = graph.vertexCount();

            // The total cost of the MST is identical for both algorithms.
            if (prim.getTotalCost() != kruskal.getTotalCost()) {
                throw new AssertionError("Total MST cost mismatch for graph " + gEntry.id);
            }

            //The number of edges in each MST equals V − 1.
            if (prim.getMstEdges().size() != vertices - 1) {
                throw new AssertionError("Prim MST edge count != V−1 for graph " + gEntry.id);
            }
            if (kruskal.getMstEdges().size() != vertices - 1) {
                throw new AssertionError("Kruskal MST edge count != V−1 for graph " + gEntry.id);
            }

            //The MST contains no cycles (acyclic).
            if (!isAcyclic(prim.getMstEdges())) {
                throw new AssertionError("Prim MST contains a cycle for graph " + gEntry.id);
            }
            if (!isAcyclic(kruskal.getMstEdges())) {
                throw new AssertionError("Kruskal MST contains a cycle for graph " + gEntry.id);
            }

            //Each MST connects all vertices (single connected component).
            if (!isConnected(prim.getMstEdges(), graph.getVertices())) {
                throw new AssertionError("Prim MST not connected for graph " + gEntry.id);
            }
            if (!isConnected(kruskal.getMstEdges(), graph.getVertices())) {
                throw new AssertionError("Kruskal MST not connected for graph " + gEntry.id);
            }





            Output.AlgorithmResult primResult = new Output.AlgorithmResult(
                    prim.getMstEdges(),
                    prim.getTotalCost(),
                    prim.getMetrics().totalOpers(),
                    (double) prim.getTimeMs()
            );

            Output.AlgorithmResult kruskalResult = new Output.AlgorithmResult(
                    kruskal.getMstEdges(),
                    kruskal.getTotalCost(),
                    kruskal.getMetrics().totalOpers(),
                    (double) kruskal.getTimeMs()
            );

            Map<String, Integer> stats = Map.of(
                    "vertices", graph.vertexCount(),
                    "edges", graph.edgeCount()
            );

            Output.Result result = new Output.Result(
                    gEntry.id, stats, primResult, kruskalResult
            );
            results.add(result);

            // CSV line
            csvLines.add(String.format(
                    Locale.CANADA,
                    "%d,%d,%d,%d,%d,%.3f,%.3f,%d,%d",
                    gEntry.id,
                    graph.vertexCount(),
                    graph.edgeCount(),
                    prim.getTotalCost(),
                    kruskal.getTotalCost(),
                    (double) prim.getTimeMs(),
                    (double) kruskal.getTimeMs(),
                    prim.getMetrics().totalOpers(),
                    kruskal.getMetrics().totalOpers()
            ));
        }

        //write json output
        Output outputModel = new Output(results);
        mapper.writeValue(new File(output_test), outputModel);

        //write csv results
        Files.write(Paths.get(csv_results), csvLines);

        System.out.println("Output written to: " + output_test);
        System.out.println("CSV summary written to: " + csv_results);
    }

    private boolean isAcyclic(List<org.example.graph.Edge> edges) {
        Map<String, String> parent = new HashMap<>();

        for (org.example.graph.Edge e : edges) {
            parent.putIfAbsent(e.getFrom(), e.getFrom());
            parent.putIfAbsent(e.getTo(), e.getTo());
        }

        java.util.function.Function<String, String> find = new java.util.function.Function<>() {
            @Override
            public String apply(String x) {
                if (!parent.get(x).equals(x))
                    parent.put(x, this.apply(parent.get(x)));
                return parent.get(x);
            }
        };

        for (org.example.graph.Edge e : edges) {
            String rootU = find.apply(e.getFrom());
            String rootV = find.apply(e.getTo());
            if (rootU.equals(rootV)) return false;
            parent.put(rootU, rootV);
        }
        return true;
    }

    private boolean isConnected(List<org.example.graph.Edge> edges, Set<String> vertices) {
        if (edges.isEmpty()) return vertices.size() <= 1;
        Map<String, List<String>> adj = new HashMap<>();
        for (org.example.graph.Edge e : edges) {
            adj.computeIfAbsent(e.getFrom(), k -> new ArrayList<>()).add(e.getTo());
            adj.computeIfAbsent(e.getTo(), k -> new ArrayList<>()).add(e.getFrom());
        }

        Set<String> visited = new HashSet<>();
        Deque<String> stack = new ArrayDeque<>();
        String start = vertices.iterator().next();
        stack.push(start);

        while (!stack.isEmpty()) {
            String v = stack.pop();
            if (!visited.add(v)) continue;
            for (String n : adj.getOrDefault(v, Collections.emptyList())) {
                if (!visited.contains(n)) stack.push(n);
            }
        }
        return visited.containsAll(vertices);
    }







}
