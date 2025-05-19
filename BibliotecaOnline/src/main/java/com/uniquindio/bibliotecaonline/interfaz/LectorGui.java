package com.uniquindio.bibliotecaonline.interfaz;

import com.uniquindio.bibliotecaonline.modelo.Libro;
import com.uniquindio.bibliotecaonline.estructuras.ListaEnlazada;
import com.uniquindio.bibliotecaonline.logica.GestorLectores;
import com.uniquindio.bibliotecaonline.logica.GestorLibros;
import com.uniquindio.bibliotecaonline.logica.GestorPrestamos;
import com.uniquindio.bibliotecaonline.modelo.Lector;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class LectorGui extends JFrame {

    private Lector lectorActual;
    private final BibliotecaGUI bibliotecaGUI;
    private final GestorPrestamos gestor;

    private JButton btnSolicitarPrestamo, btnDevolverLibro, btnValorarLibro, btnRecomendaciones, btnSugerencias;
    private JButton btnBuscar, btnAtras, btnChat;

    private JTextField campoBusqueda;
    private JComboBox<String> comboCategorias, comboAutores;
    private JRadioButton rbtnTitulo, rbtnAutor, rbtnCategoria;
    private JScrollPane scrollPane;

    private JPanel panelOpciones;
    private JPanel panelBotones;
    private JPanel panelPrincipal;

    public LectorGui(BibliotecaGUI bibliotecaGUI, Lector lector) {
        this.bibliotecaGUI = Objects.requireNonNull(bibliotecaGUI, "BibliotecaGUI no puede ser null");
        this.lectorActual = Objects.requireNonNull(lector, "Lector no puede ser null");
        this.gestor = inicializarGestor();

        configurarFrameBasico();
        inicializarComponentes();
        configurarEventos();

        setTitle("Interfaz de Lector - " + lector.getNombre());
        setVisible(true);
    }

    private GestorPrestamos inicializarGestor() {
    try {
        GestorLibros gestorLibros = new GestorLibros();     // crea gestor de libros
        GestorLectores gestorLectores = new GestorLectores(); // crea gestor de lectores
        return new GestorPrestamos(gestorLibros, gestorLectores);  // pásalos al constructor
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,
                "Error crítico: No se pudo inicializar el sistema.\n" + e.getMessage(),
                "Error de inicialización",
                JOptionPane.ERROR_MESSAGE);
        System.exit(1);
        return null;
    }
}


    private void configurarFrameBasico() {
        setTitle("Interfaz de Lector");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {
        // Panel de búsqueda superior
        JPanel panelBusqueda = new JPanel(new FlowLayout());
        campoBusqueda = new JTextField(20);
        btnBuscar = new JButton("Buscar");
        panelBusqueda.add(new JLabel("¿Qué estás buscando?"));
        panelBusqueda.add(campoBusqueda);
        panelBusqueda.add(btnBuscar);

        // Panel de opciones (radio buttons y combo)
        configurarPanelOpciones();

        // Panel principal para mostrar libros
        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(panelPrincipal);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Panel inferior de navegación
        JPanel panelInferior = new JPanel(new FlowLayout());
        btnAtras = new JButton("Atrás");
        btnChat = new JButton("Chat con Bibliotecario");
        panelInferior.add(btnAtras);
        panelInferior.add(btnChat);

        // Panel con botones de acciones del lector
        configurarPanelBotones();

        // Agregar componentes al frame
        add(panelBusqueda, BorderLayout.NORTH);
        add(panelOpciones, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
        add(panelBotones, BorderLayout.EAST);
    }

    private void configurarPanelOpciones() {
        panelOpciones = new JPanel();
        panelOpciones.setLayout(new BoxLayout(panelOpciones, BoxLayout.Y_AXIS));
        panelOpciones.setBorder(BorderFactory.createTitledBorder("Opciones de Búsqueda"));

        rbtnTitulo = new JRadioButton("Por Título");
        rbtnAutor = new JRadioButton("Por Autor");
        rbtnCategoria = new JRadioButton("Por Categoría");

        ButtonGroup grupoBusqueda = new ButtonGroup();
        grupoBusqueda.add(rbtnTitulo);
        grupoBusqueda.add(rbtnAutor);
        grupoBusqueda.add(rbtnCategoria);
        rbtnTitulo.setSelected(true);

        comboAutores = new JComboBox<>();
        comboCategorias = new JComboBox<>(new String[]{
            "Ficción", "Fantasía", "Drama", "Romance", "Ciencia ficción"
        });

        comboAutores.setEnabled(false);
        comboCategorias.setEnabled(false);

        panelOpciones.add(rbtnTitulo);
        panelOpciones.add(rbtnAutor);
        panelOpciones.add(comboAutores);
        panelOpciones.add(rbtnCategoria);
        panelOpciones.add(comboCategorias);
    }

    private void configurarPanelBotones() {
        panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(6, 1, 5, 5));

        btnSolicitarPrestamo = new JButton("Solicitar préstamo de libros");
        btnDevolverLibro = new JButton("Devolver libros");
        btnValorarLibro = new JButton("Valorar libros");
        btnRecomendaciones = new JButton("Consultar Recomendaciones");
        btnSugerencias = new JButton("Ver sugerencias");

        panelBotones.add(btnSolicitarPrestamo);
        panelBotones.add(btnDevolverLibro);
        panelBotones.add(btnValorarLibro);
        panelBotones.add(btnRecomendaciones);
        panelBotones.add(btnSugerencias);
    }

    private void configurarEventos() {
        rbtnTitulo.addActionListener(e -> {
            campoBusqueda.setEnabled(true);
            comboCategorias.setEnabled(false);
            comboAutores.setEnabled(false);
        });

        rbtnAutor.addActionListener(e -> {
            campoBusqueda.setEnabled(false);
            comboCategorias.setEnabled(false);
            comboAutores.setEnabled(true);
            cargarAutores();
        });

        rbtnCategoria.addActionListener(e -> {
            campoBusqueda.setEnabled(false);
            comboAutores.setEnabled(false);
            comboCategorias.setEnabled(true);
            cargarLibrosPorCategoria((String) comboCategorias.getSelectedItem());
        });

        comboCategorias.addActionListener(e -> {
            if (rbtnCategoria.isSelected()) {
                cargarLibrosPorCategoria((String) comboCategorias.getSelectedItem());
            }
        });

        comboAutores.addActionListener(e -> {
            if (rbtnAutor.isSelected()) {
                cargarLibrosPorAutor((String) comboAutores.getSelectedItem());
            }
        });

        btnBuscar.addActionListener(e -> {
            if (rbtnTitulo.isSelected()) {
                buscarPorTitulo(campoBusqueda.getText());
            }
        });

        btnAtras.addActionListener(e -> {
            bibliotecaGUI.setVisible(true);
            dispose();
        });

        // Aquí puedes agregar listeners para btnSolicitarPrestamo, btnValorarLibro, etc.
    }

    private void cargarAutores() {
        comboAutores.removeAllItems();
        ListaEnlazada<String> autores = gestor.obtenerTodosLosAutores();
        for (String autor : autores) {
            comboAutores.addItem(autor);
        }
    }

    private void cargarLibrosPorCategoria(String categoria) {
        panelPrincipal.removeAll();
        ListaEnlazada<Libro> libros = gestor.buscarPorCategoria(categoria);

        for (Libro libro : libros) {
            panelPrincipal.add(crearPanelLibro(libro));
            panelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        panelPrincipal.revalidate();
        panelPrincipal.repaint();
    }

    private void cargarLibrosPorAutor(String autor) {
        panelPrincipal.removeAll();
        ListaEnlazada<Libro> libros = gestor.buscarPorAutor(autor);

        for (Libro libro : libros) {
            panelPrincipal.add(crearPanelLibro(libro));
            panelPrincipal.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        panelPrincipal.revalidate();
        panelPrincipal.repaint();
    }

    private void buscarPorTitulo(String titulo) {
        panelPrincipal.removeAll();
        Libro libro = gestor.buscarPorTitulo(titulo);

        if (libro != null) {
            panelPrincipal.add(crearPanelLibro(libro));
        } else {
            panelPrincipal.add(new JLabel("No se encontró el libro: " + titulo));
        }

        panelPrincipal.revalidate();
        panelPrincipal.repaint();
    }

    private JPanel crearPanelLibro(Libro libro) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panel.setPreferredSize(new Dimension(500, 60));

        JLabel lblTitulo = new JLabel(libro.getTitulo());
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel lblDetalles = new JLabel(String.format(
                "Autor: %s | Categoría: %s | Año: %d | Calificación: %.1f | Estado: %s",
                libro.getAutor(), libro.getCategoria(), libro.getAñoPublicacion(),
                libro.getCalificacion(), libro.getEstado()
        ));

        JButton btnAccion = new JButton(libro.getEstado().equals(Libro.DISPONIBLE) ? "Solicitar" : "Devolver");
        btnAccion.addActionListener(e -> manejarAccionLibro(libro, btnAccion));

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(lblDetalles, BorderLayout.CENTER);
        panel.add(btnAccion, BorderLayout.EAST);

        return panel;
    }

    private void manejarAccionLibro(Libro libro, JButton btnAccion) {
        if (libro.getEstado().equals(Libro.DISPONIBLE)) {
            try {
                gestor.registrarPrestamo(lectorActual, libro); // usar registrarPrestamo directamente
                btnAccion.setText("Devolver");
                JOptionPane.showMessageDialog(this, "Préstamo exitoso.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "No se pudo realizar el préstamo: " + e.getMessage());
            }
        } else {
            boolean devuelto = gestor.devolverLibro(lectorActual, libro);
            if (devuelto) {
                btnAccion.setText("Solicitar");
                JOptionPane.showMessageDialog(this, "Libro devuelto correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al devolver el libro.");
            }
        }
    }

}
