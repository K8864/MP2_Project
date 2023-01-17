package main;

import entity.Bullet;

import java.awt.*;

public class UI {
    private final GamePanel gp;
    private final Font font;
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
        if(gp.getGameState() == gp.getTitleState()) {
            drawTitleScreen();
        }
        else if(gp.getGameState() == gp.getPlayState()) {
            if(Bullet.getAmmo() != 0)
                g2.drawString("Ammo = " + Bullet.getAmmo(), 25, gp.getScreenHeight()-50);
            else {
                g2.drawString("Reloading...", 25, gp.getScreenHeight()-50);
            }
            if(gp.getPlayer().getHp() > 0) {
                g2.setColor(Color.RED);
                g2.fillRect(25, 50, gp.getPlayer().getHp() * (gp.getTileSize()), gp.getTileSize());
            }
            g2.setColor(Color.BLACK);
            g2.drawRect(25, 50, gp.getPlayer().getMaxHp() * (gp.getTileSize()), gp.getTileSize());
        }
        if(gp.getGameState() == gp.getDeadState()){
            drawDeadScreen();
        }
    }

    public void drawDeadScreen() {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.getScreenWidth(), gp.getScreenHeight());
        g2.setColor(Color.RED);
        String text = "Haha Ded";
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110F));
        int x = centerX(text);
        int y = gp.getScreenHeight()/2 - gp.getTileSize();
        g2.drawString(text, x, y);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 48F));
        g2.setColor(Color.WHITE);
        text = "Seconds survived: " + gp.getFrame()/60;
        y = gp.getScreenHeight()/2;
        g2.drawString(text, x, y);
        text = "Kills: " + gp.getKills();
        y = gp.getScreenHeight()/2 + gp.getTileSize();
        g2.drawString(text, x, y);
    }

    public void drawTitleScreen() {
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
        String text = "Purged";
        int x = centerX(text);
        int y = gp.getTileSize()*3;

        g2.setColor(Color.GRAY);
        g2.drawString(text, x+5, y+5);
        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);


        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
        text = "Play";
        x = centerX(text);
        y = gp.getTileSize()*7;
        if(play)
            g2.drawString(text, x, y);
        if(commandNum == 0) {
            g2.setColor(Color.GRAY);
            g2.drawRect(x-gp.getTileSize(), y-gp.getTileSize(), (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth()*2, gp.getTileSize()+16);
            g2.setColor(Color.WHITE);
        }

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
        text = "Don't Play";
        x = centerX(text);
        y = (int)(gp.getTileSize()*8.5);
        g2.drawString(text, x, y);
        if(commandNum == 1) {
            g2.setColor(Color.GRAY);
            g2.drawRect(x-gp.getTileSize(), y-gp.getTileSize(), (int)(g2.getFontMetrics().getStringBounds(text, g2).getWidth()*1.5), gp.getTileSize()+16);
        }
    }

    public int centerX(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.getScreenWidth()/2 - length/2;
        return x;
    }

    public void update() {
        if(ClickDetection.getY() >= gp.getTileSize()*7 - (gp.getTileSize()+16) && ClickDetection.getY() <= gp.getTileSize()*6 + (gp.getTileSize()+16) && play) {
            commandNum = 0;
            if(ClickDetection.getClick())
                gp.setGameState(gp.getPlayState());
        }
        else if(ClickDetection.getY() >= gp.getTileSize()*8.5 - (gp.getTileSize()+16) && ClickDetection.getY() <= gp.getTileSize()*7.5 + (gp.getTileSize()+16)) {
            commandNum = 1;
            if(ClickDetection.getClick())
                play = false;
        }
        else commandNum = 2;
    }
}