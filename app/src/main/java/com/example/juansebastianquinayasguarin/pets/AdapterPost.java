package com.example.juansebastianquinayasguarin.pets;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by juansebastianquinayasguarin on 5/4/18.
 */

public class AdapterPost implements ListAdapter {

    private Context context;
    private ArrayList<Post> listaPost = new ArrayList<>();
    private ArrayList<Bitmap> listaImagen = new ArrayList<>();
    private TextView tv_titulo, tv_desc;
    private ImageView imagen, imginfo;
    Bitmap bitmap = null;
    String mPath;
    Activity a;

    public AdapterPost() {
    }

    //hola vhghg
    public AdapterPost(Context context, ArrayList<Post> listaPost, ArrayList<Bitmap> listaImagen) {
        this.context = context;
        this.listaPost = listaPost;
        this.listaImagen = listaImagen;
    }

    public TextView getTv_titulo() {
        return tv_titulo;
    }

    public void setTv_titulo(TextView tv_titulo) {
        this.tv_titulo = tv_titulo;
    }

    public TextView getTv_desc() {
        return tv_desc;
    }

    public void setTv_desc(TextView tv_desc) {
        this.tv_desc = tv_desc;
    }

    public ImageView getImagen() {
        return imagen;
    }

    public void setImagen(ImageView imagen) {
        this.imagen = imagen;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }



    @Override
    public int getCount() {
        return listaPost.size();
    }

    @Override
    public Object getItem(int position) {
        return listaPost.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View modeloVistaPost = inflater.inflate(R.layout.list_view_post, parent, false);
        imagen = modeloVistaPost.findViewById(R.id.img_list_view_post);
        tv_titulo = (TextView) modeloVistaPost.findViewById(R.id.tv_list_view_titulo);
        tv_desc = (TextView) modeloVistaPost.findViewById(R.id.tv_list_view_desc);
        imagen = (ImageView) modeloVistaPost.findViewById(R.id.img_list_view_post);
        imginfo = (ImageView) modeloVistaPost.findViewById(R.id.img_list_view_post_info);
        tv_titulo.setText(listaPost.get(position).getTitulo());
        tv_desc.setText(listaPost.get(position).getDescripcion());
        Log.v("1", listaPost.get(position).getImagenpost());
       // Log.v("2", listaImagen.get(position).toString());
        imagen.setImageBitmap(listaImagen.get(position));

           /** try {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReferenceFromUrl("gs://adoptpet-f1b0d.appspot.com").child("post")
                        .child(listaPost.get(position).getIdPost()).child("imagenpost");
                final File localFile;

                localFile = File.createTempFile("images", "jpg");
                storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap miBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        imagen.setImageBitmap(miBitmap);

                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            */

            //imagen.setImageURI(Uri.parse(listaPost.get(position).getImagenpost()));
            //TOCAR PARA COGER IMG DEL DATABASE
            return modeloVistaPost;

    }


    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return listaPost.isEmpty();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }




}
