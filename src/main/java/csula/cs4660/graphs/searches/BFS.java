package csula.cs4660.graphs.searches;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by eric on 9/24/16.
 */
public class BFS implements SearchStrategy {
    @Override

    public List<Edge> search(Graph graph, Node source, Node dist) {
        ArrayList<Edge> returnList = new ArrayList<Edge>();
        LinkedList<Node> searchQueue = new LinkedList<Node>();
        HashMap<Node, Node> parentMap = new HashMap<>();

        searchQueue.add(source);
        while (!searchQueue.isEmpty())
        {
            Node currentNode = searchQueue.pop();
            if (!parentMap.containsKey(currentNode))
            {
                for (Node n: graph.neighbors(currentNode))
                {
                    parentMap.put(currentNode, n);
                    searchQueue.add(n);
                }
                if (currentNode.equals(dist))
                {
                    Node child = dist;
                    Node parent = parentMap.get(child);
                    while (!child.equals(source))
                    {
                        returnList.add(new Edge(parent, child, graph.distance(parent, child)));
                        child = parent;
                        parent = parentMap.get(child);
                    }
                    return returnList;
                }
            }
        }

        return returnList;
    }
}
