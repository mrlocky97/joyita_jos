package com.example.juansebastianquinayasguarin.pets;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistroActivity extends AppCompatActivity {
    private EditText nombre, telefono, email, cont1, cont2;
    private Button registrar, cancelar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        nombre = (EditText) findViewById(R.id.ET_nombre_reg);
        telefono = (EditText) findViewById(R.id.ET_telefono_reg);
        email = (EditText) findViewById(R.id.ET_email_reg);
        cont1 = (EditText) findViewById(R.id.ET_cont1_reg);
        cont2 = (EditText) findViewById(R.id.ET_cont2_reg);

        registrar = (Button) findViewById(R.id.btn_registrar_reg);
        cancelar = (Button) findViewById(R.id.btn_cancelar_reg);

        firebaseAuth = FirebaseAuth.getInstance();

        //lisener al btn registro
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = nombre.getText().toString().trim();
                String tel = telefono.getText().toString().trim();
                String ema = email.getText().toString().trim();
                String contrasena1 = cont1.getText().toString().trim();
                String contrasena2 = cont2.getText().toString().trim();
                //comprovamos que coje todos lo datos
                Log.v("datos reg", nom + " / " + tel + " / " + ema + " / " + contrasena1 + " / " + contrasena2);
                if (!TextUtils.isEmpty(nom) && !TextUtils.isEmpty(tel) && !TextUtils.isEmpty(ema) && !TextUtils.isEmpty(contrasena1) && !TextUtils.isEmpty(contrasena2)) {
                    if (contrasena1.equals(contrasena2)) {
                        //la cont1 y cont2 son iguales y registramos
                        registrarUsuario(nom, tel, ema, contrasena1);
                    } else {
                        Toast.makeText(RegistroActivity.this, "Error: las contraseñas no coinciden, vuelva a intentarlo.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegistroActivity.this, "Por favor rellene todos los campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void registrarUsuario(final String nombre, final String telefono, final String email, final String contraseña) {
        firebaseAuth.createUserWithEmailAndPassword(email, contraseña).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.v("autenticacion", "Creando usuario con hotmail en proceso..." + task.isSuccessful());
                if (!task.isSuccessful()) {
                    Toast.makeText(RegistroActivity.this, "autentificacion fallida.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegistroActivity.this, "autenticacion correcta.", Toast.LENGTH_SHORT).show();
                    Usuario user = new Usuario(nombre, telefono, email, contraseña, "null");
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("usuarios");
                    DatabaseReference databaseReference1 = databaseReference.child(firebaseAuth.getCurrentUser().getUid());
                    databaseReference1.setValue(new Usuario(user.getNombre(), user.getTelefono(), user.getEmail(), user.getContraseña(), "null"));
                    //pasar a la pantalla login
                    Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
