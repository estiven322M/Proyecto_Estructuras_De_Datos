package com.uniquindio.bibliotecaonline.interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BibliotecaGUI extends JFrame {

    public BibliotecaGUI() {
        setTitle("Biblioteca Digital");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel izquierdo con botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(2, 1, 10, 10));
        panelBotones.setBackground(new Color(30, 130, 76)); // Color similar al de la imagen

        JButton btnLector = new JButton("Lector");
        JButton btnAdmin = new JButton("Administrador");

        panelBotones.add(btnLector);
        panelBotones.add(btnAdmin);



        btnLector.addActionListener(e -> {
            setVisible(false);
            new LoguinLectorGui(this); // Primero autentica
        });
        
        btnAdmin.addActionListener(e->{
            setVisible(false);
            new LoguinAdminGui(this);
        });

        // Panel derecho con la imagen
        ImageIcon imagenOriginal = new ImageIcon(getClass().getResource("/images/biblioteca.jpg"));

        Image imagenEscalada = imagenOriginal.getImage().getScaledInstance(600, 500, Image.SCALE_SMOOTH);
        ImageIcon imagenFinal = new ImageIcon(imagenEscalada);

        JLabel labelImagen = new JLabel(imagenFinal);

        labelImagen.setHorizontalAlignment(JLabel.CENTER);

        // Cambia esto a la imagen correcta
        // Agregar paneles al frame
        add(panelBotones, BorderLayout.WEST);
        add(labelImagen, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BibliotecaGUI::new);
    }

}
