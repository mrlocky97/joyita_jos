package com.example.juansebastianquinayasguarin.pets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ProtectorasActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ListView listView;
    private Adaptador adaptador;
    private TextView nombreMenu, emailMenu;
    private CircularImageView miImagenN;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protectoras);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView)findViewById(R.id.listv);
        adaptador = new Adaptador(GetArrayItems(), this);
        listView.setAdapter(adaptador);
        firebaseAuth = FirebaseAuth.getInstance();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prueba(ProtectorasActivity.this,position);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private ArrayList<Entidad> GetArrayItems(){
        ArrayList<Entidad> lsData = new ArrayList<>();
        lsData.add(new Entidad(R.drawable.imgalba,"ALBA"));
        lsData.add(new Entidad(R.drawable.imganaa,"ANAA"));
        lsData.add(new Entidad(R.drawable.imglamadrilena,"LAMADRILEÑA"));
        lsData.add(new Entidad(R.drawable.imgperrikus,"PERRIKUS"));
        lsData.add(new Entidad(R.drawable.imgrivanimal,"RIVANIMAL"));
        lsData.add(new Entidad(R.drawable.imgspap,"SPAP"));
        return lsData;
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
        getMenuInflater().inflate(R.menu.menu_navigation, menu);

        nombreMenu = (TextView) findViewById(R.id.tv_nom_menu);
        emailMenu = (TextView) findViewById(R.id.tv_email_menu);
        miImagenN = findViewById(R.id.img_foto_menu);

        database.getReference("usuarios").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.v("pp", String.valueOf(dataSnapshot.child("nombre")));
                Usuario u1 = dataSnapshot.getValue(Usuario.class);
                String nom = u1.getNombre();
                String ema = u1.getEmail();
                //Log.v("pp2", nom + " " + ema);
                emailMenu.setText(ema);
                nombreMenu.setText(nom);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        try {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReferenceFromUrl("gs://adoptpet-f1b0d.appspot.com").child("usuarios")
                    .child(firebaseAuth.getCurrentUser().getUid()).child("imagenperfil");
            final File localFile;

            localFile = File.createTempFile("images", "jpg");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    miImagenN.setImageBitmap(bitmap);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


        return true;
    }

    public void prueba (final Context context, final int position ){

        if(position == 0){
            Intent intent = new Intent(context,protectora_view.class);
            startActivity(intent);
        }else if(position == 1){
            Intent intent = new Intent(context,protectora_view1.class);
            startActivity(intent);
        }
        else if(position == 2){
            Intent intent = new Intent(context,protectora_view2.class);
            startActivity(intent);
        }
        else if(position == 3){
            Intent intent = new Intent(context,protectora_view3.class);
            startActivity(intent);
        }
        else if(position == 4){
            Intent intent = new Intent(context,protectora_view4.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(context,protectora_view5.class);
            startActivity(intent);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(ProtectorasActivity.this, HomeActivity.class);
            startActivity(intent);
        }
        if (id == R.id.nav_editPerfil) {
            Intent intent = new Intent(ProtectorasActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_protectoras) {
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(ProtectorasActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
