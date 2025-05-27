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
        cargarLibrosEnEstructuras(); // Carga libros en las estructuras de datos

    }

    /**
     * Carga los libros obtenidos de GestorLibros en las estructuras de búsqueda
     * de GestorPrestamos (árboles).
     */
    private void cargarLibrosEnEstructuras() {
        if (this.gestorLibros != null) {
            ListaEnlazada<Libro> librosDelArchivo = this.gestorLibros.getListaLibros();
            if (librosDelArchivo != null && !librosDelArchivo.isEmpty()) {
                for (Libro libro : librosDelArchivo) {
                    if (libro != null) {
                        this.agregarLibro(libro); // Este método ya los agrega a los tres árboles
                    }
                }
            } else {
                System.err.println("GestorPrestamos: No se encontraron libros en GestorLibros para cargar en los árboles.");
            }
        } else {
            System.err.println("GestorPrestamos: gestorLibros es null. No se pueden cargar libros en los árboles.");
        }
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
        if (libro.getTitulo() == null || libro.getTitulo().trim().isEmpty()) {
            System.err.println("GestorPrestamos: Intentando agregar libro con título nulo o vacío. Libro: " + libro);
            return; // No agregar libros sin título
        }

        // Agregar al árbol por título
        // La clase ArbolBinarioBusqueda tiene un método insertar(Libro libro)
        // que internamente llama a insercion((T) libro). Esto es funcional.
        arbolPorTitulo.insertar(libro);

        // Agregar al árbol por autor
        if (libro.getAutor() != null && !libro.getAutor().trim().isEmpty()) {
            LibroPorAutor libroPorAutor = new LibroPorAutor(libro.getAutor());
            LibroPorAutor existenteAutor = arbolPorAutor.buscar(libroPorAutor);

            if (existenteAutor == null) {
                libroPorAutor.agregarLibro(libro);
                arbolPorAutor.insercion(libroPorAutor); // Usar insercion genérica
            } else {
                existenteAutor.agregarLibro(libro);
            }
        }

        // Agregar al árbol por categoría
        if (libro.getCategoria() != null && !libro.getCategoria().trim().isEmpty()) {
            LibroPorCategoria libroPorCategoria = new LibroPorCategoria(libro.getCategoria());
            LibroPorCategoria existenteCategoria = arbolPorCategoria.buscar(libroPorCategoria);

            if (existenteCategoria == null) {
                libroPorCategoria.agregarLibro(libro);
                arbolPorCategoria.insercion(libroPorCategoria); // Usar insercion genérica
            } else {
                existenteCategoria.agregarLibro(libro);
            }
        }

    }

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

    /**
     * Obtiene todos los libros directamente desde GestorLibros. Estos son los
     * libros tal como se cargaron del archivo.
     *
     * @return Lista enlazada de todos los libros.
     */
    public ListaEnlazada<Libro> obtenerTodosLosLibros() {
        if (gestorLibros != null) {
            return gestorLibros.getListaLibros();
        }
        System.err.println("GestorPrestamos: gestorLibros es null al intentar obtener todos los libros.");
        return new ListaEnlazada<>(); // Devolver lista vacía para evitar NullPointerExceptions
    }

    // El método buscarPorTitulo ya existente es correcto para usar el árbol.
    public Libro buscarPorTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return null;
        }
        // Necesitas un constructor en Libro que solo tome el título para la búsqueda,
        // o adaptar el árbol para buscar con solo el String del título.
        // Suponiendo que Libro tiene un constructor Libro(String titulo).
        Libro libroBusqueda = new Libro(titulo);
        return arbolPorTitulo.buscar(libroBusqueda);
    }

    public ListaEnlazada<String> obtenerTodosLosAutores() {
        if (gestorLibros == null) {
            System.err.println("GestorPrestamos: gestorLibros es null al intentar obtener todos los autores.");
            return new ListaEnlazada<>();
        }
        return gestorLibros.obtenerTodosLosAutores(); // Delegación correcta
    }

    /**
     * Busca libros por autor
     */
    public ListaEnlazada<Libro> buscarPorAutor(String autor) {
        if (autor == null) {
            return new ListaEnlazada<>();
        }
        LibroPorAutor busqueda = new LibroPorAutor(autor);
        LibroPorAutor resultado = arbolPorAutor.buscar(busqueda);
        return resultado != null ? resultado.getLibrosDelAutor() : new ListaEnlazada<>();
    }

    /**
     * Busca libros por categoría
     */
    public ListaEnlazada<Libro> buscarPorCategoria(String categoria) {
        if (categoria == null) {
            return new ListaEnlazada<>();
        }
        LibroPorCategoria busqueda = new LibroPorCategoria(categoria);
        LibroPorCategoria resultado = arbolPorCategoria.buscar(busqueda);
        return resultado != null ? resultado.getLibrosDeLaCategoria() : new ListaEnlazada<>();
    }

//    private void cargarDatosIniciales() {
//        // Datos de ejemplo
//        agregarLibro(new Libro("Cien años de soledad", "Gabriel García Márquez", "Ficción", 1967, "Disponible", 4.8));
//        agregarLibro(new Libro("El principito", "Antoine de Saint-Exupéry", "Fantasía", 1943, "Disponible", 4.9));
//        // Libros clásicos y populares
//        agregarLibro(new Libro("Cien años de soledad", "Gabriel García Márquez", "Realismo mágico", 1967, "Disponible", 4.8));
//        agregarLibro(new Libro("1984", "George Orwell", "Ciencia ficción", 1949, "Disponible", 4.7));
//        agregarLibro(new Libro("Orgullo y prejuicio", "Jane Austen", "Romance", 1813, "Disponible", 4.6));
//        agregarLibro(new Libro("Matar a un ruiseñor", "Harper Lee", "Drama", 1960, "Disponible", 4.8));
//        agregarLibro(new Libro("El gran Gatsby", "F. Scott Fitzgerald", "Ficción literaria", 1925, "Disponible", 4.5));
//        agregarLibro(new Libro("Don Quijote de la Mancha", "Miguel de Cervantes", "Clásico", 1605, "Disponible", 4.7));
//        agregarLibro(new Libro("Crónica de una muerte anunciada", "Gabriel García Márquez", "Novela", 1981, "Disponible", 4.4));
//        agregarLibro(new Libro("El señor de los anillos", "J.R.R. Tolkien", "Fantasía épica", 1954, "Disponible", 4.9));
//        agregarLibro(new Libro("Harry Potter y la piedra filosofal", "J.K. Rowling", "Fantasía", 1997, "Disponible", 4.8));
//        agregarLibro(new Libro("Los juegos del hambre", "Suzanne Collins", "Ciencia ficción", 2008, "Disponible", 4.5));
//// Agregar más libros...
//    }
    /**
     * Obtiene sugerencias básicas de libros
     */
    public ListaEnlazada<Libro> obtenerSugerencias() {
        ListaEnlazada<Libro> sugerencias = new ListaEnlazada<>();
        Libro sugerido1 = buscarPorTitulo("El principito");
        Libro sugerido2 = buscarPorTitulo("1984");
        Libro sugerido3 = buscarPorTitulo("Cien años de soledad");

        if (sugerido1 != null) {
            sugerencias.insertarElementoAlFinal(sugerido1);
        }
        if (sugerido2 != null) {
            sugerencias.insertarElementoAlFinal(sugerido2);
        }
        if (sugerido3 != null) {
            sugerencias.insertarElementoAlFinal(sugerido3);
        }

        return sugerencias;
    }

    /**
     * Devuelve una lista de todos los autores (delegando a gestorLibros)
     */
//    public ListaEnlazada<String> obtenerTodosLosAutores() {
//        if (gestorLibros == null) {
//            return new ListaEnlazada<>();
//        }
//        return gestorLibros.obtenerTodosLosAutores();
//    }
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

        Libro libroEnSistema = arbolPorTitulo.buscar(libro); // Busca la instancia manejada por el sistema
        if (libroEnSistema == null) {
            throw new IllegalStateException("El libro no existe en el catálogo.");
        }
        if (!libroEnSistema.getEstado().equals(Libro.DISPONIBLE)) {
            throw new IllegalStateException("El libro '" + libroEnSistema.getTitulo() + "' no está disponible para préstamo. Estado actual: " + libroEnSistema.getEstado());
        }

        // 1. Crear el nuevo préstamo
        Prestamo nuevoPrestamo = new Prestamo(
                libroEnSistema,
                LocalDate.now(), // Fecha actual como fecha de préstamo
                LocalDate.now().plusWeeks(2) // Fecha de devolución (2 semanas después)
        );

        // 2. Agregar al historial del lector
        lector.agregarPrestamo(nuevoPrestamo);

        // 3. Actualizar estado del libro
        libroEnSistema.setEstado(Libro.PRESTADO); // Cambia el estado del libro en el sistema

    }

    /**
     * Procesa la devolución de un libro por parte de un lector. Actualiza el
     * estado del Préstamo y del Libro.
     *
     * @param lector El lector que devuelve el libro.
     * @param prestamo El préstamo específico que se está devolviendo.
     * @throws IllegalArgumentException Si el lector o el préstamo son null.
     * @throws IllegalStateException Si el préstamo no pertenece al lector o ya
     * fue devuelto.
     */
    public void procesarDevolucion(Lector lector, Prestamo prestamo) {
        if (lector == null) {
            throw new IllegalArgumentException("El lector no puede ser null.");
        }
        if (prestamo == null) {
            throw new IllegalArgumentException("El préstamo no puede ser null.");
        }

        // Verificar que el préstamo pertenece al historial del lector (opcional pero buena práctica)
        boolean prestamoEncontradoEnHistorial = false;
        for (Prestamo p : lector.getHistorialPrestamos()) {
            if (p.equals(prestamo)) { // Necesitarás un método equals en Prestamo si comparas objetos
                // o compara por referencia si es la misma instancia
                prestamoEncontradoEnHistorial = true;
                break;
            }
        }
        // Si comparas por referencia y el objeto Prestamo viene de lector.getPrestamosActivos(),
        // esta verificación podría no ser estrictamente necesaria pero no hace daño.
        // Por simplicidad, vamos a asumir que el 'prestamo' es válido y pertenece al lector.

        if (prestamo.isDevuelto()) {
            throw new IllegalStateException("Este libro ya fue devuelto previamente.");
        }

        Libro libroADevolver = prestamo.getLibro();
        if (libroADevolver == null) {
            throw new IllegalStateException("El préstamo no tiene un libro asociado.");
        }

        // 1. Actualizar el objeto Prestamo
        prestamo.setDevuelto(true);
        prestamo.setFechaDevolucionEfectiva(LocalDate.now());

        // 2. Actualizar el estado del Libro en el sistema
        // Buscamos la instancia del libro en nuestro sistema para asegurar que modificamos la correcta
        Libro libroEnSistema = arbolPorTitulo.buscar(new Libro(libroADevolver.getTitulo()));
        if (libroEnSistema != null) {
            libroEnSistema.setEstado(Libro.DISPONIBLE);
            // Si el estado afecta cómo se almacena o busca en los árboles (aparte del de título),
            // podrías necesitar actualizar esos árboles también. Generalmente, modificar el estado
            // del objeto que ya está en el árbol es suficiente si la comparación del árbol no depende del estado.
        } else {
            // Esto no debería ocurrir si el libro prestado estaba en el sistema.
            System.err.println("ADVERTENCIA: El libro '" + libroADevolver.getTitulo() + "' del préstamo devuelto no se encontró en el arbolPorTitulo para actualizar su estado.");
            // Aun así, marcamos el libro original del préstamo como disponible
            libroADevolver.setEstado(Libro.DISPONIBLE);
        }

        // (Opcional) Lógica adicional:
        // - Calcular multas si la devolución es tardía.
        // - Registrar evento de devolución en un log.
    }

}
