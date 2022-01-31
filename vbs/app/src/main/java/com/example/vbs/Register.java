package com.example.vbs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private TextView textView, registerUser;
    private EditText editTextFullName, editTextPhone, editTextPassword, editTextEmail;
    private DatabaseReference Mreferans;
    private FirebaseAuth mAuth;
    private FirebaseUser muser;
    private HashMap<String,Object> Mdata,M2data;
    private FirebaseFirestore mFirestore;
    private boolean adkullanici=false,kullanicionline=false,cocukonline=false;
    private String cocukuid="";
    private Long tiklamazamani;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ///tam ekran yapabilmek icin
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        textView= (TextView) findViewById(R.id.createText);
        textView.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.registerBtn);
        registerUser.setOnClickListener(this);

        editTextFullName = (EditText) findViewById(R.id.fullName);
        editTextPhone = (EditText) findViewById(R.id.age);
        editTextEmail = (EditText) findViewById(R.id.Email);
        editTextPassword = (EditText) findViewById(R.id.password);
        // tabloları getirmekte
        Mreferans=FirebaseDatabase.getInstance().getReference();
        mFirestore= FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.createText:
                startActivity(new Intent(this, Login.class));
                break;
            case R.id.registerBtn:
                registerUser();
                break;
        }

    }

    private void registerUser() {
        String Email = editTextEmail.getText().toString();
        String sifre = editTextPassword.getText().toString();
        String fullName = editTextFullName.getText().toString();
        String phone = editTextPhone.getText().toString();

        if(fullName.isEmpty()){
            editTextFullName.setError("Kullanıcı Adını Dolurmayı Unutmayınız!");
            editTextFullName.requestFocus();
            return;
        }

        if(phone.isEmpty()){
            editTextPhone.setError("Telefon Numarasını Girmeyi Unutmayınız!");
            editTextPhone.requestFocus();
            return;
        }

        if(Email.isEmpty()){
            editTextEmail.setError("E-mail giriniz");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            editTextEmail.setError("Lütfen Doğru Bir Email Adresi Giriniz");
            editTextEmail.requestFocus();
            return;
        }
        if(fullName==Email){
            editTextFullName.setError("Kullanıcı Adı Mail İle Aynı Olamaz!");
            editTextFullName.requestFocus();
            return;
        }
        if(editTextPhone.length()!=10){
            editTextPhone.setError("Telefon Numaranız 10 Haneli Olmalıdır!   Örn:'5xx xxx xx xx' ");
            editTextPhone.requestFocus();
            return;
        }

        if (sifre.isEmpty()){
            editTextPassword.setError("Şifrenizi Boş Bırakmamalısınız!");
            editTextPassword.requestFocus();
            return;
        }

        if(sifre.length()<6){
            editTextPassword.setError("Şifreniz 6 Karakterden Uzun Olmalıdır!");
            editTextPassword.requestFocus();
            return;
        }


        mAuth.createUserWithEmailAndPassword(Email,sifre)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            muser = mAuth.getCurrentUser();

                            Mdata = new HashMap<>();
                            Mdata.put("kullaniciadi", fullName);
                            Mdata.put("sifre", sifre);
                            Mdata.put("Email", Email);
                            Mdata.put("adkullanici", adkullanici);
                            Mdata.put("Kullanicionline",kullanicionline);
                            Mdata.put("kullaniciid", muser.getUid());

                            mFirestore.collection("Kullanıcılar").document(muser.getUid())
                                    .set(Mdata).addOnCompleteListener(Register.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        ///Veli uid parçalanır


                                        for(int i=0;i<11;i++){

                                            char c =muser.getUid().charAt(i);
                                            cocukuid=cocukuid+c;
                                        }

                                        //
                                        M2data=new HashMap<>();
                                        M2data.put("cocukonline",cocukonline);
                                        M2data.put("cocukuid",cocukuid);
                                        M2data.put("konumsurealma",600);
                                        M2data.put("veliuid",muser.getUid());

                                        //COCUK GİRİS YAZMA
                                    mFirestore.collection("Kullanıcılar").document(muser.getUid()).collection("Cocukgiris").document(cocukuid)
                                            .set(M2data).addOnCompleteListener( new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                basarilikayit();
                                            }
                                            else{
                                                Toast.makeText(Register.this,"Kayıt İşlemi Baraşısız!",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                        Toast.makeText(Register.this,"Kayıt İşlemi Başarılı!",Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(Register.this,"Kayıt İşlemi Baraşısız!",Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });

                        }
                        else {
                            Toast.makeText(Register.this, "Bu E-mail Adresi Zaten Kullanılmaktadır!", Toast.LENGTH_LONG).show();
                        }

                        }


                });


    }
    public void basarilikayit(){
        Intent openpage=new Intent(this,Login.class);
        startActivity(openpage);
    }
   
}