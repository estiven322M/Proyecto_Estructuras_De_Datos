package com.uniquindio.bibliotecaonline.interfaz;

import com.uniquindio.bibliotecaonline.modelo.Libro;
import com.uniquindio.bibliotecaonline.estructuras.ListaEnlazada;
import com.uniquindio.bibliotecaonline.logica.GestorLectores;
import com.uniquindio.bibliotecaonline.logica.GestorLibros;
import com.uniquindio.bibliotecaonline.logica.GestorPrestamos;
import com.uniquindio.bibliotecaonline.modelo.DialogoMisPrestamos;
import com.uniquindio.bibliotecaonline.modelo.Lector;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class LectorGui extends JFrame {

    private Lector lectorActual;
    private final BibliotecaGUI bibliotecaGUI;
    private  GestorPrestamos gestor;
    private GestorLibros gestorLibros;

    private JButton btnSolicitarPrestamo, btnDevolverLibro, btnValorarLibro, btnRecomendaciones, btnSugerencias;
    private JButton btnBuscar, btnAtras, btnChat;

    private JTextField campoBusqueda;
    private JComboBox<String> comboCategorias, comboAutores;
    private JRadioButton rbtnTitulo, rbtnAutor, rbtnCategoria;
    private JScrollPane scrollPane;

    private JPanel panelOpciones;
    private JPanel panelBotones;
    private JPanel panelPrincipal; // Panel donde se mostrarán los libros

    public LectorGui(BibliotecaGUI bibliotecaGUI, Lector lector) {
        this.bibliotecaGUI = Objects.requireNonNull(bibliotecaGUI, "BibliotecaGUI no puede ser null");
        this.lectorActual = Objects.requireNonNull(lector, "Lector no puede ser null");

        // La inicialización de GestorPrestamos ahora usa los libros del archivo vía GestorLibros
        //this.gestor = inicializarGestor();
        // Inicializar ambos gestores juntos
        inicializarGestoresPrincipales();
        
        // Estas llamadas deben ir DESPUÉS de que los gestores estén listos
        configurarFrameBasico();
        inicializarComponentes(); // Depende de gestores para cargar datos iniciales en combos, etc.
        configurarEventos();    // Los eventos pueden usar los gestores

        

        mostrarLibrosIniciales(); // <<---- LLAMADA AL METODO

        setTitle("Interfaz de Lector - " + lector.getNombre());
        setSize(900, 700); // Ajusta el tamaño si es necesario para ver todo
        setLocationRelativeTo(null);
        setVisible(true);

     
    }

     /**
     * Inicializa GestorLibros y GestorPrestamos.
     * GestorPrestamos depende de GestorLibros.
     */
    private void inicializarGestoresPrincipales() {
        try {
            this.gestorLibros = new GestorLibros(); // 1. Crear GestorLibros
            GestorLectores gestorLectores = new GestorLectores(); // Crear GestorLectores
            
            // 2. Crear GestorPrestamos, pasándole las instancias necesarias
            this.gestor = new GestorPrestamos(this.gestorLibros, gestorLectores); 

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error crítico: No se pudo inicializar el sistema de gestores.\n" + e.getMessage(),
                    "Error de Inicialización de Gestores", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(1); // Salir si los gestores no pueden inicializarse
        }
    }

    private void configurarFrameBasico() {
        //setTitle("Interfaz de Lector"); // Se establece después con el nombre del lector
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Correcto
        //setSize(800, 600); // Se puede ajustar en el constructor
        //setLocationRelativeTo(null); // Se puede ajustar en el constructor
        setLayout(new BorderLayout(10, 10)); // Añadir algunos gaps
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margen general
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
        panelPrincipal.setBackground(Color.WHITE); // Fondo blanco para el área de libros
        scrollPane = new JScrollPane(panelPrincipal);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // Mejor que ALWAYS
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Catálogo de Libros"));

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
        panelOpciones.setPreferredSize(new Dimension(200, 0)); // Ancho preferido

        rbtnTitulo = new JRadioButton("Por Título");
        rbtnAutor = new JRadioButton("Por Autor");
        rbtnCategoria = new JRadioButton("Por Categoría");

        ButtonGroup grupoBusqueda = new ButtonGroup();
        grupoBusqueda.add(rbtnTitulo);
        grupoBusqueda.add(rbtnAutor);
        grupoBusqueda.add(rbtnCategoria);
        rbtnTitulo.setSelected(true);

        comboAutores = new JComboBox<>();
        // Las categorías podrían cargarse dinámicamente también, o desde un archivo de configuración
        comboCategorias = new JComboBox<>(new String[]{
            "Ficción", "Fantasía", "Drama", "Romance", "Ciencia ficción"
        });
        comboCategorias.insertItemAt("-- Seleccione Categoría --", 0); // Item por defecto
        comboCategorias.setSelectedIndex(0);

        comboAutores.setEnabled(false);
        comboCategorias.setEnabled(false);
        //campoBusqueda.setEnabled(true); // Habilitado por defecto ya que rbtnTitulo está seleccionado

        // Mejorar la disposición de los ComboBox con los RadioButton
        JPanel panelAutorConCombo = new JPanel(new BorderLayout());
        panelAutorConCombo.add(rbtnAutor, BorderLayout.NORTH);
        panelAutorConCombo.add(comboAutores, BorderLayout.CENTER);

        JPanel panelCategoriaConCombo = new JPanel(new BorderLayout());
        panelCategoriaConCombo.add(rbtnCategoria, BorderLayout.NORTH);
        panelCategoriaConCombo.add(comboCategorias, BorderLayout.CENTER);

        panelOpciones.add(rbtnTitulo);
        panelOpciones.add(Box.createRigidArea(new Dimension(0, 5)));
        panelOpciones.add(panelAutorConCombo);
        panelOpciones.add(Box.createRigidArea(new Dimension(0, 5)));
        panelOpciones.add(panelCategoriaConCombo);
    }

    private void configurarPanelBotones() {
        panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(5, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0)); // Margen izquierdo

        //btnSolicitarPrestamo = new JButton("Solicitar préstamo de libros"); // Este botón ya está por libro
        btnDevolverLibro = new JButton("Ver mis libros prestados"); // Cambiado
        btnValorarLibro = new JButton("Valorar libros (Mis préstamos)"); // Cambiado
        btnRecomendaciones = new JButton("Consultar Recomendaciones");
        btnSugerencias = new JButton("Ver Sugerencias del Sistema");

        //panelBotones.add(btnSolicitarPrestamo); // Eliminado de aquí
        panelBotones.add(btnDevolverLibro);
        panelBotones.add(btnValorarLibro);
        panelBotones.add(btnRecomendaciones);
        panelBotones.add(btnSugerencias);
    }

    private void mostrarLibrosIniciales() {
        panelPrincipal.removeAll(); // Limpia el panel antes de añadir nuevos libros

        if (gestor == null) {
            panelPrincipal.add(new JLabel("Error: El gestor de préstamos no está inicializado."));
            panelPrincipal.revalidate();
            panelPrincipal.repaint();
            return;
        }

        ListaEnlazada<Libro> todosLosLibros = gestor.obtenerTodosLosLibros();

        if (todosLosLibros == null || todosLosLibros.isEmpty()) {
            panelPrincipal.add(new JLabel("No hay libros disponibles en el catálogo en este momento."));
        } else {
            for (Libro libro : todosLosLibros) {
                if (libro != null) { // Verificación adicional
                    panelPrincipal.add(crearPanelLibro(libro));
                    panelPrincipal.add(Box.createRigidArea(new Dimension(0, 5))); // Espacio reducido
                }
            }
        }
        panelPrincipal.revalidate();
        panelPrincipal.repaint();
        //scrollPane.getViewport().setViewPosition(new Point(0,0)); // Asegura que el scroll esté arriba
    }

    private void configurarEventos() {
        rbtnTitulo.addActionListener(e -> {
            campoBusqueda.setEnabled(true);
            comboCategorias.setEnabled(false);
            comboCategorias.setSelectedIndex(0); // Resetear combo
            comboAutores.setEnabled(false);
            if (comboAutores.getItemCount() > 0) {
                comboAutores.setSelectedIndex(0); // Resetear combo
            }            // Opcional: Limpiar la lista o mostrar todos los libros de nuevo
            // mostrarLibrosIniciales();
            campoBusqueda.requestFocus();
        });

        rbtnAutor.addActionListener(e -> {
            campoBusqueda.setEnabled(false);
            campoBusqueda.setText(""); // Limpiar campo de texto
            comboCategorias.setEnabled(false);
            comboCategorias.setSelectedIndex(0);
            comboAutores.setEnabled(true);
            cargarAutores(); // Carga autores en el JComboBox
            if (comboAutores.getItemCount() > 0) {
                cargarLibrosPorAutor((String) comboAutores.getSelectedItem());
            } else {
                panelPrincipal.removeAll();
                panelPrincipal.add(new JLabel("No hay autores disponibles."));
                panelPrincipal.revalidate();
                panelPrincipal.repaint();
            }
        });

        rbtnCategoria.addActionListener(e -> {
            campoBusqueda.setEnabled(false);
            campoBusqueda.setText("");
            comboAutores.setEnabled(false);
            if (comboAutores.getItemCount() > 0) {
                comboAutores.setSelectedIndex(0);
            }
            comboCategorias.setEnabled(true);
            // Cargar libros de la categoría seleccionada por defecto o la primera
            if (comboCategorias.getSelectedIndex() > 0) { // Evitar "-- Seleccione Categoría --"
                cargarLibrosPorCategoria((String) comboCategorias.getSelectedItem());
            } else { // Si es "-- Seleccione Categoría --", limpiar resultados
                panelPrincipal.removeAll();
                panelPrincipal.add(new JLabel("Seleccione una categoría para ver los libros."));
                panelPrincipal.revalidate();
                panelPrincipal.repaint();
            }
        });

        comboCategorias.addActionListener(e -> {
            if (rbtnCategoria.isSelected() && comboCategorias.getSelectedIndex() > 0) {
                cargarLibrosPorCategoria((String) comboCategorias.getSelectedItem());
            } else if (rbtnCategoria.isSelected() && comboCategorias.getSelectedIndex() == 0) {
                panelPrincipal.removeAll();
                panelPrincipal.add(new JLabel("Seleccione una categoría para ver los libros."));
                panelPrincipal.revalidate();
                panelPrincipal.repaint();
            }
        });

        comboAutores.addActionListener(e -> {
            if (rbtnAutor.isSelected() && comboAutores.getSelectedItem() != null) {
                cargarLibrosPorAutor((String) comboAutores.getSelectedItem());
            }
        });

        btnBuscar.addActionListener(e -> {
            if (rbtnTitulo.isSelected()) {
                String textoBusqueda = campoBusqueda.getText().trim();
                if (!textoBusqueda.isEmpty()) {
                    buscarPorTitulo(textoBusqueda);
                } else {
                    JOptionPane.showMessageDialog(this, "Por favor, ingrese un título para buscar.", "Campo Vacío", JOptionPane.INFORMATION_MESSAGE);
                    mostrarLibrosIniciales(); // O mostrar todos si el campo está vacío
                }
            }
            // Si se quiere que el botón buscar funcione para autor/categoría (aunque ya lo hacen los combos)
            // else if (rbtnAutor.isSelected() && comboAutores.getSelectedItem() != null) {
            //    cargarLibrosPorAutor((String) comboAutores.getSelectedItem());
            // } else if (rbtnCategoria.isSelected() && comboCategorias.getSelectedItem() != null && comboCategorias.getSelectedIndex() > 0) {
            //    cargarLibrosPorCategoria((String) comboCategorias.getSelectedItem());
            // }
        });

        btnAtras.addActionListener(e -> {
            bibliotecaGUI.setVisible(true);
            dispose();
        });

        // Listeners para los botones de acción del lector
        btnDevolverLibro.addActionListener(e -> {
            // Crear y mostrar el nuevo diálogo
            DialogoMisPrestamos dialogo = new DialogoMisPrestamos(this, lectorActual, gestor, this);
            dialogo.setVisible(true);
            // La lógica de devolución y actualización de UI ocurrirá dentro del diálogo
            // y mediante la llamada a `this.actualizarVistaPanelPrincipal()` desde el diálogo.
        });
        btnValorarLibro.addActionListener(e -> {
            // Asegúrate que gestorLibros y gestor (GestorPrestamos) estén disponibles
            if (this.gestorLibros == null || this.gestor == null) { // 'gestor' es tu GestorPrestamos
                JOptionPane.showMessageDialog(this, "Error: Los gestores de sistema no están inicializados.", "Error Interno", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Esta es la línea clave que abre el nuevo diálogo:
            DialogoValorarLibros dialogo = new DialogoValorarLibros(this, lectorActual, this.gestorLibros, this.gestor, this);
            dialogo.setVisible(true);
        });
        btnRecomendaciones.addActionListener(e -> consultarRecomendaciones());
        btnSugerencias.addActionListener(e -> verSugerencias());

        // Chat (implementación pendiente)
        btnChat.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidad de Chat no implementada.", "Chat", JOptionPane.INFORMATION_MESSAGE));
    }

    // --- MÉTODOS PARA LOS NUEVOS BOTONES DE ACCIÓN ---
//    private void mostrarDialogoDevolverLibros() {
//        // Aquí necesitarás obtener los libros prestados por `lectorActual`
//        // y mostrarlos en un diálogo para que elija cuál devolver.
//        // Ejemplo:
//        // ListaEnlazada<Prestamo> prestamos = lectorActual.getPrestamosActivos();
//        // if (prestamos.isEmpty()) {
//        //     JOptionPane.showMessageDialog(this, "No tienes libros prestados actualmente.", "Devolver Libros", JOptionPane.INFORMATION_MESSAGE);
//        //     return;
//        // }
//        // ...lógica para mostrar un JList o similar y permitir la devolución...
//        JOptionPane.showMessageDialog(this, "Funcionalidad 'Ver mis libros prestados / Devolver' no implementada completamente.", "Devolver Libros", JOptionPane.INFORMATION_MESSAGE);
//    }
    private void mostrarDialogoValorarLibros() {
        // Similar a devolver, mostrar los libros que el lector ha leído (prestamos completados)
        // para que pueda valorarlos.
        JOptionPane.showMessageDialog(this, "Funcionalidad 'Valorar libros' no implementada completamente.", "Valorar Libros", JOptionPane.INFORMATION_MESSAGE);
    }

    private void consultarRecomendaciones() {
        // Lógica para obtener y mostrar recomendaciones personalizadas para el lectorActual
        // panelPrincipal.removeAll();
        // ListaEnlazada<Libro> recomendaciones = gestor.obtenerRecomendacionesParaLector(lectorActual);
        // ... mostrar recomendaciones en panelPrincipal ...
        JOptionPane.showMessageDialog(this, "Funcionalidad 'Consultar Recomendaciones' no implementada.", "Recomendaciones", JOptionPane.INFORMATION_MESSAGE);
    }

    private void verSugerencias() {
        panelPrincipal.removeAll();
        // El método obtenerSugerencias() ya existe en GestorPrestamos
        ListaEnlazada<Libro> sugerencias = gestor.obtenerSugerencias();
        if (sugerencias.isEmpty()) {
            panelPrincipal.add(new JLabel("No hay sugerencias disponibles en este momento."));
        } else {
            for (Libro libro : sugerencias) {
                panelPrincipal.add(crearPanelLibro(libro));
                panelPrincipal.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }
        panelPrincipal.revalidate();
        panelPrincipal.repaint();
    }

    private void cargarAutores() {
        comboAutores.removeAllItems(); // Limpiar antes de cargar
        ListaEnlazada<String> autores = gestor.obtenerTodosLosAutores(); // Desde GestorPrestamos
        if (autores != null && !autores.isEmpty()) {
            for (String autor : autores) {
                comboAutores.addItem(autor);
            }
        } else {
            comboAutores.addItem("No hay autores"); // Placeholder si no hay autores
        }
    }

    private void cargarLibrosPorCategoria(String categoria) {
        panelPrincipal.removeAll();
        if (categoria == null || categoria.equals("-- Seleccione Categoría --")) {
            panelPrincipal.add(new JLabel("Por favor, seleccione una categoría válida."));
            panelPrincipal.revalidate();
            panelPrincipal.repaint();
            return;
        }
        ListaEnlazada<Libro> libros = gestor.buscarPorCategoria(categoria);

        if (libros.isEmpty()) {
            panelPrincipal.add(new JLabel("No se encontraron libros para la categoría: " + categoria));
        } else {
            for (Libro libro : libros) {
                panelPrincipal.add(crearPanelLibro(libro));
                panelPrincipal.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }
        panelPrincipal.revalidate();
        panelPrincipal.repaint();
    }

    private void cargarLibrosPorAutor(String autor) {
        panelPrincipal.removeAll();
        if (autor == null || autor.equals("No hay autores")) {
            panelPrincipal.add(new JLabel("Por favor, seleccione un autor válido."));
            panelPrincipal.revalidate();
            panelPrincipal.repaint();
            return;
        }
        ListaEnlazada<Libro> libros = gestor.buscarPorAutor(autor);

        if (libros.isEmpty()) {
            panelPrincipal.add(new JLabel("No se encontraron libros para el autor: " + autor));
        } else {
            for (Libro libro : libros) {
                panelPrincipal.add(crearPanelLibro(libro));
                panelPrincipal.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }
        panelPrincipal.revalidate();
        panelPrincipal.repaint();
    }

    private void buscarPorTitulo(String titulo) {
        panelPrincipal.removeAll();
        Libro libro = gestor.buscarPorTitulo(titulo); // Usa el método de GestorPrestamos

        if (libro != null) {
            panelPrincipal.add(crearPanelLibro(libro));
        } else {
            panelPrincipal.add(new JLabel("No se encontró ningún libro con el título: '" + titulo + "'"));
        }
        panelPrincipal.revalidate();
        panelPrincipal.repaint();
    }

    private JPanel crearPanelLibro(Libro libro) {
        JPanel panel = new JPanel(new BorderLayout(5, 0)); // Gap horizontal
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1, true), // Borde redondeado suave
                BorderFactory.createEmptyBorder(8, 8, 8, 8) // Padding interno
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); // Altura máxima para consistencia
        panel.setBackground(new Color(245, 245, 245)); // Color de fondo suave

        JLabel lblTitulo = new JLabel(libro.getTitulo());
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16)); // Fuente mejorada

        // Usar HTML para mejor formato en el JLabel de detalles
        String detallesTexto = String.format(
                "<html><b>Autor:</b> %s<br><b>Categoría:</b> %s | <b>Año:</b> %d<br><b>Calificación:</b> %.1f | <b>Estado:</b> %s</html>",
                libro.getAutor(), libro.getCategoria(), libro.getAñoPublicacion(),
                libro.getCalificacion(), libro.getEstado()
        );
        JLabel lblDetalles = new JLabel(detallesTexto);
        lblDetalles.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JButton btnAccion = new JButton();
        configurarBotonAccionLibro(btnAccion, libro); // Método para configurar el botón

        btnAccion.addActionListener(e -> manejarAccionLibro(libro, btnAccion));

        JPanel panelInfo = new JPanel(new BorderLayout());
        panelInfo.setOpaque(false); // Hacer transparente para que se vea el fondo del panel principal
        panelInfo.add(lblTitulo, BorderLayout.NORTH);
        panelInfo.add(lblDetalles, BorderLayout.CENTER);

        panel.add(panelInfo, BorderLayout.CENTER);
        panel.add(btnAccion, BorderLayout.EAST);

        // Tooltip con más información si es necesario
        panel.setToolTipText("Más detalles sobre " + libro.getTitulo());

        return panel;
    }

    private void configurarBotonAccionLibro(JButton btnAccion, Libro libro) {
        if (libro.getEstado().equals(Libro.DISPONIBLE)) {
            btnAccion.setText("Solicitar Préstamo");
            btnAccion.setBackground(new Color(70, 130, 180)); // Azul acero
            btnAccion.setForeground(Color.WHITE);
            btnAccion.setEnabled(true);
        } else if (libro.getEstado().equals(Libro.PRESTADO)) {
            // Verificar si el lector actual es quien tiene el libro prestado
            // Esta lógica requiere que Lector tenga una lista de sus préstamos.
            // boolean esDeLectorActual = lectorActual.tienePrestado(libro); (Método hipotético)
            // Por ahora, asumimos que si está prestado, el lector actual podría querer devolverlo si es suyo.
            // O, simplemente mostrar "No Disponible" si no es del lector.

            // Simplificación: Si está prestado, y es este lector quien lo tiene, puede devolverlo.
            // Necesitaríamos una forma de verificar esto. Por ahora, si está prestado, no se puede solicitar.
            // Y la devolución se manejaría desde "Mis libros prestados".
            // Por ahora, el botón "Devolver" individual por libro se puede quitar si se maneja globalmente.
            // O, cambiar su comportamiento.
            // Para la visualización general del catálogo, si está PRESTADO, no se puede solicitar.
            btnAccion.setText("No Disponible");
            btnAccion.setBackground(Color.LIGHT_GRAY);
            btnAccion.setForeground(Color.DARK_GRAY);
            btnAccion.setEnabled(false);
        }
        btnAccion.setFocusPainted(false);
        btnAccion.setFont(new Font("SansSerif", Font.BOLD, 12));
    }

    private void manejarAccionLibro(Libro libro, JButton btnAccion) {
        // Solo se maneja la acción de "Solicitar Préstamo" desde aquí.
        // La devolución se gestiona desde la sección "Mis libros prestados".
        if (libro.getEstado().equals(Libro.DISPONIBLE)) {
            try {
                gestor.registrarPrestamo(lectorActual, libro);
                JOptionPane.showMessageDialog(this, "Préstamo de '" + libro.getTitulo() + "' registrado exitosamente.", "Préstamo Exitoso", JOptionPane.INFORMATION_MESSAGE);
                // Actualizar el estado del libro en la UI
                configurarBotonAccionLibro(btnAccion, libro); // Reconfigura el botón (mostrará "No Disponible")
                // Opcional: podrías querer refrescar toda la lista o solo este item.
                // Para actualizar solo este item, podrías guardar una referencia al panel del libro.
                // Por simplicidad, el usuario verá el cambio si vuelve a buscar o la lista se refresca.
                // Si el libro sigue visible, el botón se actualizará.
                // Es importante que el objeto 'libro' que se muestra sea el mismo que se modifica en gestor.registrarPrestamo.
                // GestorPrestamos.registrarPrestamo actualiza el estado del Libro en el arbolPorTitulo.
                // Por lo tanto, la instancia de libro aquí debería reflejar ese cambio si se obtiene de nuevo o
                // si la referencia es la misma.
                // Re-renderizar el panel del libro específico o toda la lista para ver el cambio de estado del libro globalmente.
                // Una forma simple de verlo es refrescar la vista actual:
                actualizarVistaPanelPrincipal();

            } catch (IllegalStateException | IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Error al solicitar préstamo: " + ex.getMessage(), "Error de Préstamo", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) { // Captura genérica para otros errores inesperados
                JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Este libro no está disponible para préstamo.", "Libro No Disponible", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Método auxiliar para refrescar la vista actual del panelPrincipal
     * manteniendo el filtro o búsqueda actual si es posible.
     */
    public void actualizarVistaPanelPrincipal() {
        if (rbtnTitulo.isSelected() && !campoBusqueda.getText().trim().isEmpty()) {
            buscarPorTitulo(campoBusqueda.getText().trim());
        } else if (rbtnAutor.isSelected() && comboAutores.getSelectedItem() != null && !comboAutores.getSelectedItem().toString().equals("No hay autores")) {
            cargarLibrosPorAutor((String) comboAutores.getSelectedItem());
        } else if (rbtnCategoria.isSelected() && comboCategorias.getSelectedIndex() > 0) {
            cargarLibrosPorCategoria((String) comboCategorias.getSelectedItem());
        } else {
            // Si no hay filtro activo, muestra todos los libros (o la vista por defecto)
            mostrarLibrosIniciales();
        }
    }

}
