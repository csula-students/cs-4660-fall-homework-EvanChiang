package csula.cs4660.graphs.utils;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;
import csula.cs4660.graphs.representations.Representation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * A quick parser class to read different format of files
 */
public class Parser {
    public static Graph readRectangularGridFile(Representation.STRATEGY graphRepresentation, File file) {
        Graph graph = new Graph(Representation.of(graphRepresentation));
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            line = br.readLine();
            while (line != null) {
                String[] data = line.split(":");
                int[] idata = new int[data.length];
                for (int j = 0; j < data.length; j++) {
                    idata[j] = Integer.parseInt(data[j]);
                }
                ArrayList<Edge> temp = (ArrayList<Edge>) graph.get(new Node(idata[0]));
                temp.add(new Edge(new Node(idata[0]), new Node(idata[1]), idata[2]));
                graph.put(new Node(idata[0]), temp);
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
        }
        return graph;
    }

    public static String converEdgesToAction(Collection<Edge> edges) {
        // TODO: convert a list of edges to a list of action
        return "";
    }
}
