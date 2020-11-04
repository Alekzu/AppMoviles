package co.edu.unal.tictactoe;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import co.edu.unal.tictactoe.TicTacToeGame.*;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "dunno";
    // Represents the internal state of the game
    private TicTacToeGame mGame;
    private BoardView mBoardView;
    //private Settings mPrefs;
    SharedPreferences mPrefs;
    //classvariables
    private boolean mGameOver;
    private int humWon;
    private int andWon;
    private int tiesM;
    //sound variables
    private boolean mSoundOn = true;
    MediaPlayer mHumanMediaPlayer;
    MediaPlayer mComputerMediaPlayer;
    //online
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    GameStat gameOn = new GameStat(101, false, false, "alex", "juan", 0, 2);
    DocumentReference gameRef;
    DocumentSnapshot gameSnap;
    private String gameDoc = "ONGL8Jh7FaYKXG3Cgq4V";
    private String player1 = "GoOnline";
    private int playerNum = 0;
    private boolean playingOnline = false;
    private boolean playerTurn = false;


    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGame = new TicTacToeGame();
        //mPrefs = new Settings();
        mBoardView = (BoardView) findViewById(R.id.board);
        mBoardView.setGame(mGame);
        mBoardView.setOnTouchListener(mTouchListener);

        FloatingActionButton fab = findViewById(R.id.onlineP);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetOnline();
                Snackbar.make(view, "reset", Snackbar.LENGTH_LONG)
                       .setAction("Action", null).show();

            }
        });

        final Button button = findViewById(R.id.onlineButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                joinOnline();
            }
        });

        mInfoTextView = new TextView[4];
        mInfoTextView[0] = (TextView) findViewById(R.id.information);
        mInfoTextView[1] = (TextView) findViewById(R.id.scoreH);
        mInfoTextView[2] = (TextView) findViewById(R.id.scoreT);
        mInfoTextView[3] = (TextView) findViewById(R.id.scoreA);



        startNewGame();
        humWon = 0;
        andWon = 0;
        tiesM = 0;

        // Restore the scores from the persistent preference data source
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mSoundOn = mPrefs.getBoolean("sound", true);
        String difficultyLevel = mPrefs.getString("difficulty_level",
                getResources().getString(R.string.difficulty_harder));
        if (difficultyLevel.equals(getResources().getString(R.string.difficulty_easy)))
            mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
        else if (difficultyLevel.equals(getResources().getString(R.string.difficulty_harder)))
            mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
        else
            mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);


    }
    @Override
    protected void onResume() {
        super.onResume();

        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.mario_haha);
        mComputerMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.yoshi);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mHumanMediaPlayer.release();
        mComputerMediaPlayer.release();
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
            case R.id.settings:
                startActivityForResult(new Intent(this, Settings.class), 0);
                return true;
            case R.id.quit:
                showDialog(DIALOG_QUIT_ID);
                return true;
        }
        return false;

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CANCELED) {
            // Apply potentially new settings

            mSoundOn = mPrefs.getBoolean("sound", true);

            String difficultyLevel = mPrefs.getString("difficulty_level",
                    getResources().getString(R.string.difficulty_harder));

            if (difficultyLevel.equals(getResources().getString(R.string.difficulty_easy)))
                mGame.setDifficultyLevel(DifficultyLevel.Easy);
            else if (difficultyLevel.equals(getResources().getString(R.string.difficulty_harder)))
                mGame.setDifficultyLevel(DifficultyLevel.Harder);
            else
                mGame.setDifficultyLevel(DifficultyLevel.Expert);
        }
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
        mBoardView.invalidate();   // Redraw the board
        /* not needed with graphics
        // Reset all buttons
        for (int i = 0; i < mBoardButtons.length; i++) {
            mBoardButtons[i].setText("");
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }*/
        //if online
        if(playingOnline){
            softResetOnline();
            if(playerNum == 1)mInfoTextView[0].setText("You go first.");
            else mInfoTextView[0].setText("Rival goes first");
        }
        else{//if not online
            // Alternate who goes first
            if((humWon+andWon+tiesM)%2==0)
                mInfoTextView[0].setText("You go first.");
            else{
                mInfoTextView[0].setText("Android goes first.");
                int move = mGame.getComputerMove();
                setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                mComputerMediaPlayer.start(); //play computer move sound
            }
        }

    }


    // Listen for touches on the board
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {

            if (!playingOnline){
                // Determine which cell was touched
                int col = (int) event.getX() / mBoardView.getBoardCellWidth();
                int row = (int) event.getY() / mBoardView.getBoardCellHeight();
                int pos = row * 3 + col;

                if (!mGameOver && setMove(TicTacToeGame.HUMAN_PLAYER, pos)) {
                    gameSounds(TicTacToeGame.HUMAN_PLAYER);    // Play the sound effect

                    // If no winner yet, let the computer make a move
                    int winner = mGame.checkForWinner();
                    if (winner == 0) {
                        mInfoTextView[0].setText("It's Android's turn.");
                        int move = mGame.getComputerMove();
                        setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                        gameSounds(TicTacToeGame.COMPUTER_PLAYER); //play computer move sound
                        winner = mGame.checkForWinner();
                    }

                    if (winner == 0)
                        mInfoTextView[0].setText("It's your turn.");
                    else if (winner == 1) {
                        mInfoTextView[0].setText("It's a tie!");
                        tiesM++;
                        mInfoTextView[2].setText("  Ties:" + tiesM + "  ");
                        mGameOver = true;
                    } else if (winner == 2) {
                        //mInfoTextView[0].setText("You won!");
                        String defaultMessage = getResources().getString(R.string.result_human_wins);
                        mInfoTextView[0].setText(mPrefs.getString("victory_message", defaultMessage));
                        humWon++;
                        mInfoTextView[1].setText("Human:" + humWon);
                        mGameOver = true;
                    } else {
                        mInfoTextView[0].setText("Android won!");
                        andWon++;
                        mInfoTextView[3].setText("Android:" + andWon);
                        mGameOver = true;
                    }
                }
            }
            if (playingOnline && playerTurn){
                int col = (int) event.getX() / mBoardView.getBoardCellWidth();
                int row = (int) event.getY() / mBoardView.getBoardCellHeight();
                int pos = row * 3 + col;

                if (!mGameOver && setMove(TicTacToeGame.HUMAN_PLAYER, pos)) {
                    updateGame(pos);
                    gameSounds(TicTacToeGame.HUMAN_PLAYER);    // Play the sound effect
                    playerTurn = false;
                    realTimeUpdate();
                    // If no winner yet, let the computer make a move
                    int winner = mGame.checkForWinner();
                    if (winner == 0) {
                        mInfoTextView[0].setText("It's rival's turn.");
                    }
                    else if (winner == 1) {
                        mInfoTextView[0].setText("It's a tie!");
                        tiesM++;
                        mInfoTextView[2].setText("  Ties:" + tiesM + "  ");
                        mGameOver = true;
                        softResetOnline();
                    } else if (winner == 2) {
                        //mInfoTextView[0].setText("You won!");
                        String defaultMessage = getResources().getString(R.string.result_human_wins);
                        mInfoTextView[0].setText(mPrefs.getString("victory_message", defaultMessage));
                        humWon++;
                        mInfoTextView[1].setText("Human:" + humWon);
                        mGameOver = true;
                        softResetOnline();
                    } else {
                        mInfoTextView[0].setText("Rival won!");
                        andWon++;
                        mInfoTextView[3].setText("Android:" + andWon);
                        mGameOver = true;
                        softResetOnline();
                    }
                }

            }

// So we aren't notified of continued events when finger is moved
            return false;
        }
    };
    private boolean setMove(char player, int location) {
        if (mGame.setMove(player, location)) {
            mBoardView.invalidate();   // Redraw the board
            return true;
        }
        return false;
    }
    private void gameSounds(char player){
        if(mSoundOn) {
            if (player == 'X') mHumanMediaPlayer.start();    // Human player sound effect

            if (player == 'O') mComputerMediaPlayer.start(); //play computer move sound
        }
        return;
    }
    private void createGame(){
        //Timestamp timestamp = snapshot.getTimestamp("created_at");
        //java.util.Date date = timestamp.toDate();
        //db.collection("gameList").add(gameOn);
        gameOn.player1 = "Alex";

// Add a new document with a generated ID
        db.collection("gameList")
                .add(gameOn)
        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
    //register player as player 1 if no players, or player2 if already a player and sets game to full
    //starts listener
    private void joinOnline() {
        gameRef = db.collection("gameList").document(gameDoc);
        gameRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                gameOn = documentSnapshot.toObject(GameStat.class);
                playingOnline = true;
                if (gameOn.player1 == null || gameOn.player1 == "") {
                    playerNum = 1;
                    playerTurn = true;
                    db.collection("gameList").document(gameDoc)
                            .update(
                                    "player1", "jugador1"
                            );

                } else {
                    playerNum = 2;
                    playerTurn = false;
                    db.collection("gameList").document(gameDoc)
                            .update(
                                    "player2", "jugador2",
                                    "full", true
                            );
                    mInfoTextView[0].setText("It's rival's turn.");
                }
            }
        });
        realTimeUpdate();
    }
    private void realTimeUpdate(){
        gameRef = db.collection("gameList").document(gameDoc);
        gameRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                //if update is from rival, set rival move to board
                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    gameOn = snapshot.toObject(GameStat.class);
                    if((playerNum != gameOn.nextTurn) && gameOn.full && !playerTurn && (gameOn.nextTurn != -1)){
                        setMove(TicTacToeGame.COMPUTER_PLAYER, gameOn.move);
                        gameSounds(TicTacToeGame.COMPUTER_PLAYER); //play computer move sound
                        playerTurn = true;
                        //check winner
                        int winner = mGame.checkForWinner();
                        if (winner == 0) {
                            mInfoTextView[0].setText("It's your turn.");
                        }
                        else if (winner == 1) {
                            mInfoTextView[0].setText("It's a tie!");
                            tiesM++;
                            mInfoTextView[2].setText("  Ties:" + tiesM + "  ");
                            mGameOver = true;
                            softResetOnline();
                        } else if (winner == 2) {
                            //mInfoTextView[0].setText("You won!");
                            String defaultMessage = getResources().getString(R.string.result_human_wins);
                            mInfoTextView[0].setText(mPrefs.getString("victory_message", defaultMessage));
                            humWon++;
                            mInfoTextView[1].setText("Human:" + humWon);
                            mGameOver = true;
                            softResetOnline();
                        } else {
                            mInfoTextView[0].setText("Rival won!");
                            andWon++;
                            mInfoTextView[3].setText("Android:" + andWon);
                            mGameOver = true;
                            softResetOnline();
                        }
                    }
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }



    private void updateGame(int move){

        db.collection("gameList").document(gameDoc)
                .update(
                        "move", move,
                        "nextTurn",playerNum
                );

    }

    private void resetOnline(){
        if(playerNum == 2){
            playerTurn = false;

        }
        db.collection("gameList").document(gameDoc)
                .update(
                        "move", 0,
                        "nextTurn", -1,
                        "full", false,
                        "player1", null,
                        "player2", null

                );
    }
    private void softResetOnline(){
        db.collection("gameList").document(gameDoc)
                .update(
                        "move", 0,
                        "nextTurn", -1
                );
    }


}