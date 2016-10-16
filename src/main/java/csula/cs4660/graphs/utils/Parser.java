package csula.cs4660.graphs.utils;

import csula.cs4660.games.models.Tile;
import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;
import csula.cs4660.graphs.representations.AdjacencyMatrix;
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
        ArrayList<Node> nodeList = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String start = br.readLine();
            String line = br.readLine();
            int lineLength = line.length()/2 - 1;
            int y = 0;
            int x = 0;
            while (!line.equals(start)) {
                String[] data = splitTiles(line, lineLength);
                for (String s: data)
                {
                    Tile tile = new Tile(x, y, s);
                    Node n = new Node (tile);
                    nodeList.add(n);
                    graph.addNode(n);
                    x++;
                    if (x >= lineLength)
                    {
                        x = 0;
                        y++;
                    }
                }
                line = br.readLine();
            }
            for (int i = 0; i < nodeList.size(); i++)
            {
                int column = i % lineLength;
                int row = i / lineLength;
                if (row - 1 > 0)
                    graph.addEdge(new Edge(nodeList.get(i), nodeList.get(i - lineLength), 1));
                if (row + 1 < nodeList.size() / lineLength)
                    graph.addEdge(new Edge(nodeList.get(i), nodeList.get(i + lineLength), 1));
                if (column - 1 > 0)
                    graph.addEdge(new Edge(nodeList.get(i), nodeList.get(i - 1), 1));
                if (column + 1 < lineLength)
                    graph.addEdge(new Edge(nodeList.get(i), nodeList.get(i + 1), 1));

            }
            br.close();
        } catch (IOException e) {
        }
        return graph;
    }

    private static String[] splitTiles(String line, int length)
    {
        String[] array = new String[length];
        int index = 1;
        for (int i = 0; i < length; i++)
        {
            array[i] = line.substring(index, index + 2);
            index += 2;
        }
        return array;
    }

    public static String converEdgesToAction(Collection<Edge> edges) {
        String actions = "";

        for (Edge e: edges)
        {
            Tile fromTile = (Tile)e.getFrom().getData();
            Tile toTile = (Tile)e.getTo().getData();
            int x = fromTile.getX() - toTile.getX();
            int y = fromTile.getY() - toTile.getY();
            if (x == 1)
                actions = actions + "W";
            else if (x == -1)
                actions = actions + "E";
            else if (y == -1)
                actions = actions + "S";
            else if (y == 1)
                actions = actions + "N";
        }
        System.out.println("actions: " + actions);
        return actions;
    }
}
