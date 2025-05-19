
package com.uniquindio.bibliotecaonline.logica;

import com.uniquindio.bibliotecaonline.modelo.Libro;
import com.uniquindio.bibliotecaonline.estructuras.ListaEnlazada;
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
    
    // Otros métodos como buscarLibro, eliminarLibro, etc.
}