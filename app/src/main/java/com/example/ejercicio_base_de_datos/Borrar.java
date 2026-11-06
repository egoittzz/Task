package com.example.ejercicio_base_de_datos;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class Borrar extends AppCompatActivity {
    String Usuario ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_borrar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Bundle usuario = getIntent().getExtras();
        Usuario = usuario.getString("Usuario");
        listar_tarreas();
    }
    public void listar_tarreas(){
        int idUsu = -1;
        ListView tareas = findViewById(R.id.ListV_borrar);
        List<String> Tareas_borrar = new ArrayList<>();
        Adminbasesdedatos admin = new Adminbasesdedatos(this,"bd_usuarios",null,1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        Cursor id = bd.rawQuery("Select Codigo From usuarios where Usuario ='"+Usuario+"'",null);
        if(id.moveToFirst()){
            idUsu = id.getInt(0);
        }
        id.close();
        Cursor tareass = bd.rawQuery("Select Texto from tareas where cod_usuario ="+ idUsu,null);
        if(tareass.moveToFirst()){
            do{
                Tareas_borrar.add(tareass.getString(0));
            }while (tareass.moveToNext());
        }
        tareass.close();
        ArrayAdapter<String> list = new ArrayAdapter<>(this,R.layout.item_tarea_simple,Tareas_borrar);
        tareas.setAdapter(list);
        tareas.setDivider(null);
        tareas.setDividerHeight(30);
        tareas.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }
    public void borrar (View v){
        List<String> datos = new ArrayList<>();
        int idUsu = -1;
        Button verificado = findViewById(R.id.Bt_aceptar);

        if(verificado.isActivated()){
            String texto ;
            Adminbasesdedatos admin = new Adminbasesdedatos(this,"bd_usuarios",null,1);
            SQLiteDatabase bd = admin.getWritableDatabase();

            Cursor id = bd.rawQuery("Select Codigo From usuarios where Usuario ='"+Usuario+"'",null);
            if(id.moveToFirst()){
                idUsu = id.getInt(0);
            }
            id.close();
            Cursor tareass = bd.rawQuery("Select Texto from tareas where cod_usuario ="+ idUsu,null);
            if(tareass.moveToFirst()){
                do{
                    datos.add(tareass.getString(0));
                }while (tareass.moveToNext());
            }
            tareass.close();
            ListView lista = findViewById(R.id.ListV_borrar);
            for(int x = 0;x < lista.getCount() ;x++){
                if(lista.isItemChecked(x)){
                    texto = datos.get(x).toString();
                    bd.execSQL("Delete from tareas where Texto ='"+texto+"'");
                    datos.remove(x);


                }

            }
            listar_tarreas();
        }else{
            Toast.makeText(this,"Acepta que sabes que se borrara permanentemente",Toast.LENGTH_LONG).show();
        }


    }
    public void volver (View v){
        finish();
    }


}