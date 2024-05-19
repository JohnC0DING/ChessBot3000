import fenfilemanagement.MoveReader;
import fenfilemanagement.MoveWriter;
import board.Board;
import player.Bot;
import player.Opponent;

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

            String fen;

            if(isBotsTurn) {
                fen = bot.makeMove(board);
                MoveWriter.writeMove(fen);
                isBotsTurn = false;
            } else {
                fen = opponent.makeMove(board);
                board.updateBoard(fen);
                if (opponent.isBot()){
                    MoveWriter.writeMove(fen);
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
