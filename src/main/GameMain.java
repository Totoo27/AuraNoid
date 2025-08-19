package main;

import javax.swing.*;
import java.awt.*;

public class GameMain extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private GamePanel gamePanel;

    public GameMain() {
        setTitle("AuraNoid");
        setSize(700, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout para alternar pantallas
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 🔹 Crear menú
        JPanel menuPanel = crearMenu();

        // 🔹 Crear juego
        gamePanel = new GamePanel();

        // Agregar ambos al contenedor principal
        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(gamePanel, "Juego");

        add(mainPanel);
        setVisible(true);
    }

    // Función para crear botones con estilo uniforme
    private JButton crearBoton(String texto, int ancho, int alto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 24));
        boton.setForeground(Color.RED);             // letras rojas
        boton.setBackground(Color.BLACK);           // fondo negro
        boton.setOpaque(true);
        boton.setBorder(BorderFactory.createLineBorder(Color.RED, 3)); // borde rojo
        boton.setFocusPainted(false);               // quitar el borde azul al seleccionar
        boton.setPreferredSize(new Dimension(ancho, alto));
        return boton;
    }

    private JPanel crearMenu() {
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(Color.BLACK);
        menuPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);

        // Título
        JLabel titulo = new JLabel("BRICK BREAKER");
        titulo.setFont(new Font("Arial", Font.BOLD, 40));
        titulo.setForeground(Color.WHITE);
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
        SwingUtilities.invokeLater(() -> new GameMain());
    }
}
