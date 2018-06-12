package com.example.juansebastianquinayasguarin.pets;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity

        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener, GotPost {
    ArrayList<Post> listaPOST = new ArrayList<>();
    ArrayList<Bitmap> listaImagen = new ArrayList<Bitmap>();
    TextView nombreMenu, emailMenu;
    EditText tituloDialog, descDialod;
    ImageView miImagenAnimal;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseAuth firebaseAuth;
    // private AdapterPost adapterPost = new AdapterPost(HomeActivity.this, listaPOST);
    //variables de ampliar_post_dialog.xml
    ImageView img_amp_dialog, miimg_list_view_post;
    CircularImageView miImagenN;
    TextView tv_amp_titulo_dialog, tv_amp_descripsion_dialog;
    ListView vista_lista_post;
    String miVariable = null;
    Post post;
    Bitmap miBitmap;
    String idPost;
    StorageReference storageReference;
    FirebaseStorage storage;
    private Database Ddatabase = new Database();
    RelativeLayout mimiRly;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_navigation);
        //declaracion de variables
        vista_lista_post = (ListView) findViewById(R.id.lv_principal_pg);
        miimg_list_view_post = findViewById(R.id.img_list_view_post);
        mimiRly = findViewById(R.id.miRly);
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
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
                Intent intent = new Intent(HomeActivity.this, AddFavorActivity.class);
                startActivity(intent);
                //addFavor(view.getContext());
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
                final Post p1 = dataSnapshot.getValue(Post.class);

                miVariable = dataSnapshot.getKey();
                Log.v("miV", miVariable);

                listaPOST.add(p1);
                listaImagen.add(cargarImagenes());
                Log.v("miva", String.valueOf(listaImagen.size()));


                // codigo a probar.
                System.out.println("llega hasta aqui");


                AdapterPost adapterPost = new AdapterPost(HomeActivity.this, listaPOST, listaImagen);
                vista_lista_post.setAdapter(adapterPost);
                //pantallaAbajo();
                vista_lista_post.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ampliarPost(HomeActivity.this, position);
                    }
                });
                //vista_lista_post.setOnItemClickListener(HomeActivity.this,listaPOST);
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

    private Bitmap cargarImagenes() {
        try {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReferenceFromUrl("gs://adoptpet-f1b0d.appspot.com").child("post")
                    .child(miVariable).child("imagenpost");
            final File localFile;

            localFile = File.createTempFile("images", "jpg");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    miBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return miBitmap;
    }

    private void ampliarPost(final Context context, final int position) {

        final String[] tituloText = {null};
        final String[] descText = {null};
        final String[] imgText = {null};

        String idPost = listaPOST.get(position).getIdPost();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("post").child(idPost);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post p1 = dataSnapshot.getValue(Post.class);
                //Log.v("123", p1.getTitulo());
                tituloText[0] = p1.getTitulo();
                descText[0] = p1.getDescripcion();
                imgText[0] = p1.getImagenpost();
                final View dialogView = LayoutInflater.from(context).inflate(R.layout.ampliar_post_dialog, null);
                img_amp_dialog = dialogView.findViewById(R.id.img_ampliar_post_dialog);
                tv_amp_titulo_dialog = dialogView.findViewById(R.id.tv_amp_dialog_titulo);
                tv_amp_descripsion_dialog = dialogView.findViewById(R.id.tv_amp_dialog_descripsion);

                try {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageReference = storage.getReferenceFromUrl("gs://adoptpet-f1b0d.appspot.com").child("post")
                            .child(p1.getIdPost()).child("imagenpost");
                    final File localFile;

                    localFile = File.createTempFile("images", "jpg");
                    storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            img_amp_dialog.setImageBitmap(bitmap);

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

                tv_amp_titulo_dialog.setText(tituloText[0]);
                tv_amp_descripsion_dialog.setText(descText[0]);


                //Log.v("enero", tituloText[0] + " " + descText[0] + " " + imgText[0]);

                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("INFORMACION DEL POST")
                        .setView(dialogView)
                        .setPositiveButton(R.string.Aceptar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //se codifica para llamar al movil de la persona
                            }
                        })
                        .setNegativeButton(R.string.salir, null)
                        .create();
                dialog.show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                idPost = dataSnapshot.getKey();
                //Log.v("pp2", nom + " " + ema);
                emailMenu.setText(ema);
                nombreMenu.setText(nom);
                post = Ddatabase.getLoggedPost(HomeActivity.this, idPost);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            return true;
        }
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {

        }
        if (id == R.id.nav_editPerfil) {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_protectoras) {
            Intent intent = new Intent(HomeActivity.this, ProtectorasActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void getPostdate(Post post) {

    }

    @Override
    public Bitmap getImgPost(Post post) {
        return null;
    }
}
