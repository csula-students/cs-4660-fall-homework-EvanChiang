package csula.cs4660.graphs.representations;

import csula.cs4660.games.models.Tile;
import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Node;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Adjacency matrix in a sense store the nodes in two dimensional array
 *
 * TODO: please fill the method body of this class
 */
public class AdjacencyMatrix implements Representation {
    private Node[] nodes;
    private int[][] adjacencyMatrix;

    public AdjacencyMatrix(File file)
    {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            int count = Integer.parseInt(line);
            nodes = new Node[count];
            adjacencyMatrix = new int[count][count];
            for (int i = 0; i < count; i++)
            {
                int[] column = new int[count];
                adjacencyMatrix[i] = column;
                nodes[i] = new Node(i);
            }
            line = br.readLine();
            while (line != null)
            {
                String[] data = line.split(":");
                int[] idata = new int[data.length];
                for (int j = 0; j < data.length; j++) {
                    idata[j] = Integer.parseInt(data[j]);
                }
                Node n = new Node(idata[0]);
                adjacencyMatrix[idata[0]][idata[1]] = idata[2];
                line = br.readLine();
            }
//            for (int i = 0; i < adjacencyMatrix.length; i++)
//            {
//                for (int j = 0; j < adjacencyMatrix.length; j++)
//                {
//                    System.out.print(adjacencyMatrix[i][j] + "");
//                }
//                System.out.println();
//            }
            br.close();
        }catch(IOException e){}
    }

    public AdjacencyMatrix() {
        nodes = new Node[0];
        adjacencyMatrix = new int[0][0];
    }

    @Override
    public boolean adjacent(Node x, Node y) {
        int row = -1;
        int column = -1;
        for (int i = 0; i < nodes.length; i++)
        {
            if (nodes[i].equals(x))
                row = i;
            if (nodes[i].equals(y))
                column = i;
        }
        if (row > -1 && column > -1)
        {
            return (adjacencyMatrix[row][column] == 0 ? false : true);
        }
        else
            return false;
    }

    @Override
    public List<Node> neighbors(Node x) {
        ArrayList<Node> neighbors = new ArrayList<>();
        int row = -1;
        for (int i = 0; i < nodes.length; i++)
        {
            if (nodes[i].equals(x))
                row = i;
        }
        if (row > -1)
        {
            for (int j = 0; j < adjacencyMatrix[row].length; j++)
            {
                if (adjacencyMatrix[row][j] != 0)
                    neighbors.add(nodes[j]);
            }
        }
        return neighbors;
    }

    @Override
    public boolean addNode(Node x)
    {
        if (nodes.length == 0)
        {
            nodes = new Node[1];
            nodes[0] = x;
            adjacencyMatrix = new int[1][1];
            adjacencyMatrix[0][0] = 0;
        }
        else
        {
            Node[] temp = new Node[nodes.length + 1];
            for (int i = 0; i < nodes.length; i ++)
            {
                if (nodes[i].equals(x))
                    return false;
                temp[i] = nodes[i];
            }
            temp[nodes.length] = x;
            nodes = temp;

            int[][] tempMatrix = new int[adjacencyMatrix.length+1][adjacencyMatrix.length+1];
            for (int i = 0; i < adjacencyMatrix.length; i++)
            {
                for (int j = 0; j < adjacencyMatrix.length; j++)
                    tempMatrix[i][j] = adjacencyMatrix[i][j]; //copy matrix
            }
            for (int i = 0; i < adjacencyMatrix.length + 1; i++)
                tempMatrix[adjacencyMatrix.length][i] = 0; //set new column to 0;
            for (int i = 0; i < adjacencyMatrix.length + 1; i++)
                tempMatrix[i][adjacencyMatrix.length] = 0; //set new row to 0;

            adjacencyMatrix = tempMatrix;
        }

        return true;
    }

    @Override
    public boolean removeNode(Node x)
    {
        Node[] temp = new Node[nodes.length - 1];
        int count = 0;
        int index = -1;
        for (int i = 0; i < nodes.length; i ++)
        {
            if (nodes[i].equals(x))
                index = i;
        }
        if (index > -1)
        {
            for (int i = 0; i < nodes.length; i ++)
            {
                if (i != index)
                {
                    temp[count] = nodes[i];
                    count++;
                }
            }
        }
        else
            return false;
        int countRow = 0;
        int countColumn = 0;
        int[][] tempMatrix = new int[nodes.length - 1][nodes.length - 1];
        for (int i = 0; i < nodes.length; i++)
        {
            if (i != index)
            {
                for (int j = 0; j < nodes.length; j++)
                {
                    countColumn = 0;
                    if (j != index)
                    {
                        tempMatrix[countRow][countColumn] = adjacencyMatrix[i][j];
                        countColumn++;
                    }
                }
                countRow++;
            }
        }

        nodes = temp;
        adjacencyMatrix = tempMatrix;
        return true;
    }

    @Override
    public boolean addEdge(Edge x)
    {
        int indexFrom = -1;
        int indexTo = -1;
        for (int i = 0; i < nodes.length; i++)
        {
            if (nodes[i].equals(x.getFrom()))
                indexFrom = i;
            if (nodes[i].equals(x.getTo()))
                indexTo = i;
        }
        if (indexFrom > -1 && indexTo > -1)
        {
            if (adjacencyMatrix[indexFrom][indexTo] != 0)
                return false;
            else
            {
                adjacencyMatrix[indexFrom][indexTo] = x.getValue();
                return true;
            }
        }
        else
            return false;
    }

    @Override
    public boolean removeEdge(Edge x) {
        int indexFrom = -1;
        int indexTo = -1;
        for (int i = 0; i < nodes.length; i ++)
        {
            if (nodes[i].equals(x.getFrom()))
                indexFrom = i;
            if (nodes[i].equals(x.getTo()))
                indexTo = i;
        }
        if (indexFrom > -1 && indexTo > -1)
        {
            if (adjacencyMatrix[indexFrom][indexTo] == 0)
                return false;
            else
            {
                adjacencyMatrix[indexFrom][indexTo] = 0;
                return true;
            }

        }
        else
            return false;
    }

    @Override
    public int distance(Node from, Node to) {
        int indexFrom = -1;
        int indexTo = -1;
        for (int i = 0; i < nodes.length; i ++)
        {
            if (nodes[i].equals(from))
                indexFrom = i;
            if (nodes[i].equals(to))
                indexTo = i;
        }
        if (indexFrom > -1 && indexTo > -1)
        {
            return adjacencyMatrix[indexFrom][indexTo];
        }
        else
            return -1;
    }

    @Override
    public Optional<Node> getNode(int index) {
        return null;
    }
}
