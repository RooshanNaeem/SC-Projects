package com.example.roosh.snakesandladders;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class Game extends AppCompatActivity {

    public static int HEIGHT;
    public static int WIDTH;
    public boolean computersTurn = false;
    boolean Climbhum = false;
    boolean computerClimb = false;
    private int humanPos;
    private int computerPos;
    private ArrayList<Snake> snakes;
    private ArrayList<ladders> ladders;

    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setBackgroundColor(Color.rgb(250, 250, 210));
        setContentView(R.layout.activity_display);
        int currentApiVersion = Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }

        final Point size = new Point();
        getWindow().getWindowManager().getDefaultDisplay().getSize(size);
        final RelativeLayout im = (RelativeLayout) findViewById(R.id.row2);
        im.post(new Runnable() {
            @Override
            public void run() {
                WIDTH = im.getWidth();
                HEIGHT = im.getHeight();
                Toast.makeText(getApplicationContext(), "Snakes and Ladders!", Toast.LENGTH_SHORT).show();


            }
        });

        // Create snakes
        snakes = new ArrayList<>();
        snakes.add(new Snake(17, 13));
        snakes.add(new Snake(52, 29));
        snakes.add(new Snake(62, 22));
        snakes.add(new Snake(57, 40));
        snakes.add(new Snake(88, 18));
        snakes.add(new Snake(95, 51));
        snakes.add(new Snake(97, 79));

        // Create ladders
        ladders = new ArrayList<>();
        ladders.add(new ladders(3, 21));
        ladders.add(new ladders(8, 30));
        ladders.add(new ladders(28, 84));
        ladders.add(new ladders(58, 77));
        ladders.add(new ladders(75, 86));
        ladders.add(new ladders(80, 100));
        ladders.add(new ladders(90, 91));


        humanPos = 0;
        computerPos = 0;

            final Button btn = (Button) findViewById(R.id.rollDiceButton);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!computersTurn && (computerPos != 100 && humanPos != 100) ) {
                        btn.setEnabled(false);
                        android.os.Handler han = new android.os.Handler();
                        han.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                findViewById(R.id.rollDiceButton).setEnabled(true);
                            }
                        }, 5000);
                        humanTurn();

                    }
                }
            });
        }


    private int rollDice() {
        Random rand = new Random();
        int n = rand.nextInt(6) + 1;

        ImageView img = (ImageView) findViewById(R.id.dice);
        if (n == 1) {
            img.setImageResource(R.drawable.dice1);
        }
        if (n == 2) {
            img.setImageResource(R.drawable.dice2);
        }

        if (n == 3) {
            img.setImageResource(R.drawable.dice3);
        }
        if (n == 4) {
            img.setImageResource(R.drawable.dice4);
        }
        if (n == 5) {
            img.setImageResource(R.drawable.dice5);
        }
        if (n == 6) {
            img.setImageResource(R.drawable.dice6);

        }
        return n;
    }

    public void humanTurn() {
        if(computersTurn == false) {
            int n = rollDice();

            Toast.makeText(getApplicationContext(), "You threw: " + n, Toast.LENGTH_SHORT).show();
            moveHuman(n);


            if ((n == 6 && humanPos <94) ) {
                Toast.makeText(getApplicationContext(), "Again your turn:", Toast.LENGTH_SHORT).show();
                computersTurn = false;
                // moveHuman(n);
            }
            else{
                computersTurn = true;
            }
        }
    }

    public void computersTurn() {
        if (computersTurn == true) {
            int n = rollDice();

            Toast.makeText(getApplicationContext(), "Computer threw: " + n, Toast.LENGTH_SHORT).show();
            moveComputer(n);

            if ((n == 6 && computerPos < 94) ) {
                Toast.makeText(getApplicationContext(), "Again Computer's turn:", Toast.LENGTH_SHORT).show();
                computersTurn = true;
                //moveComputer(n);
            }
            else{
                computersTurn = false;
            }
        }
    }

    public void moveHuman(int dice) {
        int oldPos = humanPos;
        humanPos += dice;

        if (humanPos == 100 && computerPos != 100) {
            Toast.makeText(getApplicationContext(), "Player1 Wins", Toast.LENGTH_SHORT).show();
            TextView humText = (TextView) findViewById(R.id.humanwin);
            humText.setText("Winner");
            humText.setTextSize(28);


        } else if (humanPos > 100) {
            Toast.makeText(getApplicationContext(), "Cant Move the player", Toast.LENGTH_SHORT).show();
            humanPos -= dice;
        } else {
            //TODO: Check for snakes and ladders
            for(ladders l : ladders){
                if(humanPos == l.start){
                    Toast.makeText(getApplicationContext(), "Climb to Ladder:", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Again your turn", Toast.LENGTH_SHORT).show();
                    Climbhum = true;

                    humanPos = l.end;

                }
                else{
                    for(Snake snake: snakes){
                        if(humanPos == snake.head){
                            Toast.makeText(getApplicationContext(), "Ouch! Bitten by Snake:", Toast.LENGTH_SHORT).show();
                            humanPos = snake.tail;
                        }
                    }
                }
            }
        }

        TextView txt = (TextView) findViewById(R.id.humanPosition);
        txt.setText("Player: " +String.valueOf(humanPos));
        moveOnScreen(R.id.humanPlayer, oldPos, humanPos);
    }

    public void moveComputer(int dice) {
        int oldPos = computerPos;
        computerPos += dice;

        if (computerPos == 100 && humanPos != 100) {
            Toast.makeText(getApplicationContext(), "Computer Wins", Toast.LENGTH_SHORT).show();
            TextView comText = (TextView) findViewById(R.id.compwin);
            comText.setText("Winner");
            comText.setTextSize(28);


        } else if (computerPos > 100) {
            Toast.makeText(getApplicationContext(), "Cant move the player", Toast.LENGTH_SHORT).show();
            computerPos -= dice;
        } else {
            // TODO: Check for snakes and ladders
            for(ladders l : ladders){
                if(computerPos == l.start){
                    Toast.makeText(getApplicationContext(), "Climb to Ladder", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Again Computer's Turn", Toast.LENGTH_SHORT).show();
                    computerClimb = true;
                    computerPos = l.end;
                }
                else{
                    for(Snake snake: snakes){
                        if(computerPos == snake.head){
                            Toast.makeText(getApplicationContext(), "Ouch! Bitten by Snake:", Toast.LENGTH_SHORT).show();
                            computerPos = snake.tail;
                        }
                    }
                }
            }
        }

        TextView txt = (TextView) findViewById(R.id.computerPosition);
        txt.setText("Computer: " +String.valueOf(computerPos));
        moveOnScreen(R.id.computerPlayer, oldPos, computerPos);
    }

    private void moveOnScreen(final int id, final int oldPosition, final int newPosition) {
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, 0);
        animation.setDuration(10);
        animation.setAnimationListener(new PlayerAnimator(findViewById(id), oldPosition, newPosition, this));
        animation.setFillAfter(true);
        findViewById(id).startAnimation(animation);
    }

    private class Snake {
        int head;
        int tail;

        Snake(int h, int t) {
            this.head = h;
            this.tail = t;
        }

    }

    private class ladders {
        int start;
        int end;

        ladders(int s, int e) {
            this.start = s;
            this.end = e;
        }
    }


}