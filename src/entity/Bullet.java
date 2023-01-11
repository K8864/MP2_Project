package entity;

import main.GamePanel;
import main.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Bullet extends Entity {
    static GamePanel gp;
    public static int ammo = 30;
    public double worldX, worldY, startX, startY, endX, endY, screenX, screenY, cosine, sine, slope;
    public int speed = 9;
    private static int frameR = 0;
    public BufferedImage pew;
    public int count = 0;


    public Bullet(GamePanel gp) {
        getBulletImage();
        Bullet.gp = gp;
    }

    public void getBulletImage() {
        try {
            pew = ImageIO.read(getClass().getResourceAsStream("/bullet/Bullet.png"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        solidArea = new Rectangle(21, 21, 6, 6);
        speed = 9;
        collisionOn = false;
    }

    public void target(int x, int y) {
        screenX = gp.player.screenX;
        screenY = gp.player.screenY;
        startX = gp.player.worldX;
        startY = gp.player.worldY;
        worldX = gp.player.worldX + 24;
        worldY = gp.player.worldY + 24;
        endX = gp.player.worldX - gp.player.screenX + x-24;
        endY = gp.player.worldY - gp.player.screenY + y-24;
        slope = Math.sqrt((endX - startX) * (endX - startX) + (endY - startY) * (endY - startY));
        cosine = (endX - startX)/slope;
        sine = (endY - startY)/slope;
        ammo--;
    }

    public void shoot(Bullet b) {
        gp.click.shot = false;
        gp.click.x = MouseInfo.getPointerInfo().getLocation().x- Main.window.getLocation().x;
        gp.click.y = MouseInfo.getPointerInfo().getLocation().y-Main.window.getLocation().y;
        b.target(gp.click.x, gp.click.y-30);
    }

    public static void reload() {
        if(frameR == 180) {
            Bullet.ammo = 30;
            frameR = 0;
        }
        else {
            frameR++;
            gp.click.shot = false;
        }
    }

    public void draw(Graphics2D g2) {
        double x = cosine * speed;
        double y = sine * speed;
        worldX += x;
        worldY += y;
        screenX += x + (startX - gp.player.worldX);
        screenY += y + (startY - gp.player.worldY);
        startX = gp.player.worldX;
        startY = gp.player.worldY;
        collisionOn = false;
        int n = gp.tileM.mapTileNum[(int)(worldX/gp.tileSize)][(int)(worldY/gp.tileSize)];
        if(gp.tileM.tile[n].collision && !gp.tileM.tile[n].flat) {
            collisionOn = true;
            startX = 1000000;
        }
        if(!collisionOn) {
            g2.drawImage(pew, (int) screenX, (int) screenY, gp.tileSize, gp.tileSize, null);
        }
        else
            g2.drawImage(pew, Integer.MAX_VALUE, Integer.MAX_VALUE, gp.tileSize, gp.tileSize, null);
    }
}