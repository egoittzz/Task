package com.example.ejercicio_base_de_datos;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Nueva_categoria extends AppCompatActivity {
    String id_usuario;
    int cod_usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_nueva_categoria);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Bundle usuario = getIntent().getExtras();
        id_usuario = usuario.getString("Usuario");


    }
    public void crearcat (View v){
        EditText categoria = findViewById(R.id.Et_Nombre_Categoria);
        String cate = categoria.getText().toString();
        if(cate.isEmpty()){
            Toast.makeText(this,"No has puesto el nombre de la nueva categoria",Toast.LENGTH_LONG).show();
        }else{
            Adminbasesdedatos admin = new Adminbasesdedatos(this,"bd_usuarios",null,1);
            SQLiteDatabase bd = admin.getWritableDatabase();
            Cursor categorias = bd.rawQuery("SELECT Codigo_cat FROM categoria WHERE Categoria ='"+ cate+"'",null);
            if(categorias.moveToFirst()){
                Toast.makeText(this,"Esta categoria ya a sido creada",Toast.LENGTH_LONG).show();

            }else{
                Cursor c = bd.rawQuery("SELECT Codigo FROM usuarios WHERE Usuario = '"+id_usuario+"'",null);
                if (c.moveToFirst()) {
                    cod_usuario = c.getInt(0);
                }
                c.close();
                ContentValues cargarcate = new ContentValues();
                cargarcate.put("Categoria",cate);
                cargarcate.put("Id_Usuario",cod_usuario);
                bd.insert("categoria",null,cargarcate);
                bd.close();
                Toast.makeText(this,"Categoria creada con exito",Toast.LENGTH_LONG).show();
                finish();

            }
        }
    }
    public void volver_atras(View v){
        finish();
    }
}