package main;

import entity.Bullet;

import java.awt.*;

public class UI {
    private GamePanel gp;
    private Font font;
    private Graphics2D g2;
    private int commandNum = 2;
    private boolean play = true;
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
        else if(gp.gameState == gp.playState) {
            if(Bullet.getAmmo() != 0)
                g2.drawString("Ammo = " + Bullet.getAmmo(), 25, gp.screenHeight-50);
            else {
                g2.drawString("Reloading...", 25, gp.screenHeight-50);
            }
            if(gp.player.hp > 0) {
                g2.setColor(Color.RED);
                g2.fillRect(25, 50, gp.player.hp * (gp.tileSize), gp.tileSize);
            }
            g2.setColor(Color.BLACK);
            g2.drawRect(25, 50, gp.player.maxHp * (gp.tileSize), gp.tileSize);
        }
        if(gp.gameState == gp.deadState){
            drawDeadScreen();
        }
    }

    public void drawDeadScreen() {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2.setColor(Color.BLACK);
        String text = "Haha Ded";
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110F));
        int x = centerX(text);
        int y = gp.screenHeight/2;
        g2.drawString(text, x, y);
    }

    public void drawTitleScreen() {
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
        String text = "Purged";
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
        if(play)
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
        if(ClickDetection.getY() >= gp.tileSize*7 - (gp.tileSize+16) && ClickDetection.getY() <= gp.tileSize*6 + (gp.tileSize+16) && play) {
            commandNum = 0;
            if(ClickDetection.getClick())
                gp.gameState = gp.playState;
        }
        else if(ClickDetection.getY() >= gp.tileSize*8.5 - (gp.tileSize+16) && ClickDetection.getY() <= gp.tileSize*7.5 + (gp.tileSize+16)) {
            commandNum = 1;
            if(ClickDetection.getClick())
                play = false;
        }
        else commandNum = 2;
    }
}