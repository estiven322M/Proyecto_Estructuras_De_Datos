
package com.uniquindio.bibliotecaonline.modelo;


public class Valoracion implements Comparable <Valoracion>{
    private Libro libro;
    private int calificacion;

    @Override
    public int compareTo(Valoracion otro) {
         return Integer.compare(this.calificacion, otro.calificacion);
    }
    
}
