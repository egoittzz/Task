package com.example.ejercicio_base_de_datos;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    String usuario_Bienve;
    int id_usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        //TextView usuario = findViewById(R.id.Txt_usu_bienvenida);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Bundle usuario_pasado = getIntent().getExtras();
        usuario_Bienve = usuario_pasado.getString("Usuario");
        //usuario.setText(usuario_Bienve);
        cargarSpiner();
        listartareas();


    }
    @Override
    protected void onResume() {
        super.onResume();
        cargarSpiner();
        listartareas();
    }
    public void nueva_Tarea(View v){
        Intent mas_Tarea = new Intent(this,Anadir_tarea.class);
        mas_Tarea.putExtra("Usuario",usuario_Bienve);
        startActivity(mas_Tarea);
    }
    void listartareas () {
        TextView restantes = findViewById(R.id.Txt_restantes);
        Spinner categoria = findViewById(R.id.Spinner_cat);
        TextView completadas = findViewById(R.id.Txt_completados);
        String Categoria_final = categoria.getSelectedItem().toString();
        int codigo = -1;
        List<String> tareas = new ArrayList<>();
        List<Integer>Marcado = new ArrayList<>();
        Adminbasesdedatos admin = new Adminbasesdedatos(this, "bd_usuarios",null,1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor id_usuario = bd.rawQuery("Select Codigo From usuarios where Usuario = '"+usuario_Bienve+"'",null);
        if(id_usuario.moveToFirst()){
            codigo = id_usuario.getInt(0);
        }
        id_usuario.close();
        if(Categoria_final.equals("Todas")){
            Cursor tareas_cursor = bd.rawQuery("Select Texto,Marcado From tareas where cod_usuario = '"+codigo+"'",null);
            if(tareas_cursor.moveToFirst()){
                do {
                    tareas.add(tareas_cursor.getString(0));
                    Marcado.add(tareas_cursor.getInt(1));

                }while(tareas_cursor.moveToNext());
            }
            tareas_cursor.close();
        }else{
            int cate = -1;
            Cursor id_categorias = bd.rawQuery("Select Codigo_cat From categoria where Categoria ='"+Categoria_final+"'",null);
            if(id_categorias.moveToFirst()){
                cate = id_categorias.getInt(0);
            }
            id_categorias.close();
            Cursor tareas_cursor = bd.rawQuery("SELECT Texto, Marcado FROM tareas WHERE cod_usuario = " + codigo + " AND cod_Categoria = " + cate,null);
            if(tareas_cursor.moveToFirst()){
                do {
                    tareas.add(tareas_cursor.getString(0));
                    Marcado.add(tareas_cursor.getInt(1));

                }while(tareas_cursor.moveToNext());
            }
            tareas_cursor.close();
        }
        ListView lista = findViewById(R.id.lt_tareas);
        lista.setDivider(null);
        lista.setDividerHeight(15);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_tarea_simple,tareas);
        lista.setAdapter(adapter);
        lista.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        int cot= 0;
        for(int x  = 0;x<lista.getCount();x++){
            if(Marcado.get(x)==1){
                lista.setItemChecked(x,true);
                cot++;
            }
        }
        lista.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        completadas.setText(""+cot+"");
        int total = tareas.size()-cot;
        restantes.setText(""+total+"");
        //ECHO CON IA LA CABECERA DEL METODO
        lista.setOnItemClickListener((parent, view, position, id) -> {
            int estaMarcado = -1;
            String textoTarea = tareas.get(position);
            Cursor Marcados = bd.rawQuery("Select Marcado From tareas where Texto ='"+textoTarea+"'",null);
            if(Marcados.moveToFirst()){
                estaMarcado=Marcados.getInt(0);
            }
            if(estaMarcado == 0){
                bd.execSQL("UPDATE tareas SET Marcado = 1 WHERE Texto = '" + textoTarea + "'");
                bd.close();

            } else if (estaMarcado == 1) {
                bd.execSQL("UPDATE tareas SET Marcado = 0 WHERE Texto = '" + textoTarea + "'");
                bd.close();

            }else{
                Toast.makeText(this, "A ocurrido un error", Toast.LENGTH_SHORT).show();
            }
            listartareas();
        });
        // Hasta aqui


    }
    public void listartareas(View v){
        listartareas();
    }
    public void salir (View v){
        finish();
    }

    public void cargarSpiner(){
        Spinner spinner = findViewById(R.id.Spinner_cat);
        List<String> todasCat = new ArrayList<>();
        todasCat.add("Todas");
        Adminbasesdedatos admin = new Adminbasesdedatos(this,"bd_usuarios",null,1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor sacar_id = bd.rawQuery("Select Codigo FROM usuarios where Usuario ='"+usuario_Bienve+"'",null);
        if(sacar_id.moveToFirst()){
            id_usuario = sacar_id.getInt(0);
        }
        sacar_id.close();
        Cursor categorias_prueba = bd.rawQuery("SELECT Categoria FROM categoria WHERE Id_usuario = " + id_usuario, null);
        if(categorias_prueba.moveToFirst()){
            do{
                todasCat.add(categorias_prueba.getString(0));
            }while (categorias_prueba.moveToNext());
        }
        categorias_prueba.close();
        bd.close();
        if (todasCat.isEmpty()) {
            todasCat.add("Sin categorías");
        }
        ArrayAdapter <String> Adaptador = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,todasCat);
        Adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(Adaptador);
    }
}