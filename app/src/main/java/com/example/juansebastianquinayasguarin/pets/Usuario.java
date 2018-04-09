package com.example.juansebastianquinayasguarin.pets;

/**
 * Created by juansebastianquinayasguarin on 4/4/18.
 */

public class Usuario {
    private String nombre, telefono, email, contraseña, imagenperfil;

    public Usuario() {
    }

    public Usuario(String nombre, String telefono, String email, String contraseña, String imagenperfil) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.contraseña = contraseña;
        this.imagenperfil = imagenperfil;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getImagenperfil() {
        return imagenperfil;
    }

    public void setImagenperfil(String imagenperfil) {
        this.imagenperfil = imagenperfil;
    }
}
