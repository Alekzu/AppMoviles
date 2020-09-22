package co.edu.unal.tictactoe;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import co.edu.unal.tictactoe.TicTacToeGame.*;

public class MainActivity extends AppCompatActivity {

    // Represents the internal state of the game
    private TicTacToeGame mGame;
    private boolean mGameOver;
    private int humWon;
    private int andWon;
    private int tiesM;

    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 1;


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

        mInfoTextView = new TextView[4];
        mInfoTextView[0] = (TextView) findViewById(R.id.information);
        mInfoTextView[1] = (TextView) findViewById(R.id.scoreH);
        mInfoTextView[2] = (TextView) findViewById(R.id.scoreT);
        mInfoTextView[3] = (TextView) findViewById(R.id.scoreA);

        mGame = new TicTacToeGame();

        startNewGame();
        humWon = 0;
        andWon = 0;
        tiesM = 0;

    }

    @Override
    @SuppressLint("RestrictedApi") //added to show icon
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu); over
        super.onCreateOptionsMenu(menu);
        //menu.add("New Game");
        //added
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        //end
        //show icons
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        //show icons


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*old
        startNewGame();
        return true;*/

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);*/
        //added with menu
        switch (item.getItemId()) {
            case R.id.new_game:
                startNewGame();
                return true;
            case R.id.ai_difficulty:
                showDialog(DIALOG_DIFFICULTY_ID);
                return true;
            case R.id.quit:
                showDialog(DIALOG_QUIT_ID);
                return true;
        }
        return false;

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch(id) {
            case DIALOG_DIFFICULTY_ID:

                builder.setTitle(R.string.difficulty_choose);

                final CharSequence[] levels = {
                        getResources().getString(R.string.difficulty_easy),
                        getResources().getString(R.string.difficulty_harder),
                        getResources().getString(R.string.difficulty_expert)};

                // TODO: Set selected, an integer (0 to n-1), for the Difficulty dialog.
                // selected is the radio button that should be selected.
                int selected = 2;

                builder.setSingleChoiceItems(levels, selected,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                dialog.dismiss();   // Close dialog

                                // TODO: Set the diff level of mGame based on which item was selected.
                                if(item == 0) mGame.setDifficultyLevel(DifficultyLevel.Easy);
                                if(item == 1) mGame.setDifficultyLevel(DifficultyLevel.Harder);
                                if(item == 2) mGame.setDifficultyLevel(DifficultyLevel.Expert);


                                // Display the selected difficulty level
                                Toast.makeText(getApplicationContext(), levels[item],
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                dialog = builder.create();

                break;
            case DIALOG_QUIT_ID:
                // Create the quit confirmation dialog

                builder.setMessage(R.string.quit_question)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainActivity.this.finish();
                            }
                        })
                        .setNegativeButton(R.string.no, null);
                dialog = builder.create();

                break;

        }

        return dialog;
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
                    tiesM ++;
                    mInfoTextView[2].setText("  Ties:" + tiesM + "  ");
                    mGameOver = true;
                }
                else if (winner == 2) {
                    mInfoTextView[0].setText("You won!");
                    humWon++;
                    mInfoTextView[1].setText("Human:" + humWon);
                    mGameOver = true;
                }
                else {
                    mInfoTextView[0].setText("Android won!");
                    andWon++;
                    mInfoTextView[3].setText("Android:" + andWon);
                    mGameOver = true;
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