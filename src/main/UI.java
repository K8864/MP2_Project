package main;

import entity.Bullet;

import java.awt.*;

public class UI {
    GamePanel gp;
    Font font;
    Graphics2D g2;
    public int commandNum = 0;
    public UI(GamePanel gp) {
        this.gp = gp;
        font = new Font("Arial", Font.PLAIN, 30);
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;
        g2.setFont(font);
        g2.setColor(Color.orange);
        if(gp.gameState == gp.titleState) {
            drawTitleScreen();
        }
        else{
            if(Bullet.ammo != 0)
                g2.drawString("Ammo = " + Bullet.ammo, 25, 50);
            else {
                g2.drawString("Reloading...", 25, 50);
            }
        }
    }

    public void drawTitleScreen() {
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
        String text = "Insert Name";
        int x = centerX(text);
        int y = gp.tileSize*3;

        g2.setColor(Color.GRAY);
        g2.drawString(text, x+5, y+5);
        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);


        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
        text = "Play";
        x = centerX(text);
        y = gp.tileSize*7;
        g2.drawString(text, x, y);
        if(commandNum == 0) {
            g2.setColor(Color.GRAY);
            g2.drawRect(x-gp.tileSize, y-gp.tileSize, (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth()*2, gp.tileSize+16);
            g2.setColor(Color.WHITE);
        }

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
        text = "Don't Play";
        x = centerX(text);
        y = (int)(gp.tileSize*8.5);
        g2.drawString(text, x, y);
        if(commandNum == 1) {
            g2.setColor(Color.GRAY);
            g2.drawRect(x-gp.tileSize, y-gp.tileSize, (int)(g2.getFontMetrics().getStringBounds(text, g2).getWidth()*1.5), gp.tileSize+16);
        }
    }

    public int centerX(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/2 - length/2;
        return x;
    }

    public void update() {
        gp.click.x = MouseInfo.getPointerInfo().getLocation().x- Main.window.getLocation().x-7;
        gp.click.y = MouseInfo.getPointerInfo().getLocation().y-Main.window.getLocation().y-30;
        if(gp.click.y >= gp.tileSize*7 - (gp.tileSize+16) && gp.click.y <= gp.tileSize*6 + (gp.tileSize+16)) {
            commandNum = 0;
            if(gp.click.click)
                gp.gameState = gp.playState;
        }
        else if(gp.click.y >= gp.tileSize*8.5 - (gp.tileSize+16) && gp.click.y <= gp.tileSize*7.5 + (gp.tileSize+16)) {
            commandNum = 1;
            if(gp.click.click)
                System.out.println("Why would u click this");
        }
    }
}