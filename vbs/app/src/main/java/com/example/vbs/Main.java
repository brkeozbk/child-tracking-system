package com.example.vbs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.auth.User;


public class Main extends AppCompatActivity {
    private Button girisbutton,kayitbutton;
    private long tiklamazamani;
    private TextView kullanicisartlari;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //BACKGROUNDU TAM EKRAN Yapabilmek icin
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //kayit olustur butonunu listen ile dinledim tıklandıgında login() çağrılacaktır.
        girisbutton = (Button) findViewById(R.id.button2);
        kayitbutton =(Button) findViewById(R.id.button1);
        kullanicisartlari=(TextView)findViewById(R.id.kullanicisozlesme);

        girisbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giris();
            }
        });
        kullanicisartlari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoggoster();
            }
        });
        kayitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 kayit();
            }
        });
    }


    @Override
    public void onBackPressed() {

        if (tiklamazamani + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Çıkmak İçin Tekrar Tıklayınız!", Toast.LENGTH_SHORT).show();
        }
        tiklamazamani = System.currentTimeMillis();

    }

    public void kayit(){
        Intent openpage=new Intent(this, Register.class);
        startActivity(openpage);
    }
    public void dialoggoster() {

        kullanicisozlesmesi kullanicisoz=new kullanicisozlesmesi();
        kullanicisoz.show(getSupportFragmentManager(),"example dialog");
    }
    public void giris(){
        Intent openpage=new Intent(this,Login.class);
        startActivity(openpage);
    }

}