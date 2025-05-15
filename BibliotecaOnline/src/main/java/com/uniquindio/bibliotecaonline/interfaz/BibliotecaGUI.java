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

        btnLector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false); // Oculta la interfaz principal
                LectorGui lectorGUI = new LectorGui(BibliotecaGUI.this); // Pasamos la referencia de la interfaz principal
                lectorGUI.setVisible(true);
            }
        });

        btnAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false); // Oculta la interfaz principal
                new LoguinAdminGui(BibliotecaGUI.this); // Abre la pantalla de login
            }
        });

        // Panel derecho con la imagen
        JLabel labelImagen = new JLabel();
        labelImagen.setHorizontalAlignment(JLabel.CENTER);
        labelImagen.setIcon(new ImageIcon("/home/user/Escritorio/Estructuras de Datos/Proyecto/BibliotecaOnline/src/main/java/resources/Files"));

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
