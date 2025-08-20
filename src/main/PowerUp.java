package main;

import java.awt.Rectangle;
import java.awt.Color;

public class PowerUp {
	
	public int x, y;
	Color color;
	
	static int dimension = 20;
	double dy = 2.5;
	int tipo_powerup;
	
	public PowerUp(int x, int y) {
		
		this.x = x;
		this.y = y;
		this.tipo_powerup = Math.random() < 0.5 ? 2 : 1;
		
		if(tipo_powerup == 1) {
			this.color = new Color(0xFF5100);
		} else {
			this.color = new Color(0x09FF00);
		}
	}
	
	public void move(int panelWidth, int panelHeight) {
        y += dy;
    }
	
	
	
	public Rectangle getBounds() {
        return new Rectangle(x, y, dimension, dimension);
    }
	
}
