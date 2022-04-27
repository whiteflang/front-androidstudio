package com.example.usuario_webservices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class registroActivity extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {
    EditText jetusr,jetnombre,jetcorreo,jetclave;
    Button jbtRegresar,jbtRegistrar,jbtConsultar;
    RequestQueue rq;
    JsonRequest jrq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        getSupportActionBar().hide();
        jetclave = findViewById(R.id.etclave);
        jetusr = findViewById(R.id.etuser);
        jetcorreo = findViewById(R.id.etcorreo);
        jetnombre = findViewById(R.id.etnombre);
        jbtRegistrar = findViewById(R.id.btregistrar);
        jbtRegresar = findViewById(R.id.btregresar);
        jbtConsultar = findViewById(R.id.btconsultar);
        jbtRegresar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intMain = new Intent(getBaseContext(),MainActivity.class);
                intMain.setAction(Intent.ACTION_MAIN);
                intMain.addCategory(Intent.CATEGORY_HOME);
                intMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intMain);
            }
        });

        jbtConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consultar();
            }
        });

        jbtRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar();
            }
        });
    }

    public void registrar() {
        if (jetnombre.getText().toString().isEmpty() || jetclave.getText().toString().isEmpty() || jetusr.getText().toString().isEmpty() || jetcorreo.getText().toString().isEmpty()) {
            Toast.makeText(this, "Todos Los Campos Son Obligatorios!", Toast.LENGTH_SHORT).show();
        } else {
            String url = "http://172.16.61.101:8081/usuarios/registrocorreo.php";
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            jetclave.setText("");
                            jetcorreo.setText("");
                            jetnombre.setText("");
                            jetusr.setText("");
                            jetusr.requestFocus();
                            Toast.makeText(getApplicationContext(), "Registro de usuario realizado correctamente!", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Registro de usuario incorrecto!", Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("usr",jetusr.getText().toString().trim());
                    params.put("nombre", jetnombre.getText().toString().trim());
                    params.put("correo",jetcorreo.getText().toString().trim());
                    params.put("clave",jetclave.getText().toString().trim());
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);
        }
    }

    public  void consultar(){
        if (jetusr.getText().toString().isEmpty()){
            Toast.makeText(this,"El usuario es requerido para consultar",Toast.LENGTH_SHORT).show();
            jetusr.requestFocus();
        }
        else{
            String url = "http://172.16.61.101:8081/usuarios/consulta.php?usr="+jetusr.getText().toString();
            jrq = new JsonObjectRequest(Request.Method.GET,url,null,this::onResponse,this::onErrorResponse);
            rq.add(jrq);
        }
    }


    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Usuario Invalido", Toast.LENGTH_SHORT).show();
    }
    public void onResponse(JSONObject response) {
        ClsUsuario usuario = new ClsUsuario();
         Toast.makeText(this,"Se ha encontrado el usuario "+jetusr.getText().toString(),Toast.LENGTH_SHORT).show();
        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;
        try {
            jsonObject = jsonArray.getJSONObject(0);//posicion 0 del arreglo....
            usuario.setUsr(jsonObject.optString("usr"));
            usuario.setNombre(jsonObject.optString("nombre"));
            usuario.setCorreo(jsonObject.optString("correo"));

            jetnombre.setText(usuario.getNombre());
            jetcorreo.setText(usuario.getCorreo());

        }
        catch ( JSONException e)
        {
            e.printStackTrace();
        }
    }
}