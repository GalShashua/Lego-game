package com.example.mygame;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TheGame extends AppCompatActivity implements SensorEventListener {
    final int BOUND = 1600, THREAD_SLEEP_TIME = 20, NUM = 2000, NUM_OF_PATHS = 5;
    protected final static String scoreString =  "TOTAL_SCORE";
    private boolean timeForPrize = false , timeForBomb = false;
    private volatile boolean playing = true;
    private final String scoreLine;
    private Vibrator vibrator;
    private RelativeLayout layout;
    private ImageView player,plusFive, life1,life2, life3;
    private TextView score = null;
    private ImageView[] balls;
    private double points = 0;
    private int numberOfLives = 3;
    private ImageView[] lives, lives1;
    private ImageView livePrize,cakePrize;
    Random rand = new Random();

//
//
//    final int BOUND = 1600;
//    final int NUM = 2000;
//    final int THREAD_SLEEP_TIME = 20;
//    final int NUM_OF_PATHS = 5;
//    protected final static String scoreString = "TOTAL_SCORE";
//    private boolean timeForPrize = false;
//    private ImageView prize;
//    private final String scoreLine;
//    private Vibrator vibrator;
//    private RelativeLayout layout;
//    private ImageView player;
//    private TextView score = null;
//    private ImageView[] balls;
//    private volatile boolean playing = true;
//    private ImageView life1, life2, life3;
//    private double points = 0;
    private int countCollision = 0;
    ////---------------
    private SensorManager sensorManager;
    private Sensor accelerometer;

    ////------------

    public TheGame() {
        scoreLine = "SCORE: ";
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_game);
        initGame();
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(THREAD_SLEEP_TIME);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!playing)
                                    return;
                                render();
                            }
                        });
                    }
                } catch (InterruptedException ignored) {
                }
            }
        };
        t.start();
    }

    public void initGame() {
        this.layout = findViewById(R.id.big_layout);
        this.score = findViewById(R.id.scoreText);
        this.player = new ImageView(this);
        player.setImageResource(PlayerDetails.legoPlayer);
        player.setX(getScreenWidth()/2);
        player.setY(getScreenHeight()-50);
        player.setLayoutParams(new android.view.ViewGroup.LayoutParams(getScreenWidth()/5,getScreenWidth()/5));
        layout.addView(player);
        ///--------------------------
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(TheGame.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        ///------------
        createLives(PlayerDetails.legoLives);
        createLives(PlayerDetails.legoPlayer);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        createBalls();
    }

    private void render() {
        points = points + 0.02;
        int scoreCastInt = (int) points;
        if (scoreCastInt % 9 == 0 && scoreCastInt != 0 && numberOfLives < 3) {
            livePrize = createPrizes(R.drawable.hearts);
            timeForPrize = true;
        }
        if(scoreCastInt % 10 == 0 && scoreCastInt != 0){
            cakePrize = createPrizes(R.drawable.cake_slice);
            timeForBomb = true;
        }
        this.score.setText(scoreLine + scoreCastInt);
        changePosition();
        if (timeForPrize == true && numberOfLives < 3) {
            changePrizesPosition(livePrize);
            checkCollisionWithLives(livePrize);
        //    checkCollisionWithLives();
        }
        if(timeForBomb == true){
            changePrizesPosition(cakePrize);
            checkCollisionWithCakes(cakePrize);

            //   checkCollisionWithBomb();
        }
        for (ImageView ball : balls) {
            if (collision(player, ball) && ball.getVisibility() == View.VISIBLE) {
                countCollision++;
                Log.i("null" , "count collision " +countCollision);
                if (numberOfLives == 3) {
                    ball.setVisibility(View.INVISIBLE);
                    lives[0].setVisibility(View.INVISIBLE);
                    vibrator.vibrate(200);
                    numberOfLives = 2;
                    break;
                }
                if (numberOfLives == 2) {
                    ball.setVisibility(View.INVISIBLE);
                    lives[1].setVisibility(View.INVISIBLE);
                    vibrator.vibrate(200);
                    numberOfLives = 1;
                    break;
                }
                if (numberOfLives == 1) {
                    vibrator.vibrate(200);
                    ball.setVisibility(View.INVISIBLE);
                    lives[2].setVisibility(View.INVISIBLE);
                    numberOfLives = 0;
                    if(!isDestroyed()) {
                        Intent intent = new Intent(this, EndGame.class);
                        intent.putExtra(scoreString, scoreCastInt);
                        startActivity(intent);
                    }
//                    Intent intent = new Intent(this, EndGame.class);
//                    intent.putExtra(scoreString, scoreCastInt);
 //                   startActivity(intent);
                }
            }
        }
    }

    private void checkCollisionWithLives(ImageView img){
        if(collision(player, img) && img.getVisibility() == View.VISIBLE) {
            switch (numberOfLives) {
                case 1:
                    img.setVisibility(View.INVISIBLE);
                    lives[1].setVisibility(View.VISIBLE);
                    numberOfLives = 2;
                    break;
                case 2:
                    img.setVisibility(View.INVISIBLE);
                    lives[0].setVisibility(View.VISIBLE);
                    numberOfLives = 3;
                    break;
                default:
                    break;
            }
        }
    }

    private void checkCollisionWithCakes(ImageView img){
        if(collision(player, img) && img.getVisibility() == View.VISIBLE){
            ObjectAnimator aniY = ObjectAnimator.ofFloat(player, "y", 300f);
            aniY.setDuration(1000);
            ObjectAnimator alpha = ObjectAnimator.ofFloat(player, View.ALPHA, 1.0f, 0.0f);
            alpha.setDuration(1000);
            ObjectAnimator rotate = ObjectAnimator.ofFloat(player, "rotation", 0f, 360f);
            rotate.setDuration(1000);
            AnimatorSet aniSet = new AnimatorSet();
            aniSet.playTogether(rotate);
            aniSet.start();
            img.setVisibility(View.INVISIBLE);
            //add points to the game
            plusFive = new ImageView(this);
            plusFive.setImageResource(R.drawable.fivee);
            plusFive.animate().alpha(0f).setDuration(2000);
            plusFive.setX(getScreenWidth() / 3);
            plusFive.setY(layout.getHeight() / 3);
            layout.addView(plusFive);
            points = points + 5;
        }
    }

    private boolean collision(ImageView net, ImageView ball) {
        Rect BallRect = new Rect();
        ball.getHitRect(BallRect);
        Rect NetRect = new Rect();
        net.getHitRect(NetRect);
        return BallRect.intersect(NetRect);
    }

    public void changePosition() {
        float ballDownY = 10;
        for (ImageView imageView : balls) {
            imageView.setY(ballDownY + imageView.getY());
        }
        for (ImageView ball : balls) {
            if (ball.getY() > layout.getHeight()) {
                ball.setY(rand.nextInt(BOUND) - NUM);
            }
        }
    }

    public void changePrizesPosition(ImageView img) {
        float prizeDownY = 10;
        img.setY(prizeDownY + img.getY());
        if (img.getY() > layout.getHeight()) {
            timeForPrize = false;
        }
    }


    public int getScreenWidth() {
        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        return size.x;
    }
    public int getScreenHeight() {
        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public void createLives(int imageResource) {
        lives= new ImageView[3];
        for (int i=0; i<lives.length; i++) {
            lives[i] = new ImageView(this);
            lives[i].setImageResource(imageResource);
            lives[i].setY(0);
            lives[i].setX(getScreenWidth()-getScreenWidth()/9);
            lives[i].setLayoutParams(new android.view.ViewGroup.LayoutParams(getScreenWidth()/9,getScreenWidth()/9));
            layout.addView(lives[i]);
        }
        lives[2].setX(getScreenWidth()-1*getScreenWidth()/9);
        lives[1].setX(getScreenWidth()-2*getScreenWidth()/9);
        lives[0].setX(getScreenWidth()-3*getScreenWidth()/9);
    }

        public void createBalls() {
        int ball_size = getScreenWidth() / 9;
        balls = new ImageView[8];
        float curX = (((getScreenWidth() / NUM_OF_PATHS) / 2) - (ball_size / 2));
        for (int i = 0; i < balls.length; i++) {
            balls[i] = new ImageView(this);
            int[] imagesToFall= {R.drawable.chips, R.drawable.chicken, R.drawable.burger};
            balls[i].setImageResource(imagesToFall[rand.nextInt(imagesToFall.length)]);
            balls[i].setLayoutParams(new android.view.ViewGroup.LayoutParams(ball_size, ball_size));
            balls[i].setY(rand.nextInt(BOUND) - NUM);
            balls[i].setX(curX);
            layout.addView(balls[i]);
            curX = curX + (getScreenWidth() / NUM_OF_PATHS);
            if (i % NUM_OF_PATHS == 0) {
                curX = (((getScreenWidth() / NUM_OF_PATHS) / 2) - (ball_size / 2));
            }
        }
    }

    public ImageView createPrizes(int imageToShow) {
        int prize_size = getScreenWidth()/9;
        float positionPrize= (((getScreenWidth()/NUM_OF_PATHS)/2)-(prize_size/2));
        float nextPath=(getScreenWidth()/NUM_OF_PATHS);
        List<Float> givenList = Arrays.asList(positionPrize, positionPrize+nextPath, positionPrize+nextPath*2,positionPrize+nextPath*3,positionPrize+nextPath*4);
        float curX =  givenList.get(rand.nextInt(givenList.size()));
        ImageView prize = new ImageView(this);
        prize.setImageResource(imageToShow);
        prize.setLayoutParams(new android.view.ViewGroup.LayoutParams(prize_size, prize_size));
        prize.setY(rand.nextInt(BOUND ) - NUM);
        prize.setX(curX);
        layout.addView(prize);
        return prize;
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && PlayerDetails.moveWithSensors==true){
            int x = getScreenWidth() / 2 - (player.getWidth() / 2);
            x = x - 80*(int)event.values[0];
            Log.d(null, "position" + x);
            if(x > getScreenWidth()-player.getWidth()) {
                player.setX(getScreenWidth() - player.getWidth());
            }
            else if(x < 0) {
                player.setX(0);
            }
            else {
                player.setX(x);
            }
        }

}

        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_MOVE && event.getX() < getScreenWidth() - player.getWidth() && PlayerDetails.moveWithSensors==false) {
                player.setX(event.getX());
            }
            return true;
        }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
