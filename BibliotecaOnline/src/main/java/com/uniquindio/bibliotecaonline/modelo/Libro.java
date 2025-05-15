
package com.uniquindio.bibliotecaonline.modelo;


public class Libro {
    private String titulo;
    private String autor;
    private String categoria;
    private int añoPublicacion; 
    private String estado; 
    private double calificacion;

    // Constantes para los estados
    public static final String DISPONIBLE = "Disponible";
    public static final String PRESTADO = "Prestado";

    public Libro(String titulo, String autor, String categoria, int añoPublicacion, String estado, double calificacion) {
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.añoPublicacion = añoPublicacion;
        this.estado = estado;
        this.calificacion = calificacion;
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

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }
    
    
    
}
