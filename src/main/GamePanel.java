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

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import java.io.File;


public class GamePanel extends JPanel implements ActionListener {
	private boolean gameOver = false;  // << NUEVO
    private Ball ball;
    private Timer timer;
    private Player player;
    private Bloques[][] bloques;

    // Variables Niveles
    
    int nivel = 1;
    
    double probabilidadDuras = 0.1;
    double vel_pelota = 5;
    
    int filas = 6;
	int columnas = 8;
	int ancho = 79;
	int alto = 30;
	
	// Tiempo
	private int segundos = 0;
	private int minutos = 0;
	
    
    // Labels
    private JLabel contador;
    private JLabel tiempo;
    private JLabel textNivel;
    
    public int puntosJug = 0;
    
    private Bloques ultimoBloqueGolpeado = null;

    public GamePanel() {
    	
    	inicializarBloques();
    	
        setBackground(Color.BLACK);
        timer = new Timer(10, this); // actualiza cada 10 ms
        
        
        // ---- Labels
        
        // Contador
        contador = new JLabel("Puntos: " + puntosJug);
        contador.setForeground(Color.WHITE); // color del texto
        contador.setFont(new Font("Arial", Font.BOLD, 30)); // fuente
        contador.setBounds(20, 30, 200, 40); // posición y tamaño
        
        // Tiempo
        tiempo = new JLabel("00:00");
        tiempo.setForeground(Color.WHITE);
        tiempo.setFont(new Font("Arial", Font.BOLD, 30));
        tiempo.setBounds(315, 30, 200, 40);
        
        // Tiempo
        textNivel = new JLabel("Nivel " + nivel);
        textNivel.setForeground(Color.WHITE);
        textNivel.setFont(new Font("Arial", Font.BOLD, 30));
        textNivel.setBounds(550, 30, 200, 40);
      
        // Tiempo
        
        Timer timerTiempo = new Timer(1000, new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        	    if (!gameOver) {
        	        ball.move(getWidth(), getHeight());
        	        player.move(getWidth());

        	        // Choque pelota con jugador
        	        if (ball.getBounds().intersects(player.getBounds())) {
        	            reproducirSonido("src/sonidos/choque.wav");
        	            ball.dy = -ball.dy;
        	            ball.y = player.y - ball.diameter;
        	        }

        	        // Choque con bloques
        	        Colisionesconbloques();

        	        // Verificar si se completó el nivel
        	        if (nivelCompletado()) {
        	            siguienteNivel();
        	        }

        	        // 🔹 Verificar si tocó el piso
        	        if (ball.y + ball.diameter >= getHeight()) {
        	            gameOver = true;
        	            timer.stop(); // detiene el juego
        	        }
        	    }

        	    repaint();
        	}

        });
        timerTiempo.start();
        
        // Usar layout absoluto para poder ubicar los labels
        this.setLayout(null);
        this.add(contador);
        this.add(tiempo);
        this.add(textNivel);
        
        
    }
    
    public void iniciarJuego() {
        // Reiniciar variables
        segundos = 0;
        minutos = 0;
        puntosJug = 0;

        // Reiniciar pelota y jugador
        ball = new Ball(320, 700, 20, vel_pelota);
        this.removeKeyListener(player);
        player = new Player(320, 700, 100, 15);
        this.addKeyListener(player);
        this.requestFocus();

        inicializarBloques();

        // Ahora sí arranca el juego
        timer.start();
    }



    private void inicializarBloques() {
        int xInicio = 10;  // margen izquierdo
        int yInicio = 110; // margen superior (después de marcador)

        bloques = new Bloques[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int x = xInicio + j * (ancho + 5);
                int y = yInicio + i * (alto + 5);

                int vida = Math.random() < probabilidadDuras ? 2 : 1;
                bloques[i][j] = new Bloques(x, y, ancho, alto, vida);
            }
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);


        // Si terminó el juego, mostrar cartel por encima
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 60));
            g.drawString("GAME OVER", getWidth()/2 - 180, getHeight()/2);
        }
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
        	reproducirSonido("src/sonidos/choque.wav");
            ball.dy = -ball.dy;
            ball.y = player.y - ball.diameter;
        }

        // Choque con bloques
        Colisionesconbloques();
        
        // Verificar si se completó el nivel
        if (nivelCompletado()) {
        	
            siguienteNivel();
        }
        
        repaint();
    }
    
    private boolean nivelCompletado() {
        for (int i = 0; i < bloques.length; i++) {
            for (int j = 0; j < bloques[i].length; j++) {
                if (bloques[i][j].isVisible()) {
                    return false; // queda al menos un bloque
                }
            }
        }
        return true; // todos destruidos
    }
    
    private void siguienteNivel() {
        nivel++;
        textNivel.setText("Nivel " + nivel);

        // Aumentar dificultad
        
        if(nivel%3==0) { // Cada 3 Niveles sube velocidad
        	vel_pelota += 0.5;
        }
        
        // Cant Filas y Columnas
        if(filas<10) {
        	filas += 1;	
        } else {
        	
        	double proxAncho;
        	
        	columnas += 1;
        	proxAncho = 670 - columnas * 5;
        	proxAncho /= columnas;
        	
        	System.out.print(proxAncho);	
        	ancho = (int) Math.floor(proxAncho);
        	System.out.print(ancho);
        	
        }
        
        // Probabilidad Bloques Duros
        	
        if(probabilidadDuras<0.8) {
        	probabilidadDuras += 0.1;
        }
        
        
        

        // Reiniciar pelota y jugador
        ball = new Ball(320, 700, 20, vel_pelota);
        this.removeKeyListener(player); // remover el viejo
        player = new Player(320, 700, 100, 15);
        this.addKeyListener(player);
        this.requestFocus();


        // Crear nueva tanda de bloques
        inicializarBloques();
    }


    private void Colisionesconbloques() {
        Rectangle pelotaRect = ball.getBounds();

        boolean golpeoBloque = false; // Nuevo: para controlar si hubo colisión

        for (int i = 0; i < bloques.length; i++) {
            for (int j = 0; j < bloques[i].length; j++) {
                Bloques b = bloques[i][j];

                if (b.isVisible() && pelotaRect.intersects(b.getBounds())) {
                    // evitar doble golpe en el mismo bloque
                    if (b != ultimoBloqueGolpeado) {

                        // posición anterior de la pelota
                        double prevX = ball.x - ball.dx;
                        double prevY = ball.y - ball.dy;

                        // límites del bloque
                        Rectangle r = b.getBounds();

                        // determinar eje de colisión según posición anterior
                        boolean colisionVertical = prevY + ball.diameter <= r.y || prevY >= r.y + r.height;
                        boolean colisionHorizontal = prevX + ball.diameter <= r.x || prevX >= r.x + r.width;

                        if (colisionVertical) {
                            ball.dy = -ball.dy;
                            if (prevY + ball.diameter <= r.y)
                                ball.y = r.y - ball.diameter; // colocar arriba del bloque
                            else
                                ball.y = r.y + r.height;      // colocar abajo del bloque
                        } else if (colisionHorizontal) {
                            ball.dx = -ball.dx;
                            if (prevX + ball.diameter <= r.x)
                                ball.x = r.x - ball.diameter; // colocar a la izquierda
                            else
                                ball.x = r.x + r.width;       // colocar a la derecha
                        } else {
                            // colisión en esquina: invertir ambos ejes
                            ball.dx = -ball.dx;
                            ball.dy = -ball.dy;
                        }

                        b.recibirGolpe();
                        if (!b.isVisible()) {
                            puntosJug++;
                            contador.setText("Puntos: " + puntosJug);
                        }

                        // recordar este bloque como el último golpeado
                        ultimoBloqueGolpeado = b;
                        golpeoBloque = true; // marcamos que hubo colisión
                    }
                    // rompemos solo el bucle interior, no toda la función
                    break;
                }
            }
        }

        // si no hubo colisión, resetear último bloque golpeado
        if (!golpeoBloque) {
            ultimoBloqueGolpeado = null;
        }

        // 🔹 Verificar game over después de todas las colisiones
        if (ball.y + ball.diameter >= getHeight()) {
            gameOver = true;
            timer.stop();
        }
    }

    
    // Reproducir Sonidos
    
    private void reproducirSonido(String rutaArchivo) {
        try {
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(new File(rutaArchivo));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();
        } catch (Exception e) {
            System.out.println("Error al reproducir sonido: " + e.getMessage());
        }
    }


}
