package csula.cs4660.quizes;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;
import csula.cs4660.graphs.representations.AdjacencyList;
import csula.cs4660.graphs.representations.Representation;
import csula.cs4660.quizes.models.DTO;
import csula.cs4660.quizes.models.State;
import csula.cs4660.quizes.models.DTO;

import java.util.*;

import java.io.*;
import java.util.*;

/**
 * Here is your quiz entry point and your app
 */
public class App {
<<<<<<< HEAD
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
=======
    public enum SEARCH_STRATEGY {
        BFS,
        DIJKSTRA
    }

    public static void main(String[] args) {
        Map<State, Integer> bfsPath = search(
            SEARCH_STRATEGY.BFS,
            "10a5461773e8fd60940a56d2e9ef7bf4",
            "e577aa79473673f6158cc73e0e5dc122"
        );
        Map<State, Integer> dijkstraPath = search(
            SEARCH_STRATEGY.DIJKSTRA,
            "10a5461773e8fd60940a56d2e9ef7bf4",
            "e577aa79473673f6158cc73e0e5dc122"
        );

        System.out.println("BFS:");
        printPathReverse(bfsPath);
        System.out.println("DijkStra:");
        printPathReverse(dijkstraPath);
    }

    public static void printPathReverse(Map<State, Integer> path) {
        // traverse the map in reverse order by iterator
        ListIterator<Map.Entry<State, Integer>> iterator =
            new ArrayList<>(path.entrySet()).listIterator(path.size());
        while (iterator.hasPrevious()) {
            Map.Entry<State, Integer> entry = iterator.previous();
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }

    public static Map<State, Integer> search(SEARCH_STRATEGY strategy, String start, String end) {
        State initialState = Client.getState(start).get();
        Map<State, Integer> distances = new HashMap<>();
        Map<State, State> parents = new HashMap<>();
        Set<State> exploredSet = new HashSet<>();
        Queue<State> frontier;
        switch (strategy) {
            case BFS:
                frontier = new LinkedList<>();
                break;
            case DIJKSTRA:
                frontier = new PriorityQueue<>(10, new StateComparator(distances));
                break;
            default:
                frontier = new LinkedList<>();
                break;
        }
        frontier.add(initialState);
        distances.put(initialState, 0);

        while (!frontier.isEmpty()) {
            State current = frontier.poll();
            exploredSet.add(current);

            // state transition
            if (current.getId().equals(end)) {
                // construct actions from endTile
                System.out.println("Found path");
                return constructPath(parents, distances, current);
            }

            // for every possible action
            for (State neighbor: Client.getState(current.getId()).get().getNeighbors()) {
                if (!exploredSet.contains(neighbor)) {
                    Optional<DTO> transition = Client.stateTransition(current.getId(), neighbor.getId());
                    distances.put(
                        neighbor,
                        distances.getOrDefault(current, 0) + transition.get().getEvent().getEffect()
                    );
                    parents.put(neighbor, current);
                    frontier.add(neighbor);
                }
            }
        }

        return new HashMap<>();
    }

    public static Map<State, Integer> constructPath(Map<State, State> parents, Map<State, Integer> distances, State current) {
        State c = current;
        Map<State, Integer> result = new LinkedHashMap<>();

        while (parents.get(c) != null) {
            result.put(c, distances.get(c));
            c = parents.get(c);
        }
        result.put(c, distances.get(c));

        return result;
    }

    public static class StateComparator implements Comparator<State> {
        private final Map<State, Integer> distances;
        public StateComparator(Map<State, Integer> distances) {
            this.distances = distances;
        }

        @Override
        public int compare(State a, State b) {
            return distances.getOrDefault(b, Integer.MAX_VALUE)
                .compareTo(distances.getOrDefault(a, Integer.MAX_VALUE));
        }
>>>>>>> 959fbb379b17aba053f911cd4a7ae7ce8efe757c
    }
}
