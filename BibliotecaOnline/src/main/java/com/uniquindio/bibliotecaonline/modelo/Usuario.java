
package com.uniquindio.bibliotecaonline.modelo;

import java.time.LocalDate;


public class Usuario {
    
    private String idUsuario;
    private String nombre;
    private String correo;
    private String contraseña;
    private LocalDate añoNacimiento;

    public Usuario(String idUsuario, String nombre, String correo, String contraseña, LocalDate añoNacimiento) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.contraseña = contraseña;
        this.añoNacimiento = añoNacimiento;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public LocalDate getAñoNacimiento() {
        return añoNacimiento;
    }

    public void setAñoNacimiento(LocalDate añoNacimiento) {
        this.añoNacimiento = añoNacimiento;
    }
    
    
    
    
}
