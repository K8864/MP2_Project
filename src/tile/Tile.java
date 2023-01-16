package tile;

import java.awt.image.BufferedImage;

public class Tile {
    private BufferedImage image;
    public BufferedImage getImage() {return image;}
    public void setImage(BufferedImage image) {this.image = image;}
    private boolean collision = false;
    public Boolean isCollision() {return collision;}
    public void setCollision(boolean collision) {this.collision = collision;}
    private boolean flat = false;
    public boolean isFlat() {return flat;}
    public void setFlat(boolean flat) {this.flat = flat;}
}
