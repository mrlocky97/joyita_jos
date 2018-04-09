package com.example.juansebastianquinayasguarin.pets;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mikhaellopez.circularimageview.CircularImageView;

public class LoginActivity extends AppCompatActivity {
    private CircularImageView imagenCircular;
    private EditText email, contraseña;
    private Button entrar, registrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        imagenCircular = (CircularImageView) findViewById(R.id.img_circular_loign);
        email = (EditText) findViewById(R.id.ET_email_login);
        contraseña = (EditText) findViewById(R.id.ET_cont_login);

        entrar = (Button) findViewById(R.id.btn_entrar_login);
        registrarse = (Button) findViewById(R.id.btn_registarse_login);

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ema = email.getText().toString().trim();
                String cont = contraseña.getText().toString().trim();
                if (!TextUtils.isEmpty(ema) && !TextUtils.isEmpty(cont)) {
                    logearse(ema, cont);
                } else {
                    Toast.makeText(LoginActivity.this, "Por favor rellene todos los campos.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void logearse(String email, String contraseña) {
        FirebaseAuth fauth = FirebaseAuth.getInstance();
        fauth.signInWithEmailAndPassword(email, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Se ha logeado con exito.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MenuNavigationActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Error de logeo vuelva a intentarlo.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
