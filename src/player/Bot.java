package player;

public class Bot {

    private boolean isWhite;

    public Bot(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public String makeMove(){
        //Construct tree of possible moves
            //for now create one level of the tree then pick at random
        //Evaluate moves
        //Convert to fen
        return "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1";
    }
}
