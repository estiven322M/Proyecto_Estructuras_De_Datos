
package com.uniquindio.bibliotecaonline.modelo;

import java.time.LocalDate;


public class Prestamo implements Comparable<Prestamo> {
    
    private Libro libro;
    private boolean devuelto;
    private LocalDate fechaDevolucionEfectiva;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;

    public Prestamo(Libro libro, LocalDate fechaPrestamo, LocalDate fechaDevolucion) {
        this.libro = libro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }
    
    
    

    @Override
    public int compareTo(Prestamo otro) {
        return this.fechaPrestamo.compareTo(otro.fechaPrestamo);
    }

    public void setFechaDevolucionEfectiva(LocalDate fechaDevolucionEfectiva) {
        this.fechaDevolucionEfectiva = fechaDevolucionEfectiva;
    }
    public LocalDate getFechaDevolucionEfectiva() {
        return this.fechaDevolucionEfectiva;
    }

    public void setDevuelto(boolean devuelto) {
        this.devuelto=devuelto;
         
    }

    public boolean isDevuelto() {
       return this.devuelto; 
    }

    Object getFechaDevolucionEsperada() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
