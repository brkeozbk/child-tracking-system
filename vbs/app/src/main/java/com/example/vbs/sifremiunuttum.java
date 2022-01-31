package com.example.vbs;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class sifremiunuttum extends AppCompatActivity {
    private Button mailgonder;
    private EditText mailadresiedit;
    private String mailstr;
    private FirebaseAuth mauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifremiunuttum);
        mailgonder=(Button)findViewById(R.id.button3);
        mailadresiedit=(EditText)findViewById(R.id.forgotmail);

        mailgonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mailgonder();
            }
        });

    }
    public void mailgonder(){
        mailstr=mailadresiedit.getText().toString();

       if(mailstr.isEmpty()){
            mailadresiedit.setError("E-mail Adresi Boş Bırakılmamalıdır!");
            mailadresiedit.requestFocus();
            return;
        }
        System.out.println(mailstr);


        if(!Patterns.EMAIL_ADDRESS.matcher(mailstr).matches()){
            mailadresiedit.setError("Lütfen Doğru Bir Email Adresi Giriniz");
            mailadresiedit.requestFocus();
            return;
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(mailstr)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(sifremiunuttum.this,"Şifrenizi Yenilemeniz İçin Mail Gönderilmiştir!",Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

}