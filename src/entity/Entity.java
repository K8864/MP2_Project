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

    private int hp;
    public int getHp() {return hp;}
    public void setHp(int hp) {this.hp = hp;}
    public void loseHp() {hp--;}
    private int maxHp;
    public int getMaxHp() {
        return maxHp;
    }
    public void setMaxHp(int maxHp) {this.maxHp = maxHp;}

    private int speed;
    public int getSpeed() {return speed;}
    public void setSpeed(int s) {speed = s;}

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
        gp.getcChecker().checkTile(this);
    }

    public void searchPath(int goalCol, int goalRow) {
        int startCol = (worldX + solidArea.x)/gp.getTileSize();
        int startRow = (worldY + solidArea.y)/gp.getTileSize();

        gp.getpFinder().setNode(startCol, startRow, goalCol, goalRow);

        if(gp.getpFinder().search()) {
            int nextX = gp.getpFinder().getPathList().get(0).getCol() * gp.getTileSize();
            int nextY = gp.getpFinder().getPathList().get(0).getRow() * gp.getTileSize();

            int enLeftX = worldX + solidArea.x;
            int enRightX = worldX + solidArea.x + solidArea.width;
            int enTopY = worldY + solidArea.y;
            int enBottomY = worldY + solidArea.y + solidArea.height;

            if(enTopY > nextY && enLeftX > nextX && enRightX < nextX + gp.getTileSize()) {
                direction = "up";
            }
            else if(enTopY < nextY && enLeftX < nextX && enRightX > nextX + gp.getTileSize()) {
                direction = "down";
            }
            else if(enTopY > nextY && enBottomY < nextY + gp.getTileSize()) {
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
                    setCollisionOn(false);
                    direction = "left";
                }
            }
            else if(enTopY > nextY && enLeftX < nextX) {
                direction = "up";
                checkCollision();
                if(collisionOn) {
                    setCollisionOn(false);
                    direction = "right";
                }
            }
            else if(enTopY < nextY && enLeftX > nextX) {
                direction = "down";
                checkCollision();
                if(collisionOn) {
                    setCollisionOn(false);
                    direction = "left";
                }
            }
            else if(enTopY < nextY && enLeftX < nextX) {
                direction = "down";
                checkCollision();
                if(collisionOn) {
                    setCollisionOn(false);
                    direction = "right";
                }
            }
            else if(enTopY < nextY)
                direction = "down";
            else if(enBottomY > nextY)
                direction = "up";
            else if(enLeftX < nextX)
                direction = "down";
            else if(enRightX > nextX)
                direction = "up";
        }
    }

    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX();
        int screenY = worldY - gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY();
        BufferedImage image = null;

        if(worldX + gp.getTileSize() > gp.getPlayer().getWorldX() - gp.getPlayer().getScreenX() &&
                worldX - gp.getTileSize() < gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX() &&
                worldY + gp.getTileSize() > gp.getPlayer().getWorldY() - gp.getPlayer().getScreenY() &&
                worldY - gp.getTileSize() < gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY()) {

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

            g2.drawImage(image, screenX, screenY, gp.getTileSize(), gp.getTileSize(), null);
        }
    }
}