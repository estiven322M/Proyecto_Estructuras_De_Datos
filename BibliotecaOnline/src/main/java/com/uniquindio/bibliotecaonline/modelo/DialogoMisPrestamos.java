package com.uniquindio.bibliotecaonline.modelo;

import com.uniquindio.bibliotecaonline.modelo.Lector;
import com.uniquindio.bibliotecaonline.modelo.Prestamo;
import com.uniquindio.bibliotecaonline.estructuras.ListaEnlazada;
import com.uniquindio.bibliotecaonline.interfaz.LectorGui;
import com.uniquindio.bibliotecaonline.logica.GestorPrestamos;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class DialogoMisPrestamos extends JDialog {

    private final Lector lectorActual;
    private final GestorPrestamos gestorPrestamos;
    private final LectorGui ventanaPrincipal; // Para refrescar el catálogo después de devolver
    private JPanel panelPrestamos;
    private JScrollPane scrollPane;

    public DialogoMisPrestamos(Frame owner, Lector lector, GestorPrestamos gestor, LectorGui ventanaPrincipal) {
        super(owner, "Mis Libros Prestados", true); // true para modal
        this.lectorActual = lector;
        this.gestorPrestamos = gestor;
        this.ventanaPrincipal = ventanaPrincipal;

        setSize(600, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        panelPrestamos = new JPanel();
        panelPrestamos.setLayout(new BoxLayout(panelPrestamos, BoxLayout.Y_AXIS));
        panelPrestamos.setBackground(Color.WHITE);

        scrollPane = new JScrollPane(panelPrestamos);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSur.add(btnCerrar);
        add(panelSur, BorderLayout.SOUTH);

        cargarPrestamosActivos();
    }

    private void cargarPrestamosActivos() {
        panelPrestamos.removeAll();
        ListaEnlazada<Prestamo> prestamosActivos = lectorActual.getPrestamosActivos();

        if (prestamosActivos.isEmpty()) {
            panelPrestamos.add(new JLabel("  No tienes libros prestados actualmente."));
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
            for (Prestamo prestamo : prestamosActivos) {
                if (prestamo.getLibro() != null) { // Asegurarse que el libro no es null
                    panelPrestamos.add(crearPanelPrestamo(prestamo, formatter));
                    panelPrestamos.add(Box.createRigidArea(new Dimension(0, 5)));
                }
            }
        }
        panelPrestamos.revalidate();
        panelPrestamos.repaint();
    }

    private JPanel crearPanelPrestamo(Prestamo prestamo, DateTimeFormatter formatter) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        panel.setBackground(new Color(248, 248, 248));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel lblTitulo = new JLabel(prestamo.getLibro().getTitulo());
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));

        String infoPrestamo = String.format("<html>Prestado: %s<br>Devolver antes de: %s</html>",
                prestamo.getFechaPrestamo().format(formatter),
                prestamo.getFechaDevolucion().format(formatter)
        );
        JLabel lblFechas = new JLabel(infoPrestamo);
        lblFechas.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JPanel panelInfo = new JPanel(new GridLayout(0, 1));
        panelInfo.setOpaque(false);
        panelInfo.add(lblTitulo);
        panelInfo.add(lblFechas);

        JButton btnDevolver = new JButton("Devolver");
        btnDevolver.setBackground(new Color(220, 53, 69)); // Rojo
        btnDevolver.setForeground(Color.WHITE);
        btnDevolver.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnDevolver.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Estás seguro de que quieres devolver el libro '" + prestamo.getLibro().getTitulo() + "'?",
                    "Confirmar Devolución",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    // Llama al método en GestorPrestamos que se creará en el siguiente paso
                    gestorPrestamos.procesarDevolucion(lectorActual, prestamo);
                    JOptionPane.showMessageDialog(this, "Libro '" + prestamo.getLibro().getTitulo() + "' devuelto exitosamente.", "Devolución Exitosa", JOptionPane.INFORMATION_MESSAGE);
                    cargarPrestamosActivos(); // Refrescar la lista en este diálogo
                    if (ventanaPrincipal != null) {
                        ventanaPrincipal.actualizarVistaPanelPrincipal(); // Refrescar el catálogo principal
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al devolver el libro: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        panel.add(panelInfo, BorderLayout.CENTER);
        panel.add(btnDevolver, BorderLayout.EAST);
        return panel;

    }
}
