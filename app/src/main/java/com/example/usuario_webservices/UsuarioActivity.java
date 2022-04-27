package com.example.usuario_webservices;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class UsuarioActivity extends AppCompatActivity {

    public static final String nombre="nombre";
        TextView jtvnombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        getSupportActionBar().hide();
        jtvnombre = findViewById(R.id.tvnombre);
        String nombre = getIntent().getStringExtra("nombre");
        jtvnombre.setText(jtvnombre.getText().toString() + " " + nombre);
    }
}