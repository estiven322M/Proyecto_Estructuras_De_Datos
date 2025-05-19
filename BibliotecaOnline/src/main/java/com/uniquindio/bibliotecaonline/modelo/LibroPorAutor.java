package com.uniquindio.bibliotecaonline.modelo;

import com.uniquindio.bibliotecaonline.estructuras.ListaEnlazada;

public class LibroPorAutor implements Comparable<LibroPorAutor> {
    private final String autor;
    private final ListaEnlazada<Libro> librosDelAutor;

    public LibroPorAutor(String autor) {
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("El autor no puede estar vacío");
        }
        this.autor = autor;
        this.librosDelAutor = new ListaEnlazada<>();
    }

    public void agregarLibro(Libro libro) {
        if (libro == null) {
            throw new IllegalArgumentException("El libro no puede ser null");
        }
        librosDelAutor.insertarElementoAlFinal(libro);
    }

    @Override
    public int compareTo(LibroPorAutor otro) {
        return this.autor.compareToIgnoreCase(otro.autor);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LibroPorAutor that = (LibroPorAutor) obj;
        return autor.equalsIgnoreCase(that.autor);
    }

    @Override
    public int hashCode() {
        return autor.toLowerCase().hashCode();
    }

    // Getters
    public String getAutor() { return autor; }
    public ListaEnlazada<Libro> getLibrosDelAutor() { return librosDelAutor; }
}