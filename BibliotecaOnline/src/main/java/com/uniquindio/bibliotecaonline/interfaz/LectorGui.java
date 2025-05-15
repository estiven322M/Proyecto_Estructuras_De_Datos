package com.uniquindio.bibliotecaonline.interfaz;

import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LectorGui extends JFrame {

    private BibliotecaGUI bibliotecaGUI; // Referencia a la interfaz principal

    // **Constructor corregido**
    public LectorGui(BibliotecaGUI bibliotecaGUI) {
        this.bibliotecaGUI = bibliotecaGUI;

        setTitle("Interfaz de Lector");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel superior con barra de búsqueda
        JPanel panelBusqueda = new JPanel();
        JTextField campoBusqueda = new JTextField(30);
        JButton btnBuscar = new JButton("Buscar");
        panelBusqueda.add(new JLabel("¿Qué estás buscando?"));
        panelBusqueda.add(campoBusqueda);
        panelBusqueda.add(btnBuscar);

        // Panel con opciones de búsqueda
        JPanel panelOpciones = new JPanel();
        JRadioButton rbtnTitulo = new JRadioButton("Por Título");
        JRadioButton rbtnAutor = new JRadioButton("Por Autor");
        JRadioButton rbtnCategoria = new JRadioButton("Por Categoría");
        ButtonGroup grupoOpciones = new ButtonGroup();
        grupoOpciones.add(rbtnTitulo);
        grupoOpciones.add(rbtnAutor);
        grupoOpciones.add(rbtnCategoria);

        JComboBox<String> comboCategorias = new JComboBox<>(new String[]{
            "Seleccione", "Ficción", "No Ficción", "Ciencia", "Historia", "Fantasía", "Misterio", "Tecnología"
        });
        panelOpciones.add(rbtnTitulo);
        panelOpciones.add(rbtnAutor);
        panelOpciones.add(rbtnCategoria);
        panelOpciones.add(comboCategorias);

        // Panel con botones de opciones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(3, 2, 10, 10));
        panelBotones.add(new JButton("Solicitar préstamo de libros"));
        panelBotones.add(new JButton("Devolver libros"));
        panelBotones.add(new JButton("Valorar libros"));
        panelBotones.add(new JButton("Consultar Recomendaciones de libros según valoraciones propias"));
        panelBotones.add(new JButton("Ver sugerencias de lectores con gustos similares"));
        JButton btnAtras = new JButton("Atras");
        panelBotones.add(btnAtras);

        // Botón de Chat en la parte inferior derecha
        JPanel panelChat = new JPanel();
        JButton btnChat = new JButton("Chat");
        panelChat.add(btnChat);

        // Agregar los paneles al frame
        add(panelBusqueda, BorderLayout.NORTH);
        add(panelOpciones, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        add(panelChat, BorderLayout.EAST);

        // **Acción del botón Atrás para regresar a BibliotecaGUI**
        btnAtras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bibliotecaGUI.setVisible(true); // Muestra la interfaz principal
                dispose(); // Cierra la interfaz de lector
            }
        });

        setVisible(true);
    }
}
