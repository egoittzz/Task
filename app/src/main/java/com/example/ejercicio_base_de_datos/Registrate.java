package com.example.ejercicio_base_de_datos;

import android.content.ContentValues;
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

public class Registrate extends AppCompatActivity {

    private Object finish;
    private int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registrate);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
    public void salir(View i){
        finish();
    }
    public void registrarse(View v) {
        EditText tx_usuario = findViewById(R.id.et_usuario);
        EditText tx_contrasena = findViewById(R.id.et_contrasena);
        EditText tx_contrasena2 = findViewById(R.id.et_contrasenacopy);

        String Usu = tx_usuario.getText().toString();
        String contra = tx_contrasena.getText().toString();
        String contra2 = tx_contrasena2.getText().toString();

        if(Usu.isBlank() || contra.isBlank() || contra2.isBlank()){
            Toast.makeText(this,"Rellena todos los campos para poder Registrarse",Toast.LENGTH_LONG).show();
        }else{
            if(!contra.equals(contra2)){
                Toast.makeText(this,"Compruebe que las contraseñas sean iguales" ,Toast.LENGTH_LONG).show();
                tx_contrasena.setText("");
                tx_contrasena2.setText("");
            }else{

                //Conexion a la base de datos
                Adminbasesdedatos admin = new Adminbasesdedatos(this,"bd_usuarios",null,1);
                SQLiteDatabase bd = admin.getWritableDatabase();
                Cursor comprueba = bd.rawQuery("select Codigo from usuarios where Usuario='" + Usu + "' OR Contrasena='" + contra + "'",null);
                if(comprueba.moveToFirst()){
                    Toast.makeText(this,"Lo sentimos el usuario o la contraseña ya estan en uso",Toast.LENGTH_LONG).show();
                }else{
                    //Contenedor para agrupar los datos
                    ContentValues registro = new ContentValues();
                    //Insertamos datos
                    registro.put("Usuario",Usu);
                    registro.put("Contrasena",contra);
                    bd.insert("usuarios",null,registro);
                    bd.close();
                    Toast.makeText(this,"Usuario registrado Correctamente",Toast.LENGTH_LONG).show();
                    finish();
                }

            }

        }

    }
}