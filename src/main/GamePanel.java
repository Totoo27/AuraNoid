package main;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import java.io.File;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class GamePanel extends JPanel implements ActionListener {
	
	private boolean gameOver = false;  // << NUEVO
	private JButton btnReiniciar;
	
    private Timer timer;
    private Player player;
    private Bloques[][] bloques;
    private final String archivoHighScore = "highscore.txt"; // nombre del archivo
    
    // Records
    private int highScore = 0;
    private int highLevel = 0;
    private int bestMin = 0;
    private int bestSec = 0;
    
    // Pre-Records
    
    private int antScore = 0;
    private int antLevel = 0;
    private int antMin = 0;
    private int antSec = 0;

    // Pelotas
 	public ArrayList<Ball> balls = new ArrayList<>();

    // Variables Niveles
    
    int nivel = 1;
    
    double probabilidadDuras = 0.1;
    double vel_pelota = 5.5;
    
    int filas = 6;
	int columnas = 8;
	int ancho = 79;
	int alto = 30;
	
	// Tiempo
	private int segundos = 0;
	private int minutos = 0;
	Timer timerTiempo;
	
    
    // Labels
    private JLabel contador;
    private JLabel tiempo;
    private JLabel textNivel;
    
    public int puntosJug = 0;
    
    // Bloques
    private Bloques ultimoBloqueGolpeado = null;
    
    // PowerUp
    
    public ArrayList<PowerUp> powerUps = new ArrayList<>();

    public GamePanel() {
    	
    	inicializarBloques();
    	
        setBackground(Color.BLACK);
        timer = new Timer(10, this); // actualiza cada 10 ms
        
        
        // ---- Labels
        
        // Contador
        contador = new JLabel("Puntos: " + puntosJug);
        contador.setForeground(Color.WHITE); // color del texto
        contador.setFont(GameMain.Pixelart.deriveFont(30f)); // fuente
        contador.setBounds(20, 42, 200, 40); // posición y tamaño
        
        // Tiempo
        tiempo = new JLabel("00:00");
        tiempo.setForeground(Color.WHITE);
        tiempo.setFont(GameMain.Pixelart.deriveFont(40f));
        tiempo.setBounds(300, 40, 200, 40);
        
        // Tiempo
        textNivel = new JLabel("Nivel " + nivel);
        textNivel.setForeground(Color.WHITE);
        textNivel.setFont(GameMain.Pixelart.deriveFont(30f));
        textNivel.setBounds(550, 42, 200, 40);
      
        // Tiempo
        
        timerTiempo = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
                if (!gameOver) {
                    // actualizar tiempo
                    segundos++;
                    if (segundos == 60) {
                        segundos = 0;
                        minutos++;
                    }
                    String tiempoTexto = String.format("%02d:%02d", minutos, segundos);
                    tiempo.setText(tiempoTexto);

                    // lógica del juego
                    
                    player.move(getWidth());
                    for(Ball ball : balls) {
                    	ball.move(getWidth(), getHeight());
                        if (ball.getBounds().intersects(player.getBounds())) {
                            reproducirSonido("src/sonidos/choque.wav");
                            ball.dy = -ball.dy;
                            ball.y = player.y - ball.diameter;
                        }
                    }
                    
                    Colisionesconbloques();

                    if (nivelCompletado()) {
                        siguienteNivel();
                    }
                    
                    for (int i = balls.size() - 1; i >= 0; i--) {
                        Ball ball = balls.get(i);
                        if (ball.y + ball.diameter >= getHeight()) {
                            balls.remove(i); // seguro porque no afecta los índices que quedan por recorrer
                            if (balls.isEmpty()) {
                                gameOver = true;
                                guardarHighScore();
                                timer.stop();
                            }
                        }
                    }

                }
                repaint();
            }
        });
        
        
        // Usar layout absoluto para poder ubicar los labels
        this.setLayout(null);
        this.add(contador);
        this.add(tiempo);
        this.add(textNivel);
     // ---- Botón Reiniciar
        btnReiniciar = new JButton("Volver a jugar");
        int btnWidth = 220;
        int btnHeight = 60;
        int btnX = 240;
        int btnY = 500;
        btnReiniciar.setBounds(btnX, btnY, btnWidth, btnHeight);

        // Fuente y colores
        btnReiniciar.setFont(GameMain.Pixelart);
        btnReiniciar.setForeground(Color.RED);            // texto rojo
        btnReiniciar.setBackground(Color.BLACK);          // fondo negro
        btnReiniciar.setOpaque(true);
        btnReiniciar.setBorderPainted(true);
        btnReiniciar.setFocusPainted(false);              // sin efecto de foco

        // Borde rojo grueso
        btnReiniciar.setBorder(BorderFactory.createLineBorder(Color.RED, 4));
        btnReiniciar.setVisible(false); // inicialmente oculto
        btnReiniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnReiniciar.setVisible(false);
                gameOver = false;
                reiniciarAplicacion();
            }
        });
        this.add(btnReiniciar);

        
        
    }
    
    public void reiniciarAplicacion() {
        // Cierra la ventana actual
        SwingUtilities.getWindowAncestor(this).dispose();
        
        // Crea una nueva instancia del JFrame principal
        SwingUtilities.invokeLater(() -> new GameMain());
    }
    
    public void iniciarJuego() {
        // Reiniciar variables
        segundos = 0;
        minutos = 0;
        puntosJug = 0;
        cargarHighScore();


        // Reiniciar pelota y jugador
        
        Ball new_ball = new Ball(340, 700, 20, vel_pelota);
        balls.add(new_ball);
        
        this.removeKeyListener(player);
        player = new Player(300, 700, 100, 15);
        this.addKeyListener(player);
        this.requestFocus();

        inicializarBloques();

        // Ahora sí arranca el juego
        timer.start();
        timerTiempo.start();
    }



    private void inicializarBloques() {
        int xInicio = 10;  // margen izquierdo
        int yInicio = 120; // margen superior (después de marcador)

        bloques = new Bloques[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int x = xInicio + j * (ancho + 5);
                int y = yInicio + i * (alto + 5);

                int vida = Math.random() < probabilidadDuras ? 2 : 1;
                
                if(vida == 2) {
                	vida = Math.random() < probabilidadDuras ? 3 : 2;
                }
                if(vida == 3) {
                	vida = Math.random() < probabilidadDuras ? 4 : 3;
                }
                bloques[i][j] = new Bloques(x, y, ancho, alto, vida);
            }
        }
    }

    private void cargarHighScore() {
    	try (BufferedReader br = new BufferedReader(new FileReader("records.txt"))) {
            String linea = br.readLine();
            if (linea != null) {
                String[] datos = linea.split(";");
                highScore = Integer.parseInt(datos[0]);
                highLevel = Integer.parseInt(datos[1]);
                bestMin = Integer.parseInt(datos[2]);
                bestSec = Integer.parseInt(datos[3]);
            }
    	} catch (IOException e) {
            System.out.println("No se encontró archivo de récords, se crearán al guardar.");
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (contadorEspera > 0) {
        	 
        	 g.setFont(GameMain.Pixelart.deriveFont(50f);

        	 String linea1 = "Nivel Completado";
        	 String linea2 = "Siguiente en: " + contadorEspera + "...";

        	 int ancho1 = g.getFontMetrics().stringWidth(linea1);
        	 int ancho2 = g.getFontMetrics().stringWidth(linea2);

        	 int x1 = getWidth()/2 - ancho1/2;
        	 int x2 = getWidth()/2 - ancho2/2;
        	 int y = getHeight()/2;
        	 
        	 g.setColor(new Color(0x330033));
        	 g.drawString(linea1, x1 + 3, y + 3);
        	 g.drawString(linea2, x2 + 3, y + 73);
        	 
        	 g.setColor(new Color(0xFF00FF));
        	 g.drawString(linea1, x1, y);
        	 g.drawString(linea2, x2, y + 70);
        	 
        	 
        	 return; // detiene temporalmente el juego hasta que termine la cuenta
        }

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(GameMain.Pixelart.deriveFont(50f));
            g.drawString("GAME OVER", getWidth()/2 - 130, getHeight()/2 - 110);

            g.setColor(Color.WHITE);
            g.setFont(GameMain.Pixelart.deriveFont(30f));
            int xIzquierda = 50;                  // lado izquierdo: partida actual
            int xDerecha = getWidth() - 300;      // lado derecho: récords históricos
            int yBase = getHeight()/2 - 20;
            int yOffset = 40;
            
            g.drawString("Record Puntaje: " + highScore, xDerecha, yBase);
            g.drawString("Record Nivel: " + highLevel, xDerecha, yBase + yOffset);
            g.drawString("Mejor Tiempo: " + String.format("%02d:%02d", bestMin, bestSec), xDerecha, yBase + 2*yOffset);
            
            if (puntosJug > antScore) {
            	g.setColor(Color.YELLOW);
            } else { 
            	g.setColor(Color.WHITE); 
            }
            g.drawString("Puntaje: " + puntosJug, xIzquierda, yBase);
            
            if (nivel > antLevel) {
            	g.setColor(Color.YELLOW);
            } else { 
            	g.setColor(Color.WHITE); 
            }
            g.drawString("Nivel: " + nivel, xIzquierda, yBase + yOffset);
            
            if (minutos > antMin || (minutos == antMin && segundos > antSec)) {
            	g.setColor(Color.YELLOW);
            } else { 
            	g.setColor(Color.WHITE); 
            }
            g.drawString("Tiempo: " + String.format("%02d:%02d", minutos, segundos), xIzquierda, yBase + 2*yOffset);


            

            btnReiniciar.setVisible(true);
            return;
        }

        
        Graphics2D g2 = (Graphics2D) g; // casteo a Graphics2D	
        int grosorBorde = 3;
        int grosorBordeB = 1;
        
        // Pelota
        for (Ball ball : balls) {
        	g2.setStroke(new BasicStroke(grosorBordeB));
            g2.setColor(new Color(0x00FFFB));
            g2.drawOval(ball.x, ball.y, ball.diameter, ball.diameter);
            g2.fillOval(ball.x + 2, ball.y + 2, ball.diameter - 4, ball.diameter - 4);
        }
        

        // Jugador

        g2.setStroke(new BasicStroke(grosorBorde));
        g2.setColor(new Color(0xFF00FF));
        g2.drawRect(player.x, player.y, player.width, player.height);
        g2.fillRect(player.x + 6, player.y + 6, player.width - 12, player.height - 12);

        // Línea de Marcador
        g.setColor(new Color(0xFF00FF));
        int y = 100;
        g.fillRect(0, y-5, getWidth(), 5);
        
        // PowerUps
        
        for (PowerUp pu : powerUps) {
        	g2.setStroke(new BasicStroke(grosorBorde));
            g2.setColor(pu.color);
            g2.drawRect(pu.x, pu.y, PowerUp.dimension, PowerUp.dimension);
            g2.fillRect(pu.x + 6, pu.y + 6, PowerUp.dimension - 12, PowerUp.dimension - 12);
        }

        // Dibujar bloques
        for (int i = 0; i < bloques.length; i++) {
            for (int j = 0; j < bloques[i].length; j++) {
                bloques[i][j].dibujar(g);
            }
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        
    	// Movimiento
    	for(Ball ball : balls) {
    		ball.move(getWidth(), getHeight());
    	}
       
    	// Power Ups
        for (int i = 0; i < powerUps.size(); i++) {
            PowerUp pu = powerUps.get(i);
            pu.move(getWidth(), getHeight());

            if (pu.y > getHeight()) {
                powerUps.remove(i);
                i--; 
            }
        }
        player.move(getWidth());
        
        // Choque pelota con jugador
        for(Ball ball : balls) {
        	if (ball.getBounds().intersects(player.getBounds())) {
        		reproducirSonido("src/sonidos/choque.wav");
            	ball.dy = -ball.dy;
            	ball.y = player.y - ball.diameter;
        	}
        }

        // Choque con bloques
        Colisionesconbloques();
        
        // Choque Jugador con PowerUps
        for (int i = powerUps.size() - 1; i >= 0; i--) {
            PowerUp pu = powerUps.get(i);
            if (pu.getBounds().intersects(player.getBounds())) {
            	
            	if(pu.tipo_powerup == 1) {
            		Ball new_ball = new Ball(player.x + player.width/2, player.y - 20, 20, vel_pelota);
                    balls.add(new_ball);
            	} else {
            		ArrayList<Ball> nuevasPelotas = new ArrayList<>();

            		for (Ball ball : balls) {
            		    Ball new_ball = new Ball(ball.x, ball.y, 20, vel_pelota);
            		    new_ball.dy = ball.dy;
            		    new_ball.dx = -ball.dx;
            		    nuevasPelotas.add(new_ball);
            		}
            		
            		balls.addAll(nuevasPelotas);
            	}
                

                powerUps.remove(i);
            }
        }
        
        
        // Verificar si se completó el nivel
        if (nivelCompletado()) {
            siguienteNivel();
        }
        
        repaint();
    }
    
    private boolean nivelCompletado() {
        for (int i = 0; i < bloques.length; i++) {
            for (int j = 0; j < bloques[i].length; j++) {
                if (bloques[i][j].isVisible() || contadorEspera != 0) {
                    return false; // queda al menos un bloque
                }
            }
        }
        	return true; // todos destruidos
    }
    
 // Variable de clase
    private int contadorEspera = 0; // 0 = no mostrar, >0 = mostrar cuenta regresiva

    private void siguienteNivel() {
        nivel++;
        textNivel.setText("Nivel " + nivel);

        // Aumentar dificultad
        if (nivel % 3 == 0) {
            vel_pelota += 0.5;
        }

        if (filas < 10) {
            filas += 1;
        } else {
            columnas += 1;
            double proxAncho = (670 - columnas * 5) / (double) columnas;
            ancho = (int) Math.floor(proxAncho);
        }

        if (probabilidadDuras < 0.8) {
            probabilidadDuras += 0.1;
        }
        timer.stop();
        timerTiempo.stop();
        
        inicializarBloques();
        
        // Resettear powerups y pelotas
        
        for (int i = powerUps.size() - 1; i >= 0; i--) {
            powerUps.remove(i);
        }
        
        for(int i = balls.size() - 1; i >= 0; i--) {
        	balls.remove(i);
        }

        // Resetear pelota y jugador YA MISMO
        Ball new_ball = new Ball(340, 680, 20, vel_pelota);
        balls.add(new_ball);
        
        this.removeKeyListener(player);
        player = new Player(300, 700, 100, 15);
        this.addKeyListener(player);
        this.requestFocus();
        
        contadorEspera = 3;

        Timer timerCuenta = new Timer(1000, null);
        timerCuenta.addActionListener(e -> {
            if (contadorEspera > 0) {
                contadorEspera--;
                repaint();
            } else {
                timerCuenta.stop();
                // Ahora sí arranca el juego
                timer.start();
                timerTiempo.start();
            }
        });
        timerCuenta.start();
    }




    private void Colisionesconbloques() {
        for (Ball ball : balls) { // recorremos todas las pelotas
            Rectangle pelotaRect = ball.getBounds();

            boolean golpeoBloque = false;

            for (int i = 0; i < bloques.length; i++) {
                for (int j = 0; j < bloques[i].length; j++) {
                    Bloques b = bloques[i][j];

                    if (b.isVisible() && pelotaRect.intersects(b.getBounds())) {
                        reproducirSonido("src/sonidos/choque.wav");

                        if (b != ultimoBloqueGolpeado) {
                            Rectangle re = b.getBounds();

                            double prevX = ball.x - ball.dx;
                            double prevY = ball.y - ball.dy;

                            Rectangle r = b.getBounds();

                            boolean colisionVertical = prevY + ball.diameter <= r.y || prevY >= r.y + r.height;
                            boolean colisionHorizontal = prevX + ball.diameter <= r.x || prevX >= r.x + r.width;

                            if (colisionVertical) {
                                ball.dy = -ball.dy;
                                if (prevY + ball.diameter <= r.y)
                                    ball.y = r.y - ball.diameter;
                                else
                                    ball.y = r.y + r.height;
                            } else if (colisionHorizontal) {
                                ball.dx = -ball.dx;
                                if (prevX + ball.diameter <= r.x)
                                    ball.x = r.x - ball.diameter;
                                else
                                    ball.x = r.x + r.width;
                            } else {
                                ball.dx = -ball.dx;
                                ball.dy = -ball.dy;
                            }

                            b.recibirGolpe();

                            if (!b.isVisible()) {
                                double probabilidad = 0.2;
                                if (Math.random() < probabilidad) {
                                    PowerUp nuevo = new PowerUp(re.x + re.width / 2 - PowerUp.dimension / 2,
                                            re.y + re.height / 2 - PowerUp.dimension / 2);
                                    powerUps.add(nuevo);
                                }
                                puntosJug++;
                                contador.setText("Puntos: " + puntosJug);
                            }

                            ultimoBloqueGolpeado = b;
                            golpeoBloque = true;
                        }

                        break; // sale del bucle interior de bloques
                    }
                }
            }

            if (!golpeoBloque) {
                ultimoBloqueGolpeado = null;
            }
        }

            for (int i = balls.size() - 1; i >= 0; i--) {
                Ball ball = balls.get(i);
                if (ball.y + ball.diameter >= getHeight()) {
                    balls.remove(i); // seguro porque no afecta los índices que quedan por recorrer
                    if (balls.isEmpty()) {
                        gameOver = true;
                        
                        antScore = highScore;
                        antLevel = highLevel;
                        antMin = bestMin;
                        antSec = bestSec;
                        
                        // Actualizar récords si la partida los supera
                        if (puntosJug > highScore) {
                            highScore = puntosJug;
                        }
                        
                        if (nivel > highLevel) {
                            highLevel = nivel;
                        }
                        
                        if (minutos > bestMin || (minutos == bestMin && segundos > bestSec)) {
                            bestMin = minutos;
                            bestSec = segundos;
                        }

                        guardarHighScore();
                        timer.stop();
                    }
                }
            }

    }

    private void guardarHighScore() {
    	try (BufferedWriter bw = new BufferedWriter(new FileWriter("records.txt"))) {
            bw.write(highScore + ";" + highLevel + ";" + bestMin + ";" + bestSec);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Reproducir Sonidos
    
    public static void reproducirSonido(String rutaArchivo) {
        try {
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(new File(rutaArchivo));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();
        } catch (Exception e) {
            System.out.println("Error al reproducir sonido: " + e.getMessage());
        }
    }
    
    public static void reproducirMusica(String rutaArchivo) {
        try {
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(new File(rutaArchivo));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // bucle infinito
            clip.start();
        } catch (Exception e) {
            System.out.println("Error al reproducir música: " + e.getMessage());
        }
    }



}
