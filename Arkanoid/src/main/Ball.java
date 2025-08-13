package main;

import java.awt.Rectangle;

public class Ball {
    public int x, y, diameter;
    public int dx = 5, dy = 5; // velocidad
     
    public Ball(int x, int y, int diameter) {
        this.x = x;
        this.y = y;
        this.diameter = diameter;
    }

    public void move(int panelWidth, int panelHeight) {
        x += dx;
        y += dy;

        // Rebote horizontal
        if (x <= 0 || x + diameter >= panelWidth) {
            dx = -dx;
        }

        // Rebote vertical
        if (y <= 0 || y + diameter >= panelHeight) {
            dy = -dy;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, diameter, diameter);
    }
}
