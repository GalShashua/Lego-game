package com.example.mygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PlayerDetails extends AppCompatActivity {
    protected static boolean moveWithSensors=false;
    Switch switchCompat;
    protected static int legoPlayer;
    protected static int legoLives;
    protected static String PlayerName;
    private EditText userNameEditText;
    private ImageView startAgain;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_details);
        userNameEditText = (EditText) findViewById(R.id.userName);
        startAgain = (ImageView) findViewById(R.id.start_game_btn);
        startAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGameActivity();
            }
        });
        LinearLayout gallery = findViewById(R.id.players);
        LayoutInflater inflater = LayoutInflater.from(this);
        final int[] keys= {R.drawable.lego1, R.drawable.lego2,R.drawable.lego3,
                R.drawable.lego4,R.drawable.lego5,R.drawable.lego6,R.drawable.lego7,R.drawable.lego8};
        final int[] values= {R.drawable.lego11, R.drawable.lego22,R.drawable.lego33,
                R.drawable.lego44,R.drawable.lego55,R.drawable.lego66,R.drawable.lego77,R.drawable.lego88};

        for(int i=0; i<8; i++){
            View view = inflater.inflate(R.layout.item_player, gallery, false);
            ImageView imageView = view.findViewById(R.id.imageView);
            imageView.setId(i);
            imageView.setImageResource(keys[i]);
            gallery.addView(view);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    legoPlayer = keys[v.getId()];
                    legoLives = values[v.getId()];
                }
            });
        }

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

    public void openGameActivity() {
        PlayerName = userNameEditText.getText().toString();
        Log.i("null" , "player name start " + PlayerName);
        Intent intent = new Intent(this, TheGame.class);
        startActivity(intent);
    }


}
