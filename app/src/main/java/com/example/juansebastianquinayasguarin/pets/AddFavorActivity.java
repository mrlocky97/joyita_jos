package com.example.juansebastianquinayasguarin.pets;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class AddFavorActivity extends AppCompatActivity {


    Button miButtonAddImg, miButtonCancelar, miButtonAceptar;
    TextView tituloDialog, descDialod;
    String user;
    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth;
    private String mPath;
    ImageView miImagen;
    private ProgressDialog mProgressDialog;
    private StorageReference miStorage;
    String idPOST = null;
    ArrayList<String> listIdPost = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_favor);

        miButtonCancelar = findViewById(R.id.btnCancelaraddFavor);
        miButtonAceptar = findViewById(R.id.btnAceptaraddFavor);
        tituloDialog = findViewById(R.id.Et_titulo_add_post);
        descDialod = findViewById(R.id.Et_desc_add_post);
        mProgressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        miStorage = FirebaseStorage.getInstance().getReference();
        user = firebaseAuth.getInstance().getCurrentUser().getUid();

        miButtonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddFavorActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        miButtonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFavor();
            }
        });
    }

    @SuppressLint("NewApi")
    private void showOptions() {
        final CharSequence[] options = {"Galeria", "Cancelar"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(AddFavorActivity.this);
        builder.setTitle("Elige una opcion");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which] == "Galeria") {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Selecciona la app de imagenes"), SELECT_PICTURE);
                } else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void addFavor() {

        String titulo = tituloDialog.getText().toString();
        String descripcion = descDialod.getText().toString();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("post");
        Post p1 = new Post(titulo, descripcion, "null", user, "null");
        mDatabase.push().setValue(p1);
        Intent intent = new Intent(AddFavorActivity.this, AddFavorImagenActivity.class);
        startActivity(intent);
    }

}
