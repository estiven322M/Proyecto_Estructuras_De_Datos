package com.uniquindio.bibliotecaonline.estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;





/**
 * Implementación de una lista enlazada simple genérica que almacena elementos comparables.
 * 
 * @param <T> Tipo de elementos que implementan Comparable<T>
 */
public class ListaEnlazada<T extends Comparable<T>> implements Iterable<T>{

    protected Nodo<T> primero;
    protected Nodo<T> ultimo;
    private int tamaño;

    /**
     * Constructor que inicializa una lista enlazada vacía.
     */
    public ListaEnlazada() {
        this.primero = null;
        this.ultimo = null;
        this.tamaño = 0;
    }

    public Nodo<T> getPrimero() {
        return primero;
    }

    public void setPrimero(Nodo<T> primero) {
        this.primero = primero;
    }

    public Nodo<T> getUltimo() {
        return ultimo;
    }

    public void setUltimo(Nodo<T> ultimo) {
        this.ultimo = ultimo;
    }

    public int getTamaño() {
        return tamaño;
    }

    public void setTamaño(int tamaño) {
        this.tamaño = tamaño;
    }
    
    

    /**
     * Verifica si la lista está vacía.
     *
     * @return true si la lista está vacía, false en caso contrario
     */
    public boolean isEmpty() {
        return primero == null;
    }

    /**
     * Obtiene el número de elementos en la lista.
     *
     * @return tamaño de la lista
     */
    public int size() {
        return tamaño;
    }

    /**
     * Inserta un elemento al inicio de la lista.
     *
     * @param valor Elemento a insertar
     */
    public void insertarAlInicio(T valor) {
        Nodo<T> nuevoNodo = new Nodo<>(valor);
        nuevoNodo.enlace = primero;
        primero = nuevoNodo;
        if (ultimo == null) {
            ultimo = primero;
        }
        tamaño++;
    }

    /**
     * Inserta un elemento al final de la lista.
     *
     * @param valor Elemento a insertar
     */
    public void insertarElementoAlFinal(T valor) {
        Nodo<T> nuevoNodo = new Nodo<>(valor);
        if (isEmpty()) {
            primero = nuevoNodo;
        } else {
            ultimo.enlace = nuevoNodo;
        }
        ultimo = nuevoNodo;
        tamaño++;
    }

    /**
     * Elimina la primera ocurrencia del elemento especificado.
     *
     * @param valor Elemento a eliminar
     */
    public void eliminarNodo(T valor) {
        if (isEmpty()) {
            return;
        }

        // Caso especial: eliminar el primer nodo
        if (primero.dato.compareTo(valor) == 0) {
            primero = primero.enlace;
            if (primero == null) {
                ultimo = null;
            }
            tamaño--;
            return;
        }

        // Buscar el nodo a eliminar
        Nodo<T> actual = primero;
        Nodo<T> anterior = null;

        while (actual != null && actual.dato.compareTo(valor) != 0) {
            anterior = actual;
            actual = actual.enlace;
        }

        // Si se encontró el nodo, eliminarlo
        if (actual != null) {
            anterior.enlace = actual.enlace;
            if (actual == ultimo) {
                ultimo = anterior;
            }
            tamaño--;
        }
    }

    /**
     * Busca un elemento en la lista usando comparación.
     *
     * @param valor Elemento a buscar
     * @return true si el elemento está en la lista, false en caso contrario
     */
    public boolean busqueda(T valor) {
        Nodo<T> actual = primero;

        while (actual != null) {
            if (actual.dato.compareTo(valor) == 0) {
                return true;
            }
            actual = actual.enlace;
        }
        return false;
    }

     @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Nodo<T> actual = primero;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                T dato = actual.dato;
                actual = actual.enlace;
                return dato;
            }
        };
    }

    /**
     * Clase interna que representa un nodo de la lista enlazada.
     *
     * @param <T> Tipo de dato almacenado en el nodo
     */
    public static class Nodo<T> {

        T dato;
        Nodo<T> enlace;

        /**
         * Constructor del nodo.
         *
         * @param dato Valor a almacenar en el nodo
         */
        public Nodo(T dato) {
            this.dato = dato;
            this.enlace = null;
        }

        public T getDato() {
            return dato;
        }

        public void setDato(T dato) {
            this.dato = dato;
        }

        public Nodo<T> getEnlace() {
            return enlace;
        }

        public void setEnlace(Nodo<T> enlace) {
            this.enlace = enlace;
        }
        
    }
}
