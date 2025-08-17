package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Bloques {
    private int x, y;       
    private int ancho, alto; 
    private Color color;     
    private boolean visible;
    private int vida;
    
    public Bloques(int x, int y, int ancho, int alto, int vida) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.vida = vida;
        this.visible = true; // al inicio, todos los bloques son visibles
        
        if(vida == 1) {
            this.color = Color.WHITE;
        } else {
        	this.color = Color.GRAY;
        }
    }
    
    public void recibirGolpe() {
        vida--;
        if (vida <= 0) {
            visible = false;
        } else {
            // Cambiar color según vida
            color = Color.WHITE;
        }
    }

    public void dibujar(Graphics g) {
    	if(visible) { // solo dibuja si el bloque no fue roto
        	g.setColor(color);
        	g.fillRect(x, y, ancho, alto);  // bloque lleno
        	g.setColor(Color.black);
        	g.drawRect(x, y, ancho, alto);  // borde
    	}
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, ancho, alto);
    }
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
}
