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
        setSolidArea(new Rectangle(15, 21, 24, 36));
        getEnemyImage();
        setDirection("down");
        setWorldX(x);
        setWorldY(y);
        setCollisionOn(false);
    }

    public void getEnemyImage(){
        setDown(setUp("Down"));
        setDown2(setUp("Down2"));
        setLeft(setUp("Left"));
        setLeft2(setUp("Left2"));
        setRight(setUp("Right"));
        setRight2(setUp("Right2"));
        setUp(setUp("Up"));
        setUp2(setUp("Up2"));
    }

    public BufferedImage setUp(String imageName) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/melee/" + imageName + ".png"));
            image = uTool.scaleImage(image, getGp().getTileSize(), getGp().getTileSize());
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
        if(!isDead()) {
            int goalCol = (getGp().getPlayer().getWorldX() + getGp().getPlayer().getSolidArea().x) / getGp().getTileSize();
            int goalRow = (getGp().getPlayer().getWorldY() + getGp().getPlayer().getSolidArea().y) / getGp().getTileSize();
            searchPath(goalCol, goalRow);
        }
    }

    public void update() {
        if(!isDead()) {
            speed = 2 + (int) (Math.random() * 2);
            checkCollision();
            chase();
            setCollisionOn(false);
            getGp().getcChecker().checkTile(this);

            if (!isCollisionOn()) {
                if (getDirection().equals("up"))
                    setWorldY(getWorldY()-speed);
                else if (getDirection().equals("down"))
                    setWorldY(getWorldY()+speed);
                else if (getDirection().equals("left"))
                    setWorldX(getWorldX()-speed);
                else if (getDirection().equals("right"))
                    setWorldX(getWorldX()+speed);
            }

            if (Math.abs(getWorldX() - getGp().getPlayer().getWorldX()) < 36 && Math.abs(getWorldY() - getGp().getPlayer().getWorldY()) < 48) {
                if (Player.getHitCoolDown() >= 90) {
                    getGp().getPlayer().hp--;
                    Player.setHitCoolDown(0);
                }
            }

            if (hp <= 0) {
                setDead(true);
            }

            incrementSpriteCounter();
            if (getSpriteCounter() > 10)
                if (getSpriteNum() == 1) {
                    setSpriteNum(2);
                    resetSpriteCounter();
                } else {
                    setSpriteNum(1);
                    resetSpriteCounter();
                }
        }
        else {
            setWorldX(0);
            setWorldY(0);
        }
    }

    public void draw(Graphics2D g2) {
        if(!isDead()) {
            int screenX = getWorldX() - getGp().getPlayer().getWorldX() + getGp().getPlayer().getScreenX();
            int screenY = getWorldY() - getGp().getPlayer().getWorldY() + getGp().getPlayer().getScreenY();
            BufferedImage image = null;
            switch (getDirection()) {
                case "up":
                    if (getSpriteNum() == 1)
                        image = getUp();
                    else
                        image = getUp2();
                    break;
                case "down":
                    if (getSpriteNum() == 1)
                        image = getDown();
                    else
                        image = getDown2();
                    break;
                case "left":
                    if (getSpriteNum() == 1)
                        image = getLeft();
                    else
                        image = getLeft2();
                    break;
                case "right":
                    if (getSpriteNum() == 1)
                        image = getRight();
                    else
                        image = getRight2();
                    break;
            }

            g2.drawImage(image, screenX, screenY, getGp().getTileSize(), getGp().getTileSize(), null);
        }
    }

}
