package co.edu.unal.tictactoe;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import co.edu.unal.tictactoe.TicTacToeGame.*;

public class MainActivity extends AppCompatActivity {

    // Represents the internal state of the game
    private TicTacToeGame mGame;
    private boolean mGameOver;
    private int humWon;
    private int andWon;
    private int tiesM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        //gameButtons
        mBoardButtons = new Button[TicTacToeGame.BOARD_SIZE];
        mBoardButtons[0] = (Button) findViewById(R.id.one);
        mBoardButtons[1] = (Button) findViewById(R.id.two);
        mBoardButtons[2] = (Button) findViewById(R.id.three);
        mBoardButtons[3] = (Button) findViewById(R.id.four);
        mBoardButtons[4] = (Button) findViewById(R.id.five);
        mBoardButtons[5] = (Button) findViewById(R.id.six);
        mBoardButtons[6] = (Button) findViewById(R.id.seven);
        mBoardButtons[7] = (Button) findViewById(R.id.eight);
        mBoardButtons[8] = (Button) findViewById(R.id.nine);

        mInfoTextView = new TextView[2];
        mInfoTextView[0] = (TextView) findViewById(R.id.information);
        mInfoTextView[1] = (TextView) findViewById(R.id.score);

        mGame = new TicTacToeGame();

        startNewGame();
        humWon = 0;
        andWon = 0;
        tiesM = 0;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu); over
        super.onCreateOptionsMenu(menu);
        menu.add("New Game");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startNewGame();
        return true;

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);*/
    }

    // Buttons making up the board
    private Button mBoardButtons[];

    // Various text displayed
    private TextView[] mInfoTextView;

    //game logic
    private void startNewGame() {
        mGame.clearBoard();
        mGameOver = false;
        // Reset all buttons
        for (int i = 0; i < mBoardButtons.length; i++) {
            mBoardButtons[i].setText("");
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }
        // Alternate who goes first
        if((humWon+andWon+tiesM)%2==0)
        mInfoTextView[0].setText("You go first.");
        else{
            mInfoTextView[0].setText("Android goes first.");
            int move = mGame.getComputerMove();
            setMove(TicTacToeGame.COMPUTER_PLAYER, move);
        }
    }
    // Handles clicks on the game board buttons
    private class ButtonClickListener implements View.OnClickListener {
        int location;

        public ButtonClickListener(int location) {
            this.location = location;
        }

        public void onClick(View view) {
            if (mBoardButtons[location].isEnabled() && !mGameOver) {
                setMove(TicTacToeGame.HUMAN_PLAYER, location);

                // If no winner yet, let the computer make a move
                int winner = mGame.checkForWinner();
                if (winner == 0) {
                    mInfoTextView[0].setText("It's Android's turn.");
                    int move = mGame.getComputerMove();
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    winner = mGame.checkForWinner();
                }

                if (winner == 0)
                    mInfoTextView[0].setText("It's your turn.");
                else if (winner == 1) {
                    mInfoTextView[0].setText("It's a tie!");
                    mGameOver = true;
                    tiesM ++;
                    mInfoTextView[1].setText("Human: " + humWon + " Ties:" + tiesM + " Android:" + andWon);
                }
                else if (winner == 2) {
                    mInfoTextView[0].setText("You won!");
                    mGameOver = true;
                    humWon++;
                    mInfoTextView[1].setText("Human: " + humWon + " Ties:" + tiesM + " Android:" + andWon);
                }
                else {
                    mInfoTextView[0].setText("Android won!");
                    mGameOver = true;
                    andWon++;
                    mInfoTextView[1].setText("Human: " + humWon + " Ties:" + tiesM + " Android:" + andWon);
                }
            }
        }
    }
    private void setMove(char player, int location) {

        mGame.setMove(player, location);
        mBoardButtons[location].setEnabled(false);
        mBoardButtons[location].setText(String.valueOf(player));
        if (player == TicTacToeGame.HUMAN_PLAYER)
            mBoardButtons[location].setTextColor(Color.rgb(0, 200, 0));
        else
            mBoardButtons[location].setTextColor(Color.rgb(200, 0, 0));
    }



}