package com.example.mygame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;


public class MarginActivities extends AppCompatActivity implements TableFragment.FragmentHighScoreListener {
    private TableFragment tableFragment;
    private MappingFragment mappingFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_margin_activities);

         tableFragment = new TableFragment();
         mappingFragment = new MappingFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.add(R.id.upper_layout, tableFragment);
        fragmentTransaction.add(R.id.lower_layout, mappingFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onGameUserInfoSent(Winner user) {
        mappingFragment.getGameUserInfo(user);
    }


//    @Override
//    public void onInputASent(String input1, String input2) {
//        mappingFragment.updateText(input1, input2);
//    }

}
