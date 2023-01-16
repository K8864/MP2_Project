package entity;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    private GamePanel gp;
    private int worldX, worldY;
    private BufferedImage left, left2, down, down2, right, right2, up, up2;
    private String direction;
    private int spriteCounter = 0;
    private int spriteNum = 1;
    private Rectangle solidArea;
    private boolean collisionOn = false;
    private boolean dead = false;

    public int hp;
    public int maxHp;
    public int speed;

    public boolean onPath = false;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public GamePanel getGp() {
        return gp;
    }

    public int getWorldX() {
        return worldX;
    }

    public void setWorldX(int x) {
        worldX = x;
    }

    public int getWorldY() {
        return worldY;
    }

    public void setWorldY(int y) {
        worldY = y;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public boolean isCollisionOn() {
        return collisionOn;
    }

    public void setCollisionOn(boolean collisionOn) {
        this.collisionOn = collisionOn;
    }

    public Rectangle getSolidArea() {
        return solidArea;
    }

    public void setSolidArea(Rectangle r) {
        solidArea = r;
    }

    public BufferedImage getLeft() {
        return left;
    }

    public BufferedImage getLeft2() {
        return left2;
    }

    public BufferedImage getDown() {
        return down;
    }

    public BufferedImage getDown2() {
        return down2;
    }

    public BufferedImage getRight() {
        return right;
    }

    public BufferedImage getRight2() {
        return right2;
    }

    public int getSpriteNum() {
        return spriteNum;
    }

    public void setSpriteNum(int n) {
        spriteNum = n;
    }

    public int getSpriteCounter() {
        return spriteCounter;
    }

    public void incrementSpriteCounter() {
        spriteCounter++;
    }

    public void resetSpriteCounter() {
        spriteCounter = 0;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String d) {
        direction = d;
    }

    public BufferedImage getUp() {
        return up;
    }

    public BufferedImage getUp2() {
        return up2;
    }

    public void setLeft(BufferedImage i) {left = i;}

    public void setLeft2(BufferedImage i) {
        left2 = i;
    }

    public void setDown(BufferedImage i) {
        down = i;
    }

    public void setDown2(BufferedImage i) {
        down2 = i;
    }

    public void setRight(BufferedImage i) {
        right = i;
    }

    public void setRight2(BufferedImage i) {
        right2 = i;
    }

    public void setUp(BufferedImage i) {
        up = i;
    }

    public void setUp2(BufferedImage i) {
        up2 = i;
    }

    public void checkCollision() {
        collisionOn = false;
        gp.cChecker.checkTile(this);
    }

    public void searchPath(int goalCol, int goalRow) {
        int startCol = (worldX + solidArea.x)/gp.tileSize;
        int startRow = (worldY + solidArea.y)/gp.tileSize;

        gp.pFinder.setNode(startCol, startRow, goalCol, goalRow);

        if(gp.pFinder.search()) {
            int nextX = gp.pFinder.getPathList().get(0).getCol() * gp.tileSize;
            int nextY = gp.pFinder.getPathList().get(0).getRow() * gp.tileSize;

            int enLeftX = worldX + solidArea.x;
            int enRightX = worldX + solidArea.x + solidArea.width;
            int enTopY = worldY + solidArea.y;
            int enBottomY = worldY + solidArea.y + solidArea.height;

            if(enTopY > nextY && enLeftX > nextX && enRightX < nextX + gp.tileSize) {
                direction = "up";
            }
            else if(enTopY < nextY && enLeftX < nextX && enRightX > nextX + gp.tileSize) {
                direction = "down";
            }
            else if(enTopY > nextY && enBottomY < nextY + gp.tileSize) {
                if(enLeftX >= nextX) {
                    direction = "left";
                }
                if(enLeftX <= nextX) {
                    direction = "right";
                }
            }
            else if(enTopY > nextY && enLeftX > nextX) {
                direction = "up";
                checkCollision();
                if(collisionOn) {
                    direction = "left";
                }
            }
            else if(enTopY > nextY && enLeftX < nextX) {
                direction = "up";
                checkCollision();
                if(collisionOn) {
                    direction = "right";
                }
            }
            else if(enTopY < nextY && enLeftX > nextX) {
                direction = "down";
                checkCollision();
                if(collisionOn) {
                    direction = "left";
                }
            }
            else if(enTopY < nextY && enLeftX < nextX) {
                direction = "down";
                checkCollision();
                if(collisionOn) {
                    direction = "right";
                }
            }
            else if(enTopY < nextY)
                direction = "down";
            else if(enBottomY > nextY)
                direction = "up";
        }
    }

    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.getWorldX() + gp.player.getScreenX();
        int screenY = worldY - gp.player.getWorldY() + gp.player.getScreenY();
        BufferedImage image = null;

        if(worldX + gp.tileSize > gp.player.getWorldX() - gp.player.getScreenX() &&
                worldX - gp.tileSize < gp.player.getWorldX() + gp.player.getScreenX() &&
                worldY + gp.tileSize > gp.player.getWorldY() - gp.player.getScreenY() &&
                worldY - gp.tileSize < gp.player.getWorldY() + gp.player.getScreenY()) {

            switch(direction) {
                case "up":
                    if(spriteNum == 1)
                        image = up;
                    else
                        image = up2;
                    break;
                case "down":
                    if(spriteNum == 1)
                        image = down;
                    else
                        image = down2;
                    break;
                case "left":
                    if(spriteNum == 1)
                        image = left;
                    else
                        image = left2;
                    break;
                case "right":
                    if(spriteNum == 1)
                        image = right;
                    else
                        image = right2;
                    break;
            }

            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }
}