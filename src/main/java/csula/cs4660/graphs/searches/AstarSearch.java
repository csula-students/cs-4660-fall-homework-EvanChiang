package csula.cs4660.graphs.searches;

import csula.cs4660.games.models.Tile;
import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;
import csula.cs4660.graphs.utils.Parser;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Perform A* search
 */
public class AstarSearch implements SearchStrategy {

    @Override
    public List<Edge> search(Graph graph, Node source, Node dist) {
        ArrayList<Edge> returnList = new ArrayList<>();
        ArrayList<Node> searchQueue = new ArrayList<>();
        HashSet<Node> visited = new HashSet<>();
        HashMap<Node, Node> parentMap = new HashMap<>(); //key is child, value is parent
        HashMap<Node, Double> distanceMap = new HashMap<>(); //the key's distance away from source
        HashMap<Node, Double> heuristicMap = new HashMap<>(); //heuristic value of a node;
        searchQueue.add(source); // index 0 is the last priority, index searchQueue.size() - 1 is first priority
        distanceMap.put(source, 0.0);
        heuristicMap.put(source, heuristic(source, dist));
        String distType = ((Tile)dist.getData()).getType();

        while (!searchQueue.isEmpty())
        {
            Node currentNode = searchQueue.remove(searchQueue.size() - 1);
            Tile t = (Tile)currentNode.getData();
//            System.out.println(t.getX() + ", " + t.getY());
            if (!visited.contains(currentNode))
            {
                for (Node neighbor: graph.neighbors(currentNode))
                {
                    double distance = graph.distance(currentNode, neighbor) + distanceMap.get(currentNode);
                    String neighborType = ((Tile)neighbor.getData()).getType();
                    if (neighborType.equals(distType))
                    {
                        parentMap.put(neighbor, currentNode);
                        distanceMap.put(dist, distance);
                        Node child = dist;
                        Node parent = parentMap.get(child);
                        while (!child.equals(source))
                        {
                            Tile childTile = (Tile)child.getData();
//                            System.out.println(childTile.getX() + ":" + childTile.getY());
                            returnList.add(0, new Edge(parent, child, graph.distance(parent, child)));
                            child = parent;
                            parent = parentMap.get(child);
                        }
                        return returnList;
                    }
                    else if (neighborType.equals("##"))
                    {
                        visited.add(neighbor);
                    }
                    else if (parentMap.containsKey(neighbor))
                    {
                        if (distanceMap.get(neighbor) > distance)
                        {
                            parentMap.remove(neighbor);
                            distanceMap.remove(neighbor);
                            parentMap.put(neighbor, currentNode);
                            distanceMap.put(neighbor, distance);
                        }
                    }
                    else
                    {
                        parentMap.put(neighbor, currentNode);
                        distanceMap.put(neighbor, distance);
                        double heuristicNeighbor = heuristic(neighbor, dist) + distanceMap.get(neighbor);
                        heuristicMap.put(neighbor, heuristicNeighbor);
                        if (searchQueue.isEmpty())
                            searchQueue.add(neighbor);
                        else
                        {
                            for (int i = searchQueue.size() - 1; i >= 0; i--)
                            {
                                double heuristicNodeInQueue = heuristicMap.get(searchQueue.get(i));
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
            visited.add(currentNode);
        }
        return returnList;
    }

    private double heuristic(Node start, Node dest)
    {
        Tile startTile = (Tile)start.getData();
        Tile destTile = (Tile)dest.getData();
        double scale = 5;
        double deltaX = Math.abs(startTile.getX() - destTile.getX());
        double deltaY = Math.abs(startTile.getY() - destTile.getY());
        double distance = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
        return distance * scale;
    }
}