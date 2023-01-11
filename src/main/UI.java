package main;

import entity.Bullet;

import java.awt.*;

public class UI {
    GamePanel gp;
    Font text;

    public UI(GamePanel gp) {
        this.gp = gp;
        text = new Font("Arial", Font.PLAIN, 30);
    }

    public void draw(Graphics2D g2) {
        g2.setFont(text);
        g2.setColor(Color.orange);
        if(Bullet.ammo != 0)
            g2.drawString("Ammo = " + Bullet.ammo, 25, 50);
        else {
            g2.drawString("Reloading...", 25, 50);
        }
    }
}
