package main;

import java.awt.Rectangle;

public class Ball {
    public int x, y, diameter;
    public double velocidad = 5; // Default
    public double dx = velocidad, dy = velocidad; // velocidad
     
    public Ball(int x, int y, int diameter, double velocidad) {
        this.x = x;
        this.y = y;
        this.diameter = diameter;
        this.velocidad = velocidad;
        
        dx = velocidad;
        dy = velocidad;
    }

    public void move(int panelWidth, int panelHeight) {
        x += dx;
        y += dy;

        // Rebote horizontal
        if (x <= 0) {
            dx = -dx;
            this.x = 0;
        }
        if ( x + diameter >= panelWidth) {
        	dx = -dx;
        	this.x = panelWidth - diameter;
        }

        // Rebote vertical
        if (y <= 100 ) {
            dy = -dy;
            this.y = 100;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, diameter, diameter);
    }
}
