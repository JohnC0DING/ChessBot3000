import fenfilemanagement.MoveReader;
import fenfilemanagement.MoveWriter;
import board.Board;
import movement.Move;
import player.Bot;
import player.Opponent;
import util.Pair;

public class Game {

    private Board board;

    private Bot bot;

    private Opponent opponent;

    private boolean isBotsTurn;

    private boolean winner;

    public Game(boolean isBotWhite, boolean isSinglePlayer) {
        this.board =  new Board();
        this.bot = new Bot(isBotWhite);
        this.isBotsTurn = isBotWhite;
        this.opponent = new Opponent(!isBotWhite,isSinglePlayer);
    }

    public void start() throws InterruptedException {
        //board.setupBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", bot.isWhite());
        board.setupBoard("8/8/7p/7R/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", bot.isWhite());

        while (!board.isCheckMate()){

            Pair<String, Move> fenToMove;

            if(isBotsTurn) {
                fenToMove = bot.makeMove(board);
                MoveWriter.writeMove(fenToMove.left());
                isBotsTurn = false;
            } else {
                fenToMove = opponent.makeMove(board);
                board.updateBoard(fenToMove.right());
                if (opponent.isBot()){
                    MoveWriter.writeMove(fenToMove.left());
                }
                isBotsTurn = true;
            }

            System.out.println(MoveReader.getOpponentMove(false));
            MoveWriter.writeMove("TEST");
            System.out.println("White wins");
        }

        if(winner){
            System.out.println("You win");
        } else {
            System.out.println("Aaahhhhh ur out");
        }
    }

    public boolean isBotsTurn() {
        return isBotsTurn;
    }

    public void setBotsTurn(boolean botsTurn) {
        isBotsTurn = botsTurn;
    }
}
