package com.example.mygame;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Comparator;

public class EndGame extends AppCompatActivity {
    protected static double lat;
    protected static double longi;
    private static final int REQUEST_LOCATION=1;
  //  private EditText nickName_txt;
    private int getTotalScore;
    protected static String nickName;
    private Winner winner;
    LocationManager locationManager;
    String latitude, longitude;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
       // nickName_txt = findViewById(R.id.txt_input_name);
        getTotalScore = getIntent().getIntExtra(TheGame.scoreString, 0);
        TextView textScore = findViewById(R.id.game_over);
        textScore.setText("YOUR SCORE: "+ getTotalScore);
        ImageView play_again = findViewById(R.id.play_again);

        //add permission
        ActivityCompat.requestPermissions(this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            onGPS();
        }
        else {
            getLocation();
        }
        //
        play_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewGameActivity();
            }
        });
    }

    private void getLocation() {
        if(ActivityCompat.checkSelfPermission(EndGame.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(EndGame.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        }
        else {
            Location LocationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetWork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (LocationGps!=null)
            {
                lat=LocationGps.getLatitude();
                longi=LocationGps.getLongitude();
                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

            }
            else if (LocationNetWork!=null)
            {
                double lat=LocationNetWork.getLatitude();
                double longi=LocationNetWork.getLongitude();
                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);
            }
            else if (LocationPassive !=null)
            {
                double lat=LocationPassive.getLatitude();
                //  latCast=(int)lat;
                double longi=LocationPassive.getLongitude();
                //  longCast=(int)longi;

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);

            }
            else
            {
                Toast.makeText(this, "Cant Get Your Location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void onGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void openNewGameActivity() {
        addToWinnersList();
        Intent intent = new Intent(this, TheGame.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        addToWinnersList();
        Intent intent = new Intent(this, TheGame.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    private void addToWinnersList() {
        winner = new Winner();
        winner.setName(PlayerDetails.PlayerName);
        Log.i("null" , "player name end " + winner.getName());
        winner.setScore(getTotalScore);
        winner.setLatitude(latitude);
        winner.setLongitude(longitude);
        Winner.winners.add(winner);
        saveData();
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences" , MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(Winner.winners);
        editor.putString("task list", json);
        editor.apply();
    }

}
