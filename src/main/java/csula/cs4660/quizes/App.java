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
        HashSet<String> visited = new HashSet<>();
        HashMap<String, String> parentMapBFS = new HashMap<>();
        HashMap<String, String> parentMapD = new HashMap<>();
        HashMap<String, Integer> distanceMapD = new HashMap<>();
        HashMap<String, Integer> distanceMapBFS = new HashMap<>();
        ArrayList<String> pathBFS = new ArrayList<>();
        ArrayList<String> pathD = new ArrayList<>();
        boolean found = false;
        queue.add(initialState.getId());
        distanceMapD.put(initialState.getId(), 0);
        distanceMapBFS.put(initialState.getId(), 0);
        while (!queue.isEmpty())
        {
            String currentId = queue.pop();

            for (State neighbor: (Client.getState(currentId).get()).getNeighbors())
            {
                String neighborId = neighbor.getId();
                int event = Client.stateTransition(currentId, neighborId).get().getEvent().getEffect();
//                System.out.println(neighborId + " " + event);
                int distance = event + distanceMapD.get(currentId);
                if (neighborId.equals(destState.getId()) && !found)
                {
                    found = true;
                    System.out.println("BFS found exit");
                    parentMapBFS.put(neighborId, currentId);
                    distanceMapBFS.put(neighborId, distance);
                    String child = destState.getId();
                    String parent = parentMapBFS.get(child);
                    while (!child.equals(initialState.getId()))
                    {
                        State childState = Client.getState(child).get();
                        State parentState = Client.getState(parent).get();
                        pathBFS.add(0, parentState.getLocation().getName() + ":"
                                + childState.getLocation().getName() + ":"
                                + distanceMapBFS.get(child));
                        child = parent;
                        parent = parentMapBFS.get(child);
                    }
                    writeTextFile(pathBFS, "BFS.txt");
                    System.out.println("BFS.txt written");
                }
                if (!currentId.equals(destState.getId()) && !visited.contains(neighborId))
                {
                    if (parentMapD.containsKey(neighborId))
                    {
                        if (distanceMapD.get(neighborId) < distance)
                        {
//                            System.out.println(neighborId);
                            parentMapD.remove(neighborId);
                            distanceMapD.remove(neighborId);
                            parentMapD.put(neighborId, currentId);
                            distanceMapD.put(neighborId, distance);
                            queue.add(neighborId);
                        }
                    }
                    else
                    {
                        parentMapBFS.put(neighborId, currentId);
                        parentMapD.put(neighborId, currentId);
                        distanceMapD.put(neighborId, distance);
                        distanceMapBFS.put(neighborId, distance);
                        queue.add(neighborId);
                    }
                }
            }
            visited.add(currentId);
        }
        System.out.println("Search complete");
        String child = destState.getId();
        String parent = parentMapD.get(child);
        while (!child.equals(initialState.getId()))
        {
            System.out.println(child);
            State childState = Client.getState(child).get();
            State parentState = Client.getState(parent).get();
            pathD.add(0, parentState.getLocation().getName() + ":"
                    + childState.getLocation().getName() + ":"
                    + distanceMapD.get(child));
            child = parent;
            parent = parentMapD.get(child);
        }
        writeTextFile(pathD, "Dijkstra.txt");
    }

    private static void writeTextFile(ArrayList<String> path, String outputName)
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
                if (neighborId.equals(destinationId))
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
