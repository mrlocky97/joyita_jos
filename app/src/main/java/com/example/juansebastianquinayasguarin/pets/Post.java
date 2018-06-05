package com.example.juansebastianquinayasguarin.pets;

import android.graphics.Bitmap;

/**
 * Created by juansebastianquinayasguarin on 5/4/18.
 */

public class Post {
    String titulo, descripcion, idUsuario;
    String imagenpost, idPost;

    public Post() {
    }

    public Post(String titulo, String descripcion, String imagenpost, String idUsuario, String idPost) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagenpost = imagenpost;
        this.idUsuario = idUsuario;
        this.idPost = idPost;
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

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

}

