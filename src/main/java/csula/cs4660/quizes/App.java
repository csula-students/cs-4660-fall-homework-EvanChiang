package csula.cs4660.quizes;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;
import csula.cs4660.graphs.representations.AdjacencyList;
import csula.cs4660.graphs.representations.Representation;
import csula.cs4660.quizes.models.DTO;
import csula.cs4660.quizes.models.State;

import java.io.*;
import java.util.*;

/**
 * Here is your quiz entry point and your app
 */
public class App {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        // to get a state, you can simply call `Client.getState with the id`
        State initialState = Client.getState("10a5461773e8fd60940a56d2e9ef7bf4").get();
        State destState = Client.getState("e577aa79473673f6158cc73e0e5dc122").get();
        // to get an edge between state to its neighbor, you can call stateTransition
        LinkedList<String> queue = new LinkedList<>();
        HashMap<String, String> parentMapBFS = new HashMap<>();
        HashMap<String, String> parentMapD = new HashMap<>();
        HashMap<String, Integer> distanceMap = new HashMap<>();

        queue.add(initialState.getId());

        while (!queue.isEmpty())
        {
            String currentId = queue.poll();

            for (State neighbor: Client.getState(currentId).get().getNeighbors())
            {
                String neighborId = neighbor.getId();
                int distance = (Client.stateTransition(currentId, neighborId)).get().getEvent().getEffect();
                if (neighborId == destState.getId())
                {
                    System.out.println("found the exit!");
                }
                if (distanceMap.containsKey(neighborId))
                {
                    if (distanceMap.get(neighborId) > (distanceMap.get(currentId) + distance))
                    {
                        parentMapD.put(neighborId, currentId);
                        distanceMap.put(neighborId, distanceMap.get(currentId) + distance);
                    }
                }
                else
                {
                    parentMapBFS.put(neighborId, currentId);
                    parentMapD.put(neighborId, currentId);
                    distanceMap.put(neighborId, distance);
                    queue.add(neighborId);
                }
            }
        }
        /*
        HashSet<String> visited = new HashSet<>();
        LinkedList<String> graphBuilder = new LinkedList<>();
        Graph graph = new Graph(Representation.of(Representation.STRATEGY.ADJACENCY_LIST));
        graphBuilder.add(initialState.getId());
        graph.addNode(new Node(initialState));
        int count = 0;
        while (!graphBuilder.isEmpty())
        {
            String currentId = graphBuilder.pop();
            State currentState = Client.getState(currentId).get();
            if (!visited.contains(currentId))
            {
                for (State state: (currentState.getNeighbors()))
                {
                    State neighborState = Client.getState(state.getId()).get();
                    Node node = new Node(neighborState);
                    graphBuilder.add(neighborState.getId());
                    graph.addNode(node);
                    Edge edge = new Edge(new Node(currentState), node, (Client.stateTransition
                            (currentState.getId(), neighborState.getId()).get().getEvent().getEffect()));
                    graph.addEdge(edge);
                }
            }
            visited.add(currentId);
        }
        System.out.println("graph built");
        Node initialNode = new Node(initialState);
        Node initialDest = new Node(destState);
//        String[] BFSanswer = BFS(graph, initialNode, initialDest);
//        String[] DijkstraAnswer = Dijkstra(graph, initialNode, initialDest);
//        writeTextFile(BFSanswer, "BFS.txt");
        */
    }

    private static void writeTextFile(String[] path, String outputName)
            throws FileNotFoundException, UnsupportedEncodingException
    {
        PrintWriter writer = new PrintWriter(outputName, "UTF-8");
        writer.println("BFS Path: ");
        for (String line : path)
        {
            writer.println(line);
        }
        writer.close();
    }

    private static String[] BFS(Graph graph, Node initial, Node destination)
    {
        HashMap<Node, Node> parentMap = new HashMap<>(); //key is child, parent is value
        LinkedList<Node> queue = new LinkedList<>();
        HashSet<Node> visited = new HashSet<>();
        ArrayList<Edge> returnList = new ArrayList<>();
        String initialId = ((State)initial.getData()).getId();
        String destinationId = ((State)destination.getData()).getId();
        String[] path = new String[0];
        queue.add(initial);
        while (!queue.isEmpty())
        {
            Node currentNode = queue.pop();
            String currentId = ((State)currentNode.getData()).getId();
            visited.add(currentNode);
            for (Node neighborNode: graph.neighbors(currentNode))
            {
                String neighborId = ((State)neighborNode.getData()).getId();
                State neighborState = Client.getState(neighborId).get();
                if (neighborId == destinationId)
                {
                    System.out.println("destination found!");
                    parentMap.put(neighborNode, currentNode);
                    Node child = destination;
                    Node parent = parentMap.get(destination);
                    while (((State)child.getData()).getId() != initialId)
                    {
                        returnList.add(new Edge(parent, child, graph.distance(parent, child)));
                        child = parent;
                        parent = parentMap.get(child);
                    }
                    path = new String[returnList.size()+1];
                    int count = 0;
                    for (Edge edge : returnList)
                    {
                        String from = ((State)edge.getFrom().getData()).getLocation().getName();
                        String to = ((State)edge.getTo().getData()).getLocation().getName();
                        int cost = edge.getValue();
                        path[count] = from + ":" + to + ":" + cost;
                        count++;
                    }
                }
                if (!visited.contains(neighborNode))
                {
                    parentMap.put(neighborNode, currentNode);
                    queue.add(neighborNode);
                }
            }
        }
        return path;
    }

    private static String[] Dijkstra(Graph graph, Node initial, Node destination)
    {
        HashMap<Node, Node> parentMap = new HashMap<>(); //key is child, parent is value
        LinkedList<Node> queue = new LinkedList<>();
        ArrayList<Edge> returnList = new ArrayList<>();
        String[] path = new String[0];
        queue.add(initial);
        while (!queue.isEmpty())
        {
            Node currentNode = queue.pop();
            if (!parentMap.containsKey(currentNode))
            {
                for (Node neighbor: graph.neighbors(currentNode))
                {
                    parentMap.put(neighbor, currentNode);
                    queue.add(neighbor);
                    if (neighbor.equals(destination))
                    {
                        Node child = destination;
                        Node parent = parentMap.get(child);
                        while (!child.equals(initial))
                        {
                            returnList.add(new Edge(parent, child, graph.distance(parent, child)));
                            child = parent;
                            parent = parentMap.get(child);
                        }
                        path = new String[returnList.size()+1];
                        int count = 0;
                        for (Edge edge : returnList)
                        {
                            String from = ((State)edge.getFrom().getData()).getLocation().getName();
                            String to = ((State)edge.getTo().getData()).getLocation().getName();
                            int cost = edge.getValue();
                            path[count] = from + ":" + to + ":" + cost;
                            count++;
                        }
                    }
                }
            }
        }
        return path;
    }
}
