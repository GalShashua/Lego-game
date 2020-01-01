package com.example.mygame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

public class GameSettings extends AppCompatActivity {
    protected static boolean moveWithSensors=false;
    Switch switchCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
//        LinearLayout gallery = findViewById(R.id.players);
//        LayoutInflater inflater = LayoutInflater.from(this);
//        int[] images= {R.drawable.lego1, R.drawable.lego2,R.drawable.lego3,
//                R.drawable.lego4,R.drawable.lego5,R.drawable.lego6,R.drawable.lego7,R.drawable.lego8};
//
//        for(int i=0; i<8; i++){
//            View view = inflater.inflate(R.layout.item_player, gallery, false);
//            ImageView imageView = view.findViewById(R.id.imageView);
//            imageView.setImageResource(images[i]);
//            gallery.addView(view);
//        }
        switchCompat = findViewById(R.id.on_off_switch);
        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchCompat.isChecked()){
                    moveWithSensors=true;
                }
                else
                {
                    moveWithSensors=false;
                }
            }
        });


    }
}
