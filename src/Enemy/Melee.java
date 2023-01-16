package Enemy;

import entity.Entity;
import entity.Player;
import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Melee extends Entity {
    private final static int[] spawnX = {15, 24, 38, 33, 11, 11, 20, 31};
    private final static int[] spawnY = {11, 29, 32, 9, 30, 39, 39, 39};

    public Melee(GamePanel gp, int x, int y) {
        super(gp);
        speed = 3;
        maxHp = 10;
        hp = 10;
        solidArea = new Rectangle(15, 21, 24, 36);
        getEnemyImage();
        direction = "down";
        setWorldX(x);
        setWorldY(y);
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
            image = uTool.scaleImage(image, getGp().tileSize, getGp().tileSize);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private static Point generateSpawnPoint() {
        int index = (int)(Math.random() * spawnX.length);
        return new Point(spawnX[index] * 48, spawnY[index] * 48);
    }

    public static Melee spawn(GamePanel gp) {
        Point p = generateSpawnPoint();
        int x = (int) p.getX();
        int y = (int) p.getY();
        return new Melee(gp, x, y);
    }

    public void chase() {
        if(!dead) {
            int goalCol = (getGp().player.getWorldX() + getGp().player.solidArea.x) / getGp().tileSize;
            int goalRow = (getGp().player.getWorldY() + getGp().player.solidArea.y) / getGp().tileSize;
            searchPath(goalCol, goalRow);
        }
    }

    public void update() {
        if(!dead) {
            speed = 2 + (int) (Math.random() * 2);
            checkCollision();
            chase();
            collisionOn = false;
            getGp().cChecker.checkTile(this);

            if (!collisionOn) {
                if (direction.equals("up"))
                    setWorldY(getWorldY()-speed);
                else if (direction.equals("down"))
                    setWorldY(getWorldY()+speed);
                else if (direction.equals("left"))
                    setWorldX(getWorldX()-speed);
                else if (direction.equals("right"))
                    setWorldX(getWorldX()+speed);
            }

            if (Math.abs(getWorldX() - getGp().player.getWorldX()) < 36 && Math.abs(getWorldY() - getGp().player.getWorldY()) < 48) {
                if (Player.hitCoolDown >= 90) {
                    getGp().player.hp--;
                    Player.hitCoolDown = 0;
                }
            }

            if (hp <= 0) {
                dead = true;
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
        else {
            setWorldX(0);
            setWorldY(0);
        }
    }

    public void draw(Graphics2D g2) {
        if(!dead) {
            int screenX = getWorldX() - getGp().player.getWorldX() + getGp().player.screenX;
            int screenY = getWorldY() - getGp().player.getWorldY() + getGp().player.screenY;
            BufferedImage image = null;
            switch (direction) {
                case "up":
                    if (spriteNum == 1)
                        image = up;
                    else
                        image = up2;
                    break;
                case "down":
                    if (spriteNum == 1)
                        image = down;
                    else
                        image = down2;
                    break;
                case "left":
                    if (spriteNum == 1)
                        image = left;
                    else
                        image = left2;
                    break;
                case "right":
                    if (spriteNum == 1)
                        image = right;
                    else
                        image = right2;
                    break;
            }

            g2.drawImage(image, screenX, screenY, getGp().tileSize, getGp().tileSize, null);
        }
    }

}
