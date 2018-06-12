package com.example.juansebastianquinayasguarin.pets;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

public class ProtectorasActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ListView listView;
    private Adaptador adaptador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protectoras);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView)findViewById(R.id.listv);
        adaptador = new Adaptador(GetArrayItems(), this);
        listView.setAdapter(adaptador);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

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
        } else if (id == R.id.nav_mis_post) {
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(ProtectorasActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
