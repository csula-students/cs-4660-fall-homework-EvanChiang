package csula.cs4660.graphs.representations;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Node;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Adjacency list is probably the most common implementation to store the unknown
 * loose graph
 *
 * TODO: please implement the method body
 */
public class AdjacencyList implements Representation {
    private Map<Node, Collection<Edge>> adjacencyList;

    public AdjacencyList(File file) {
        try {
            adjacencyList = new HashMap<Node, Collection<Edge>>();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            int count = Integer.parseInt(line);
            for (int i = 0; i < count; i++) {
                adjacencyList.put(new Node(i), new ArrayList<Edge>());
            }
            line = br.readLine();
            while (line != null) {
                String[] data = line.split(":");
                int[] idata = new int[data.length];
                for (int j = 0; j < data.length; j++) {
                    idata[j] = Integer.parseInt(data[j]);
                }
                ArrayList<Edge> temp = (ArrayList<Edge>) adjacencyList.get(new Node(idata[0]));
                temp.add(new Edge(new Node(idata[0]), new Node(idata[1]), idata[2]));
                adjacencyList.put(new Node(idata[0]), temp);
                line = br.readLine();
            }
            br.close();
        }catch(IOException e){}

        }

    protected AdjacencyList() {
        adjacencyList = new HashMap<>();
    }


    @Override
    public boolean adjacent(Node x, Node y)
    {
        ArrayList<Edge> list = (ArrayList<Edge>) adjacencyList.get(x);
        if (list != null)
        {
            for (Edge e: list)
            {
                if (e.getTo().equals(y))
                    return true;
            }
        }
        return false;
    }

    @Override
    public List<Node> neighbors(Node x)
    {
        ArrayList<Edge> list = (ArrayList<Edge>) adjacencyList.get(x);
        ArrayList<Node> neighbors = new ArrayList<Node>();
        if (list != null)
        {
            for (Edge e: list)
            {
                neighbors.add(e.getTo());
            }
        }
        return neighbors;
    }

    @Override
    public boolean addNode(Node x) {
        if (!adjacencyList.containsKey(x))
        {
            adjacencyList.put(x, new ArrayList<Edge>());
            return true;
        }
        else
            return false;
    }

    @Override
    public boolean removeNode(Node x) {
        if (adjacencyList.containsKey(x))
        {
            adjacencyList.remove(x);
            ArrayList<Edge> removeList = new ArrayList<Edge>();
            for (Map.Entry<Node, Collection<Edge>> entry: adjacencyList.entrySet())
            {
                ArrayList<Edge> tempList = (ArrayList<Edge>)entry.getValue();
                for (Edge e: tempList)
                {
                    if (e.getTo().equals(x))
                        removeList.add(e);
                }
            }
            for (Edge e: removeList)
                removeEdge(e);
            return true;
        }
        else
            return false;
    }

    @Override
    public boolean addEdge(Edge x)
    {
        Node from = x.getFrom();
        if (!adjacencyList.containsKey(from))
            addNode(from);

        ArrayList<Edge> temp = (ArrayList<Edge>)adjacencyList.get(from);
        for (Edge e: temp)
        {
            if (e.getFrom().equals(from) && e.getTo().equals(x.getTo()))
                return false;
        }

        temp.add(x);
        adjacencyList.put(from, temp);
        return true;
    }

    @Override
    public boolean removeEdge(Edge x) {
        Node from = x.getFrom();
        if (!adjacencyList.containsKey(from))
            return false;

        ArrayList<Edge> temp = (ArrayList<Edge>)adjacencyList.get(from);
        if (!temp.contains(x))
            return false;
        temp.remove(x);
        adjacencyList.put(from, temp);
        return true;
    }

    @Override
    public int distance(Node from, Node to)
    {
        ArrayList<Edge> list = (ArrayList<Edge>) adjacencyList.get(from);
        if (list != null)
        {
            for (Edge e: list)
            {
                if (e.getTo().equals(to))
                    return e.getValue();
            }
        }
        return -1;
    }

    @Override
    public Optional<Node> getNode(int index) {
        return null;
    }
}
