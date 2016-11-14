package csula.cs4660.games;

import csula.cs4660.games.models.MiniMaxState;
import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class AlphaBeta {
    public static Node getBestMove(Graph graph, Node source, Integer depth, Integer alpha, Integer beta, Boolean max) {

        int i = alphaBeta(graph, source, depth, max, source, Integer.MIN_VALUE, Integer.MAX_VALUE);

        ArrayList<Edge> newEdges = new ArrayList<>();
        MiniMaxState mms = (MiniMaxState) source.getData();
        MiniMaxState newMMS = new MiniMaxState(mms.getIndex(), i);
        Node newNode = new Node(newMMS);
        for (Node neighbor: graph.neighbors(source))
            newEdges.add(new Edge(newNode, neighbor, graph.distance(source, neighbor)));
        graph.removeNode(source);
        graph.addNode(newNode);
        for (Edge e: newEdges)
            graph.addEdge(e);

        for (Node n: graph.neighbors(source))
        {
            if (((MiniMaxState)n.getData()).getValue() == i)
                return n;
        }

        return null;
    }

    private static Integer alphaBeta(Graph graph, Node node, Integer depth, Boolean player, Node parentNode,
                                     Integer alpha, Integer beta)
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
                Integer nodeValue = alphaBeta(graph, neighbor, depth - 1, !player, node, alpha, beta);
                bestValue = Integer.max(bestValue, nodeValue);
                if (bestValue >= beta)
                {
                    if (mms.getValue() > bestValue)
                    {
                        MiniMaxState newMMS = new MiniMaxState(mms.getIndex(), bestValue);
                        Node newNode = new Node(newMMS);
                        for (Node n: graph.neighbors(node))
                            newEdges.add(new Edge(newNode, n, graph.distance(node, n)));
                        newEdges.add(new Edge(parentNode, newNode, graph.distance(parentNode, node)));
                        graph.removeNode(node);
                        graph.addNode(newNode);
                        for (Edge e: newEdges)
                            graph.addEdge(e);
                    }
                    return bestValue;
                }
                else
                    alpha = bestValue;
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
                Integer nodeValue = alphaBeta(graph, neighbor, depth - 1, !player, node, alpha, beta);
                bestValue = Integer.min(bestValue, nodeValue);
                if (bestValue <= alpha)
                {
                    if (mms.getValue() > bestValue)
                    {
                        MiniMaxState newMMS = new MiniMaxState(mms.getIndex(), bestValue);
                        Node newNode = new Node(newMMS);
                        for (Node n: graph.neighbors(node))
                            newEdges.add(new Edge(newNode, n, graph.distance(node, n)));
                        newEdges.add(new Edge(parentNode, newNode, graph.distance(parentNode, node)));
                        graph.removeNode(node);
                        graph.addNode(newNode);
                        for (Edge e: newEdges)
                            graph.addEdge(e);
                    }
                    return bestValue;
                }
                else
                    beta = bestValue;
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
