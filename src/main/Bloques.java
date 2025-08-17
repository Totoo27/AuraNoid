package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Bloques {
    private int x, y;       
    private int ancho, alto; 
    private Color color;     
    private boolean visible;
    
    public Bloques(int x, int y, int ancho, int alto, Color color) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.color = color;
        this.visible = true; // al inicio, todos los bloques son visibles
    }

    public void dibujar(Graphics g) {
    if(visible) { // solo dibuja si el bloque no fue roto
        g.setColor(color);
        g.fillRect(x, y, ancho, alto);  // bloque lleno
        g.setColor(Color.black);
        g.drawRect(x, y, ancho, alto);  // borde
    }}
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
