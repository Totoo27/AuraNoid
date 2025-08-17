package main;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import java.awt.Font;



public class GamePanel extends JPanel implements ActionListener {

    private Ball ball;
    private Timer timer;
    private Player player;
    private Bloques[][] bloques; 

    
    // Labels
    private JLabel contador;
    private JLabel tiempo;
    
    public int puntosJug = 0;

    public GamePanel() {
    	inicializarBloques();
        setBackground(Color.BLACK);
        ball = new Ball(320, 700, 20); // posición inicial y tamaño
        timer = new Timer(10, this); // actualiza cada 10 ms
        timer.start();
        
        // ---- Labels
        
        // Contador
        contador = new JLabel("Puntos: " + puntosJug);
        contador.setForeground(Color.WHITE); // color del texto
        contador.setFont(new Font("Arial", Font.BOLD, 30)); // fuente
        contador.setBounds(10, 30, 200, 40); // posición y tamaño
        
        // Tiempo
        tiempo = new JLabel("00:00");
        tiempo.setForeground(Color.WHITE);
        tiempo.setFont(new Font("Arial", Font.BOLD, 30));
        tiempo.setBounds(315, 30, 200, 40);
        
        player = new Player(320, 700, 100, 15); // posición inicial y tamaño
        this.addKeyListener(player);
        this.setFocusable(true);
        this.requestFocus();
        
        // Usar layout absoluto para poder ubicar los labels
        this.setLayout(null);
        this.add(contador);
        this.add(tiempo);
        
        
    }

    private void inicializarBloques() {
    	int anchopantalla=getWidth()+8;
    	int ylineamarcador=100;
    	 int filas = 6;
    	    int columnas = 9;
    	    int ancho = 70;  // ancho del bloque
    	    int alto = 40;   // alto del bloque

    	    bloques = new Bloques[filas][columnas];
    	    for (int i = 0; i < filas; i++) {
    	        for (int j = 0; j < columnas; j++) {
    	        	int x = anchopantalla + j * (ancho + 5);
    	    	    int y = ylineamarcador + 10 + i * (alto + 5);
    	            bloques[i][j] = new Bloques(x, y, ancho, alto, Color.RED);
    	        }
    	    }
		
	}

	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
       
        // Pelota
        g.setColor(Color.WHITE);
        g.fillOval(ball.x, ball.y, ball.diameter, ball.diameter);
        
        // Jugador
        g.setColor(Color.WHITE);
        g.fillRect(player.x, player.y, player.width, player.height);
        
        // Línea de Marcador
        g.setColor(Color.WHITE);
        int y = 100;
        g.fillRect(0, y-5, getWidth(), 5);
        
        // Dibujar bloques
        for (int i = 0; i < bloques.length; i++) {
            for (int j = 0; j < bloques[i].length; j++) {
                bloques[i][j].dibujar(g);
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ball.move(getWidth(), getHeight());
        player.move(getWidth());
        
        // Choque pelota con jugador
        if (ball.getBounds().intersects(player.getBounds())) {
            ball.dy = -ball.dy;
            ball.y = player.y - ball.diameter;
        }

        // Choque con bloques
        Colisionesconbloques();
        
        repaint();
    }

	private void Colisionesconbloques() {
		Rectangle pelotaRect = ball.getBounds();

	    for (int i = 0; i < bloques.length; i++) {
	        for (int j = 0; j < bloques[i].length; j++) {
	            Bloques b = bloques[i][j];
	            
	            if (b.isVisible() && pelotaRect.intersects(b.getBounds())) {
	                b.setVisible(false);   // Romper bloque
	                ball.dy = -ball.dy;    // Rebote vertical
	                puntosJug++;           // Sumar puntos
	                contador.setText("Puntos: " + puntosJug);

	                return; 
	            }
	        }
	    }
		
	}
}
