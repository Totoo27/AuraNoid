package main;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Player implements KeyListener {
    public int x, y, width, height;
    public int dx = 0; // velocidad horizontal
    public int speed = 7;

    public Player(int startX, int startY, int width, int height) {
        this.x = startX;
        this.y = startY;
        this.width = width;
        this.height = height;
    }

    // Mover la paleta según la velocidad
    public void move(int panelWidth) {
        x += dx;
        if (x < 0) x = 0;
        if (x + width > panelWidth) x = panelWidth - width;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // KeyListener
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            dx = -speed;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            dx = speed;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            dx = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
