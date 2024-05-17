public class Bot implements Player{

    private boolean isWhite;

    public Bot(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public String makeMove(){
        return "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1";
    }
}
