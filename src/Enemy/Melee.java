package Enemy;

import entity.Entity;
import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Melee extends Entity {

    public Melee(GamePanel gp) {
        super(gp);
        speed = 2;
        maxHp = 3;
        hp = 3;
        solidArea = new Rectangle(15, 21, 17, 24);
        getEnemyImage();
        direction = "down";
        worldX = 900;
        worldY = 1250;
        collisionOn = false;
    }

    public void getEnemyImage(){
        down = setUp("Down");
        down2 = setUp("Down2");
        left = setUp("Left");
        left2 = setUp("Left2");
        right = setUp("Right");
        right2 = setUp("Right2");
        up = setUp("Up");
        up2 = setUp("Up2");
    }

    public BufferedImage setUp(String imageName) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/melee/" + imageName + ".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void chase() {
        int goalCol = (gp.player.worldX + gp.player.solidArea.x)/gp.tileSize;
        int goalRow = (gp.player.worldY + gp.player.solidArea.y)/gp.tileSize;
        searchPath(goalCol, goalRow);
    }

    public void update() {
        checkCollision();
        chase();
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
        spriteCounter++;
        if (spriteCounter > 10)
            if (spriteNum == 1) {
                spriteNum = 2;
                spriteCounter = 0;
            } else {
                spriteNum = 1;
                spriteCounter = 0;
            }
    }

    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
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

        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }

}
