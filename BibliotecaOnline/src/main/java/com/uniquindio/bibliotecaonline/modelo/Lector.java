package com.uniquindio.bibliotecaonline.modelo;

import com.uniquindio.bibliotecaonline.estructuras.ListaEnlazada;
import java.time.LocalDate;

public class Lector extends Usuario implements Comparable<Lector> {

    protected ListaEnlazada<Prestamo> historialPrestamos;
    protected ListaEnlazada<Valoracion> valoracionesRealizadas;

    public Lector(String idUsuario, String nombre, String correo, String contraseña, LocalDate añoNacimiento) {
        super(idUsuario, nombre, correo, contraseña, añoNacimiento);
        this.historialPrestamos = new ListaEnlazada<>();
        this.valoracionesRealizadas = new ListaEnlazada<>();
    }

    public void agregarPrestamo(Prestamo prestamo) {
        if (prestamo != null) {
            historialPrestamos.insertarElementoAlFinal(prestamo);
        }
    }

    public void agregarValoracion(Valoracion valoracion) {
        if (valoracion != null) {
            valoracionesRealizadas.insertarElementoAlFinal(valoracion);
        }
    }

    @Override
    public int compareTo(Lector otro) {
        return this.getNombre().compareToIgnoreCase(otro.getNombre());
    }

    public ListaEnlazada<Prestamo> getHistorialPrestamos() {
        return this.historialPrestamos;
    }

    public ListaEnlazada<Prestamo> getPrestamosActivos() {
        ListaEnlazada<Prestamo> activos = new ListaEnlazada<>();
        if (this.historialPrestamos != null) {
            for (Prestamo p : this.historialPrestamos) {
                // Aquí es crucial que Prestamo tenga el método isDevuelto()
                if (p != null && !p.isDevuelto()) {
                    activos.insertarElementoAlFinal(p);
                }
            }
        }
        return activos;
    }
    
    /**
     * Agrega una valoración hecha por este lector a su lista de valoraciones realizadas.
     * Verifica que la valoración pertenezca a este lector y evita duplicados
     * si el lector ya ha valorado el mismo libro anteriormente.
     *
     * @param valoracion La valoración a agregar.
     * @return true si la valoración fue agregada exitosamente, false en caso contrario
     * (e.g., si la valoración es nula, no pertenece al lector, o el lector ya valoró ese libro).
     */
    public boolean agregarValoracionRealizada(Valoracion valoracion) {
        if (valoracion == null || valoracion.getLibro() == null) {
            System.err.println("Intento de agregar valoración nula o sin libro asociado.");
            return false;
        }

        // Opcional: Verificar si la valoración realmente fue hecha por este lector
        // if (!valoracion.getLector().equals(this)) {
        //     System.err.println("Intento de agregar una valoración que no pertenece a este lector.");
        //     return false;
        // }

        // Verificar si ya valoró este libro específico
        for (Valoracion existente : this.valoracionesRealizadas) {
            if (existente.getLibro().equals(valoracion.getLibro())) {
                // System.out.println("Lector " + this.getNombre() + " ya ha valorado el libro " + valoracion.getLibro().getTitulo());
                return false; // Ya valoró este libro, no se agrega de nuevo. La excepción se manejará en GestorLibros.
            }
        }

        // Si no la encontró, la agrega
        this.valoracionesRealizadas.insertarElementoAlFinal(valoracion);
        return true;
    }
    
    
    /**
     * Verifica si el lector ya ha realizado una valoración para un libro específico.
     * Itera sobre la lista 'valoracionesRealizadas' del lector.
     *
     * @param libro El libro a verificar.
     * @return true si ya existe una valoración para ese libro por este lector, false en caso contrario.
     */
    public boolean haValoradoLibro(Libro libro) {
        if (libro == null || this.valoracionesRealizadas == null || this.valoracionesRealizadas.isEmpty()) {
            return false; // No hay libro para verificar, o el lector no ha hecho ninguna valoración.
        }

        for (Valoracion v : valoracionesRealizadas) {
            if (v.getLibro() != null && v.getLibro().equals(libro)) {
                // Se asume que Libro.equals() compara adecuadamente los libros (ej. por título o ID)
                // Y que Valoracion.getLibro() devuelve el libro asociado a esa valoración.
                return true; // Encontró una valoración hecha por este lector para este libro.
            }
        }
        return false; // No se encontró ninguna valoración para este libro hecha por este lector.
    }

   
}
