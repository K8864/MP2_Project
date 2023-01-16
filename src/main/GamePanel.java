package main;
import Enemy.Melee;
import ai.PathFinder;
import entity.Bullet;
import entity.Entity;
import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

    //Stuff
    public PathFinder pFinder = new PathFinder(this);

    //Entity
    public ArrayList<Entity> entities = new ArrayList<Entity>();

    //Game State
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int deadState = 2;

    //FPS
    int FPS = 60;

    public int frameS = 0;
    private int frame = 0;

    public TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    public ClickDetection clickChecker = new ClickDetection();
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyH);
    public UI ui = new UI(this);

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.addMouseListener(clickChecker);
        this.setFocusable(true);
    }

    public void setUpGame() {
        gameState = titleState;
    }

    public void startGameThread() {
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
        ClickDetection.update();
        if(gameState == titleState) {
            ui.update();
        }
        else if(gameState == playState){
            player.update();
            if(player.hp == 0) {
                gameState = deadState;
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //Debug
        long drawStart = 0;
        if(keyH.isShowDrawTime())
            drawStart = System.nanoTime();

        if(gameState == titleState) {
            ui.draw(g2);
        }
        else {
            tileM.draw(g2);

            entities.add(player);
            if(frame % 300 == 0) {
                Melee killer = Melee.spawn(this);
                entities.add(killer);
            }
            if (gameState == playState) {
                if (clickChecker.getClick() && Bullet.getAmmo() > 0 && frameS == 7) {
                    Bullet shot = new Bullet(this);
                    shot.shoot(shot);
                    entities.add(shot);
                    frameS = 0;
                } else if (Bullet.getAmmo() == 0)
                    Bullet.reload();
                else if (frameS < 7)
                    frameS++;
                for (int i = 0; i < entities.size(); i++) {
                    if (entities.get(i) instanceof Bullet) {
                        if (((Bullet) entities.get(i)).getCount() < ((Bullet) entities.get(i)).getSlope() / ((Bullet) entities.get(i)).getSpeed()) {
                            ((Bullet) entities.get(i)).update();
                            ((Bullet) entities.get(i)).setCount(((Bullet) entities.get(i)).getCount()+1);
                            if(cChecker.checkHit(((Bullet) entities.get(i)), entities) != -1) {
                                int j = cChecker.checkHit(((Bullet) entities.get(i)), entities);
                                entities.get(j).hp--;
                                entities.remove(i);
                            }
                        } else {
                            entities.remove(i);
                            i--;
                        }
                    } else if (entities.get(i) instanceof Melee) {
                        ((Melee) entities.get(i)).update();
                    }
                }

                Collections.sort(entities, new Comparator<Entity>() {
                    @Override
                    public int compare(Entity e1, Entity e2) {
                        return Integer.compare(e1.getWorldX(), e2.getWorldY());
                    }

                    @Override
                    public boolean equals(Object obj) {
                        return false;
                    }
                });
            }

            for(Entity e: entities) {
                if (e instanceof Bullet)
                    e.draw(g2);
                else if(e instanceof Player)
                    e.draw(g2);
                else if(e instanceof Melee)
                    e.draw(g2);
            }
            entities.remove(player);
            ui.draw(g2);
            if(keyH.isShowDrawTime()) {
                g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 36F));
                long drawEnd = System.nanoTime();
                long passed = drawEnd - drawStart;
                g2.setColor(Color.RED);
                g2.drawString("Draw Time: " + passed, 10, 500);
                System.out.println("Draw Time: " + passed);
                player.hp = 5;
            }
            g2.dispose();
        }
        frame++;
    }
}