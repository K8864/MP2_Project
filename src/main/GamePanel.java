package main;
import entity.Bullet;
import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {
    //Screen Settings
    final int originalTileSize = 16; // 16 x 16 tiles (default sizes for things)
    final int scale = 3;
    public final int tileSize = originalTileSize * scale; // 48 x 48 tile size
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 960 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    //World settings
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    //Game State
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;

    //FPS
    int FPS = 60;

    public int frameS = 0;

    public TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    public ClickDetection click = new ClickDetection();
    public ArrayList<Bullet> chamber = new ArrayList<Bullet>();
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyH);
    public UI ui = new UI(this);

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.addMouseListener(click);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameState = playState;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS; // 0.0167 seconds
        double nextDrawTime = System.nanoTime() + drawInterval;
        while (gameThread != null) {
            update();
            repaint();
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000;
                if (remainingTime < 0) {
                    remainingTime = 0;
                }
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        if(gameState == playState) {
            player.update();
        }
        else if(gameState == titleState){

        }

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if(gameState == titleState) {

        }
        else {
            tileM.draw(g2);
            player.draw(g2);
            if (click.shooting && Bullet.ammo>0 && frameS==7) {
                Bullet shot = new Bullet(this);
                shot.shoot(shot);
                chamber.add(shot);
                frameS = 0;
            }
            else if(Bullet.ammo==0)
                Bullet.reload();
            else if(frameS != 7)
                frameS++;
            for(int i=0; i<chamber.size(); i++) {
                if(chamber.get(i).count < chamber.get(i).slope / chamber.get(i).speed) {
                    chamber.get(i).draw(g2);
                    chamber.get(i).count++;
                }
                else
                    chamber.remove(i);
            }
            ui.draw(g2);
            g2.dispose();
        }
    }
}