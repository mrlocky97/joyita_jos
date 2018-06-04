package com.example.juansebastianquinayasguarin.pets;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddFavorImagenActivity extends AppCompatActivity {
    String idPOST = null;
    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;
    private StorageReference miStorage;
    private ProgressDialog mProgressDialog;
    private String mPath;
    private Button mibtnaddimagen, mibtnAceptaraddFavor;
    FirebaseAuth mAuth;
    ImageView miImagenPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_favor_imagen);

        miImagenPost = findViewById(R.id.imagen_animal_dialogo);
        mibtnaddimagen = findViewById(R.id.btnaddimagen);
        mibtnAceptaraddFavor = findViewById(R.id.btnAceptaraddFavor);

        mAuth = FirebaseAuth.getInstance();
        miStorage = FirebaseStorage.getInstance().getReference();
        mProgressDialog = new ProgressDialog(this);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("post");
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                idPOST = dataSnapshot.getKey();
                Log.v("id_", idPOST);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mibtnaddimagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptions();
            }
        });

        mibtnAceptaraddFavor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddFavorImagenActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }

    @SuppressLint("NewApi")
    private void showOptions() {
        final CharSequence[]options = {"Galeria", "Cancelar"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(AddFavorImagenActivity.this);
        builder.setTitle("Elige una opcion");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(options[which] == "Galeria"){
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Selecciona la app de imagenes"), SELECT_PICTURE);
                }else{
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path", mPath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mPath = savedInstanceState.getString("file_path");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_CODE:
                    MediaScannerConnection.scanFile(this,
                            new String[]{mPath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path);
                                    Log.i("ExternalStorage", "-> Uri " + uri);
                                }
                            });
                    Bitmap bitmap = BitmapFactory.decodeFile(mPath);
                    miImagenPost.setImageBitmap(bitmap);
                    break;
                case SELECT_PICTURE:
                    mProgressDialog.setTitle("Subiendo Foto");
                    mProgressDialog.setMessage("Subiendo Foto");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();


                    final Uri path = data.getData();
                    final String foto = "fotopost";

                    StorageReference filePath = miStorage.child("post")
                            .child(idPOST).child("imagenpost");
                    filePath.putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String photourl = "gs://adoptpet-f1b0d.appspot.com/fotospost/" + foto;
                            //******************************************************************

                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("post");
                            DatabaseReference currentUserDB = mDatabase.child(idPOST).child("imagenpost");
                            currentUserDB.setValue(photourl);
                            DatabaseReference mDatabase2 = mDatabase.child(idPOST).child("idPost");
                            mDatabase2.setValue(idPOST);

                            mProgressDialog.dismiss();
                            miImagenPost.setImageURI(path);


                            Toast.makeText(AddFavorImagenActivity.this, "Foto añadida con exito", Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(AddFavorImagenActivity.this, "Permisos aceptados", Toast.LENGTH_SHORT).show();
            }
        } else {
            showExplanation();
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddFavorImagenActivity.this);
        builder.setTitle("PERMISOS DENEGADOS");
        builder.setMessage("Para usar las funciones de la app necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.show();
    }
}

