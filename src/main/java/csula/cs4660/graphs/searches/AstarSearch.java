package csula.cs4660.graphs.searches;

import csula.cs4660.games.models.Tile;
import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Perform A* search
 */
public class AstarSearch implements SearchStrategy {

    @Override
    public List<Edge> search(Graph graph, Node source, Node dist) {
        ArrayList<Edge> returnList = new ArrayList<>();
        ArrayList<Node> searchQueue = new ArrayList<>();
        HashMap<Node, Node> parentMap = new HashMap<>(); //key is child, value is parent
        HashMap<Node, Double> distanceMap = new HashMap<>(); //the key's distance away from source
        searchQueue.add(source); // index 0 is the last priority, index searchQueue.size() - 1 is first priority
        distanceMap.put(source, 0.0);
        while (!searchQueue.isEmpty())
        {
            Node currentNode = searchQueue.remove(searchQueue.size()-1);
            Tile t = (Tile)currentNode.getData();
            System.out.println(t.getX() + ", " + t.getY());
            for (Node n: graph.neighbors(currentNode))
            {
                Tile tilen = (Tile)n.getData();
                System.out.println("neighbor: " + tilen.getX() + ", " + tilen.getY());

                double distance = graph.distance(currentNode, n) + distanceMap.get(currentNode);
                if (n.equals(dist))
                {
                    parentMap.put(n, currentNode);
                    distanceMap.put(n, distance);
                    searchQueue.clear();
                    break;
                }
                else if (parentMap.containsKey(n))
                {
                    if (distanceMap.get(n) > distance)
                    {
                        parentMap.put(n, currentNode);
                        distanceMap.put(n, distance);
                    }
                }
                else
                {
                    parentMap.put(n, currentNode);
                    distanceMap.put(n, distance);
                    if (searchQueue.isEmpty())
                        searchQueue.add(0, n);
                    else
                    {
                        for (int i = searchQueue.size() - 1; i >= 0; i--)
                        {
                            if (heuristic(graph, searchQueue.get(i), dist) > heuristic(graph, n, dist))
                            {
                                searchQueue.add(i + 1, n);
                                break;
                            }
                            else if (i == 0)
                            {
                                searchQueue.add(0, n);
                            }
                        }
                    }
                }
            }
        }
        Node child = dist;
        Node parent = parentMap.get(child);
        while (!child.equals(source))
        {
            System.out.println(((Tile)child.getData()).getType());
            System.out.println(((Tile)parent.getData()).getType());
            returnList.add(0, new Edge(parent, child, graph.distance(parent, child)));
            child = parent;
            System.out.println(((Tile)child.getData()).getType());
            parent = parentMap.get(child);
        }
        return returnList;
    }

    private double heuristic(Graph graph, Node start, Node dest)
    {
        Tile startTile = (Tile)start.getData();
        Tile destTile = (Tile)dest.getData();
        double scale = 1;
        double distance = Math.sqrt(Math.pow(startTile.getX() - destTile.getX(), 2) +
                                    Math.pow(startTile.getY() - destTile.getY(), 2));
        return distance * scale;
    }
}