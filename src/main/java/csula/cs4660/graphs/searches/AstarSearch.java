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
        HashSet<Tile> visited = new HashSet<>();
        HashMap<Node, Node> parentMap = new HashMap<>(); //key is child, value is parent
        HashMap<Node, Double> distanceMap = new HashMap<>(); //the key's distance away from source
        searchQueue.add(source); // index 0 is the last priority, index searchQueue.size() - 1 is first priority
        distanceMap.put(source, heuristic(source, dist));
        Tile distTile = (Tile)dist.getData();
        String distType = distTile.getType();
        while (!searchQueue.isEmpty())
        {
            Node currentNode = searchQueue.remove(searchQueue.size() - 1);
            Tile t = (Tile)currentNode.getData();
//            if (distType.equals("@8"))
//                System.out.print(t.getX() + ", " + t.getY() + " : " + distanceMap.get(currentNode) + " ");
            if (t.getType().equals(distType))
            {
//                        distanceMap.put(dist, distance);
                Node child = dist;
                Node parent = parentMap.get(child);
                while (!child.equals(source))
                {
//                    Tile childTile = (Tile)child.getData();
//                            System.out.println(childTile.getX() + ":" + childTile.getY());
                    returnList.add(0, new Edge(parent, child, graph.distance(parent, child)));
                    child = parent;
                    parent = parentMap.get(child);
                }
                return returnList;
            }
            visited.add(t);
            for (Node neighbor: graph.neighbors(currentNode))
            {
                if (!distanceMap.containsKey(neighbor))
                    distanceMap.put(neighbor, Double.MAX_VALUE);
                double distance = graph.distance(currentNode, neighbor) + distanceMap.get(currentNode);
                double distanceHeuristic = distance + heuristic(currentNode, dist);
                Tile neighborTile = (Tile)neighbor.getData();
                String neighborType = neighborTile.getType();
                if (visited.contains(neighborTile))
                {
                    if (distanceMap.get(neighbor) > distance)
                    {
                        parentMap.remove(neighbor);
                        distanceMap.remove(neighbor);
                        parentMap.put(neighbor, currentNode);
                        distanceMap.put(neighbor, distance);
                    }
                    continue;
                }
                if (neighborType.equals("##")) {
                    continue;
                }
                double heuristicNeighbor = heuristic(neighbor, dist);
                if (searchQueue.isEmpty()) {
                    searchQueue.add(neighbor);
//                    System.out.print(" - " + neighborTile.getX() + ", " + neighborTile.getY() + " " + distance);
                }
                else if (distance >= distanceMap.get(neighbor))
                {
                    continue;
                }
                else
                {
//                    System.out.print(" - " + neighborTile.getX() + ", " + neighborTile.getY() + " " + distance);
                    if (!searchQueue.contains(neighbor))
                    {
                        for (int i = searchQueue.size() - 1; i >= 0; i--) {
                            double heuristicNodeInQueue = heuristic(searchQueue.get(i), dist);
                            if (heuristicNeighbor < heuristicNodeInQueue) {
                                if (i == searchQueue.size() - 1)
                                    searchQueue.add(neighbor);
                                else
                                    searchQueue.add(i + 1, neighbor);
                                i = -1;
                            } else if (heuristicNeighbor == heuristicNodeInQueue) {
                                Tile tileNeighbor = (Tile) neighbor.getData();
                                Tile tileQueue = (Tile) searchQueue.get(i).getData();
                                double deltaX = tileNeighbor.getX() - tileQueue.getX();
                                double deltaY = tileNeighbor.getY() - tileQueue.getY();
                                if (deltaY > 0)
                                    searchQueue.add(i + 1, neighbor);
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
                parentMap.put(neighbor, currentNode);
                distanceMap.put(neighbor, distance);
            }
//            System.out.println();
//            for (Node nq: searchQueue)
//            {
//                Tile tnq = (Tile)nq.getData();
//                System.out.print(" * " + tnq.getX() + ", " + tnq.getY());
//            }
//            System.out.println();
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