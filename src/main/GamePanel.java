package main;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import java.awt.Font;



public class GamePanel extends JPanel implements ActionListener {

    private Ball ball;
    private Timer timer;
    private Player player;
    
    // Labels
    private JLabel contador;
    private JLabel tiempo;
    
    public int puntosJug = 0;

    public GamePanel() {
        setBackground(Color.BLACK);
        ball = new Ball(100, 100, 20); // posición inicial y tamaño
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
        
        repaint();
    }
}
