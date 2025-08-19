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

    private JPanel crearMenu() {
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(Color.BLACK);
        menuPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);

        JLabel titulo = new JLabel("BRICK BREAKER");
        titulo.setFont(new Font("Arial", Font.BOLD, 40));
        titulo.setForeground(Color.WHITE);
        gbc.gridy = 0;
        menuPanel.add(titulo, gbc);

        JButton startButton = new JButton("Jugar");
        gbc.gridy = 1;
        menuPanel.add(startButton, gbc);

        JButton exitButton = new JButton("Salir");
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
