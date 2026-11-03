package com.example.ejercicio_base_de_datos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Adminbasesdedatos extends SQLiteOpenHelper {
    public Adminbasesdedatos(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table usuarios (Codigo INTEGER PRIMARY KEY AUTOINCREMENT,Usuario text,Contrasena text)");
        db.execSQL("Create table categoria (Codigo_cat INTEGER PRIMARY KEY AUTOINCREMENT,Categoria text,Id_usuario INT,FOREIGN KEY(Id_Usuario) REFERENCES usuarios(Codigo) ON DELETE CASCADE)");
        db.execSQL("Create table tareas (Codigo_tarea INTEGER PRIMARY KEY AUTOINCREMENT,Texto text,Fecha text,Marcado Bool,cod_Categoria INT ,cod_usuario INT, FOREIGN KEY (cod_usuario) REFERENCES usuarios(Codigo) ON DELETE CASCADE,FOREIGN KEY (cod_Categoria) REFERENCES Categoria(Codigo_cat) ON DELETE CASCADE)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }
}
