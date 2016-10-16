package csula.cs4660.graphs.searches;

import csula.cs4660.games.models.Tile;
import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;
import csula.cs4660.graphs.utils.Parser;

import java.lang.reflect.Array;
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
            Node currentNode = searchQueue.remove(searchQueue.size() - 1);
            System.out.println("test");
            for (Node neighbor: graph.neighbors(currentNode))
            {
                System.out.println("test2");
                double distance = graph.distance(currentNode, neighbor) + distanceMap.get(currentNode);

                if (neighbor.equals(dist))
                {
                    Node child = dist;
                    Node parent = parentMap.get(child);
                    while (!child.equals(source))
                    {
                        returnList.add(0, new Edge(parent, child, graph.distance(parent, child)));
                        child = parent;
                        parent = parentMap.get(child);
                    }
                    return returnList;
                }
                else if (parentMap.containsKey(neighbor))
                {
                   if (distanceMap.get(neighbor) > distance)
                   {
                       parentMap.put(neighbor, currentNode);
                       distanceMap.put(neighbor, distance);
                   }
                }
                else
                {
                    parentMap.put(neighbor, currentNode);
                    distanceMap.put(neighbor, distance);
                    if (searchQueue.isEmpty())
                        searchQueue.add(neighbor);
                    else
                    {
                        double heuristicNeighbor = heuristic(neighbor, dist) + distanceMap.get(neighbor);
                        for (int i = searchQueue.size() - 1; i >= 0; i--)
                        {
                            double heuristicNodeInQueue = heuristic(searchQueue.get(i), dist)
                                                                    + distanceMap.get(searchQueue.get(i));
                            if (heuristicNeighbor < heuristicNodeInQueue)
                            {
                                searchQueue.add(i+1, neighbor);
                                i = -1;
                            }
                            else if (heuristicNeighbor == heuristicNodeInQueue)
                            {
                                Tile tileNeighbor = (Tile)neighbor.getData();
                                Tile tileQueue = (Tile)searchQueue.get(i).getData();
                                double deltaX = tileNeighbor.getX() - tileQueue.getX();
                                double deltaY = tileNeighbor.getY() - tileQueue.getY();
                                if (deltaY > 0)
                                    searchQueue.add(i+1, neighbor);
                                else if (deltaY < 0)
                                    searchQueue.add(i, neighbor);
                                else if (deltaX > 0)
                                    searchQueue.add(i + 1, neighbor);
                                else
                                    searchQueue.add(i, neighbor);
                            }
                            else if (i == 0)
                                searchQueue.add(0, neighbor);
                        }
                    }
                }
            }
        }
        return returnList;
    }

    private double heuristic(Node start, Node dest)
    {
        Tile startTile = (Tile)start.getData();
        Tile destTile = (Tile)dest.getData();
        double scale = 1;
        double deltaX = Math.abs(startTile.getX() - destTile.getX());
        double deltaY = Math.abs(startTile.getY() - destTile.getY());
        double distance = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
        return distance * scale;
    }
}