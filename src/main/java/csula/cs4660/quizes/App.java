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

}
