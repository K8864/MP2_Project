package main;

import java.awt.*;
import java.awt.event.*;

public class ClickDetection implements MouseListener {
    public int x = 0;
    public int y = 0;
    public boolean shot = false;
    public boolean click = false;
    boolean shooting = false;

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
        shooting = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        shooting = false;
        click = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}