package com.uniquindio.bibliotecaonline.interfaz;

import com.uniquindio.bibliotecaonline.modelo.Lector;
import com.uniquindio.bibliotecaonline.modelo.Libro;
import com.uniquindio.bibliotecaonline.modelo.Prestamo;
import com.uniquindio.bibliotecaonline.modelo.Valoracion;
import com.uniquindio.bibliotecaonline.estructuras.ListaEnlazada;
import com.uniquindio.bibliotecaonline.logica.GestorPrestamos;
import com.uniquindio.bibliotecaonline.logica.GestorLibros;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DialogoValorarLibros extends JDialog {

    private final Lector lectorActual;
    private final GestorLibros gestorLibros;
    private final GestorPrestamos gestorPrestamos; // Puede que lo necesites si alguna lógica de valoración pasa por GestorPrestamos
    private final LectorGui ventanaPrincipal;
    private JPanel panelLibrosAValorar;

    public DialogoValorarLibros(Frame owner, Lector lector, GestorLibros gestorLibros, GestorPrestamos gestorPrestamos, LectorGui ventanaPrincipal) {
        super(owner, "Valorar Libros Leídos", true);
        this.lectorActual = lector;
        this.gestorLibros = gestorLibros;
        this.gestorPrestamos = gestorPrestamos; // Asignar
        this.ventanaPrincipal = ventanaPrincipal;

        setSize(700, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        panelLibrosAValorar = new JPanel();
        panelLibrosAValorar.setLayout(new BoxLayout(panelLibrosAValorar, BoxLayout.Y_AXIS));
        panelLibrosAValorar.setBackground(Color.WHITE);
        panelLibrosAValorar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(panelLibrosAValorar);
        add(scrollPane, BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSur.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
        panelSur.add(btnCerrar);
        add(panelSur, BorderLayout.SOUTH);

        cargarLibrosAValorar();
    }

    private void cargarLibrosAValorar() {
        panelLibrosAValorar.removeAll();
        ListaEnlazada<Libro> librosUnicosDevueltosAunNoValorados = new ListaEnlazada<>();
        ListaEnlazada<Prestamo> historial = lectorActual.getHistorialPrestamos();

        if (historial == null || historial.isEmpty()) {
            panelLibrosAValorar.add(new JLabel("  No tienes historial de préstamos."));
            panelLibrosAValorar.revalidate();
            panelLibrosAValorar.repaint();
            return;
        }

        // Obtener una lista de libros únicos que han sido devueltos y que el lector aún no ha valorado
        for (Prestamo prestamo : historial) {
            if (prestamo.isDevuelto()) {
                Libro libroDelPrestamo = prestamo.getLibro();
                if (libroDelPrestamo != null && !lectorActual.haValoradoLibro(libroDelPrestamo)) {
                    // Evitar añadir el mismo libro múltiples veces a la lista de "a valorar"
                    boolean yaEnListaAValorar = false;
                    for (Libro libroEnLista : librosUnicosDevueltosAunNoValorados) {
                        if (libroEnLista.equals(libroDelPrestamo)) {
                            yaEnListaAValorar = true;
                            break;
                        }
                    }
                    if (!yaEnListaAValorar) {
                        librosUnicosDevueltosAunNoValorados.insertarElementoAlFinal(libroDelPrestamo);
                    }
                }
            }
        }

        if (!librosUnicosDevueltosAunNoValorados.isEmpty()) {
            for (Libro libro : librosUnicosDevueltosAunNoValorados) {
                panelLibrosAValorar.add(crearPanelValoracionParaLibro(libro));
                panelLibrosAValorar.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        } else {
            panelLibrosAValorar.add(new JLabel("  No hay libros pendientes de valoración o ya has valorado todos tus libros devueltos."));
        }

        panelLibrosAValorar.revalidate();
        panelLibrosAValorar.repaint();
    }

    private JPanel crearPanelValoracionParaLibro(Libro libro) {
        JPanel panelContenedor = new JPanel(new BorderLayout(10, 5));
        panelContenedor.setBorder(BorderFactory.createTitledBorder(libro.getTitulo() + " (Autor: " + libro.getAutor() + ")"));
        panelContenedor.setBackground(new Color(250, 250, 250));
        //panelContenedor.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150)); // Controlar altura

        // Sección de calificación
        JPanel panelCalificacion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCalificacion.setOpaque(false);
        panelCalificacion.add(new JLabel("Calificación (1-5 estrellas):"));
        Integer[] estrellas = {1, 2, 3, 4, 5};
        JComboBox<Integer> comboEstrellas = new JComboBox<>(estrellas);
        comboEstrellas.setSelectedItem(5); // Por defecto 5 estrellas
        panelCalificacion.add(comboEstrellas);

        // Sección de comentario
        JPanel panelComentario = new JPanel(new BorderLayout());
        panelComentario.setOpaque(false);
        panelComentario.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        panelComentario.add(new JLabel("Comentario (opcional):"), BorderLayout.NORTH);
        JTextArea areaComentario = new JTextArea(3, 30);
        areaComentario.setLineWrap(true);
        areaComentario.setWrapStyleWord(true);
        JScrollPane scrollComentario = new JScrollPane(areaComentario);
        panelComentario.add(scrollComentario, BorderLayout.CENTER);

        // Botón Guardar
        JButton btnGuardar = new JButton("Guardar Valoración");
        btnGuardar.setBackground(new Color(40, 167, 69)); // Verde
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnGuardar.addActionListener((ActionEvent e) -> { // Especificar ActionEvent
            try {
                int calificacionSeleccionada = (Integer) comboEstrellas.getSelectedItem();
                String comentarioTexto = areaComentario.getText().trim();

                // Llamar a un método en un gestor para procesar la valoración
                gestorLibros.registrarValoracion(lectorActual, libro, calificacionSeleccionada, comentarioTexto);

                JOptionPane.showMessageDialog(this, "Valoración para '" + libro.getTitulo() + "' guardada exitosamente.", "Valoración Exitosa", JOptionPane.INFORMATION_MESSAGE);

                // Actualizar la lista en este diálogo (el libro valorado ya no aparecerá)
                cargarLibrosAValorar();

                if (ventanaPrincipal != null) {
                    ventanaPrincipal.actualizarVistaPanelPrincipal(); // Refrescar catálogo principal para ver nueva calificación
                }

            } catch (IllegalArgumentException | IllegalStateException iae) { // Capturar también IllegalStateException
                JOptionPane.showMessageDialog(this, "No se pudo guardar la valoración: " + iae.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) { // Captura más genérica para otros errores
                JOptionPane.showMessageDialog(this, "Error inesperado al guardar la valoración: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        JPanel panelGuardar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelGuardar.setOpaque(false);
        panelGuardar.add(btnGuardar);

        // Panel central para calificación y comentario
        JPanel panelCentral = new JPanel();
        panelCentral.setOpaque(false);
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.add(panelCalificacion);
        panelCentral.add(panelComentario);

        panelContenedor.add(panelCentral, BorderLayout.CENTER);
        panelContenedor.add(panelGuardar, BorderLayout.SOUTH);

        return panelContenedor;
    }

}
