
package com.uniquindio.bibliotecaonline.modelo;


public class Valoracion implements Comparable <Valoracion>{
    private Libro libro;
    private Lector lector; 
    private int calificacion; 
    private String comentario; 

    // Constructor 
    public Valoracion(Libro libro, Lector lector, int calificacion, String comentario) {
        if (libro == null || lector == null) {
            throw new IllegalArgumentException("Libro y Lector no pueden ser null para una valoración.");
        }
        if (calificacion < 1 || calificacion > 5) { 
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5.");
        }
        this.libro = libro;
        this.lector = lector;
        this.calificacion = calificacion;
        this.comentario = comentario;
    }
    
    public Valoracion(Libro libro, Lector lector, int calificacion) {
        this(libro, lector, calificacion, ""); // Constructor sin comentario
    }

    // --- GETTERS NECESARIOS ---
    public Libro getLibro() { 
        return libro;
    }

    public Lector getLector() { 
        return lector;
    }

    public int getCalificacion() { 
        return calificacion;
    }

    public String getComentario() { 
        return comentario;
    }
    // --- FIN DE GETTERS ---

    @Override
    public int compareTo(Valoracion otro) {
        // ... (tu implementación de compareTo) ...
        int compCalificacion = Integer.compare(this.calificacion, otro.calificacion);
        if (compCalificacion != 0) {
            return compCalificacion;
        }
        // Asumiendo que Libro es Comparable y no null
        if (this.libro == null && otro.libro == null) return 0;
        if (this.libro == null) return -1;
        if (otro.libro == null) return 1;
        return this.libro.compareTo(otro.libro); 
    }

    @Override
    public String toString() {
        // ... (tu implementación de toString) ...
        return "Valoracion{" +
               "libro=" + (libro != null ? libro.getTitulo() : "N/A") +
               ", lector=" + (lector != null ? lector.getNombre() : "N/A") +
               ", calificacion=" + calificacion +
               ", comentario='" + comentario + '\'' +
               '}';
    }
    
}
