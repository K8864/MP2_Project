package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public Player(GamePanel gp, KeyHandler keyH){
        this.gp = gp;
        this.keyH = keyH;
        setDefaultValues();
        getPlayerImage();
        solidArea = new Rectangle(15, 21, 17, 24); // 16, 21, 16, 24
        screenX = gp.screenWidth/2 - gp.tileSize/2;
        screenY = gp.screenHeight/2 - gp.tileSize/2;
    }

    public void setDefaultValues() {
        worldX = 1152;
        worldY = 1056;
        speed = 3;
        hp = 5;
        direction = "down";
    }

    public int getWorldX() {
        return worldX;
    }
    public int getWorldY() {
        return worldY;
    }

    public void getPlayerImage(){
        left = setUp("Left");
        left2 = setUp("Left2");
        right = setUp("Right");
        right2 = setUp("Right2");
        down = setUp("Down");
        down2 = setUp("Down2");
        up = setUp("Up");
        up2 = setUp("Up2");
    }

    public BufferedImage setUp(String imageName) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/player/" + imageName + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void update() {
        if(keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            if (keyH.upPressed) {
                direction = "up";
                spriteCounter++;
            } else if (keyH.downPressed) {
                direction = "down";
                spriteCounter++;
            } else if (keyH.leftPressed) {
                direction = "left";
                spriteCounter++;
            } else if (keyH.rightPressed) {
                direction = "right";
                spriteCounter++;
            }
            // Check tile collision
            collisionOn = false;
            gp.cChecker.checkTile(this);
            // if collision is off, player can move
            if (!collisionOn) {
                switch (direction) {
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                }
            }
            if (spriteCounter > 10)
                if (spriteNum == 1) {
                    spriteNum = 2;
                    spriteCounter = 0;
                } else {
                    spriteNum = 1;
                    spriteCounter = 0;
                }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
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
        g2.drawImage(image, screenX, screenY, null);
    }
}
