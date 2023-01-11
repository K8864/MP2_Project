package entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    public int worldX, worldY;
    public BufferedImage left, left2, down, down2, right, right2, up, up2;
    public String direction;
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public Rectangle solidArea;
    public boolean collisionOn = false;

    public int hp;
    public int speed;

    public boolean onPath = false;
}
