package entity;

import main.ClickDetection;
import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    private final KeyHandler keyH;

    private static int hitCoolDown = 90;
    private int still = 30;

    private final int screenX;
    private final int screenY;

    public Player(GamePanel gp, KeyHandler keyH){
        super(gp);
        this.keyH = keyH;
        setDefaultValues();
        getPlayerImage();
        setSolidArea(new Rectangle(15, 21, 17, 24)); // 16, 21, 16, 24
        screenX = gp.getScreenWidth()/2 - gp.getTileSize()/2;
        screenY = gp.getScreenHeight()/2 - gp.getTileSize()/2;
        ClickDetection.setShot(false);
    }

    public void setDefaultValues() {
        setWorldX(1152);
        setWorldY(1008);
        setSpeed(3);
        setMaxHp(5);
        setHp(5);
        setDirection("down");
    }

    public void getPlayerImage(){
        setDown(setUp("Down"));
        setDown2(setUp("Down2"));
        setLeft(setUp("Left"));
        setLeft2(setUp("Left2"));
        setRight(setUp("Right"));
        setRight2(setUp("Right2"));
        setUp(setUp("Up"));
        setUp2(setUp("Up2"));
    }

    public static int getHitCoolDown() {
        return hitCoolDown;
    }

    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }

    public static void setHitCoolDown(int h) {
        hitCoolDown = h;
    }

    public BufferedImage setUp(String imageName) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/player/" + imageName + ".png"));
            image = uTool.scaleImage(image, getGp().getTileSize(), getGp().getTileSize());
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void update() {
        if(Bullet.getShot()) {
            Bullet.setShot(false);
            still = 0;
            setSpeed(0);
        }
        else {
            setSpeed(3);
        }
        if(still <= 30) {
            setSpeed(1);
            still++;
        }
        else {
            ClickDetection.setShot(false);
        }
        if(keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            if (keyH.upPressed) {
                setDirection("up");
                incrementSpriteCounter();
            } else if (keyH.downPressed) {
                setDirection("down");
                incrementSpriteCounter();
            } else if (keyH.leftPressed) {
                setDirection("left");
                incrementSpriteCounter();
            } else if (keyH.rightPressed) {
                setDirection("right");
                incrementSpriteCounter();
            }
            // Check tile collision
            setCollisionOn(false);
            getGp().getcChecker().checkTile(this);
            // if collision is off, player can move
            if (!isCollisionOn()) {
                switch (getDirection()) {
                    case "up":
                        setWorldY(getWorldY()-getSpeed());
                        break;
                    case "down":
                        setWorldY(getWorldY()+getSpeed());
                        break;
                    case "left":
                        setWorldX(getWorldX()-getSpeed());
                        break;
                    case "right":
                        setWorldX(getWorldX()+getSpeed());
                        break;
                }
            }
            if (getSpriteCounter() > 10)
                if (getSpriteNum() == 1) {
                    setSpriteNum(2);
                    resetSpriteCounter();
                } else {
                    setSpriteNum(1);
                    resetSpriteCounter();
                }
        }
        hitCoolDown++;

    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        switch(getDirection()) {
            case "up":
                if(getSpriteNum() == 1)
                    image = getUp();
                else
                    image = getUp2();
                break;
            case "down":
                if(getSpriteNum() == 1)
                    image = getDown();
                else
                    image = getDown2();
                break;
            case "left":
                if(getSpriteNum() == 1)
                    image = getLeft();
                else
                    image = getLeft2();
                break;
            case "right":
                if(getSpriteNum() == 1)
                    image = getRight();
                else
                    image = getRight2();
                break;
        }
        g2.drawImage(image, screenX, screenY, null);
    }
}