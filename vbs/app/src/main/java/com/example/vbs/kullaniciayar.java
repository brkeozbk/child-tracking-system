package com.example.vbs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
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

public class kullaniciayar extends AppCompatActivity {

    CheckBox onsn, yirmisn, otuzsn;
    private HashMap<String,Object> Mdata;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DocumentReference docreferans;
    private FirebaseFirestore mFirestore;
    private String velimail,velisifre;
    private String cocukuid="",kontrol="";
    private int dakika;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullaniciayar);

        onsn=(CheckBox) findViewById(R.id.onsn);
        yirmisn=(CheckBox) findViewById(R.id.yirmisn);
        otuzsn=(CheckBox) findViewById(R.id.otuzsn);

        mAuth = FirebaseAuth.getInstance();
        mFirestore=FirebaseFirestore.getInstance();
        mUser=mAuth.getCurrentUser();

        velimail=Login.velimail;
        velisifre=Login.velisifre;


    }
    private void cocukkonumsureguncelle(){
        if (!TextUtils.isEmpty(velimail) && !TextUtils.isEmpty(velisifre) ){
            mAuth.signInWithEmailAndPassword(velimail,velisifre)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                mUser = mAuth.getCurrentUser();
                                assert mUser != null;

                                for(int i=0;i<11;i++){

                                    char c =mUser.getUid().charAt(i);
                                    cocukuid=cocukuid+c;
                                }

                                if(dakika==600){
                                    cocukdakikaguncelle(dakika);
                                    Toast.makeText(kullaniciayar.this,"Çocuğunuzun Konumu 10 Dakika da Bir Güncellenecektir!",Toast.LENGTH_SHORT).show();

                                }
                                else if(dakika==1200){
                                    cocukdakikaguncelle(dakika);
                                    Toast.makeText(kullaniciayar.this,"Çocuğunuzun Konumu 20 Dakika da Bir Güncellenecektir!",Toast.LENGTH_SHORT).show();

                                }else{
                                    cocukdakikaguncelle(dakika);
                                    Toast.makeText(kullaniciayar.this,"Çocuğunuzun Konumu 30 Dakika da Bir Güncellenecektir!",Toast.LENGTH_SHORT).show();

                                }

                            }
                            else{
                                Toast.makeText(kullaniciayar.this,"Hata Oluştu Tekrar Deneyiniz",Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
        }
    }


    public void dataguncelle(HashMap<String,Object>hashMap,final String uid){
        mFirestore.collection("Kullanıcılar").document(mUser.getUid()).collection("Cocukgiris").document(cocukuid)
                .update(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(kullaniciayar.this,"Başarılı!",Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(kullaniciayar.this,"Sayfayı Yenilemeniz Gerekmektedir!",Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }
    public void cocukdakikaguncelle(int zaman){
        Mdata=new HashMap<>();
        Mdata.clear();
        Mdata.put("cocukzaman",zaman);
        dataguncelle(Mdata,mUser.getUid());

    }





    public void onCheckboxClicked(View view) {

        switch(view.getId()) {

            case R.id.onsn:
                dakika=600;
                cocukkonumsureguncelle();
                yirmisn.setChecked(false);
                otuzsn.setChecked(false);

                break;

            case R.id.yirmisn:

                dakika=1200;
                cocukkonumsureguncelle();
               onsn.setChecked(false);
                otuzsn.setChecked(false);

                break;

            case R.id.otuzsn:
                dakika=1800;
                cocukkonumsureguncelle();
                onsn.setChecked(false);
                yirmisn.setChecked(false);

                break;
        }
    }


}
