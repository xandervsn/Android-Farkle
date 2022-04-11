/* Xander Siruno-Nebel
Java Programming
Galbraith
March 12, 2022
Android Farkle - an app that allows the user to play the game Farkle on android
 */

package com.example.androidfarkle;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //declarations
    ImageButton[] buttons = new ImageButton[6];
    int[] buttonState = new int[6];
    int[] dieImages = new int[6];
    int[] dieValue = new int[6];
    final int HOT_DIE = 0;
    final int SCORE_DIE = 1;
    final int LOCKED_DIE = 2;
    Button roll;
    Button score;
    Button stop;
    TextView currentScoreTV;
    TextView totalScoreTV;
    TextView currentRoundTV;
    int currentScore;
    int totalScore;
    int currentRound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttons[0] = (ImageButton)this.findViewById(R.id.imageButton1);
        buttons[1] = (ImageButton)this.findViewById(R.id.imageButton2);
        buttons[2] = (ImageButton)this.findViewById(R.id.imageButton3);
        buttons[3] = (ImageButton)this.findViewById(R.id.imageButton4);
        buttons[4] = (ImageButton)this.findViewById(R.id.imageButton5);
        buttons[5] = (ImageButton)this.findViewById(R.id.imageButton6);

        for (int a = 0; a < buttons.length; a++) {
            //lets user click click on dice
            buttons[a].setOnClickListener(this);
            buttons[a].setEnabled(false);
            buttons[a].setBackgroundColor(Color.LTGRAY);
        }

        roll = (Button)this.findViewById(R.id.button1);
        roll.setOnClickListener(this);
        score = (Button)this.findViewById(R.id.button2);
        score.setEnabled(false);
        score.setOnClickListener(this);
        stop = (Button)this.findViewById(R.id.button3);
        stop.setEnabled(false);
        stop.setOnClickListener(this);

        currentScoreTV = (TextView)this.findViewById(R.id.textView1);
        totalScoreTV = (TextView)this.findViewById(R.id.textView2);
        currentRoundTV = (TextView)this.findViewById(R.id.textView3);

        dieImages[0] = R.drawable.one;
        dieImages[1] = R.drawable.two;
        dieImages[2] = R.drawable.three;
        dieImages[3] = R.drawable.four;
        dieImages[4] = R.drawable.five;
        dieImages[5] = R.drawable.six;
    }

    @Override
    public void onClick(View v) {
        if(v.equals(roll)){
            //what happens on click of roll btn
            for (int a = 0; a < buttons.length; a++) {
                if(buttonState[a] == HOT_DIE){
                    //rolls dice
                    int choice = (int)(Math.random() * 6);
                    dieValue[a] = choice;
                    buttons[a].setImageResource(dieImages[choice]);
                    buttons[a].setEnabled(true);
                    roll.setEnabled(false);
                    score.setEnabled(true);
                    stop.setEnabled(false);
                }
            }
        }else if(v.equals(score)) {
            //score btn
            int[] valueCount = new int[7];
            for (int a = 0; a < buttonState.length; a++) {
                if (buttonState[a] == SCORE_DIE) {
                    valueCount[dieValue[a] + 1]++;
                }
            }
            if ((valueCount[2] > 0 && valueCount[2] < 3) ||
                    (valueCount[3] > 0 && valueCount[3] < 3) ||
                    (valueCount[4] > 0 && valueCount[4] < 3) ||
                    (valueCount[6] > 0 && valueCount[6] < 3)) {
                //if non usable dice are selected
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Invalid Die Selected");
                alertDialogBuilder
                        .setMessage("You can only select scoring dice.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            } else if(valueCount[1] == 0 &&
                    valueCount[2] == 0 &&
                    valueCount[3] == 0 &&
                    valueCount[4] == 0 &&
                    valueCount[5] == 0 &&
                    valueCount[6] == 0){
                //if no dice are pressed
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("No score!");
                alertDialogBuilder
                        .setMessage("Forfeit score and go to next round?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                currentScore = 0;
                                currentRound++;
                                currentScoreTV.setText("Current Score: " + currentScore);
                                currentRoundTV.setText("Current Round: " + currentRound);
                                resetDice();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
             }else{
                //scoring system:
                if(valueCount[1] < 3){
                    currentScore += ((valueCount[1] * 100));
                }
                if(valueCount[5] < 3){
                    currentScore += ((valueCount[5] * 50));
                }
                if(valueCount[1] >= 3){
                    currentScore += ((1000 * (valueCount[1] - 2)));
                }
                if(valueCount[2] >= 3){
                    currentScore += ((200 * (valueCount[2] - 2)));
                }
                if(valueCount[3] >= 3){
                    currentScore += ((300 * (valueCount[3] - 2)));
                }
                if(valueCount[4] >= 3){
                    currentScore += ((400 * (valueCount[4] - 2)));
                }
                if(valueCount[5] >= 3){
                    currentScore += ((500 * (valueCount[5] - 2)));
                }
                if(valueCount[6] >= 3){
                    currentScore += ((600 * (valueCount[6] - 2)));
                }
                currentScoreTV.setText("Current Score: " + currentScore);
                for (int a = 0; a < buttons.length; a++) {
                    //if a dice is scored
                    if(buttonState[a] == SCORE_DIE){
                        buttonState[a] = LOCKED_DIE;
                        buttons[a].setBackgroundColor(Color.BLUE);
                        buttons[a].setEnabled(false);
                    }
                }
                int lockedCount = 0;
                for (int a = 0; a < buttons.length; a++) {
                    //finds amount of locked dice
                    if(buttonState[a] == LOCKED_DIE){
                        lockedCount++;
                    }
                }
                if(lockedCount == 6){
                    //if all dice are locked, reset board
                    for (int a = 0; a < buttons.length; a++) {
                        buttonState[a] = HOT_DIE;
                        buttons[a].setBackgroundColor(Color.LTGRAY);
                    }
                }
                roll.setEnabled(true);
                score.setEnabled(false);
                stop.setEnabled(true);
            }
        }else if(v.equals(stop)){
            //stops the round
            totalScore += currentScore;
            currentScore = 0;
            currentScoreTV.setText("Current Score: " + currentScore);
            totalScoreTV.setText("Total Score: " + totalScore);
            currentRound++;
            currentRoundTV.setText("Current Round: " + currentRound);
            resetDice();
        }else{
            for (int a = 0; a < buttons.length; a++) {
                if(v.equals(buttons[a])){
                    if(buttonState[a] == HOT_DIE) {
                        buttons[a].setBackgroundColor(Color.RED);
                        buttonState[a] = SCORE_DIE;
                    }else{
                        buttons[a].setBackgroundColor(Color.LTGRAY);
                        buttonState[a] = HOT_DIE;
                    }
                }
            }
        }
    }

    private void resetDice() {
        //resets the dice
        for (int a = 0; a < buttons.length; a++) {
            buttons[a].setEnabled(false);
            buttonState[a] = HOT_DIE;
            buttons[a].setBackgroundColor(Color.LTGRAY);
        }
        roll.setEnabled(true);
        score.setEnabled(false);
        stop.setEnabled(false);
    }
}