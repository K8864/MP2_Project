package main;

import entity.Bullet;
import entity.Entity;
import enemy.Melee;

import java.util.ArrayList;

public class CollisionChecker {
    private final GamePanel gp;

    public CollisionChecker(GamePanel gp){
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        int entityLeftWorldX = entity.getWorldX() + entity.getSolidArea().x;
        int entityRightWorldX = entity.getWorldX() + entity.getSolidArea().x + entity.getSolidArea().width;
        int entityTopWorldY = entity.getWorldY() + entity.getSolidArea().y;
        int entityBottomWorldY = entity.getWorldY() + entity.getSolidArea().y + entity.getSolidArea().height;
        int entityLeftCol = entityLeftWorldX / gp.getTileSize();
        int entityRightCol = entityRightWorldX / gp.getTileSize();
        int entityTopRow = entityTopWorldY / gp.getTileSize();
        int entityBottomRow = entityBottomWorldY / gp.getTileSize();
        int tileNum1, tileNum2;
        switch(entity.getDirection()) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.getSpeed()) / gp.getTileSize();
                tileNum1 = gp.getTileM().mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.getTileM().mapTileNum[entityRightCol][entityTopRow];
                if(gp.getTileM().tile[tileNum1].isCollision() || gp.getTileM().tile[tileNum2].isCollision()) {
                    entity.setCollisionOn(true);
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.getSpeed()) / gp.getTileSize();
                tileNum1 = gp.getTileM().mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.getTileM().mapTileNum[entityRightCol][entityBottomRow];
                if(gp.getTileM().tile[tileNum1].isCollision() || gp.getTileM().tile[tileNum2].isCollision()) {
                    entity.setCollisionOn(true);
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.getSpeed()) / gp.getTileSize();
                tileNum1 = gp.getTileM().mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.getTileM().mapTileNum[entityLeftCol][entityBottomRow];
                if(gp.getTileM().tile[tileNum1].isCollision() || gp.getTileM().tile[tileNum2].isCollision()) {
                    entity.setCollisionOn(true);
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.getSpeed()) / gp.getTileSize();
                tileNum1 = gp.getTileM().mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.getTileM().mapTileNum[entityRightCol][entityBottomRow];
                if(gp.getTileM().tile[tileNum1].isCollision() || gp.getTileM().tile[tileNum2].isCollision()) {
                    entity.setCollisionOn(true);
                }
                break;
        }
    }

    public int checkHit(Bullet b, ArrayList<Entity> target) {
        int index = -1;
        for(int i=0; i<target.size(); i++) {
            if(target.get(i) instanceof Melee) {
                if(Math.abs(b.getWorldX() - target.get(i).getWorldX()) < target.get(i).getSolidArea().width && Math.abs(b.getWorldY()-target.get(i).getWorldY()) < target.get(i).getSolidArea().height) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    public void hitPlayer(Melee m) {
        if (Math.abs(m.getWorldX() - gp.getPlayer().getWorldX()) < 36 && Math.abs(m.getWorldY() - gp.getPlayer().getWorldY()) < 48) {
            m.setCollisionOn(true);
        }
        else {
            m.setCollisionOn(false);
        }
    }
}
