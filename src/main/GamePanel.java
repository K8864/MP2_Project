package main;

import enemy.Melee;
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
    private final int originalTileSize = 16; // 16 x 16 tiles (default sizes for things)
    private final int scale = 3;
    private final int tileSize = originalTileSize * scale; // 48 x 48 tile size
    public int getTileSize() {return tileSize;}
    private final int maxScreenCol = 20;
    private final int maxScreenRow = 12;
    private final int screenWidth = tileSize * maxScreenCol; // 960 pixels
    public int getScreenWidth() {return screenWidth;}
    private final int screenHeight = tileSize * maxScreenRow; // 576 pixels
    public int getScreenHeight() {return screenHeight;}

    //World settings
    private final int maxWorldCol = 50;
    public int getMaxWorldCol() {return maxWorldCol;}
    private final int maxWorldRow = 50;
    public int getMaxWorldRow(){return maxWorldRow;}

    //Stuff
    private final PathFinder pFinder = new PathFinder(this);
    public PathFinder getpFinder() {return pFinder;}

    //Entity
    private ArrayList<Entity> entities = new ArrayList<Entity>();
    private int kills = 0;
    public int getKills() {return kills;}
    public void kill() {kills++;}

    //Game State
    private int gameState;
    public int getGameState() {return gameState;}
    public void setGameState(int g) {gameState = g;}
    private final int titleState = 0;
    public int getTitleState() {return titleState;}
    private final int playState = 1;
    public int getPlayState() {return playState;}
    private final int deadState = 2;
    public int getDeadState() {return deadState;}

    //FPS
    private final int FPS = 60;

    private int frameS = 0;
    private int frame = 0;
    public int getFrame() {return frame;}

    private final TileManager tileM = new TileManager(this);
    public TileManager getTileM() {return tileM;}
    private final KeyHandler keyH = new KeyHandler();
    private final ClickDetection clickChecker = new ClickDetection();
    public ClickDetection getClickChecker() {return clickChecker;}
    private Thread gameThread;
    private CollisionChecker cChecker = new CollisionChecker(this);
    public CollisionChecker getcChecker() {return cChecker;}
    private final Player player = new Player(this, keyH);
    public Player getPlayer() {return player;}
    private final UI ui = new UI(this);

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
            if(frame < 60) {
                ClickDetection.setShot(false);
            }
            if(player.getHp() == 0) {
                gameState = deadState;
            }
            frame++;
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
            if(frame % 750 == 0) {
                for(int i=0; i<2; i++) {
                    Melee killer = Melee.spawn(this);
                    entities.add(killer);
                }
            }
            if (gameState == playState) {
                if (clickChecker.getClick() && Bullet.getAmmo() > 0 && frameS >= 60) {
                    Bullet shot = new Bullet(this);
                    shot.shoot(shot);
                    entities.add(shot);
                    frameS = 0;
                } else if (Bullet.getAmmo() == 0)
                    Bullet.reload();
                else if (frameS < 60)
                    frameS++;
                for (int i = 0; i < entities.size(); i++) {
                    if (entities.get(i) instanceof Bullet) {
                        if (((Bullet) entities.get(i)).getCount() < ((Bullet) entities.get(i)).getSlope() / ((Bullet) entities.get(i)).getSpeed()) {
                            ((Bullet) entities.get(i)).update();
                            ((Bullet) entities.get(i)).setCount(((Bullet) entities.get(i)).getCount()+1);
                            if(cChecker.checkHit(((Bullet) entities.get(i)), entities) != -1) {
                                int j = cChecker.checkHit(((Bullet) entities.get(i)), entities);
                                entities.get(j).loseHp();
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
                //System.out.println("Draw Time: " + passed);
                player.setHp(5);
                if((frame&2) == 0) {
                    frameS = 60;
                }
                else {
                    frameS = 0;
                }
                Bullet.setAmmo(1000); // static var being edited in diff class
            }
            g2.dispose();
        }
    }
}