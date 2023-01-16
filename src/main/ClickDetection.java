package main;

import java.awt.*;
import java.awt.event.*;

public class ClickDetection implements MouseListener {
    private static int x = 0;
    private static int y = 0;
    private static boolean shot = false;
    private static boolean click = false;

    public static void update() {
        x = MouseInfo.getPointerInfo().getLocation().x- Main.window.getLocation().x-7;
        y = MouseInfo.getPointerInfo().getLocation().y-Main.window.getLocation().y-30;
    }

    public static int getX() {
        return x;
    }

    public static int getY() {
        return y;
    }

    public static boolean getClick() {
        return click;
    }

    public static void setClick(boolean c) {
        click = c;
    }

    public static boolean getShot() {
        return shot;
    }

    public static void setShot(boolean s) {
        shot = s;
    }

    public ClickDetection() {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        shot = true;
        click = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        click = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}