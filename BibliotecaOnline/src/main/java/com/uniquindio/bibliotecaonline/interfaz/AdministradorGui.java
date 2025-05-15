
package com.uniquindio.bibliotecaonline.interfaz;

import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class AdministradorGui extends JFrame {
    
    private BibliotecaGUI bibliotecaGUI; // Referencia a la interfaz principal
    
    public AdministradorGui(BibliotecaGUI bibliotecaGUI) {
        this.bibliotecaGUI = bibliotecaGUI;
        
        setTitle("Interfaz de Administrador");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel de estadísticas
        JPanel panelEstadistica = new JPanel();
        JButton btnBuscar = new JButton("Generar Estadísticas");
        JComboBox<String> comboEstadisticas = new JComboBox<>(new String[]{
            "Seleccione", "Cantidad de préstamos por lector", "Libros más valorados", 
            "Lectores con más conexiones", "Caminos más cortos entre dos lectores", 
            "Detectar clústeres de afinidad (grupos)"
        });

        panelEstadistica.add(new JLabel("Seleccione una estadística:"));
        panelEstadistica.add(comboEstadisticas);
        panelEstadistica.add(btnBuscar);

        // Panel para los botones con FlowLayout para evitar que ocupen toda la pantalla
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Centra los botones y les da espacio

        JButton btnLibros = new JButton("Añadir y eliminar libros");
        JButton btnUsuarios = new JButton("Gestionar usuarios");
        JButton btnGrafo = new JButton("Visualizar el grafo de afinidad entre lectores");
        JButton btnAtras = new JButton("Atrás");

        // Definir tamaños preferidos para los botones
        Dimension botonSize = new Dimension(220, 40);
        btnLibros.setPreferredSize(botonSize);
        btnUsuarios.setPreferredSize(botonSize);
        btnGrafo.setPreferredSize(botonSize);
        btnAtras.setPreferredSize(botonSize);

        panelBotones.add(btnLibros);
        panelBotones.add(btnUsuarios);
        panelBotones.add(btnGrafo);
        panelBotones.add(btnAtras);

        // Agregar los paneles al frame
        add(panelEstadistica, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);

        // Acción del botón Atrás para regresar a BibliotecaGUI
        btnAtras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bibliotecaGUI.setVisible(true); // Muestra la interfaz principal
                dispose(); // Cierra la interfaz de administrador
            }
        });

        setVisible(true);
    }
}
