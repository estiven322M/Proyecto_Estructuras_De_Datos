package com.uniquindio.bibliotecaonline.estructuras;

import java.util.Iterator;

/**
 * Implementación especializada de ListaEnlazada para manejar usuarios. Hereda
 * toda la funcionalidad básica de ListaEnlazada y añade el soporte para
 * Iterable.
 *
 * @param <Usuario> Tipo de usuario que debe implementar Comparable<Usuario>
 */
public class ListaEnlazadaLectores<Usuario extends Comparable<Usuario>>
        extends ListaEnlazada<Usuario>
        implements Iterable<Usuario> {

    /**
     * Implementación básica del iterador para la lista de lectores
     */
    @Override
    public Iterator<Usuario> iterator() {
        return new Iterator<Usuario>() {
            private Nodo<Usuario> actual = primero;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public Usuario next() {
                if (!hasNext()) {
                    throw new java.util.NoSuchElementException();
                }
                Usuario dato = actual.dato;
                actual = actual.enlace;
                return dato;
            }
        };
    }

    /**
     * Busca un usuario en la lista (sobrescribe el método para mejor
     * legibilidad)
     *
     * @param usuario Usuario a buscar
     * @return true si el usuario existe en la lista
     */
    @Override
    public boolean busqueda(Usuario usuario) {
        return super.busqueda(usuario);
    }
}
