package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.algorithms.kruskal.Kruskal;
import org.example.algorithms.prims.Prim;
import org.example.graph.Graph;
import org.example.io.*;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: java -jar mst-assignment.jar <input.json> <output.json>");
            System.exit(2);
        }

        File in = Path.of(args[0]).toFile();
        File out = Path.of(args[1]).toFile();

        ObjectMapper om = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);


        Input input = om.readValue(in, Input.class);
        List<Output.Result> results = new ArrayList<>();


        for (Input.Graphs gEntry : input.graphs) {
            Graph graph = new Graph(gEntry.nodes, gEntry.edges);
            // Prim
            Prim prim = new Prim(graph);
            prim.runPrim();

            // Kruskal
            Kruskal kruskal = new Kruskal(graph);
            kruskal.runKruskal();

            Output.AlgorithmResult primRes = new Output.AlgorithmResult(
                    prim.getMstEdges(),
                    prim.getTotalCost(),
                    prim.getMetrics().totalOpers(),
                    (double) prim.getTimeMs()
            );
            Output.AlgorithmResult krusRes = new Output.AlgorithmResult(
                    kruskal.getMstEdges(),
                    kruskal.getTotalCost(),
                    kruskal.getMetrics().totalOpers(),
                    (double) kruskal.getTimeMs()
            );
            Map<String,Integer> inputStats = Map.of(
                    "vertices", graph.vertexCount(),
                    "edges", graph.edgeCount()
            );
            results.add(new Output.Result(gEntry.id, inputStats, primRes, krusRes));
        }

        Output outputModel = new Output(results);
        om.writeValue(out, outputModel);
        System.out.println("Wrote results to " + out.getAbsolutePath());


    }
}