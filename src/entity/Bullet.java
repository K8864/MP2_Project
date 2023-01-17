package entity;

import main.ClickDetection;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Bullet extends Entity {
    private static int ammo = 12;
    private double startX, startY, endX, endY, screenX, screenY, cosine, sine, slope;
    private int speed = 9;
    private static int frameR = 0;
    private BufferedImage pew;
    private int count = 0;
    private static boolean shot = false;
    public static boolean getShot() {return shot;}
    public static void setShot(boolean s) {shot = s;}
    private double randX;
    private double randY;


    public Bullet(GamePanel gp) {
        super(gp);
        getBulletImage();
        //worldX = gp.player.worldX;
        setWorldX(getGp().getPlayer().getWorldX());
        //worldY = gp.player.worldY;
        setWorldY(getGp().getPlayer().getWorldY());
        screenX = gp.getPlayer().getScreenX();
        screenY = gp.getPlayer().getScreenY();
        startX = gp.getPlayer().getWorldX();
        startY = gp.getPlayer().getWorldY();
    }

    public void getBulletImage() {
        try {
            pew = ImageIO.read(getClass().getResourceAsStream("/bullet/Bullet.png"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        setSolidArea(new Rectangle(21, 21, 6, 6));
        speed = 12;
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
        screenX = getGp().getPlayer().getScreenX();
        screenY = getGp().getPlayer().getScreenY();
        startX = getGp().getPlayer().getWorldX();
        startY = getGp().getPlayer().getWorldY();
        endX = getGp().getPlayer().getWorldX() - getGp().getPlayer().getScreenX() + x-24;
        endY = getGp().getPlayer().getWorldY() - getGp().getPlayer().getScreenY() + y-24;
        slope = Math.sqrt((endX - startX) * (endX - startX) + (endY - startY) * (endY - startY));
        cosine = (endX - startX)/slope;
        sine = (endY - startY)/slope;
        randX = Math.abs(endX - startX)/7;
        randY = Math.abs(endY - startY)/7;
        if(sine > 0 && cosine > 0) {
            endY += (randX/2)-((Math.random() * randX)+1);
            endX += (randY/2)-((Math.random() * randY)+1);
        }
        else if(sine > 0 && cosine < 0) {
            endY -= (randX/2)-((Math.random() * randX)+1);
            endX += (randY/2)-((Math.random() * randY)+1);
        }
        else if (sine < 0 && cosine < 0) {
            endY -= (randX/2)-((Math.random() * randX)+1);
            endX -= (randY/2)-((Math.random() * randY)+1);
        }
        else {
            endY += (randX/2)-((Math.random() * randX)+1);
            endX -= (randY/2)-((Math.random() * randY)+1);
        }
        cosine = (endX - startX)/slope;
        sine = (endY - startY)/slope;
    }

    public void shoot(Bullet b) {
        ClickDetection.setShot(false);
        b.target(ClickDetection.getX(), ClickDetection.getY());
        ammo--;
        shot = true;
    }

    public static void reload() {
        if(frameR == 90) {
            Bullet.ammo = 12;
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
            g2.drawImage(pew, (int) screenX, (int) screenY, getGp().getTileSize(), getGp().getTileSize(), null);
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
            screenX += x + (startX - getGp().getPlayer().getWorldX());
            screenY += y + (startY - getGp().getPlayer().getWorldY());
            startX = getGp().getPlayer().getWorldX();
            startY = getGp().getPlayer().getWorldY();
            setCollisionOn(false);
            int n = getGp().getTileM().mapTileNum[((getWorldX() + 24) / getGp().getTileSize())][((getWorldY() + 24) / getGp().getTileSize())];
            if (getGp().getTileM().tile[n].isCollision() && !getGp().getTileM().tile[n].isFlat()) {
                setCollisionOn(true);
                count = (int) (slope / speed) + 1;
            }
        }
    }
}