package main;

import javax.swing.JFrame;
import java.awt.EventQueue;

public class Main {
    public static void main(String[] args) {
    	
    	final int WIDTH = 700;
    	final int HEIGHT = 800;
    	
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Auranoid");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(WIDTH, HEIGHT);
            frame.setResizable(false);

            GamePanel panel = new GamePanel();
            frame.add(panel);

            frame.setVisible(true);
        });
    }
}
