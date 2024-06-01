import fenfilemanagement.MoveReader;
import fenfilemanagement.MoveWriter;
import board.Board;
import movement.Move;
import movement.MoveTreeService;
import player.Bot;
import player.Opponent;
import player.Player;

public class Game {

    private Board board;

    private Bot bot;

    private Opponent opponent;

    private boolean isBotsTurn;

    private boolean winner;

    private MoveTreeService moveTreeService;

    public Game(boolean isBotWhite, boolean isSinglePlayer) {
        this.board = new Board();
        this.moveTreeService = new MoveTreeService();
        this.bot = new Bot(isBotWhite, moveTreeService);
        this.isBotsTurn = isBotWhite;
        this.opponent = new Opponent(!isBotWhite, isSinglePlayer);
    }

    public void runGame() throws InterruptedException {
        board.setupBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", bot.isWhite());
        //board.setupBoard("8/8/7p/7R/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", bot.isWhite());

        while (!board.isCheckMate()) {

            Move move;

            if (isBotsTurn) {
                move = bot.resolveMove(board);
                postMoveProcessing(bot, move);
                isBotsTurn = false;
            } else {
                move = opponent.resolveMove(board);
                postMoveProcessing(bot, move);
                isBotsTurn = true;
            }

            System.out.println(MoveReader.getOpponentMove(false));
            MoveWriter.writeMove("TEST");
            System.out.println("White wins");
        }

        if (winner) {
            System.out.println("You win");
        } else {
            System.out.println("Aaahhhhh ur out");
        }
    }

    private void postMoveProcessing(Player player, Move move) {
        board.updateBoard(move, player.isWhite());

        if (player.isBot()) {
            String fen = board.convertBoardToFen(isBotsTurn);
            MoveWriter.writeMove(fen);
        }
    }

    public boolean isBotsTurn() {
        return isBotsTurn;
    }

    public void setBotsTurn(boolean botsTurn) {
        isBotsTurn = botsTurn;
    }
}
