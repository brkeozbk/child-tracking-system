package com.example.vbs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.format.DateUtils;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class veli_menu extends AppCompatActivity {

    private FirebaseFirestore mFirestore;
    Button cikisbutton;
    private HashMap<String, Object> Mdata;
    private DocumentReference docreferans;//documentreferans bilinen bir id için değişiklik
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String velimail, velisifre;
    private Boolean kullanicionline = false;
    private Long tiklamazamani;
    private Button gotogoster,cocukbulmap,konumayarlari,hesapayar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veli_menu);

        Login login = new Login();
        cikisbutton = (Button) findViewById(R.id.cikis);
        gotogoster=(Button)findViewById(R.id.gotogoster);
        cocukbulmap=(Button) findViewById(R.id.cocukbulmap);
        konumayarlari=(Button) findViewById(R.id.konumayarlari);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();

        //Velinin bilgileri ALINMAKTADIR.
        velimail = Login.velimail;
        velisifre = Login.velisifre;



        cikisbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialoggoster();

            }
        });
        gotogoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uid_olsuturcocuksayfasi();
            }
        });
        cocukbulmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cocuksuanda_nerede();
            }
        });
        konumayarlari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                konumayarlari();
            }
        });




    }

    ////// burada hesaba erişim sağlar
    public void cikis_islem_gerceklestir() {

        if (!TextUtils.isEmpty(velimail) && !TextUtils.isEmpty(velisifre)) {

            mAuth.signInWithEmailAndPassword(velimail, velisifre)
                    .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            mUser = mAuth.getCurrentUser();
                            assert mUser != null;

                            /// verileri getirir alttaki fonksiyon
                            verileri_bul(mUser.getUid());

                        }
                    });
        }


    }

    public void verileri_bul(String uid) {
        docreferans = mFirestore.collection("Kullanıcılar").document(uid);
        docreferans.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {

                    veriyi_guncelle();
                    cikis();
                } else System.out.println("dosyalar yok");
            }
        });

    }

    public void veriyi_guncelle() {
        Mdata = new HashMap<>();
        Mdata.put("Kullanicionline", kullanicionline);
        assert mUser != null;
        belirtilen_veriyi_guncelle(Mdata, mUser.getUid());
    }

    ///gullennecekyeri bulmaktadır.
    public void belirtilen_veriyi_guncelle(HashMap<String, Object> hashMap, final String uid) {
        mFirestore.collection("Kullanıcılar").document(uid)
                .update(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(veli_menu.this, "Çıkış Başarılı!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }


    public void cikis() {
        Intent openpage = new Intent(this, Main.class);
        startActivity(openpage);
    }

    @Override
    public void onBackPressed() {

        dialoggoster();
    }


    ///kullanıcıya cikis yapmak istedigi dialog gösterilir
    public void dialoggoster() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Hesaptan Çıkış Yapılsın mı?");
        builder.setMessage("Hesabınızdan Çıkış Yapılacak Emin misiniz?");

        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                /// hesap çıkışı gerçekleştirilir
                cikis_islem_gerceklestir();
            }
        });

        builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
    public void uid_olsuturcocuksayfasi(){
        Intent openpage=new Intent(this,uid_olusturcocuk.class);
        startActivity(openpage);
    }
    public void cocuksuanda_nerede(){
        Intent openpage=new Intent(this,CurrentGoogleMap.class);
        startActivity(openpage);
    }
    public void konumayarlari(){
        Intent openpage=new Intent(this,kullaniciayar.class);
        startActivity(openpage);
    }


    ////////////AÇIK OLAN AKTİVİTİLERİ İZLEMEKTE VE UYGULAMANIN NE ZAMAN ÇALIŞMIŞ OLDUĞUNU BULMAKTA

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };



        }

