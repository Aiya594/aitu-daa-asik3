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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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


}
