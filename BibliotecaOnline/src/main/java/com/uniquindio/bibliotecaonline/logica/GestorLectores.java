package com.uniquindio.bibliotecaonline.logica;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import com.uniquindio.bibliotecaonline.modelo.Lector;
import com.uniquindio.bibliotecaonline.estructuras.ListaEnlazada;


public class GestorLectores {

    //private ListaEnlazada<Lector> listaLectores;
    private ListaEnlazada<Lector> lectores;

    public GestorLectores() {
        this.lectores = new ListaEnlazada<>();
        cargarLectoresDesdeArchivo();


    }

    public boolean validarLoginDesdeArchivo(String nombreUsuario, String contrasena) {
        // Usa getResourceAsStream para acceder al archivo en la carpeta resources
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("lectores.txt");

        if (inputStream == null) {
            System.err.println("No se encontró el archivo lectores.txt en resources");
            return false;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 5) {
                    String idUsuario = partes[0].trim();
                    //String nombre = partes[1].trim();
                    String contrasenaArchivo = partes[3].trim();

                    if ((idUsuario.equals(nombreUsuario)) && contrasenaArchivo.equals(contrasena)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }

        return false; // no se encontró coincidencia
    }

    public void cargarLectoresDesdeArchivo() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("lectores.txt");

        if (inputStream == null) {
            System.err.println("No se encontró el archivo lectores.txt en resources");
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 5) {
                    String idUsuario = partes[0].trim();
                    String nombre = partes[1].trim();
                    String email = partes[2].trim();
                    String contrasena = partes[3].trim();
                    LocalDate fechaNacimiento = LocalDate.parse(partes[4].trim());
                    lectores.insertarElementoAlFinal(new Lector(idUsuario, nombre, email, contrasena, fechaNacimiento));

                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }catch(Exception e){
            System.err.println("Error al procesar los datos: " + e.getMessage());
        }
    }

    public void registrarLector(Lector lector) {
        if (lector != null && buscarLector(lector.getIdUsuario(), false) == null) {
            lectores.insertarElementoAlFinal(lector);
        }
    }

//    public Lector buscarLectorPorNombre(String nombre) {
//        var actual = lectores.getPrimero();
//        while (actual != null) {
//            if (actual.getDato().getNombre().equalsIgnoreCase(nombre)) {
//                return actual.getDato();
//            }
//            actual = actual.getEnlace();
//
//        }
//        return null;
//    }
//    public Lector buscarLectorPorId(String idUsuario) {
//        var actual = lectores.getPrimero();
//        while (actual != null) {
//            if (actual.getDato().getIdUsuario().equalsIgnoreCase(idUsuario)) {
//                return actual.getDato();
//            }
//            actual = actual.getEnlace();
//        }
//        return null;
//    }
    public Lector buscarLector(String valor, boolean porNombre) {
        var actual = lectores.getPrimero();
        while (actual != null) {
            Lector lector = actual.getDato();
            if (porNombre && lector.getNombre().equalsIgnoreCase(valor)) {
                return lector;
            } else if (!porNombre && lector.getIdUsuario().equalsIgnoreCase(valor)) {
                return lector;
            }
            actual = actual.getEnlace();
        }
        return null;
    }

    public ListaEnlazada<Lector> getLectores() {
        return lectores;
    }
}
