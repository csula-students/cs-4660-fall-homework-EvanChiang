package csula.cs4660.graphs.searches;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Depth first search
 */
public class DFS implements SearchStrategy {
    @Override
    public List<Edge> search(Graph graph, Node source, Node dist) {
        return DfsHelper(graph, source, dist, source, new HashMap<>());
    }

    public List<Edge> DfsHelper(Graph graph, Node source, Node dist, Node currentNode, HashMap<Node, Node> parentMap)
    {
        List<Edge> returnList = new ArrayList<>();
        if (currentNode.equals(dist))
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
        for (Node n: graph.neighbors(currentNode))
        {
            if (!parentMap.containsKey(n))
            {
                parentMap.put(n, currentNode);
                returnList = DfsHelper(graph, source, dist, n, parentMap);
            }
            if (!returnList.isEmpty())
                break;
        }
        return returnList;
    }
}
