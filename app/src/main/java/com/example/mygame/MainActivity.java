package com.example.mygame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
//    protected static boolean moveWithSensors=false;
//    Switch switchCompat;
    protected static String PlayerName;
    private LinearLayout linearLayout;
    private ImageView setting_bth;
    private ImageView star_btn;
    private EditText userNameEditText;
    private Context mContext=this;

    private ImageView startAgain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        linearLayout = findViewById(R.id.main_layout);
        startAgain = (ImageView) findViewById(R.id.start_game_btn);
        userNameEditText = (EditText) findViewById(R.id.userName);

        startAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlayerDetails();
                //openGameActivity();
            }
        });
        star_btn = (ImageView) findViewById(R.id.star_btn);
        star_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTopPlayersActivity();
            }
        });
      //  if (userNameEditText.getText().toString().trim().length() > 0) {
         //   userNameEditText.setEnabled(false);

       // }
    }

    private void openPlayerDetails() {
        Intent intent = new Intent(this, PlayerDetails.class);
        startActivity(intent);
    }


//    public void openGameActivity() {
//        PlayerName = userNameEditText.getText().toString();
//        Log.i("null" , "player name start " + PlayerName);
//        Intent intent = new Intent(this, TheGame.class);
//        startActivity(intent);
//    }

    public void openSettingActivity() {
        Intent intent = new Intent(this, GameSettings.class);
        startActivity(intent);
    }

    public void openTopPlayersActivity() {
        Intent intent = new Intent(this, MarginActivities.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
