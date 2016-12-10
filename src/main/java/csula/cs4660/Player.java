
import javafx.scene.effect.Light;

import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class LightCycle{
    public int x;
    public int y;
    public int number;

    LightCycle(){}

    LightCycle(int number)
    {
        this.number = number;
    }

    LightCycle(int x, int y, int number)
    {
        this.x = x;
        this.y = y;
        this.number = number;
    }
}

class Player {

    private static int[][] grid;
    private static ArrayList<LightCycle> playerList = new ArrayList<>();
    private static ArrayList<Integer> deadList = new ArrayList<>();
    private static int lastCalc = 0;
    private static int sectionCounter = 0;
    private static int depth;

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        boolean initalized = false;
        int myNumber = 0;
        LightCycle me;
        initializeGrid();
        String nextMove = "LEFT";
        ArrayList<Integer> liveList = new ArrayList<>();
        LightCycle enemy = new LightCycle();
        long startTime;
        long endTime;
        long elsapsedTime;
        int currentNumberOfPlayers = 0;
        // game loop
        while (true) {
            startTime = System.currentTimeMillis();
            int N = in.nextInt(); // total number of players (2 to 4).
            int P = in.nextInt() + 1; // your player number (0 to 3). Changed to 1-4
            if (!initalized)
            {
                currentNumberOfPlayers = N;
                myNumber = P;
                for (int n = 0; n < N; n++)
                {
                    playerList.add(new LightCycle(n+1));
                    liveList.add(n+1);
                }
            }

            for (int i = 0; i < N; i++) { // player number is i + 1
                int X0 = in.nextInt(); // starting X coordinate of lightcycle (or -1)
                int Y0 = in.nextInt(); // starting Y coordinate of lightcycle (or -1)
                int X1 = in.nextInt(); // starting X coordinate of lightcycle (can be the same as X0 if you play before this player)
                int Y1 = in.nextInt(); // starting Y coordinate of lightcycle (can be the same as Y0 if you play before this player)
                if (!initalized)
                {
                    if (X0 > 0 && Y0 > 0)
                        grid[X0][Y0] = i + 1;
                    else
                        currentNumberOfPlayers--;
                }
                if (X1 == -1)
                {
                    if (!deadList.contains(i+1))
                    {
                        removePlayer(i+1);
                        deadList.add(i+1);
                        currentNumberOfPlayers--;
                        if (currentNumberOfPlayers == 2)
                        {

                        }
                    }
                }
                else
                {
                    grid[X1][Y1] = i + 1;
                    playerList.get(i).x = X1;
                    playerList.get(i).y = Y1;
                }
            }
            if (!initalized)
                initalized = true;

            //printGrid();
            me = playerList.get(myNumber-1);
//            if (currentNumberOfPlayers == 2)
//                nextMove = oneVoneMeBro(me, enemy);
//            else


            if (sectionCounter >= 3)
                depth = 30;
            else if (currentNumberOfPlayers == 4)
                depth = 2;
            else if (currentNumberOfPlayers == 3)
                depth = 4;
            else
                depth = 6;


            nextMove = moveToMoreSpace(me.x, me.y, me.number, depth);
            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");
            endTime = System.currentTimeMillis();
            elsapsedTime = endTime - startTime;

            System.err.println("x: " + me.x + " y: " + me.y + " time: " + elsapsedTime);
            System.out.println(nextMove); // A single line with UP, DOWN, LEFT or RIGHT
        }
    }

    private static String oneVoneMeBro(LightCycle me, LightCycle enemy)
    {
        return moveTowardsEnemy(me, enemy);
    }

    private static double heuristic(LightCycle source, LightCycle dest)
    {
        double scale = .5;
        return scale * Math.sqrt(Math.pow(source.x - dest.x, 2) + Math.pow(source.y - dest.y, 2));
    }

    private static String moveTowardsEnemy(LightCycle me, LightCycle enemy) {
        ArrayList<LightCycle> returnList = new ArrayList<>();
        LinkedList<LightCycle> searchQueue = new LinkedList<>();
        HashSet<LightCycle> visited = new HashSet<>();
        HashMap<LightCycle, LightCycle> parentMap = new HashMap<>(); //key is child, value is parent
        HashMap<LightCycle, Double> distanceMap = new HashMap<>(); //the key's distance away from source
        HashMap<LightCycle, Double> heuristicMap = new HashMap<>();
        searchQueue.add(me);
        distanceMap.put(me, 0.0);
        heuristicMap.put(me, heuristic(me, enemy));
        ArrayList<LightCycle> neighborsList = new ArrayList<>();


        while (!searchQueue.isEmpty()) {
            LightCycle currentNode = searchQueue.pop();
            System.err.println(currentNode.x + " " + currentNode.y);
            if (currentNode.x == enemy.x && currentNode.y == enemy.y) {
                //build movement and return
                LightCycle child = enemy;
                LightCycle parent = parentMap.get(child);
                while (!child.equals(me)) {
                    returnList.add(0, new LightCycle(child.x, child.y, me.number));
                    child = parent;
                    parent = parentMap.get(child);
                }
                LightCycle bestMove = returnList.get(0);
                int deltaX = bestMove.x - me.x;
                int deltaY = bestMove.y - me.y;
                if (deltaX == 1)
                    return "RIGHT";
                else if (deltaX == -1)
                    return "LEFT";
                else if (deltaY == 1)
                    return "DOWN";
                else
                    return "UP";
            }
            visited.add(currentNode);

            if (!isDeath(currentNode.x - 1, currentNode.y, grid))
                neighborsList.add(new LightCycle(currentNode.x - 1, currentNode.y, me.number));
            if (!isDeath(currentNode.x + 1, currentNode.y, grid))
                neighborsList.add(new LightCycle(currentNode.x + 1, currentNode.y, me.number));
            if (!isDeath(currentNode.x, currentNode.y - 1, grid))
                neighborsList.add(new LightCycle(currentNode.x - 1, currentNode.y - 1, me.number));
            if (!isDeath(currentNode.x, currentNode.y + 1, grid))
                neighborsList.add(new LightCycle(currentNode.x - 1, currentNode.y + 1, me.number));
            //for every adjacency

            for (LightCycle neighbor : neighborsList) {
                double distance = 1 + distanceMap.get(currentNode);
                if (!distanceMap.containsKey(neighbor))
                    distanceMap.put(neighbor, distance);
                if (visited.contains(neighbor)) {
                    System.err.println("been here!");
                    if (distanceMap.get(neighbor) > distance) {
                        parentMap.remove(neighbor);
                        distanceMap.remove(neighbor);
                        parentMap.put(neighbor, currentNode);
                        distanceMap.put(neighbor, distance);
                    }
                    continue;
                }
                double heuristicNeighbor = distance + heuristic(neighbor, enemy);
                heuristicMap.put(neighbor, heuristicNeighbor);
                if (searchQueue.isEmpty())
                    searchQueue.add(neighbor);
                else if (distance >= distanceMap.get(neighbor))
                    continue;
                else
                {
                    if (!searchQueue.contains(neighbor)) {
                        for (int i = searchQueue.size() - 1; i >= 0; i--) {
                            double heuristicNodeInQueue = heuristic(searchQueue.get(i), enemy);
                            if (heuristicNeighbor > heuristicNodeInQueue) {
                                if (i == searchQueue.size() - 1)
                                    searchQueue.add(neighbor);
                                else
                                    searchQueue.add(i + 1, neighbor);
                                i = -1;
                            } else if (heuristicNeighbor == heuristicNodeInQueue) {
                                LightCycle queueNode = searchQueue.get(i);
                                double deltaX = neighbor.x - queueNode.x;
                                double deltaY = neighbor.y - queueNode.y;
                                if (deltaY > 0)
                                    searchQueue.add(i + 1, neighbor);
                                else if (deltaY < 0)
                                    searchQueue.add(i, neighbor);
                                else if (deltaX > 0)
                                    searchQueue.add(i + 1, neighbor);
                                else
                                    searchQueue.add(i, neighbor);
                            } else if (i == 0)
                                searchQueue.add(0, neighbor);
                        }
                    }
                }
                parentMap.put(neighbor, currentNode);
                distanceMap.put(neighbor, distance);
            }
            neighborsList.clear();
        }
        return "LEFT";
    }

    private static void removePlayer(int playerNumber)
    {
        for (int i = 0; i < 30; i++)
        {
            for (int j = 0; j < 20; j++)
            {
                if (grid[i][j] == playerNumber)
                    grid[i][j] = 0;
            }
        }
    }
//
//    private static String moveToCutOff(LightCycle me, int depth)
//    {
//        HashSet<String> deathSet = new HashSet<>();
//        String currentMove = "LEFT";
//        int leftCalc = 0;
//        int rightCalc = 0;
//        int upCalc = 0;
//        int downCalc = 0;
//
//        if (isDeath(me.x-1, me.y, grid))
//        {
//            deathSet.add("LEFT");
//            leftCalc = -1;
//            currentMove = "RIGHT";
//        }
//        if (isDeath(me.x+1, me.y, grid))
//        {
//            rightCalc = -1;
//            currentMove = "UP";
//            deathSet.add("RIGHT");
//        }
//        if (isDeath(me.x, me.y-1, grid))
//        {
//            upCalc = -1;
//            currentMove = "DOWN";
//            deathSet.add("UP");
//        }
//        if (isDeath(me.x, me.y+1, grid))
//        {
//            downCalc = -1;
//            deathSet.add("DOWN");
//        }
//        if (deathSet.size() == 4)
//            return "LEFT";
//
//
//        return currentMove;
//    }
//
//    private static boolean cutOffCalc(int newX, int newY, int playerNumber, int depth, int[][] tempGrid)
//    {
//        int[][] gridCopy = new int[30][20];
//        copyGrid(tempGrid, gridCopy);
//        gridCopy[newX][newY] = playerNumber;
//
//        int count = 0;
//        LinkedList<LightCycle> queue = new LinkedList<>();
//        for (int i = 0; i < playerList.size(); i++)
//        {
//            if (playerNumber == playerList.get(i).number)
//                queue.add(new LightCycle(newX, newY, playerNumber));
//            else
//                queue.add(playerList.get(i));
//        }
//        while (!queue.isEmpty())
//        {
//            LightCycle lc = queue.pop();
//            if (!isDeath(lc.x-1, lc.y, gridCopy))
//            {
//                gridCopy[lc.x-1][lc.y] = lc.number;
//                queue.add(new LightCycle(lc.x-1, lc.y, lc.number));
//                if (playerNumber == lc.number)
//                    count++;
//            }
//            if (!isDeath(lc.x+1, lc.y, gridCopy))
//            {
//                gridCopy[lc.x+1][lc.y] = lc.number;
//                queue.add(new LightCycle(lc.x+1, lc.y, lc.number));
//                if (playerNumber == lc.number)
//                    count++;
//            }
//            if (!isDeath(lc.x, lc.y-1, gridCopy))
//            {
//                gridCopy[lc.x][lc.y-1] = lc.number;
//                queue.add(new LightCycle(lc.x, lc.y-1, lc.number));
//                if (playerNumber == lc.number)
//                    count++;
//            }
//            if (!isDeath(lc.x, lc.y+1, gridCopy))
//            {
//                gridCopy[lc.x][lc.y+1] = lc.number;
//                queue.add(new LightCycle(lc.x, lc.y+1, lc.number));
//                if (playerNumber == lc.number)
//                    count++;
//            }
//
//
//        }
//        else
//        {
//            int left = -1;
//            int right = -1;
//            int down = -1;
//            int up = -1;
//
//            if (!isDeath(newX-1, newY, gridCopy))
//            {
////                int[][] leftGrid = new int[30][20];
////                copyGrid(gridCopy, leftGrid);
//                left = calculateFloodFill(newX-1, newY, playerNumber, depth-1, gridCopy);
//            }
//            if (!isDeath(newX+1, newY, gridCopy))
//            {
////                int[][] rightGrid = new int[30][20];
////                copyGrid(gridCopy, rightGrid);
//                right = calculateFloodFill(newX+1, newY, playerNumber, depth-1, gridCopy);
//            }
//            if (!isDeath(newX, newY-1, gridCopy))
//            {
////                int[][] upGrid = new int[30][20];
////                copyGrid(gridCopy, upGrid);
//                up = calculateFloodFill(newX, newY-1, playerNumber, depth-1, gridCopy);
//            }
//            if (!isDeath(newX, newY+1, gridCopy))
//            {
////                int[][] downGrid = new int[30][20];
////                copyGrid(gridCopy, downGrid);
//                down = calculateFloodFill(newX, newY+1, playerNumber, depth-1, gridCopy);
//            }
//            int max1 = Math.max(left, right);
//            int max2 = Math.max(down, up);
//            return Math.max(max1, max2);
//        }
//    }

    private static String moveToMoreSpace(int x, int y, int myNumber, int depth)
    {
        HashSet<String> deathSet = new HashSet<>();
        String currentMove = "LEFT";
        int leftCalc = 0;
        int rightCalc = 0;
        int upCalc = 0;
        int downCalc = 0;

        if (isDeath(x-1, y, grid))
        {
            deathSet.add("LEFT");
            leftCalc = -1;
            currentMove = "RIGHT";
        }
        if (isDeath(x+1, y, grid))
        {
            rightCalc = -1;
            currentMove = "UP";
            deathSet.add("RIGHT");
        }
        if (isDeath(x, y-1, grid))
        {
            upCalc = -1;
            currentMove = "DOWN";
            deathSet.add("UP");
        }
        if (isDeath(x, y+1, grid))
        {
            downCalc = -1;
            deathSet.add("DOWN");
        }
        if (deathSet.size() == 4)
            return "LEFT";

        if (!deathSet.contains("LEFT"))
            leftCalc += calculateFloodFill(x-1, y, myNumber, depth, grid);
        if (!deathSet.contains("RIGHT"))
            rightCalc += calculateFloodFill(x+1, y, myNumber, depth, grid);
        if (!deathSet.contains("DOWN"))
            downCalc += calculateFloodFill(x, y+1, myNumber, depth, grid);
        if (!deathSet.contains("UP"))
            upCalc += calculateFloodFill(x, y-1, myNumber, depth, grid);

        int max1 = Math.max(leftCalc, rightCalc);
        int max2 = Math.max(downCalc, upCalc);
        int greatest = Math.max(max1, max2);

        if (greatest == leftCalc-1)
            sectionCounter++;
        else
            sectionCounter = 0;

        lastCalc = greatest;

        System.err.println("player: " + myNumber + " left: " + leftCalc + " right: " + rightCalc + " down: " + downCalc + " up: " + upCalc);
        if (greatest == leftCalc)
        {
            grid[x-1][y] = myNumber;
            currentMove = "LEFT";
        }
        else if (greatest == rightCalc)
        {
            grid[x+1][y] = myNumber;
            currentMove = "RIGHT";
        }
        else if (greatest == downCalc)
        {
            grid[x][y+1] = myNumber;
            currentMove = "DOWN";
        }
        else if (greatest == upCalc)
        {
            grid[x][y-1] = myNumber;
            currentMove = "UP";
        }


        return currentMove;
    }

    private static Integer calculateFloodFill(int newX, int newY, int playerNumber, int depth, int[][] tempGrid)
    {
        int[][] gridCopy = new int[30][20];
        copyGrid(tempGrid, gridCopy);
        gridCopy[newX][newY] = playerNumber;

        if (depth == 1)
        {
            int count = 0;
            LinkedList<LightCycle> queue = new LinkedList<>();
            for (int i = 0; i < playerList.size(); i++)
            {
                if (playerNumber == playerList.get(i).number)
                    queue.add(new LightCycle(newX, newY, playerNumber));
                else
                    queue.add(playerList.get(i));
            }
            while (!queue.isEmpty())
            {
                LightCycle lc = queue.pop();
                if (!isDeath(lc.x-1, lc.y, gridCopy))
                {
                    gridCopy[lc.x-1][lc.y] = lc.number;
                    queue.add(new LightCycle(lc.x-1, lc.y, lc.number));
                    if (playerNumber == lc.number)
                        count++;
                }
                if (!isDeath(lc.x+1, lc.y, gridCopy))
                {
                    gridCopy[lc.x+1][lc.y] = lc.number;
                    queue.add(new LightCycle(lc.x+1, lc.y, lc.number));
                    if (playerNumber == lc.number)
                        count++;
                }
                if (!isDeath(lc.x, lc.y-1, gridCopy))
                {
                    gridCopy[lc.x][lc.y-1] = lc.number;
                    queue.add(new LightCycle(lc.x, lc.y-1, lc.number));
                    if (playerNumber == lc.number)
                        count++;
                }
                if (!isDeath(lc.x, lc.y+1, gridCopy))
                {
                    gridCopy[lc.x][lc.y+1] = lc.number;
                    queue.add(new LightCycle(lc.x, lc.y+1, lc.number));
                    if (playerNumber == lc.number)
                        count++;
                }
            }
            return count;
        }
        else
        {
            int left = -1;
            int right = -1;
            int down = -1;
            int up = -1;
//            for (LightCycle player : playerList)
//            {
//                if (!deadList.contains(player.number) && player.number != playerNumber)
//                {
//                    String enemyMove = moveToMoreSpace(player.x, player.y, player.number, depth-1);
//                    if (enemyMove == "LEFT")
//                        gridCopy[player.x-1][player.y] = player.number;
//                    else if (enemyMove == "RIGHT")
//                        gridCopy[player.x+1][player.y] = player.number;
//                    else if (enemyMove == "DOWN")
//                        gridCopy[player.x][player.y+1] = player.number;
//                    else
//                        gridCopy[player.x][player.y-1] = player.number;
//                }
//            }
            if (!isDeath(newX-1, newY, gridCopy))
            {
//                int[][] leftGrid = new int[30][20];
//                copyGrid(gridCopy, leftGrid);
                left = calculateFloodFill(newX-1, newY, playerNumber, depth-1, gridCopy);
            }
            if (!isDeath(newX+1, newY, gridCopy))
            {
//                int[][] rightGrid = new int[30][20];
//                copyGrid(gridCopy, rightGrid);
                right = calculateFloodFill(newX+1, newY, playerNumber, depth-1, gridCopy);
            }
            if (!isDeath(newX, newY-1, gridCopy))
            {
//                int[][] upGrid = new int[30][20];
//                copyGrid(gridCopy, upGrid);
                up = calculateFloodFill(newX, newY-1, playerNumber, depth-1, gridCopy);
            }
            if (!isDeath(newX, newY+1, gridCopy))
            {
//                int[][] downGrid = new int[30][20];
//                copyGrid(gridCopy, downGrid);
                down = calculateFloodFill(newX, newY+1, playerNumber, depth-1, gridCopy);
            }
            int max1 = Math.max(left, right);
            int max2 = Math.max(down, up);
            return Math.max(max1, max2);
        }
    }

    private static void copyGrid(int[][] gridToCopy, int[][] emptyGrid)
    {
        for (int i = 0; i < 30; i++)
        {
            for (int j = 0; j < 20; j++)
            {
                emptyGrid[i][j] = gridToCopy[i][j];
            }
        }
    }

    private static void initializeGrid()
    {
        grid = new int[30][20]; // grid slot will be 0 if empty. If not empty, it will contain the number of the player? Or just 1
        for (int i = 0; i < 30; i++)
        {
            for (int j = 0; j < 20; j++)
            {
                grid[i][j] = 0;
            }
        }
    }

    private static void printGrid()
    {
        for (int y = 0; y < 20; y++)
        {
            for (int x = 0; x < 30; x++)
            {
                System.err.print(grid[x][y] + " ");
            }
            System.err.println();
        }
    }


    private static boolean isDeath(int x, int y, int[][] g)
    {
        if (x < 0 || x > 29 || y < 0 || y > 19)
            return true;
        if (g[x][y] != 0)
            return true;
        return false;
    }
}