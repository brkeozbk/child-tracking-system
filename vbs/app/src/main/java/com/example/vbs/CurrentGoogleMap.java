package com.example.vbs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CurrentGoogleMap extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap map;
    SupportMapFragment mapFragment;
    SearchView searchView;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DocumentReference docreferans;
    private Location location;
    private FirebaseFirestore mFirestore;
    private Double latitude,longitude;
    public  String velimail,velisifre,cocukuid="",bugun_tarih,adress,latstr,longstr;
    private HashMap<String,Object> Mdata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_google_map);
        velimail=Login.velimail;
        velisifre=Login.velisifre;
        bugun_tarih = DateFormat.getDateInstance().format(new Date());
        mAuth = FirebaseAuth.getInstance();
        mFirestore= FirebaseFirestore.getInstance();
        mUser=mAuth.getCurrentUser();
        Uidlogin();
        searchView = findViewById(R.id.sv_location);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = adress;
                List<Address> addressList = null;
                if(location!=null || !location.equals("")){
                    Geocoder geocoder = new Geocoder(CurrentGoogleMap.this);
                    try {
                        addressList= geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(latitude,longitude);
                    map.addMarker(new MarkerOptions().position(latLng).title(location));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mapFragment.getMapAsync(this);

    }
    private void Uidlogin(){
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
                                pulldata(mUser.getUid());
                            }
                            else{
                                Toast.makeText(CurrentGoogleMap.this,"Bilgilerin Doğru Olduğundan Emin Olunuz!",Toast.LENGTH_SHORT).show();
                            }
                        }


                    });
        }
    }
     public void pulldata(String uid){
         docreferans=mFirestore.collection("Kullanıcılar").document(mUser.getUid()).collection("Cocukgiris").document(cocukuid).collection("Cocukkonum").document(bugun_tarih);
         docreferans.get()
                 .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                     @Override
                     public void onSuccess(DocumentSnapshot documentSnapshot) {

                         latstr=documentSnapshot.getData().get("latitude").toString();
                         longstr=documentSnapshot.getData().get("longitude").toString();
                         adress=documentSnapshot.getData().get("Adress").toString();
                         latitude=Double.parseDouble(latstr);
                         longitude=Double.parseDouble(longstr);

                     }
                 }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 Toast.makeText(CurrentGoogleMap.this,"Çocuğunuz  Bugünlük Konum Hizmetini Kullanmadı!",Toast.LENGTH_SHORT).show();

             }
         });
     }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;


    }
}









