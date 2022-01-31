package com.example.vbs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;

public class cocukuidgiris extends AppCompatActivity {
    private EditText cocukuid,velimail,velisifre;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DocumentReference docreferans;
    private CollectionReference colreferans;
    private FirebaseFirestore mFirestore;
    private HashMap<String,Object> Mdata;
    private Button girisbutton;
    public static String cocukuidtext,txtvelimail,txtvelisifre;
    public Boolean cocukonline=true;
    private String kontrolonlinestr="";
    private Boolean kontrolonline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocukuidgiris);

        ///tam ekran yapabilmek icin
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cocukuidgiris);

        cocukuid=findViewById(R.id.cocukuidedittext);
        girisbutton=(Button)findViewById(R.id.uidgirisbutton);
        velimail=(EditText) findViewById(R.id.veliemail);
        velisifre=(EditText)findViewById(R.id.velikullanicisifre);

        mAuth = FirebaseAuth.getInstance();
        mFirestore=FirebaseFirestore.getInstance();
        mUser=mAuth.getCurrentUser();
        girisbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Uidlogin();
            }
        });


    }
    private void Uidlogin(){
        cocukuidtext = cocukuid.getText().toString();
        txtvelimail=velimail.getText().toString();
        txtvelisifre=velisifre.getText().toString();

        if (!TextUtils.isEmpty(txtvelimail) && !TextUtils.isEmpty(txtvelisifre) && !TextUtils.isEmpty(cocukuidtext) ){
            mAuth.signInWithEmailAndPassword(txtvelimail,txtvelisifre)
                    .addOnSuccessListener( new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            mUser = mAuth.getCurrentUser();
                            assert mUser != null;

                            /// verileri getirir alttaki fonksiyon
                            pulldata(mUser.getUid());
                        }
                    });
        }

    }


    ///Document Çekme
    public void pulldata(String uid){
        docreferans=mFirestore.collection("Kullanıcılar").document(uid).collection("Cocukgiris").document(cocukuidtext);
        docreferans.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                           cocukuidtext=documentSnapshot.getData().get("cocukuid").toString();
                           kontrolonlinestr=documentSnapshot.getData().get("cocukonline").toString();
                           kontrolonline=Boolean.parseBoolean(kontrolonlinestr);


                            ///GİRİŞ BAŞARILI İSE COCUK ONLİNE OLUR

                            if(kontrolonline==false){
                                basariligiris();
                                cocukonlineolsun();

                            }
                            else{
                                Toast.makeText(cocukuidgiris.this,"Çocuk Zaten Online",Toast.LENGTH_SHORT).show();

                            }



                        }
                        else{
                            Toast.makeText(cocukuidgiris.this,"Giriş Başarısız Bilgilerinizin Doğru Olduğundan Emin Olunuz!",Toast.LENGTH_SHORT).show();


                        }

                    }
                });

    }
    //BULUNAN DOCUMENTe cocuk durumun gunceller
    public void cocukonlineolsun(){
        Mdata=new HashMap<>();
        Mdata.put("cocukonline",cocukonline);
        assert mUser !=null;
        cocukonlineguncelle(Mdata,mUser.getUid());



    }


      //BELİRTİLEN DOCUMENTİ BULUR
    public void cocukonlineguncelle(HashMap<String,Object>hashMap,final String uid){
        mFirestore.collection("Kullanıcılar").document(mUser.getUid()).collection("Cocukgiris").document(cocukuidtext)
                .update(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(cocukuidgiris.this,"Hoşgeldiniz!",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    public void basariligiris(){
        Intent openpage=new Intent(this, MenuActivity.class);
        startActivity(openpage);
    }

}