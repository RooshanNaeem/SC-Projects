package com.example.roosh.snakesandladders;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Created by roosh on 3/16/2017.
 */

class PlayerAnimator implements Animation.AnimationListener {

    private Game game;
    private View player;
    private int end;
    private int current;

    PlayerAnimator(View player, int startFrom, int endAt, Game game) {
        this.current = startFrom;
        this.end = endAt;
        this.player = player;
        this.game = game;
    }

    private int getX(int Position) {
        int col = Position % 10-1;
        col = (Position % 10 == 0) ? 10 : col;

        int row = Position / 10;
        col = (Position % 10 == 0) ? col - 1 : col;

        row = (Position % 10 == 0) ? row - 1 : row;

        col = (row % 2 != 0) ? (10 - col + 1-2) : col;

        return (int) (col / 10.f * Game.WIDTH);
    }

    private int getY(int Position) {
        int row = Position / 10;
        row = (Position % 10 == 0) ? row - 1 : row;
        row = row + 1;

        return Game.HEIGHT - (int) (row / 10.f * Game.WIDTH);

    }

    @Override
    public void onAnimationStart(Animation animation) {
        player.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (++current < end) {
            int x1 = getX(current);
            int y1 = getY(current);

            int x2 = getX(current + 1);
            int y2 = getY(current + 1);

            animation = new TranslateAnimation(x1, x2, y1, y2);
            animation.setDuration(200);
            animation.setFillAfter(true);

            animation.setAnimationListener(this);
            player.startAnimation(animation);
        } else {
           // game.computersTurn = !game.computersTurn;

            if (game.computersTurn==true) {
                android.os.Handler han = new android.os.Handler();
                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        game.computersTurn();
                    }
                };

                    han.postDelayed(run, 1000);

            }
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}

