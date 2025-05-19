package com.uniquindio.bibliotecaonline.logica;

import com.uniquindio.bibliotecaonline.estructuras.ArbolBinarioBusqueda;
import com.uniquindio.bibliotecaonline.estructuras.ListaEnlazada;
import com.uniquindio.bibliotecaonline.modelo.Lector;
import com.uniquindio.bibliotecaonline.modelo.Libro;
import com.uniquindio.bibliotecaonline.modelo.LibroPorAutor;
import com.uniquindio.bibliotecaonline.modelo.LibroPorCategoria;
import com.uniquindio.bibliotecaonline.modelo.Prestamo;
import java.time.LocalDate;

public class GestorPrestamos {

    private GestorLibros gestorLibros;  // referencia para acceder a libros
    private GestorLectores gestorLectores;
    private ArbolBinarioBusqueda<Libro> arbolPorTitulo;
    private ArbolBinarioBusqueda<LibroPorAutor> arbolPorAutor;
    private ArbolBinarioBusqueda<LibroPorCategoria> arbolPorCategoria;

    public GestorPrestamos(GestorLibros gestorLibros, GestorLectores gestorLectores) {
        this.gestorLectores = gestorLectores;
        this.gestorLibros = gestorLibros;
        this.arbolPorTitulo = new ArbolBinarioBusqueda<>();
        this.arbolPorAutor = new ArbolBinarioBusqueda<>();
        this.arbolPorCategoria = new ArbolBinarioBusqueda<>();
        cargarDatosIniciales(); // Opcional: para pruebas
    }

    /**
     * Agrega un libro a todos los índices de búsqueda
     *
     * @param libro Libro a agregar
     * @throws IllegalArgumentException si el libro es null
     */
    public void agregarLibro(Libro libro) {
        if (libro == null) {
            throw new IllegalArgumentException("El libro no puede ser null");
        }

        // Agregar al árbol por título
        arbolPorTitulo.insertar(libro);

        // Agregar al árbol por autor
        LibroPorAutor libroPorAutor = new LibroPorAutor(libro.getAutor());
        LibroPorAutor existenteAutor = arbolPorAutor.buscar(libroPorAutor);

        if (existenteAutor == null) {
            libroPorAutor.agregarLibro(libro);
            arbolPorAutor.insercion(libroPorAutor);
        } else {
            existenteAutor.agregarLibro(libro);
        }

        // Agregar al árbol por categoría
        LibroPorCategoria libroPorCategoria = new LibroPorCategoria(libro.getCategoria());
        LibroPorCategoria existenteCategoria = arbolPorCategoria.buscar(libroPorCategoria);

        if (existenteCategoria == null) {
            libroPorCategoria.agregarLibro(libro);
            arbolPorCategoria.insercion(libroPorCategoria);
        } else {
            existenteCategoria.agregarLibro(libro);
        }
    }

    /**
     * Solicita un préstamo para un lector y un libro
     *
     * @return true si se pudo prestar, false si no está disponible
     */
//    public boolean solicitarPrestamo(Lector lector, Libro libro) {
//
//        if (lector == null || libro == null) {
//            throw new IllegalArgumentException("Lector y libro no pueden ser null");
//        }
//
//        if (libro.getEstado().equals(Libro.DISPONIBLE)) {
//            libro.setEstado(Libro.PRESTADO);
//            Prestamo nuevoPrestamo = new Prestamo(libro, LocalDate.now(), LocalDate.now().plusWeeks(2));
//            lector.agregarPrestamo(nuevoPrestamo);
//            return true;
//        }
//        return false;
//    }

    /**
     * Devuelve un libro, actualizando su estado
     */
    public boolean devolverLibro(Lector lector, Libro libro) {
        if (lector == null || libro == null) {
            throw new IllegalArgumentException("Lector y libro no pueden ser null");
        }
        if (libro.getEstado().equals(Libro.PRESTADO)) {
            libro.setEstado(Libro.DISPONIBLE);
            // Opcional: actualizar historial o estado del préstamo (buscar y marcar como devuelto)
            return true;
        }
        return false;
    }

    // Resto de métodos...
    /**
     * Busca un libro por título en el árbol
     */
    public Libro buscarPorTitulo(String titulo) {
        if (titulo == null) {
            return null;
        }
        Libro libroBusqueda = new Libro(titulo);
        return arbolPorTitulo.buscar(libroBusqueda);
    }
    
    /**
     * Busca libros por autor
     */

    public ListaEnlazada<Libro> buscarPorAutor(String autor) {
        if (autor == null) return new ListaEnlazada<>();
        LibroPorAutor busqueda = new LibroPorAutor(autor);
        LibroPorAutor resultado = arbolPorAutor.buscar(busqueda);
        return resultado != null ? resultado.getLibrosDelAutor() : new ListaEnlazada<>();
    }

    /**
     * Busca libros por categoría
     */
    public ListaEnlazada<Libro> buscarPorCategoria(String categoria) {
        if (categoria == null) return new ListaEnlazada<>();
        LibroPorCategoria busqueda = new LibroPorCategoria(categoria);
        LibroPorCategoria resultado = arbolPorCategoria.buscar(busqueda);
        return resultado != null ? resultado.getLibrosDeLaCategoria() : new ListaEnlazada<>();
    }

    private void cargarDatosIniciales() {
        // Datos de ejemplo
        agregarLibro(new Libro("Cien años de soledad", "Gabriel García Márquez", "Ficción", 1967, "Disponible", 4.8));
        agregarLibro(new Libro("El principito", "Antoine de Saint-Exupéry", "Fantasía", 1943, "Disponible", 4.9));
        // Libros clásicos y populares
        agregarLibro(new Libro("Cien años de soledad", "Gabriel García Márquez", "Realismo mágico", 1967, "Disponible", 4.8));
        agregarLibro(new Libro("1984", "George Orwell", "Ciencia ficción", 1949, "Disponible", 4.7));
        agregarLibro(new Libro("Orgullo y prejuicio", "Jane Austen", "Romance", 1813, "Disponible", 4.6));
        agregarLibro(new Libro("Matar a un ruiseñor", "Harper Lee", "Drama", 1960, "Disponible", 4.8));
        agregarLibro(new Libro("El gran Gatsby", "F. Scott Fitzgerald", "Ficción literaria", 1925, "Disponible", 4.5));
        agregarLibro(new Libro("Don Quijote de la Mancha", "Miguel de Cervantes", "Clásico", 1605, "Disponible", 4.7));
        agregarLibro(new Libro("Crónica de una muerte anunciada", "Gabriel García Márquez", "Novela", 1981, "Disponible", 4.4));
        agregarLibro(new Libro("El señor de los anillos", "J.R.R. Tolkien", "Fantasía épica", 1954, "Disponible", 4.9));
        agregarLibro(new Libro("Harry Potter y la piedra filosofal", "J.K. Rowling", "Fantasía", 1997, "Disponible", 4.8));
        agregarLibro(new Libro("Los juegos del hambre", "Suzanne Collins", "Ciencia ficción", 2008, "Disponible", 4.5));
// Agregar más libros...
    }

     /**
     * Obtiene sugerencias básicas de libros
     */
    public ListaEnlazada<Libro> obtenerSugerencias() {
        ListaEnlazada<Libro> sugerencias = new ListaEnlazada<>();
        Libro sugerido1 = buscarPorTitulo("El principito");
        Libro sugerido2 = buscarPorTitulo("1984");
        Libro sugerido3 = buscarPorTitulo("Cien años de soledad");

        if (sugerido1 != null) sugerencias.insertarElementoAlFinal(sugerido1);
        if (sugerido2 != null) sugerencias.insertarElementoAlFinal(sugerido2);
        if (sugerido3 != null) sugerencias.insertarElementoAlFinal(sugerido3);

        return sugerencias;
    }
    
    /**
     * Devuelve una lista de todos los autores (delegando a gestorLibros)
     */
    public ListaEnlazada<String> obtenerTodosLosAutores() {
        if (gestorLibros == null) return new ListaEnlazada<>();
        return gestorLibros.obtenerTodosLosAutores();
    }

    /**
     * Registra un nuevo préstamo para un lector
     *
     * @param lector Lector que realiza el préstamo
     * @param libro Libro que se presta
     * @throws IllegalArgumentException Si el lector o el libro son null
     * @throws IllegalStateException Si el libro no está disponible
     */
    public void registrarPrestamo(Lector lector, Libro libro) {
        // Validaciones
        if (lector == null) {
            throw new IllegalArgumentException("El lector no puede ser null");
        }
        if (libro == null) {
            throw new IllegalArgumentException("El libro no puede ser null");
        }
        if (!libro.getEstado().equals(Libro.DISPONIBLE)) {
            throw new IllegalStateException("El libro no está disponible para préstamo");
        }

        // 1. Crear el nuevo préstamo
        Prestamo nuevoPrestamo = new Prestamo(
                libro,
                LocalDate.now(), // Fecha actual como fecha de préstamo
                LocalDate.now().plusWeeks(2) // Fecha de devolución (2 semanas después)
        );

        // 2. Agregar al historial del lector
        lector.agregarPrestamo(nuevoPrestamo);

        // 3. Actualizar estado del libro
        libro.setEstado(Libro.PRESTADO);

        // (Opcional) Registrar en el sistema
        // Puedes agregar aquí lógica adicional como:
        // - Notificaciones
        // - Registro en base de datos
        // - Actualización de estadísticas
    }
   

}
