package csula.cs4660.graphs.searches;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;

import java.util.*;

/**
 * As name, dijkstra search using graph structure
 */
public class DijkstraSearch implements SearchStrategy {
    @Override
    public List<Edge> search(Graph graph, Node source, Node dist)
    {
        ArrayList<Edge> returnList = new ArrayList<>();
        LinkedList<Node> searchQueue = new LinkedList<>();
        HashMap<Node, Node> parentMap = new HashMap<>(); //key is child, value is parent
        HashMap<Node, Integer> distanceMap = new HashMap<>(); //the key's distance away from source
        searchQueue.add(source);
        distanceMap.put(source, 0);

        while (!searchQueue.isEmpty())
        {
            Node currentNode = searchQueue.pop();
            for (Node n: graph.neighbors(currentNode))
            {
                int distance = graph.distance(currentNode, n) + distanceMap.get(currentNode);

                if (parentMap.containsKey(n))
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
                    searchQueue.add(n);
                }
            }
        }
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
}
