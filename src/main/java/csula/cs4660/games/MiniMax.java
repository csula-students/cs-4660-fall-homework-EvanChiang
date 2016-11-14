package csula.cs4660.games;

import csula.cs4660.games.models.MiniMaxState;
import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class MiniMax {
    public static Node getBestMove(Graph graph, Node root, Integer depth, Boolean max) {
        // TODO: implement minimax to retrieve best move
        // NOTE: you should mutate graph and node as you traverse and update value
        int i = minimax(graph, root, depth, max, root);

        ArrayList<Edge> newEdges = new ArrayList<>();
        MiniMaxState mms = (MiniMaxState) root.getData();
        MiniMaxState newMMS = new MiniMaxState(mms.getIndex(), i);
        Node newNode = new Node(newMMS);
        for (Node neighbor: graph.neighbors(root))
            newEdges.add(new Edge(newNode, neighbor, graph.distance(root, neighbor)));
        graph.removeNode(root);
        graph.addNode(newNode);
        for (Edge e: newEdges)
            graph.addEdge(e);

        //print(graph, root);

        for (Node n: graph.neighbors(root))
        {
            if (((MiniMaxState)n.getData()).getValue() == i)
                return n;
        }

        return null;
    }

    private static void print(Graph graph, Node source)
    {
        LinkedList<Node> searchQueue = new LinkedList<Node>();
        searchQueue.add(source);
        while (!searchQueue.isEmpty())
        {
            Node currentNode = searchQueue.pop();
            for (Node n: graph.neighbors(currentNode))
            {
                MiniMaxState mms = (MiniMaxState)n.getData();
                searchQueue.add(n);
                System.out.println(mms.getValue() + " " + mms.getIndex());
            }
        }
    }

    private static Integer minimax(Graph graph, Node node, Integer depth, Boolean player, Node parentNode)
    {

        MiniMaxState mms = (MiniMaxState)node.getData();
        if (player)
            mms = new MiniMaxState(mms.getIndex(), Integer.MIN_VALUE);
        else
            mms = new MiniMaxState(mms.getIndex(), Integer.MAX_VALUE);
        ArrayList<Edge> newEdges = new ArrayList<>();
        if (depth == 0 || graph.neighbors(node).isEmpty())
        {
            int i = ((MiniMaxState)node.getData()).getValue();
            return i;
        }
        Integer bestValue = 0;

        if (player) // if it's player's turn they will choose max
        {
            bestValue = Integer.MIN_VALUE;
            for (Node neighbor: graph.neighbors(node))
            {
                Integer nodeValue = minimax(graph, neighbor, depth - 1, !player, node);
                bestValue = Integer.max(bestValue, nodeValue);
            }
            if (mms.getValue() < bestValue)
            {
                MiniMaxState newMMS = new MiniMaxState(mms.getIndex(), bestValue);
                Node newNode = new Node(newMMS);
                for (Node neighbor: graph.neighbors(node))
                    newEdges.add(new Edge(newNode, neighbor, graph.distance(node, neighbor)));
                newEdges.add(new Edge(parentNode, newNode, graph.distance(parentNode, node)));
                graph.removeNode(node);
                graph.addNode(newNode);
                for (Edge e: newEdges)
                    graph.addEdge(e);
            }
            return  bestValue;
        }
        else // if it's not player's turn they will choose min
        {
            bestValue = Integer.MAX_VALUE;
            for (Node neighbor: graph.neighbors(node))
            {
                Integer nodeValue = minimax(graph, neighbor, depth - 1, !player, node);
                bestValue = Integer.min(bestValue, nodeValue);
            }
            if (mms.getValue() > bestValue)
            {
                MiniMaxState newMMS = new MiniMaxState(mms.getIndex(), bestValue);
                Node newNode = new Node(newMMS);
                for (Node neighbor: graph.neighbors(node))
                    newEdges.add(new Edge(newNode, neighbor, graph.distance(node, neighbor)));
                newEdges.add(new Edge(parentNode, newNode, graph.distance(parentNode, node)));
                graph.removeNode(node);
                graph.addNode(newNode);
                for (Edge e: newEdges)
                    graph.addEdge(e);
            }
            return  bestValue;
        }
    }
}
