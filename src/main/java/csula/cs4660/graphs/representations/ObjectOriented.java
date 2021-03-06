package csula.cs4660.graphs.representations;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Node;

import java.io.BufferedReader;
import java.io.File;
<<<<<<< HEAD
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
=======
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
>>>>>>> 959fbb379b17aba053f911cd4a7ae7ce8efe757c

/**
 * Object oriented representation of graph is using OOP approach to store nodes
 * and edges
 */
public class ObjectOriented implements Representation {
    private Collection<Node> nodes;
    private Collection<Edge> edges;

    public ObjectOriented(File file) {
        try {
            nodes = new ArrayList<Node>();
            edges = new ArrayList<Edge>();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            int count = Integer.parseInt(line);
            for (int i = 0; i < count; i++) {
                nodes.add(new Node(i));
            }
            line = br.readLine();
            while (line != null) {
                String[] data = line.split(":");
                int[] idata = new int[data.length];
                for (int j = 0; j < data.length; j++) {
                    idata[j] = Integer.parseInt(data[j]);
                }
                edges.add(new Edge(new Node(idata[0]), new Node(idata[1]), idata[2]));
                line = br.readLine();
            }
            br.close();
        }catch(IOException e){}
    }

    public ObjectOriented() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
    }

    @Override
    public boolean adjacent(Node x, Node y)
    {
        for (Edge e : edges)
        {
            if (e.getFrom().equals(x) && e.getTo().equals(y))
                return true;
        }
        return false;
    }

    @Override
    public List<Node> neighbors(Node x)
    {
        ArrayList<Node> neighborList = new ArrayList<Node>();
        for (Edge e : edges)
        {
            if (e.getFrom().equals(x))
                neighborList.add(e.getTo());
        }
        return neighborList;
    }

    @Override
    public boolean addNode(Node x)
    {
        if (nodes.contains(x))
            return false;
        nodes.add(x);
        return true;
    }

    @Override
    public boolean removeNode(Node x)
    {
        ArrayList<Edge> removeEdges = new ArrayList<Edge>();
        if (!nodes.contains(x))
            return false;
        nodes.remove(x);
        for (Edge e: edges)
        {
            if (e.getTo().equals(x))
                removeEdges.add(e);
        }
        for (Edge e: removeEdges)
            edges.remove(e);
        return true;
    }

    @Override
    public boolean addEdge(Edge x)
    {
        for (Edge e : edges)
        {
            if (e.getFrom().equals(x.getFrom()) && e.getTo().equals(x.getTo()))
                return false;
        }
        edges.add(x);
        return true;
    }

    @Override
    public boolean removeEdge(Edge x)
    {
        if (!edges.contains(x))
            return false;
        edges.remove(x);
        return true;
    }

    @Override
    public int distance(Node from, Node to)
    {
        for (Edge e: edges)
        {
            if (e.getFrom().equals(from) && e.getTo().equals(to))
                return e.getValue();
        }
        return -1;
    }

    @Override
    public Optional<Node> getNode(int index) {
        return null;
    }

    @Override
    public Optional<Node> getNode(Node node) {
        Iterator<Node> iterator = nodes.iterator();
        Optional<Node> result = Optional.empty();
        while (iterator.hasNext()) {
            Node next = iterator.next();
            if (next.equals(node)) {
                result = Optional.of(next);
            }
        }
        return result;
    }
}
