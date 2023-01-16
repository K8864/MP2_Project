package entity;

import main.ClickDetection;
import main.GamePanel;
import main.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Bullet extends Entity {
    private static int ammo = 30;
    private double startX, startY, endX, endY, screenX, screenY, cosine, sine, slope;
    private int speed = 9;
    private static int frameR = 0;
    private BufferedImage pew;
    private int count = 0;


    public Bullet(GamePanel gp) {
        super(gp);
        getBulletImage();
        //worldX = gp.player.worldX;
        setWorldX(getGp().player.getWorldX());
        //worldY = gp.player.worldY;
        setWorldY(getGp().player.getWorldY());
        screenX = gp.player.getScreenX();
        screenY = gp.player.getScreenY();
        startX = gp.player.getWorldX();
        startY = gp.player.getWorldY();
    }

    public void getBulletImage() {
        try {
            pew = ImageIO.read(getClass().getResourceAsStream("/bullet/Bullet.png"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        setSolidArea(new Rectangle(21, 21, 6, 6));
        speed = 10;
        setCollisionOn(false);
    }

    public static int getAmmo() {
        return ammo;
    }

    public static void setAmmo(int a) {
        ammo = a;
    }

    public double getSlope() {
        return slope;
    }

    public int getSpeed() {
        return speed;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int c) {
        count = c;
    }

    public void target(int x, int y) {
        screenX = getGp().player.getScreenX();
        screenY = getGp().player.getScreenY();
        startX = getGp().player.getWorldX();
        startY = getGp().player.getWorldY();
        endX = getGp().player.getWorldX() - getGp().player.getScreenX() + x-24;
        endY = getGp().player.getWorldY() - getGp().player.getScreenY() + y-24;
        slope = Math.sqrt((endX - startX) * (endX - startX) + (endY - startY) * (endY - startY));
        cosine = (endX - startX)/slope;
        sine = (endY - startY)/slope;
    }

    public void shoot(Bullet b) {
        ClickDetection.setShot(false);
        b.target(ClickDetection.getX(), ClickDetection.getY());
        ammo--;
    }

    public static void reload() {
        if(frameR == 180) {
            Bullet.ammo = 30;
            frameR = 0;
        }
        else {
            frameR++;
            ClickDetection.setShot(false);
        }
        ClickDetection.setClick(false);
    }

    public void draw(Graphics2D g2) {
        if(!isCollisionOn()) {
            g2.drawImage(pew, (int) screenX, (int) screenY, getGp().tileSize, getGp().tileSize, null);
        }
    }

    public void update() {
        if(!isCollisionOn()) {
            double x = cosine * speed;
            double y = sine * speed;
            //worldX += x;
            setWorldX(getWorldX()+(int)x);
            //worldY += y;
            setWorldY(getWorldY()+(int)y);
            screenX += x + (startX - getGp().player.getWorldX());
            screenY += y + (startY - getGp().player.getWorldY());
            startX = getGp().player.getWorldX();
            startY = getGp().player.getWorldY();
            setCollisionOn(false);
            int n = getGp().tileM.mapTileNum[((getWorldX() + 24) / getGp().tileSize)][((getWorldY() + 24) / getGp().tileSize)];
            if (getGp().tileM.tile[n].collision && !getGp().tileM.tile[n].flat) {
                setCollisionOn(true);
                count = (int) (slope / speed) + 1;
            }
        }
    }
}