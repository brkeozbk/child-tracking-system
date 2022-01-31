package com.example.vbs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.snapshot.BooleanNode;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class uid_olusturcocuk extends AppCompatActivity {
    private String velimail,velisifre,cocukuid="",guncelcocukid="";
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DocumentReference docreferans;
    private CollectionReference colreferans;
    private FirebaseFirestore mFirestore;
    private HashMap<String,Object> Mdata,M2data;
    private TextView text;
    private Button edittextguncelle;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uid_olusturcocuk);

        text=(TextView)findViewById(R.id.cocukgosterText);
        edittextguncelle=(Button)findViewById(R.id.cocukgosterButton);
        velimail=Login.velimail;
        velisifre=Login.velisifre;

        edittextguncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uidloging();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        mFirestore=FirebaseFirestore.getInstance();
        mUser=mAuth.getCurrentUser();

        }
    private void Uidloging(){

        if (!TextUtils.isEmpty(velimail) && !TextUtils.isEmpty(velisifre)) {

            mAuth.signInWithEmailAndPassword(velimail, velisifre)
                    .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            mUser = mAuth.getCurrentUser();
                            assert mUser != null;

                            /// verileri getirir alttaki fonksiyon
                            for(int i=0;i<11;i++){

                                char c =mUser.getUid().charAt(i);
                                cocukuid=cocukuid+c;
                            }
                            guncelcocukid=cocukuid;
                            text.setText(guncelcocukid);
                        }
                    });
        }

    }





}