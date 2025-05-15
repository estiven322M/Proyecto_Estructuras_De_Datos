
package com.uniquindio.bibliotecaonline.estructuras;


public class Nodo <T> {
    T dato;
    Nodo <T> enlace;
    
    public Nodo (T dato){
        this.dato=dato;
        enlace = null;
    }
    
    public Nodo (T dato, Nodo <T> nodo){
        this.dato=dato;
        this.enlace=nodo;
    }
    
    public T getDato(){
        return dato;
    }
    
    public void setDato(T dato){
        this.dato=dato;
    }
    
    public Nodo <T> getEnlace(){
        return enlace;
    }
    public void setEnlace( Nodo <T> enlace){
        this.enlace=enlace;
    }
    
    
    
}
