package com.example.vbs;

import static com.example.vbs.cocukuidgiris.txtvelisifre;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class UserLocation extends AppCompatActivity {

    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 5;
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DocumentReference docreferans;
    private CollectionReference colreferans;
    private Location location;
    private FirebaseFirestore mFirestore;
    TextView tv_lat, tv_lon, tv_altitude, tv_accuracy, tv_speed, tv_sensor, tv_updates, tv_address;
    Switch sw_locationupdates, sw_gps;
    private String velimail="",velisifre="",cocukuid="",bugun_tarih="",adress="",konumalmasuresstr="";
    Date sistemsaati;
    private HashMap<String,Object> Mdata,M2data;
    private int konumalmasureint;
    private Double latitude,longitude,rakim;
    private float hiz;
    private int control=1;



    boolean updateOn = false;

    LocationRequest locationRequest;

    LocationCallback locationCallBack;

    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location);

        bugun_tarih = DateFormat.getDateInstance().format(new Date());
        tv_lat = findViewById(R.id.tv_lat);
        tv_lon = findViewById(R.id.tv_lon);
        tv_altitude = findViewById(R.id.tv_altitude);
        tv_accuracy = findViewById(R.id.tv_accuracy);
        tv_speed = findViewById(R.id.tv_speed);
        tv_sensor = findViewById(R.id.tv_sensor);
        tv_updates = findViewById(R.id.tv_updates);
        tv_address = findViewById(R.id.tv_address);
        sw_gps = findViewById(R.id.sw_gps);
        sw_locationupdates = findViewById(R.id.sw_locationsupdates);
        mAuth = FirebaseAuth.getInstance();
        mFirestore=FirebaseFirestore.getInstance();
        mUser=mAuth.getCurrentUser();
        velimail=cocukuidgiris.txtvelimail;
        velisifre= cocukuidgiris.txtvelisifre;
        cocukuid=cocukuidgiris.cocukuidtext;
        sistemsaati= Calendar.getInstance().getTime();
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000*DEFAULT_UPDATE_INTERVAL);
       // locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(1000);
        //locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);

        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        locationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null && locationResult.getLastLocation() != null) {
                    latitude = locationResult.getLastLocation().getLatitude();
                    longitude = locationResult.getLastLocation().getLongitude();
                    Uidlogin();
                    Log.d("LOCATION_UPDATE", latitude + ", " + longitude);
                }
                updateUIValues(locationResult.getLastLocation());
            }
        };

        sw_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw_gps.isChecked()) {


                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    tv_sensor.setText("Lokasyon alımı ");

                } else {
                    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                    tv_sensor.setText("wifi veya mobil baglantıyı acınız");

                }
            }
        });

        sw_locationupdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw_locationupdates.isChecked()) {
                    startLocationUpdates();
                } else {
                    stopLocationUpdates();
                }

            }
        });

        updateGPS();

    }

    //VERİTABANINA GİRİŞ
    private void Uidlogin(){
        if (!TextUtils.isEmpty(velimail) && !TextUtils.isEmpty(velisifre) ){
            mAuth.signInWithEmailAndPassword(velimail,velisifre)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mUser = mAuth.getCurrentUser();
                                assert mUser != null;
                                mFirestore.collection("Kullanıcılar").document(mUser.getUid()).collection("Cocukgiris").document(cocukuid).collection("Cocukkonum").document(bugun_tarih)
                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(documentSnapshot.exists()){
                                            kullanicionlineguncelle();

                                        }
                                        else {
                                            veritabaniyoksayarat();


                                        }

                                    }
                                });


                            }
                            else{
                                Toast.makeText(UserLocation.this,"Guncellemede Sorun Oluştu",Toast.LENGTH_SHORT).show();
                            }
                        }


                    });

        }
    }

    private void veritabaniyoksayarat(){
        M2data=new HashMap<>();
        M2data.put("Adress", adress);
        M2data.put("Hiz",hiz );
        M2data.put("Rakim",rakim);
        M2data.put("Saat", sistemsaati);
        M2data.put("latitude",latitude);
        M2data.put("longitude", longitude);

        mFirestore.collection("Kullanıcılar").document(mUser.getUid()).collection("Cocukgiris").document(cocukuid).collection("Cocukkonum").document(bugun_tarih)
                .set(M2data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(UserLocation.this,"Başarıyla Güncellendi",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
//////////KONUM ALMA HIZI VERİTABANINDAN CEKME
    private void konumalmahizicekme(String uid){


        docreferans=mFirestore.collection("Kullanıcılar").document(uid).collection("Cocukgiris").document(cocukuid);
        docreferans.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {// kullanıcı bilgisi documentsnapshota geldi


                        if(documentSnapshot.exists()) {

                            konumalmasuresstr=documentSnapshot.getData().get("cocukzaman").toString();
                            konumalmasureint = Integer.parseInt(konumalmasuresstr);

                        }

                    }
                });


    }
    //VERİLERİMİZİ GUNCELLEMEK İÇİN bu sayfada adkullanici ve online için yapılmıştır.
    public void dataguncelle(HashMap<String,Object>hashMap,final String uid){
        mFirestore.collection("Kullanıcılar").document(mUser.getUid()).collection("Cocukgiris").document(cocukuid).collection("Cocukkonum").document(bugun_tarih)
                .update(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(UserLocation.this,"Veriler Güncellendi!",Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Toast.makeText(UserLocation.this,"DOSYA YOK",Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }
    public void kullanicionlineguncelle(){
        Mdata=new HashMap<>();
        Mdata.put("Adress", adress);
        Mdata.put("Hiz",hiz );
        Mdata.put("Rakim",rakim);
        Mdata.put("Saat", sistemsaati);
        Mdata.put("latitude",latitude);
        Mdata.put("longitude", longitude);
        assert mUser !=null;
        dataguncelle(Mdata,mUser.getUid());

    }

    private void stopLocationUpdates() {
        tv_updates.setText("konum izlemesi durdu");
        tv_lat.setText("Konum izlemesi durduruldu");
        tv_lon.setText("Konum izlemesi durduruldu");
        tv_speed.setText("Konum izlemesi durduruldu");
        tv_address.setText("Konum izlemesi durduruldu");
        tv_accuracy.setText("Konum izlemesi durduruldu");
        tv_altitude.setText("Konum izlemesi durduruldu");
        tv_sensor.setText("Konum izlemesi durduruldu");

        fusedLocationProviderClient.removeLocationUpdates(locationCallBack);

    }

    private void startLocationUpdates() {
        tv_updates.setText("Konum izlenmektedir!");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
        updateGPS();



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSIONS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    updateGPS();
                }
                else {
                    Toast.makeText(this, "Uygulamaya Konum Alma İzni Verilmelidir!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }

    }

    private void updateGPS() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(UserLocation.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    updateUIValues(location);

                }
            });
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }
    }

    private void updateUIValues(Location location) {

        tv_lat.setText(String.valueOf(location.getLatitude()));
        tv_lon.setText(String.valueOf(location.getLongitude()));
        tv_accuracy.setText(String.valueOf(location.getAccuracy()));

        if (location.hasAltitude()) {
            tv_altitude.setText(String.valueOf(location.getAltitude()));
            rakim=location.getAltitude();
        }
        else {
            tv_altitude.setText("Şuanda Rakım Mevcut Değildir!");
        }
        if (location.hasSpeed()) {
            tv_speed.setText(String.valueOf(location.getSpeed()));
           hiz=location.getSpeed();
        }
        else {
            tv_speed.setText("Çocuğun Hızı Alınamamaktadır");
        }
        Geocoder geocoder = new Geocoder(UserLocation.this);

        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            tv_address.setText(addresses.get(0).getAddressLine(0));
            adress=addresses.get(0).getAddressLine(0);
        }
        catch (Exception e) {
            tv_address.setText("Adres Sokak Alınamamaktadır!");
        }
    }
}






























































