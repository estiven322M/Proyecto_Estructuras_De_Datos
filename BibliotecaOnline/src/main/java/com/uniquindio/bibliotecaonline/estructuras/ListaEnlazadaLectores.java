package com.uniquindio.bibliotecaonline.estructuras;

import java.util.Iterator;

public class ListaEnlazadaLectores<Usuario> implements Iterable<Usuario> {

    private Nodo<Usuario> primero;
    private Nodo<Usuario> ultimo;
    private int tamaño;

    public ListaEnlazadaLectores() {
        this.primero = null;
        this.ultimo = null;
        this.tamaño = 0;
    }

    public boolean isEmpty() { // verifica si la lista esta vacia
        return primero == null;
    }

    public int size() {
        Nodo temporal =primero;
        int contador =0;
        while(temporal  !=null){
            contador+=1;
            temporal = temporal.enlace;
        }
        return contador;
    }
    
    public void insertarAlInicio(Usuario usuario){
        Nodo <Usuario> nuevoNodo = new Nodo(usuario);
        nuevoNodo.enlace=primero;
        primero =nuevoNodo;
        if(ultimo == null)
            ultimo =primero;
        tamaño++;
    }

    public void insertarElementoAlFinal(Usuario usuario) {
        Nodo<Usuario> nuevoNodo = new Nodo(usuario);
        if (isEmpty()) {
            primero = nuevoNodo;
            
        } else {
            Nodo<Usuario> temporal = primero;
            while (temporal.enlace != null) {
                temporal = temporal.enlace;
            }
            temporal.enlace = nuevoNodo;
        }

    }
    
    public void eliminarNodo (Usuario usuario){
        if(isEmpty()){
            return;
        }
        Nodo <Usuario> temporal = primero, previo = null;
        if(temporal!= null && temporal.dato.equals(usuario)){
            primero = temporal.enlace;
            return;
        }
        while(temporal != null && temporal.dato !=usuario){
            previo = temporal;
            temporal = temporal.enlace;
        }
        if(temporal == null)return;
            previo.enlace=temporal.enlace;
        
        
    }
    
    public boolean busqueda(Usuario usuario){
        Nodo <Usuario> actual = primero;
        while(actual != null){
            if(actual.dato== usuario){
                return true;
            }
            actual = actual.enlace;
        }
        return false;
    }

    @Override
    public Iterator<Usuario> iterator() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
