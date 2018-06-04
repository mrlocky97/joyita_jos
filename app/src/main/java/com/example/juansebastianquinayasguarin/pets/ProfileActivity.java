package com.example.juansebastianquinayasguarin.pets;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.IOException;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GotUser {

    private Database database = new Database();
    private Usuario user;

    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;

    //*************************************************************************

    TextView nombreMenu, emailMenu;
    TextView nombreUsuario, email, telefono;
    FloatingActionButton mFab;
    CircularImageView mImagenPerfil;
    CircularImageView miImagenN;
    private StorageReference miStorage;
    private ProgressDialog mProgressDialog;
    private String mPath;
    private Button mOptionButtonTransparente;
    FirebaseAuth mAuth;
    private RelativeLayout mRlView;
    FirebaseUser Fuser = FirebaseAuth.getInstance().getCurrentUser();
    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    FirebaseDatabase Fdatabase = FirebaseDatabase.getInstance();

    //***************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        nombreUsuario = findViewById(R.id.nombre_usuario_perfil);
        email = findViewById(R.id.email_usuario_perfil);
        telefono = findViewById(R.id.telefono_usuario_perfil);
        mImagenPerfil = findViewById(R.id.imagen_usuario_perfil);
        mFab = findViewById(R.id.fab_editar_perfil);
        mOptionButtonTransparente = findViewById(R.id.buttontr);
        miStorage = FirebaseStorage.getInstance().getReference();
        mProgressDialog = new ProgressDialog(this);
        mRlView = findViewById(R.id.mRlView);

        if (myRequestStorgePermission()){
            mOptionButtonTransparente.setEnabled(true);
        }else{
            mOptionButtonTransparente.setEnabled(false);
        }
    }

    private boolean myRequestStorgePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;
        if ((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;

        if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))) {
            Snackbar.make(mRlView, "Los permisos son necesarios para poder usar la app",
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
                }
            }).show();
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        }

        return false;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        nombreMenu = (TextView) findViewById(R.id.tv_nom_menu);
        emailMenu = (TextView) findViewById(R.id.tv_email_menu);
        user = database.getLoggedUser(this);
        mImagenPerfil = findViewById(R.id.imagen_usuario_perfil);
        miImagenN = findViewById(R.id.img_foto_menu);
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.camara) {
            showOptions();
        } else if (id == R.id.pencil){
            showEditProfile();
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            startActivity(intent);
        }if (id == R.id.nav_editPerfil) {

        } else if (id == R.id.nav_protectoras) {

        } else if (id == R.id.nav_mis_post) {

        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void loggedUser(Usuario user){
        nombreMenu.setText(user.getNombre());
        emailMenu.setText(user.getEmail());
        nombreUsuario.setText(user.getNombre());
        telefono.setText(user.getTelefono());
        email.setText(user.getEmail());

        try{
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReferenceFromUrl("gs://adoptpet-f1b0d.appspot.com").child("usuarios")
                    .child(mAuth.getCurrentUser().getUid()).child("imagenperfil");
            final File localFile;

            localFile = File.createTempFile("images", "jpg");
             storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                 @Override
                 public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                     Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                     mImagenPerfil.setImageBitmap(bitmap);
                     miImagenN.setImageBitmap(bitmap);
                 }
             });
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    @SuppressLint("NewApi")
    private void showOptions() {
        final CharSequence[]options = {"Galeria", "Cancelar"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
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
                    mImagenPerfil.setImageBitmap(bitmap);
                    break;
                case SELECT_PICTURE:
                    mProgressDialog.setTitle("Subiendo Foto");
                    mProgressDialog.setMessage("Subiendo Foto");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();


                    final Uri path = data.getData();
                    final String foto = "fotodeperfil";

                    StorageReference filePath = miStorage.child("usuarios")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("imagenperfil");
                    filePath.putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String photourl = "gs://adoptpet-f1b0d.appspot.com/fotos/" + foto;
                            //******************************************************************

                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("usuarios");
                            DatabaseReference currentUserDB = mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("imagenperfil");
                            currentUserDB.setValue(photourl);

                            mProgressDialog.dismiss();
                            mImagenPerfil.setImageURI(path);
                            miImagenN.setImageURI(path);

                            Toast.makeText(ProfileActivity.this, "Foto añadida con exito", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ProfileActivity.this, "Permisos aceptados", Toast.LENGTH_SHORT).show();
                mOptionButtonTransparente.setEnabled(true);
            }
        } else {
            showExplanation();
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
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


    private void showEditProfile() {
        final View editProfileDialogView = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.dialog_edit_profile_template, null);
        AlertDialog dialog = new AlertDialog.Builder(ProfileActivity.this)
                .setTitle("EDICION DE DATOS")
                .setView(editProfileDialogView)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextInputEditText tIETEditPersonName, tIETEditEmail, tIETEditPhoneNumber;
                        tIETEditPersonName = editProfileDialogView.findViewById(R.id.tIETEditPersonName);
                        tIETEditEmail = editProfileDialogView.findViewById(R.id.tIETEditEmail);
                        tIETEditPhoneNumber = editProfileDialogView.findViewById(R.id.tIETEditPhoneNumber);
                        String name = tIETEditPersonName.getText().toString();
                        String email = tIETEditEmail.getText().toString();
                        String phoneNumber = tIETEditPhoneNumber.getText().toString();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("usuarios").child(userID);
                        databaseReference.child("personName").setValue(name);
                        databaseReference.child("email").setValue(email);
                        databaseReference.child("pnumber").setValue(phoneNumber);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .create();
        dialog.show();
    }

}
