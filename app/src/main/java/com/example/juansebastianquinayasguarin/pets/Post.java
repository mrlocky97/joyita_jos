package com.example.juansebastianquinayasguarin.pets;

/**
 * Created by juansebastianquinayasguarin on 5/4/18.
 */

public class Post {
    String titulo, descripcion, imagenpost;
    public Post() {
    }

    public Post(String titulo, String descripcion, String imagenpost) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagenpost = imagenpost;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagenpost() {
        return imagenpost;
    }

    public void setImagenpost(String imagenpost) {
        this.imagenpost = imagenpost;
    }
}

