package main;

import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.awt.FontFormatException;

public class GameMain extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private GamePanel gamePanel;
    
    public static Font Pixelart; // <- aquí es accesible desde otras clases

    
    public GameMain() {
        setTitle("AuraNoid");
        setSize(700, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        

        // Layout para alternar pantallas
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 🔹 Crear menú
        JPanel menuPanel = crearMenu();

        // 🔹 Crear juego
        gamePanel = new GamePanel();
        GamePanel.reproducirMusica("src/sonidos/musica.wav");

        // Agregar ambos al contenedor principal
        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(gamePanel, "Juego");

        add(mainPanel);
        setVisible(true);
    }
    
    public static JButton crearBoton(String texto, int ancho, int alto) {
        JButton boton = new JButton(texto);
        boton.setFont(Pixelart.deriveFont(26f));
        boton.setForeground(new Color(0xFF00FF));             // letras rojas
        boton.setBackground(Color.BLACK);           // fondo negro
        boton.setOpaque(true);
        boton.setBorder(BorderFactory.createLineBorder(new Color(0xFF00FF), 3)); // borde rojo
        boton.setFocusPainted(false);               // quitar el borde azul al seleccionar
        boton.setPreferredSize(new Dimension(ancho, alto));
        return boton;
    }

    private JPanel crearMenu() {
        // 🔹 Cargar imagen de fondo
        ImageIcon fondoIcon = new ImageIcon("src/resources/portada.png");
        Image fondo = fondoIcon.getImage();

        // 🔹 Crear panel personalizado
        JPanel menuPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
                
                Graphics2D g2 = (Graphics2D) g.create();

                // Fondo negro semitransparente
                g2.setColor(new Color(0, 0, 0, 150)); // último parámetro = opacidad (0-255)
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.dispose();
                super.paintComponent(g); // dibuja el texto encima
            }
        };


        menuPanel.setOpaque(false); // transparente para ver el fondo
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);

        JLabel titulo = new JLabel("AuraNoid");
        titulo.setFont(Pixelart.deriveFont(60f));
        titulo.setForeground(new Color(0xFF00FF));
        gbc.gridy = 0;
        menuPanel.add(titulo, gbc);

        // Botón Jugar
        JButton startButton = crearBoton("Jugar", 200, 50);
        gbc.gridy = 1;
        menuPanel.add(startButton, gbc);

        // Botón Salir
        JButton exitButton = crearBoton("Salir", 200, 50);
        gbc.gridy = 2;
        menuPanel.add(exitButton, gbc);

        // 🔹 Eventos de botones
        startButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "Juego");
            gamePanel.requestFocus(); // activar teclado
            gamePanel.iniciarJuego();
        });

        exitButton.addActionListener(e -> System.exit(0));

        return menuPanel;
    }

    
    

    public static void main(String[] args) {
        

        try {
            // Ruta al archivo de fuente
            File fuenteArchivo = new File("src/resources/Pixellari.ttf");

            // Crear la fuente
            Pixelart = Font.createFont(Font.TRUETYPE_FONT, fuenteArchivo);

            // Registrar la fuente
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Pixelart);

            // Aplicar tamaño
            Pixelart = Pixelart.deriveFont(24f);

        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new GameMain());
    }
}
