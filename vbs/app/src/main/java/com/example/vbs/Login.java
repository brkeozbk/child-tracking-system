package com.example.vbs;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {

    private FirebaseFirestore mFirestore;
    private TextView forgotpass,henuzhesabınyokmu,cocukuidgiris;
    private HashMap<String,Object> Mdata;
    private DatabaseReference Mreferans;
    private DocumentReference docreferans;//documentreferans bilinen bir id için değişiklik
    private EditText editmail, editpassword;
    private String txtpassword,txtmail;
    public String  kontrolad,kontrolonline;
    public Boolean kontrolad1,kontrolonline1;
    private Button loginbtn;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    public static String velimail,velisifre;
    public  boolean kullanicionline=true,kullaniciadd=true;
    private Long tiklamazamani;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ///tam ekran yapabilmek icin
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        ////Button ve textview action
        editpassword = (EditText) findViewById(R.id.editpassword);
        editmail = (EditText) findViewById(R.id.editusername);
        loginbtn =  findViewById(R.id.loginbtn);
        forgotpass= findViewById(R.id.forgotpass);
        cocukuidgiris=findViewById(R.id.cocukuidgiris);
        henuzhesabınyokmu=findViewById(R.id.henuzhesabınyokmu);

        mAuth = FirebaseAuth.getInstance();
        mFirestore=FirebaseFirestore.getInstance();
        mUser=mAuth.getCurrentUser();

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workLogin();
            }
        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotpass();
            }
        });
        henuzhesabınyokmu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                henuzhesabınyokmu();
            }
        });
        cocukuidgiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cocukuidilegiris();
            }
        });



    }

    public void workLogin() {
        txtmail = editmail.getText().toString();
        txtpassword = editpassword.getText().toString();

        if(txtmail.isEmpty()){
            editmail.setError("E-mail Adresi Boş Bırakılmamalıdır!");
            editmail.requestFocus();
            return;
        }


        if(!Patterns.EMAIL_ADDRESS.matcher(txtmail).matches()){
            editmail.setError("Lütfen Doğru Bir Email Adresi Giriniz");
            editmail.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(txtpassword)){
            editpassword.setError("Şifre Boş Bırakılmamalıdır");
            editpassword.requestFocus();
            return;
        }


        if (!TextUtils.isEmpty(txtpassword) && !TextUtils.isEmpty(txtmail)){
            mAuth.signInWithEmailAndPassword(txtmail, txtpassword)
                     .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    mUser = mAuth.getCurrentUser();
                    assert mUser != null;

                    /// verileri getirir alttaki fonksiyon
                    pullandcontrol(mUser.getUid());


                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Login.this,"Giriş Başarısız Bilgilerinizin Doğru Olduğundan Emin Olunuz!",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void pullandcontrol(String uid){
        docreferans=mFirestore.collection("Kullanıcılar").document(uid);
        docreferans.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {// kullanıcı bilgisi documentsnapshota geldi


                if(documentSnapshot.exists()) {

                   kontrolad=documentSnapshot.getData().get("adkullanici").toString();
                   kontrolonline=documentSnapshot.getData().get("Kullanicionline").toString();
                   kontrolonline1=Boolean.parseBoolean(kontrolonline);
                   kontrolad1 = Boolean.parseBoolean(kontrolad);


                    //kullanici Online kontrol
                    if(kontrolonline1==false){

                        //Yeni hesap mı kontrol-> yeni hesap ise reklam sayfaları çıkacak
                        if(kontrolad1==true){
                            velibilgileryolla();
                            velianasayfa();
                            kullanicionlineguncelle();
                        }
                        else if(kontrolad1==false){
                            reklam();
                            kullanicionlineguncelle();
                        }

                    }
                    else if(kontrolonline1==true){
                        Toast.makeText(Login.this,"Kullanıcı Zaten Online",Toast.LENGTH_SHORT).show();
                    }
                }
                    /* GİRİŞ YAPTIĞIM MAİLİN COLLECTIONUNU getirmekte
                         Mdata=(HashMap) documentSnapshot.getData();
                        for(Map.Entry data:Mdata.entrySet()){

                            System.out.println(data.getKey()+" = "+ data.getValue());
                        }*/
            }
        }).addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Login.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    //VERİLERİMİZİ GUNCELLEMEK İÇİN bu sayfada adkullanici ve online için yapılmıştır.
    public void dataguncelle(HashMap<String,Object>hashMap,final String uid){
        mFirestore.collection("Kullanıcılar").document(uid)
                .update(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this,"Hoşgeldiniz!",Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }
    public void kullanicionlineguncelle(){
        Mdata=new HashMap<>();
        Mdata.put("Kullanicionline",kullanicionline);
        Mdata.put("adkullanici",kullanicionline);
        assert mUser !=null;
        dataguncelle(Mdata,mUser.getUid());

    }


   ////VELİ BİLGİLERİ HER SAYFADA ÇEKİLEBİLİR
   void velibilgileryolla(){
       velimail=txtmail;
       velisifre=txtpassword;
   }
    ///sayfa degistirme fonksiyonları
    public void reklam(){
        Intent openpage=new Intent(this,slider_presentation.class);
        startActivity(openpage);
    }
    public void velianasayfa(){
        Intent openpage=new Intent(this,veli_menu.class);
        startActivity(openpage);

    }
    public void forgotpass(){
        Intent openpage=new Intent(this,sifremiunuttum.class);
        startActivity(openpage);
    }
    public void henuzhesabınyokmu(){
        Intent openpage=new Intent(this,Register.class);
        startActivity(openpage);
    }
    public void cocukuidilegiris(){
        Intent openpage=new Intent(this,cocukuidgiris.class);
        startActivity(openpage);
    }



}
