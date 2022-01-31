package com.example.vbs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class MenuActivity extends AppCompatActivity {

    private Button gotolocasyon,gotomap, gotoset,cikisbutton;
    private HashMap<String, Object> Mdata;
    private DocumentReference docreferans;//documentreferans bilinen bir id için değişiklik
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;
    private String velimail, velisifre,cocukuid,kontrolonlinestr="";
    private Boolean cocukonline = false;
    private Boolean kontrolonline;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        gotolocasyon = (Button) findViewById(R.id.gotolocasyon);
        gotomap =(Button) findViewById(R.id.gotomap);
        gotoset =(Button) findViewById(R.id.gotoset);
        cikisbutton=(Button)findViewById(R.id.cikis);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();
        Toast.makeText(MenuActivity.this,"Giriş Başarılı",Toast.LENGTH_SHORT).show();

        //Velinin bilgileri ALINMAKTADIR.
        velimail = cocukuidgiris.txtvelimail;
        velisifre = cocukuidgiris.txtvelisifre;
        cocukuid=cocukuidgiris.cocukuidtext;


        gotolocasyon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGotolocasyon();
            }
        });
        gotomap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGotomap();
            }
        });
        gotoset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGotoset();
            }
        });
        cikisbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoggoster();
            }
        });
    }

    public void cikis_islem_gerceklestir(){

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
        docreferans = mFirestore.collection("Kullanıcılar").document(uid).collection("Cocukgiris").document(cocukuid);
        docreferans.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {
                    kontrolonlinestr=documentSnapshot.getData().get("cocukonline").toString();
                    kontrolonline=Boolean.parseBoolean(kontrolonlinestr);
                    veriyi_guncelle();
                    cikis();
                } else System.out.println("dosyalar yok");
            }
        });

    }
    public void veriyi_guncelle() {
        Mdata = new HashMap<>();
        if(kontrolonline==true){
            Mdata.put("cocukonline", cocukonline);
            assert mUser != null;
            belirtilen_veriyi_guncelle(Mdata, mUser.getUid());
        }
        else{

        }

    }

    public void belirtilen_veriyi_guncelle(HashMap<String, Object> hashMap, final String uid) {
        mFirestore.collection("Kullanıcılar").document(uid).collection("Cocukgiris").document(cocukuid)
                .update(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MenuActivity.this, "Çıkış Başarılı!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }
    //TELEFON GERİ TUŞUNA BASILMASI DURUMUNDA
    @Override
    public void onBackPressed() {

        dialoggoster();
    }

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

    public void setGotolocasyon(){
        Intent openpage=new Intent(this,getlocation.class);
        startActivity(openpage);
    }
    public void setGotomap(){
        Intent openpage=new Intent(this,CocukCurrentLocation.class);
        startActivity(openpage);
    }
    public void setGotoset(){
        Intent openpage=new Intent(this,UserLocation.class);
        startActivity(openpage);
    }
    public void cikis(){
        Intent openpage=new Intent(this,Main.class);
        startActivity(openpage);
    }
}