package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[10];
        getTileImage();
        mapTileNum = new int[gp.getMaxWorldCol()][gp.getMaxWorldRow()];
        loadMap("/maps/Map.txt");
    }

    public void getTileImage(){
        setUp(0, "01", true);
        tile[0].setFlat(true);
        setUp(1, "02", true);
        setUp(2, "03", true);
        tile[2].setFlat(true);
        setUp(3, "04", false);
        setUp(4, "05", false);
        setUp(5, "06", true);
        setUp(6, "07", true);
        setUp(7, "08", false);
        setUp(8, "09", false);
        setUp(9, "10", true);
    }

    public void setUp(int index, String imagePath, boolean collision) {
        UtilityTool uTool = new UtilityTool();
        try {
            tile[index] = new Tile();
            tile[index].setImage(ImageIO.read(getClass().getResourceAsStream("/tiles/" + imagePath + ".png")));
            tile[index].setImage(uTool.scaleImage(tile[index].getImage(), gp.getTileSize(), gp.getTileSize()));
            tile[index].setCollision(collision);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath){
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int col = 0;
            int row = 0;
            while(col < gp.getMaxWorldCol() && row < gp.getMaxWorldRow()) {
                String line = br.readLine();
                while(col < gp.getMaxWorldCol()) {
                    String numbers[] = line.split(" ");
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                    col++;
                }
                col = 0;
                row++;
            }
            br.close();
        } catch(Exception e) {

        }
    }

    public void draw(Graphics2D g2){
        int worldCol = 0;
        int worldRow = 0;
        while(worldCol<gp.getMaxWorldCol() && worldRow<gp.getMaxWorldRow()){
            int worldX = worldCol * gp.getTileSize();
            int worldY = worldRow * gp.getTileSize();
            int screenX = worldX - gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX();
            int screenY = worldY - gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY();
            if(worldX + gp.getTileSize() > gp.getPlayer().getWorldX() - gp.getPlayer().getScreenX() &&
               worldX - gp.getTileSize() < gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX() &&
               worldY + gp.getTileSize() > gp.getPlayer().getWorldY() - gp.getPlayer().getScreenY() &&
               worldY - gp.getTileSize() < gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY()) {
                g2.drawImage(tile[mapTileNum[worldCol][worldRow]].getImage(), screenX, screenY, null);
            }
            worldCol++;
            if(worldCol == gp.getMaxWorldCol()) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
