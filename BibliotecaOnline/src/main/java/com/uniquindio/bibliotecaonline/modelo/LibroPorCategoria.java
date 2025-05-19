package com.uniquindio.bibliotecaonline.modelo;

import com.uniquindio.bibliotecaonline.estructuras.ListaEnlazada;

public class LibroPorCategoria implements Comparable<LibroPorCategoria> {
    private final String categoria;
    private final ListaEnlazada<Libro> librosDeLaCategoria;

    public LibroPorCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría no puede estar vacía");
        }
        this.categoria = categoria;
        this.librosDeLaCategoria = new ListaEnlazada<>();
    }

    public void agregarLibro(Libro libro) {
        if (libro == null) {
            throw new IllegalArgumentException("El libro no puede ser null");
        }
        librosDeLaCategoria.insertarElementoAlFinal(libro);
    }

    @Override
    public int compareTo(LibroPorCategoria otro) {
        return this.categoria.compareToIgnoreCase(otro.categoria);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LibroPorCategoria that = (LibroPorCategoria) obj;
        return categoria.equalsIgnoreCase(that.categoria);
    }

    @Override
    public int hashCode() {
        return categoria.toLowerCase().hashCode();
    }

    // Getters
    public String getCategoria() { return categoria; }
    public ListaEnlazada<Libro> getLibrosDeLaCategoria() { return librosDeLaCategoria; }
}