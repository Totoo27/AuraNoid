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
        if (x <= 0 || x + diameter >= panelWidth) {
            dx = -dx;
        }

        // Rebote vertical
        if(y + diameter >= panelHeight) {
        System.out.println("chau");
        }
        if (y <= 100 ) {
            dy = -dy;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, diameter, diameter);
    }
}
