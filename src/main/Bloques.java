package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
            this.color = vidaToColor(vida);
        } else {
        	this.color = vidaToColor(vida);
        }
    }
    
    public void recibirGolpe() {
        vida--;
        if (vida <= 0) {
        	GamePanel.reproducirSonido("src/sonidos/break.wav");
            visible = false;
            
        } else {
            // Cambiar color según vida
            color = vidaToColor(vida);
        }
    }
    
    public Color vidaToColor(int vida) {
    	
    	Color color;
    	
    	if(vida == 1) {
    		color = new Color(0xFF00FF);
    	} else if(vida == 2) {
    		color = new Color(0xA013BF);
    	} else if(vida == 3) {
    		color = new Color(0x601B8C);
    	} else {	
    		color = new Color(0x381B75);
    	}
    	
    	return color;
    	
    }

    public void dibujar(Graphics g) {
        if (visible) {
        	Graphics2D g2 = (Graphics2D) g; // casteo a Graphics2D
        	
        	// Rectángulo
        	int grosorBorde = 3; // píxeles
            g2.setStroke(new BasicStroke(grosorBorde));

            // Dibujar borde del rectángulo
            g2.setColor(color);
            g2.drawRect(x, y, ancho, alto);

            // Franja del medio
            int franjaAltura = Math.max(2, alto / 6); // grosor de la franja
            int lineaY = y + alto / 2 - franjaAltura / 2;
            g.fillRect(x + ancho/5, lineaY, ancho - 2*ancho/5, franjaAltura);
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
