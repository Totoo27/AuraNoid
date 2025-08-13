package main;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel implements ActionListener {

    private Ball ball;
    private Timer timer;
    private Player player;

    public GamePanel() {
        setBackground(Color.BLACK);
        ball = new Ball(100, 100, 20); // posición inicial y tamaño
        timer = new Timer(10, this); // actualiza cada 10 ms
        timer.start();
        
        player = new Player(350, 700, 100, 15); // posición inicial y tamaño
        this.addKeyListener(player);
        this.setFocusable(true);
        this.requestFocus();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillOval(ball.x, ball.y, ball.diameter, ball.diameter);
        
        g.setColor(Color.WHITE);
        g.fillRect(player.x, player.y, player.width, player.height);
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
