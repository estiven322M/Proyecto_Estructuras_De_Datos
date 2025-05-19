package com.uniquindio.bibliotecaonline.estructuras;

import com.uniquindio.bibliotecaonline.modelo.Libro;

/**
 * Clase que implementa un Árbol Binario de Búsqueda (ABB/BST) genérico.
 *
 * @param <T> Tipo de datos que debe implementar Comparable para permitir
 * ordenamiento
 */
public class ArbolBinarioBusqueda<T extends Comparable<T>> {

    private NodoArbol<T> raiz;

    public void insertar(Libro libro) {
        if (libro == null) {
        throw new IllegalArgumentException("El libro no puede ser null");
    }
    insercion((T) libro);
    }

    /**
     * Clase interna que representa un nodo del árbol.
     *
     * @param <T> Tipo de dato almacenado en el nodo
     */
    private static class NodoArbol<T> {

        T valor;
        NodoArbol<T> izquierdo;
        NodoArbol<T> derecho;

        /**
         * Constructor del nodo.
         *
         * @param valor Valor a almacenar en el nodo
         */
        public NodoArbol(T valor) {
            this.valor = valor;
            this.izquierdo = null;
            this.derecho = null;
        }
    }

    /**
     * Método público para insertar un nuevo valor en el árbol.
     *
     * @param valor Valor a insertar en el árbol
     * @throws IllegalArgumentException Si el valor es null
     */
    public void insercion(T valor) {
        if (valor == null) {
            throw new IllegalArgumentException("El valor no puede ser null");
        }
        raiz = insercionRecursivo(raiz, valor);
    }

    /**
     * Método privado recursivo que realiza la inserción en el árbol.
     *
     * @param nodo Nodo actual en la recursión
     * @param valor Valor a insertar
     * @return Nodo modificado (o nuevo nodo si era null)
     */
    private NodoArbol<T> insercionRecursivo(NodoArbol<T> nodo, T valor) {
        // Caso base: si el nodo es null, crea uno nuevo
        if (nodo == null) {
            return new NodoArbol<>(valor);
        }

        // Comparación usando compareTo() en lugar de operadores
        int comparacion = valor.compareTo(nodo.valor);

        if (comparacion < 0) {
            // El valor es menor, va al subárbol izquierdo
            nodo.izquierdo = insercionRecursivo(nodo.izquierdo, valor);
        } else if (comparacion > 0) {
            // El valor es mayor, va al subárbol derecho
            nodo.derecho = insercionRecursivo(nodo.derecho, valor);
        }
        // Si comparacion == 0, el valor ya existe y no se hace nada

        return nodo;
    }

    /**
     * Encuentra el nodo con el valor mínimo en un subárbol.
     *
     * @param <T> Tipo genérico que debe implementar Comparable
     * @param nodo Nodo raíz del subárbol donde se buscará el valor mínimo
     * @return Nodo que contiene el valor mínimo en el subárbol
     */
    private NodoArbol<T> valorMinimo(NodoArbol<T> nodo) {
        // Encuentra la hoja más a la izquierda
        while (nodo.izquierdo != null) {
            nodo = nodo.izquierdo;
        }
        return nodo;
    }

    /**
     * Método público para eliminar un valor del árbol.
     *
     * @param valor Valor a eliminar del árbol
     */
    public void eliminar(T valor) {
        raiz = eliminar(raiz, valor);
    }

    /**
     * Método privado recursivo que elimina un valor del árbol.
     *
     * @param nodo Nodo actual en la recursión
     * @param valor Valor a eliminar
     * @return Nodo modificado después de la eliminación
     * @throws IllegalArgumentException Si el valor es null
     */
    private NodoArbol<T> eliminar(NodoArbol<T> nodo, T valor) {
        // Si el árbol está vacío, devuelve el mismo nodo
        if (nodo == null) {
            return nodo;
        }

        // Comparación usando compareTo() para determinar la dirección de búsqueda
        int comparacion = valor.compareTo(nodo.valor);

        if (comparacion < 0) {
            // El valor es menor, buscar en el subárbol izquierdo
            nodo.izquierdo = eliminar(nodo.izquierdo, valor);
        } else if (comparacion > 0) {
            // El valor es mayor, buscar en el subárbol derecho
            nodo.derecho = eliminar(nodo.derecho, valor);
        } else {
            // Caso 1: Nodo con solo un hijo o sin hijos
            if (nodo.derecho == null) {
                return nodo.izquierdo;
            } else if (nodo.izquierdo == null) {
                return nodo.derecho;
            }

            // Caso 2: Nodo con dos hijos
            // Obtiene el sucesor en orden (valor mínimo en subárbol derecho)
            nodo.valor = valorMinimo(nodo.derecho).valor;

            // Elimina el sucesor en orden
            nodo.derecho = eliminar(nodo.derecho, nodo.valor);
        }
        return nodo;
    }

    /**
     * Busca un valor en el árbol.
     *
     * @param valor Valor a buscar.
     * @return true si el valor existe en el árbol, false en caso contrario.
     */
    public T buscar(T valor) {
        return buscarRecursivo(raiz, valor);
    }

    /**
     * Método auxiliar recursivo para buscar un valor.
     *
     * @param nodo Nodo actual en la recursión.
     * @param valor Valor a buscar.
     * @return true si el valor existe en el subárbol, false en caso contrario.
     */
    private T buscarRecursivo(NodoArbol<T> nodo, T valor) {
        if (nodo == null) {
            return null; // Caso base: valor no encontrado.
        }

        int comparacion = valor.compareTo(nodo.valor);

        if (comparacion == 0) {
            return nodo.valor; // Valor encontrado.
        } else if (comparacion < 0) {
            return buscarRecursivo(nodo.izquierdo, valor); // Buscar en izquierda.
        } else {
            return buscarRecursivo(nodo.derecho, valor); // Buscar en derecha.
        }
    }

}
