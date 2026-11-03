package com.example.ejercicio_base_de_datos;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void registrarse (View v){
        Intent registro = new Intent(this,Registrate.class);
        startActivity(registro);
    }
    public void iniciar_sesion(View v){
       EditText usuario = findViewById(R.id.et_usu);
       EditText contrasena = findViewById(R.id.et_con);

       String usu = usuario.getText().toString();
       String con = contrasena.getText().toString();
       if(usu.isEmpty()||con.isEmpty()){
           Toast.makeText(this,"Rellene todos los campos",Toast.LENGTH_LONG).show();
       }else{
           Adminbasesdedatos admin = new Adminbasesdedatos(this,"bd_usuarios",null,1);
           SQLiteDatabase bd = admin.getWritableDatabase();
           Cursor inicio_sesion = bd.rawQuery("select Codigo from usuarios where Usuario='" + usu + "' AND Contrasena='" + con + "'",null);
           if(inicio_sesion.moveToFirst()){
               Toast.makeText(this,"Usuario encontrado Bienvenido " + usu ,Toast.LENGTH_LONG).show();
                Intent home = new Intent(this,Home.class);
                home.putExtra("Usuario",usu);
                startActivity(home);
               inicio_sesion.close();
               bd.close();
           }else{
               Toast.makeText(this,"No se a encontrado usuario",Toast.LENGTH_LONG).show();
               inicio_sesion.close();
               bd.close();
           }
       }
    }

}

