package entity;

import main.ClickDetection;
import main.GamePanel;
import main.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Bullet extends Entity {
    public static int ammo = 30;
    public double startX, startY, endX, endY, screenX, screenY, cosine, sine, slope;
    public int speed = 9;
    private static int frameR = 0;
    public BufferedImage pew;
    public int count = 0;


    public Bullet(GamePanel gp) {
        super(gp);
        getBulletImage();
    }

    public void getBulletImage() {
        try {
            pew = ImageIO.read(getClass().getResourceAsStream("/bullet/Bullet.png"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        solidArea = new Rectangle(21, 21, 6, 6);
        speed = 10;
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
        ClickDetection.shot = false;
        gp.clickChecker.x = MouseInfo.getPointerInfo().getLocation().x- Main.window.getLocation().x-7;
        gp.clickChecker.y = MouseInfo.getPointerInfo().getLocation().y-Main.window.getLocation().y-30;
        b.target(gp.clickChecker.x, gp.clickChecker.y);
    }

    public static void reload() {
        if(frameR == 180) {
            Bullet.ammo = 30;
            frameR = 0;
        }
        else {
            frameR++;
            ClickDetection.shot = false;
        }
    }

    public void draw(Graphics2D g2) {
        if(!collisionOn) {
            g2.drawImage(pew, (int) screenX, (int) screenY, gp.tileSize, gp.tileSize, null);
        }
    }

    public void update() {
        double x = cosine * speed;
        double y = sine * speed;
        worldX += x;
        worldY += y;
        screenX += x + (startX - gp.player.worldX);
        screenY += y + (startY - gp.player.worldY);
        startX = gp.player.worldX;
        startY = gp.player.worldY;
        collisionOn = false;
        int n = gp.tileM.mapTileNum[(worldX/gp.tileSize)][(worldY/gp.tileSize)];
        if(gp.tileM.tile[n].collision && !gp.tileM.tile[n].flat) {
            collisionOn = true;
            count = (int)(slope/speed) + 1;
        }
    }
}