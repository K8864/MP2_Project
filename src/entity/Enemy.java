package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Enemy extends Entity{
    GamePanel gp;

    int[][] spawns = new int[2][13];

    public Enemy(GamePanel gp) {
        this.gp = gp;
        speed = 3;
    }

    public void getEnemyImage(){
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

    public void setValues() {
        hp = 3;
        speed = 3;
        direction = "down";
        spawns[1][0] = 12;
        spawns[2][0] = 16;
    }

}
