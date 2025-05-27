
package com.uniquindio.bibliotecaonline.modelo;

import com.uniquindio.bibliotecaonline.estructuras.ListaEnlazada;



public class Libro implements Comparable<Libro> {
    private String titulo;
    private String autor;
    private String categoria; // o genero
    private int añoPublicacion; 
    private String estado; 
    private double calificacion;
    
    private ListaEnlazada<Valoracion> valoracionesRecibidas; //  Lista de valoraciones para este libro
    // Constantes para los estados
    public static final String DISPONIBLE = "Disponible";
    public static final String PRESTADO = "Prestado";
    
    

       // constructor que recibe todos los parametros
    public Libro(String titulo, String autor, String categoria, int añoPublicacion, String estado, double calificacionInicial) {
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.añoPublicacion = añoPublicacion;
        setEstado(estado); // Usamos el setter para validación
        this.calificacion = calificacionInicial;
        this.valoracionesRecibidas=new ListaEnlazada<>();
    }
    
     // Constructor simplificado para búsquedas por título (usado en GestorPrestamos)
    public Libro(String titulo) {
        this.titulo = titulo;
        // Inicializar otros campos con valores por defecto
        this.autor = "";
        this.categoria = "";
        this.añoPublicacion = 0;
        this.estado = DISPONIBLE;
        this.calificacion = 0.0;
        this.valoracionesRecibidas=new ListaEnlazada<>(); //Inicializar lista
    }
    
    @Override
    public int compareTo(Libro otro) {
        //return this.titulo.compareToIgnoreCase(otro.titulo);
        if (this.titulo == null && otro.titulo == null) {
            return 0;
        }
        if (this.titulo == null) {
            return -1; // nulls primero
        }
        if (otro.titulo == null) {
            return 1;
        }
        return this.titulo.compareToIgnoreCase(otro.titulo);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Libro libro = (Libro) obj;
        // Compara por un identificador único si lo tienes, o por título.
        // Para el funcionamiento con el árbol de búsqueda por título,
        // la igualdad basada en título (ignorando mayúsculas/minúsculas) es crucial.
        
        return titulo != null ? titulo.equalsIgnoreCase(libro.titulo) : libro.titulo == null;
    }
    
    @Override
    public int hashCode() {
        // Coherente con equals
        return titulo != null ? titulo.toLowerCase().hashCode() : 0;
    }

    // Getters y Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getAñoPublicacion() {
        return añoPublicacion;
    }

    public void setAñoPublicacion(int añoPublicacion) {
        this.añoPublicacion = añoPublicacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        if (estado.equals(DISPONIBLE) || estado.equals(PRESTADO)) {
            this.estado = estado;
        } else {
            throw new IllegalArgumentException("Estado inválido. Debe ser 'Disponible' o 'Prestado'.");
        }
    }

    public double getCalificacion() {
        return calificacion;
    }

//    public void setCalificacion(double calificacion) {
//        // Validar que la calificación esté entre 0 y 5
//        if (calificacion < 0 || calificacion > 5) {
//            throw new IllegalArgumentException("La calificación debe estar entre 0 y 5");
//        }
//        this.calificacion = calificacion;
//    }
    
     public void agregarValoracionRecibida(Valoracion valoracion) {
        if (valoracion != null && valoracion.getLibro().equals(this)) { // Asegurar que la valoración es para este libro
            this.valoracionesRecibidas.insertarElementoAlFinal(valoracion);
            recalcularCalificacionPromedio();
        }
    }
     
     public ListaEnlazada<Valoracion> getValoracionesRecibidas() {
        return valoracionesRecibidas;
    }
     
      private void recalcularCalificacionPromedio() {
        if (valoracionesRecibidas.isEmpty()) {
            this.calificacion = 0.0; // O alguna calificación base si no hay valoraciones
            return;
        }
        double sumaCalificaciones = 0;
        for (Valoracion v : valoracionesRecibidas) {
            sumaCalificaciones += v.getCalificacion();
        }
        this.calificacion = sumaCalificaciones / valoracionesRecibidas.size();
    }
    
    // Método para cambiar el estado a prestado
    public void prestar() {
        if (this.estado.equals(PRESTADO)) {
            throw new IllegalStateException("El libro ya está prestado");
        }
        this.estado = PRESTADO;
    }
    
    // Método para devolver un libro
    public void devolver() {
        if (this.estado.equals(DISPONIBLE)) {
            throw new IllegalStateException("El libro ya está disponible");
        }
        this.estado = DISPONIBLE;
    }
    
    @Override
    public String toString() {
        return "Libro{" +
               "titulo='" + titulo + '\'' +
               ", autor='" + autor + '\'' +
               ", categoria='" + categoria + '\'' +
               ", añoPublicacion=" + añoPublicacion +
               ", estado='" + estado + '\'' +
               ", calificacion=" + calificacion +
               '}';
    }
}