package com.example.ejercicio_base_de_datos;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class Anadir_tarea extends AppCompatActivity {
    String usuarioTarea;
    int cod_usuario = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_anadir_tarea);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Bundle usuario = getIntent().getExtras();
        usuarioTarea = usuario.getString("Usuario");
        cargarSpinner();

    }
    @Override
    protected void onResume() {
        super.onResume();
        cargarSpinner();
    }
    public void CrearCat (View v){
        Intent crear_cate = new Intent(this,Nueva_categoria.class);
        crear_cate.putExtra("Usuario",usuarioTarea);
        startActivity(crear_cate);
    }
    void cargarSpinner(){
        Spinner spinercat = findViewById(R.id.spinner);
        List<String> categorias = new ArrayList<>();
        int id_usuario = -1;
        Adminbasesdedatos admin = new Adminbasesdedatos(this,"bd_usuarios",null,1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        Cursor ids = bd.rawQuery("Select Codigo From usuarios where Usuario ='"+usuarioTarea+"'",null);
        if(ids.moveToFirst()){
            id_usuario = ids.getInt(0);
        }
        ids.close();

        Cursor categoria = bd.rawQuery("Select Categoria From categoria where Id_usuario ='"+id_usuario+"'",null);
        if(categoria.moveToFirst()){
            do{
                categorias.add(categoria.getString(0));
            }while (categoria.moveToNext());
        }
        categoria.close();
        ArrayAdapter<String> adapatador = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,categorias);
        adapatador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinercat.setAdapter(adapatador);
    }
    public void añadir_Tarea(View v){
        Spinner spinner = findViewById(R.id.spinner);
        EditText Tarea = findViewById(R.id.Et_tarea);
        EditText fecha = findViewById(R.id.Et_fecha);
        String categoria = spinner.getSelectedItem().toString();
        String tarea_stri = Tarea.getText().toString();
        String fechas = fecha.getText().toString();
        Adminbasesdedatos admin = new Adminbasesdedatos(this, "bd_usuarios", null, 1);
        SQLiteDatabase bd = admin.getReadableDatabase();
        if(categoria.isEmpty()||tarea_stri.isEmpty()||fechas.isEmpty()){
            Toast.makeText(this,"Rellene todos los capos",Toast.LENGTH_LONG).show();

        }else{
            int cod_categoria = -1;

            Cursor id_categoria = bd.rawQuery("Select Codigo_cat From categoria where Categoria ='" + categoria + "'",null);
            if(id_categoria.moveToFirst()){
                cod_categoria = id_categoria.getInt(0);
            }
            Cursor id_usuario = bd.rawQuery("Select Codigo From usuarios where Usuario ='" + usuarioTarea+"'",null);
            if(id_usuario.moveToFirst()){
                cod_usuario=id_usuario.getInt(0);
            }


            ContentValues tareas = new ContentValues();
            tareas.put("Texto",tarea_stri);
            tareas.put("Fecha",fechas);
            tareas.put("cod_Categoria",cod_categoria);
            tareas.put("cod_usuario",cod_usuario);

            bd.insert("tareas",null,tareas);
            Toast.makeText(this,"Se a añadido tarea sin problema",Toast.LENGTH_LONG).show();
            finish();
        }

    }
    public void volver (View v){
        finish();
    }
    public int sacar_idusu(){
        Adminbasesdedatos admin = new Adminbasesdedatos(this,"bd_usuarios",null,1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor id_usuario = bd.rawQuery("Select Codigo From usuarios where Usuario ='" + usuarioTarea+"'",null);
        if(id_usuario.moveToFirst()){
            cod_usuario=id_usuario.getInt(0);
        }
        return cod_usuario;
    }


}