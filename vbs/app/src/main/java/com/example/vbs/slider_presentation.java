package com.example.vbs;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.vbs.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import fragments.fragment_1;
import fragments.fragment_2;
import fragments.fragment_3;
import fragments.fragment_4;

public class slider_presentation extends AppCompatActivity {

    private ViewPager gviewPager;
    private ViewPagerAdapter gadapter;
    private TabLayout gtablayout;

    private void goruntule(){

        gviewPager=findViewById(R.id.main_activity_viewpager);
        gtablayout=findViewById(R.id.maintablelayout);
        gadapter= new ViewPagerAdapter(getSupportFragmentManager());

        // fragmentleri ve nameleri yani indicatoru return etmesini sagladık
        gadapter.addFragment(new fragment_1(),"  ");
        gadapter.addFragment(new fragment_2(),"  ");
        gadapter.addFragment(new fragment_3(),"  ");
        gadapter.addFragment(new fragment_4(),"  ");
        gviewPager.setAdapter(gadapter);
        gtablayout.setupWithViewPager(gviewPager);

    }
@Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_slider_presentation);

    //BACKGROUNDU TAM EKRAN Yapabilmek icin
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_slider_presentation);

    ///view pager cagrilir
    goruntule();
}

    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }
}