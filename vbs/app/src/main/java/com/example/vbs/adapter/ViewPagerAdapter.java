package com.example.vbs.adapter;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.vbs.R;

import java.util.ArrayList;

import fragments.fragment_1;
import fragments.fragment_2;
import fragments.fragment_3;
import fragments.fragment_4;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> Fragmentlistesi;
    private ArrayList<String>isimlistesi;



   public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        Fragmentlistesi= new ArrayList<>();
        isimlistesi=new ArrayList<>();
    }

    ///fragmentleri toplayan diziyi return etmesini isteriz.
    @NonNull
    @Override
    public Fragment getItem(int position) {

       return Fragmentlistesi.get(position);
    }

    @Override
    public int getCount() {

       return Fragmentlistesi.size();
    }
    //fragmentleri ve stringleri eklemek için fonksiyon-> tABLAYOUTu da guncellemek icin stringleri  alıyoruz
    public void addFragment(Fragment fragment,String name){

       Fragmentlistesi.add(fragment);
       isimlistesi.add(name);
    }
//// fragmenti return ettigimiz gibi ismide retrun etmemiz gerekmekte.
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return isimlistesi.get(position);
    }
}
