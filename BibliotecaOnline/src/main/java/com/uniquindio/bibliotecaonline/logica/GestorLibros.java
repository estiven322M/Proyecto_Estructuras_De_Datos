package com.uniquindio.bibliotecaonline.logica;

import com.uniquindio.bibliotecaonline.modelo.Libro;
import com.uniquindio.bibliotecaonline.estructuras.ListaEnlazada;
import com.uniquindio.bibliotecaonline.modelo.Lector;
import com.uniquindio.bibliotecaonline.modelo.Valoracion;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

public class GestorLibros {

    private ListaEnlazada<Libro> listaLibros;

    public GestorLibros() {
        this.listaLibros = new ListaEnlazada<>();
        cargarLibrosDesdeArchivo();
    }

    private void cargarLibrosDesdeArchivo() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("libros.txt");

        if (inputStream == null) {
            System.err.println("No se encontró el archivo libros.txt en resources");
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 6) {
                    String titulo = partes[0].trim();
                    String autor = partes[1].trim();
                    String genero = partes[2].trim();
                    int año = Integer.parseInt(partes[3].trim());
                    String estado = partes[4].trim();
                    double calificacion = Double.parseDouble(partes[5].trim());

                    Libro libro = new Libro(titulo, autor, genero, año, estado, calificacion);
                    agregarLibro(libro);
                } else {
                    System.err.println("Formato incorrecto en línea: " + linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error en formato numérico: " + e.getMessage());
        }
    }

    public ListaEnlazada<String> obtenerTodosLosAutores() {
        ListaEnlazada<String> autores = new ListaEnlazada<>();
        for (Libro libro : listaLibros) {  // Usa listaLibros que tienes aquí
            String autor = libro.getAutor();
            if (!autores.busqueda(autor)) {
                autores.insertarElementoAlFinal(autor);
            }
        }
        return autores;
    }

    public void agregarLibro(Libro libro) {
        if (libro != null) {
            listaLibros.insertarElementoAlFinal(libro);
        }
    }

    // Resto de métodos de tu GestorLibros...
    public ListaEnlazada<Libro> getListaLibros() {
        return listaLibros;
    }

    /**
     * Registra una nueva valoración para un libro, la asocia al lector y al
     * libro, y actualiza la calificación promedio del libro.
     *
     * @param lector El lector que realiza la valoración.
     * @param libroAValorar El libro que está siendo valorado (la instancia que
     * se muestra en el diálogo).
     * @param estrellas La calificación en estrellas (e.g., 1-5).
     * @param comentario El comentario opcional.
     * @throws IllegalArgumentException si los datos no son válidos o el libro
     * no se encuentra.
     * @throws IllegalStateException si el lector ya ha valorado ese libro
     * (manejado por Lector.agregarValoracionRealizada).
     */
    public void registrarValoracion(Lector lector, Libro libroAValorar, int estrellas, String comentario) {
        if (lector == null || libroAValorar == null) {
            throw new IllegalArgumentException("Lector y Libro no pueden ser nulos para registrar una valoración.");
        }

        // 1. Obtener la instancia 'real' del libro desde nuestra lista gestionada en GestorLibros
        //    Esto es crucial para que las actualizaciones de calificación se reflejen en el objeto correcto.
        Libro libroEnSistema = null;
        for (Libro l : this.listaLibros) { // 'this.listaLibros' es la lista maestra de GestorLibros
            if (l.equals(libroAValorar)) { // Asume que Libro.equals() compara por título/ID
                libroEnSistema = l;
                break;
            }
        }

        if (libroEnSistema == null) {
            // Si usas árboles en GestorPrestamos para mostrar libros, podrías necesitar
            // buscar el libro en GestorPrestamos y luego asegurar que actualizas
            // la instancia correcta que también está en GestorLibros.
            // Por ahora, asumimos que GestorLibros tiene la lista autoritativa.
            throw new IllegalArgumentException("El libro '" + libroAValorar.getTitulo() + "' no se encontró en el catálogo principal de GestorLibros.");
        }

        // 2. Crear el objeto Valoracion usando el libroEnSistema
        Valoracion nuevaValoracion = new Valoracion(libroEnSistema, lector, estrellas, comentario);

        // 3. Agregar la valoración al historial del lector
        //    El método Lector.agregarValoracionRealizada ya maneja la lógica de no duplicar.
        boolean agregadaALector = lector.agregarValoracionRealizada(nuevaValoracion);

        if (!agregadaALector) {
            // Lector.agregarValoracionRealizada devolvió false, lo que significa que ya se valoró.
            // El JOptionPane en el diálogo manejará esto si se lanza la excepción.
            throw new IllegalStateException("Ya has valorado el libro '" + libroEnSistema.getTitulo() + "'.");
        }

        // 4. Agregar la valoración a la lista de valoraciones recibidas del libro
        //    y el método agregarValoracionRecibida en Libro se encarga de recalcular su promedio.
        libroEnSistema.agregarValoracionRecibida(nuevaValoracion);

        // Opcional: Imprimir para depuración
        System.out.println("Valoración registrada para: " + libroEnSistema.getTitulo()
                + " por " + lector.getNombre()
                + ". Nueva calificación promedio: " + String.format("%.1f", libroEnSistema.getCalificacion()));
    }

}
