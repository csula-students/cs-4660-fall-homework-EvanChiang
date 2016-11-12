package csula.cs4660.games;

import csula.cs4660.games.models.MiniMaxState;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;

import java.util.HashSet;
import java.util.LinkedList;

public class MiniMax {
    public static Node getBestMove(Graph graph, Node root, Integer depth, Boolean max) {
        // TODO: implement minimax to retrieve best move
        // NOTE: you should mutate graph and node as you traverse and update value
        LinkedList<Node> queue = new LinkedList<>();
        HashSet<Integer> visited = new HashSet<>();
        queue.add(root);
        Node current = queue.get(0);
        while (!queue.isEmpty())
        {
            Integer index = ((MiniMaxState)current.getData()).getIndex();
            System.out.print("visited: ");
            for (Integer i: visited)
            {
                System.out.print(i + " ");
            }
            System.out.println();
            System.out.println(visited.contains(index) + " " + index);
            while ((!graph.neighbors(current).isEmpty()) && (!visited.contains(index)))
            {
                for (Node n: graph.neighbors(current))
                {
                    queue.add(0, n);
                    System.out.println("adding index " + ((MiniMaxState)n.getData()).getIndex());
                }
                current = queue.get(0);
            }
            visited.add(index);
            System.out.println("visited added " + (index));
            current = queue.pop();
        }

        return null;
    }
}
