package com.example.juansebastianquinayasguarin.pets;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MenuNavigationActivity extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {
    ArrayList<Post> listaPOST = new ArrayList<>();
    TextView nombreMenu, emailMenu;
    EditText tituloDialog, descDialod;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseAuth firebaseAuth;
    private AdapterPost adapterPost;
    //variables de ampliar_post_dialog.xml
    ImageView img_amp_dialog;
    TextView tv_amp_titulo_dialog, tv_amp_descripsion_dialog;
    ListView vista_lista_post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_navigation);
        //declaracion de variables
        vista_lista_post = (ListView) findViewById(R.id.lv_principal_pg);
        firebaseAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //*****************************************************************************
        //declaramos variables
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Agregando un post nuevo.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                addFavor(view.getContext());
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("post");
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Post p1 = dataSnapshot.getValue(Post.class);
                listaPOST.add(p1);
                AdapterPost adapterPost = new AdapterPost(MenuNavigationActivity.this, listaPOST);
                vista_lista_post.setAdapter(adapterPost);
                vista_lista_post.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ampliarPost(MenuNavigationActivity.this);
                    }
                });
                //vista_lista_post.setOnItemClickListener(MenuNavigationActivity.this,listaPOST);
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
    }


    private void ampliarPost(Context context) {
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.ampliar_post_dialog, null);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("INFORMACION DEL POST")
                .setView(dialogView)
                .setPositiveButton(R.string.llamar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        img_amp_dialog = dialogView.findViewById(R.id.img_ampliar_post_dialog);
                        tv_amp_titulo_dialog = dialogView.findViewById(R.id.tv_amp_dialog_titulo);
                        tv_amp_descripsion_dialog = dialogView.findViewById(R.id.tv_amp_dialog_descripsion);
                    }
                })
                .setNegativeButton(R.string.salir, null)
                .create();
        dialog.show();
    }

    //metodo añadir favor
    private void addFavor(Context context) {
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.add_dialog_post, null);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.titulo)
                .setMessage(R.string.descripcion)
                .setView(dialogView)
                .setPositiveButton(R.string.guardar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tituloDialog = dialogView.findViewById(R.id.Et_titulo_add_post);
                        descDialod = dialogView.findViewById(R.id.Et_desc_add_post);
                        String titulo = tituloDialog.getText().toString();
                        String descripcion = descDialod.getText().toString();
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("post");
                        Post p1 = new Post(titulo, descripcion, "NULL");
                        mDatabase.push().setValue(p1);
                    }
                })
                .setNegativeButton(R.string.cancelar, null)
                .create();
        dialog.show();
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
        database.getReference("usuarios").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("pp", String.valueOf(dataSnapshot.child("nombre")));
                Usuario u1 = dataSnapshot.getValue(Usuario.class);
                String nom = u1.getNombre();
                String ema = u1.getEmail();
                Log.v("pp2", nom + " " + ema);
                emailMenu.setText(ema);
                nombreMenu.setText(nom);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_editPerfil) {
            Intent intent = new Intent(MenuNavigationActivity.this, profileUserActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_protectoras) {

        } else if (id == R.id.nav_mis_post) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ampliarPost(MenuNavigationActivity.this);
    }
}
