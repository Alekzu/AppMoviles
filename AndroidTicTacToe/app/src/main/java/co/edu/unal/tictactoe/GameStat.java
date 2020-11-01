package co.edu.unal.tictactoe;

public class GameStat {
    int created;
    boolean finish;
    boolean full;
    String player1;
    String player2;
    int move;
    int nextTurn;

    public GameStat(int created, boolean finish, boolean full, String player1, String player2, int move, int nextTurn) {
        /*this.creado = creado;
        this.finish = finish;
        this.full = full;
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.mFBoard = mFBoard;*/
    }

    public GameStat() {
        /*this.creado = 000000;
        this.finish = false;
        this.full = false;
        this.jugador1 = "jugador1";
        this.jugador2 = "jugador2";
        this.mFBoard = "{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}";*/
    }

    public int getCreated() {
        return created;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public boolean isFull(){
        return full;
    }

    public boolean isFinish() {
        return finish;
    }

    public int getMove() { return move; }

    public int getNextTurn() { return nextTurn; }
}
