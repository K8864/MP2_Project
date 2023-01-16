package ai;

import main.GamePanel;

import java.util.ArrayList;

public class PathFinder {
    private final GamePanel gp;
    private Node[][] node;
    private ArrayList<Node> openList = new ArrayList<>();
    private ArrayList<Node> pathList = new ArrayList<>();
    private Node startNode, goalNode, currentNode;
    private boolean goalReached = false;
    private int step = 0;

    public PathFinder(GamePanel gp) {
        this.gp = gp;
        instantiateNode();
    }

    public ArrayList<Node> getPathList() {
        return pathList;
    }

    public void instantiateNode() {
        node = new Node[gp.getMaxWorldCol()][gp.getMaxWorldRow()];
        int col = 0;
        int row = 0;
        while(col < gp.getMaxWorldCol() && row < gp.getMaxWorldRow()) {
            node[col][row] = new Node(col, row);
            col++;
            if(col == gp.getMaxWorldCol()) {
                col = 0;
                row++;
            }
        }
    }

    public void resetNodes() {
        int col = 0;
        int row = 0;
        while(col < gp.getMaxWorldCol() && row < gp.getMaxWorldRow()) {
            node[col][row].setOpen(false);
            node[col][row].setChecked(false);
            node[col][row].setSolid(false);
            col++;
            if(col == gp.getMaxWorldCol()) {
                col = 0;
                row++;
            }
        }
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    public void setNode(int startCol, int startRow, int goalCol, int goalRow) {
        resetNodes();
        startNode = node[startCol][startRow];
        currentNode = startNode;
        goalNode = node[goalCol][goalRow];
        openList.add(currentNode);
        int col = 0;
        int row = 0;
        while(col < gp.getMaxWorldCol() && row < gp.getMaxWorldRow()) {
            int tileNum = gp.getTileM().mapTileNum[col][row];
            if(gp.getTileM().tile[tileNum].isCollision())
                node[col][row].setSolid(true);
            getCost(node[col][row]);
            col++;
            if(col == gp.getMaxWorldCol()) {
                col = 0;
                row++;
            }
        }
    }

    public void getCost(Node node) {
        int xDistance = Math.abs(node.getCol() - startNode.getCol());
        int yDistance = Math.abs(node.getRow() - startNode.getRow());
        node.setgCost(xDistance + yDistance);
        xDistance = Math.abs(node.getCol() - goalNode.getCol());
        yDistance = Math.abs(node.getRow() - goalNode.getRow());
        node.sethCost(xDistance + yDistance);
        node.setfCost(node.getgCost() + node.gethCost());
    }

    public boolean search() {
        while(!goalReached && step < 500) {
            int col = currentNode.getCol();
            int row = currentNode.getRow();
            currentNode.setChecked(true);
            openList.remove(currentNode);
            if (row - 1 >= 0)
                openNode(node[col][row - 1]);
            if (col - 1 >= 0)
                openNode(node[col - 1][row]);
            if (row + 1 < gp.getMaxWorldRow())
                openNode(node[col][row + 1]);
            if (col + 1 < gp.getMaxWorldCol())
                openNode(node[col + 1][row]);
            int bestNodeIndex = 0;
            int bestNodefCost = 999;
            for (int i = 0; i < openList.size(); i++) {
                if (openList.get(i).getfCost() < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).getfCost();
                } else if (openList.get(i).getfCost() == bestNodefCost)
                    if (openList.get(i).getgCost() < openList.get(bestNodeIndex).getgCost())
                        bestNodeIndex = i;
            }
            if (openList.size() == 0)
                break;
            currentNode = openList.get(bestNodeIndex);
            if(currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
            step++;
        }
        return goalReached;
    }

    public void openNode(Node node) {
        if(!node.isOpen() && !node.isChecked() && !node.isSolid()) {
            node.setOpen(true);
            node.parent = currentNode;
            openList.add(node);
        }
    }

    public void trackThePath() {
        Node current = goalNode;
        while(current != startNode) {
            pathList.add(0, current);
            current = current.parent;
        }
    }
}
