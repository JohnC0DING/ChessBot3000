import fenfilemanagement.MoveWriter;
import board.Board;
import movement.Move;
import movement.MoveTreeService;
import player.Bot;
import player.Opponent;
import player.Player;

public class Game {

    private Board board;

    private Bot player1;

    private Player player2;

    private boolean isBotsTurn;

    private boolean winner;

    private MoveTreeService moveTreeService;

    public Game(boolean isBotWhite, boolean isSinglePlayer, int searchDepth) {
        this.board = new Board();
        this.moveTreeService = new MoveTreeService(searchDepth);
        this.player1 = new Bot(isBotWhite, true, moveTreeService);
        this.isBotsTurn = isBotWhite;
        this.player2 = isSinglePlayer ? new Bot(!isBotWhite, false, moveTreeService) : new Opponent(!isBotWhite);
    }

    public void runGame() throws InterruptedException {
        board.setupBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", player1.isWhite());
        //board.setupBoard("kqqq4/8/8/8/8/8/8/4QQQK w KQkq - 0 1", player1.isWhite());

        while (!board.isCheckMate()) {

            if (isBotsTurn) {
                Object move =  player1.resolveMove(board);
                postMoveProcessing(player1, move);
                isBotsTurn = false;
            } else {
                Object move = player2.resolveMove(board);
                postMoveProcessing(player2, move);
                isBotsTurn = true;
            }

            System.out.println("\n\n\n\n\n\n\n\n\n\n");
            if (!isBotsTurn) {
                System.out.println("whites move");
            } else {
                System.out.println("blacks move");
            }
            System.out.println("\n------------------------------");
            for (int index = 63; index >= 0; index--) {
                String piece = board.findPieceByIndex(index).isPresent() ?
                        String.valueOf(board.getPieceByIndex(index).getCharacterRepresentation()) :
                        " ";
                System.out.print(piece + " | ");
                if ((index) % 8 == 0) {
                    System.out.println("\n------------------------------");
                }
            }
        }

        if (winner) {
            System.out.println("You win");
        } else {
            System.out.println("Aaahhhhh ur out");
        }
    }

    private void postMoveProcessing(Player player, Object move) {
        if (player instanceof Bot) {
            board.updateBoard((Move) move, player.isWhite());
            if (player2 instanceof Opponent){
                //If neither player is using the file, don't write to it
                String fen = board.convertBoardToFen(isBotsTurn);
                MoveWriter.writeMove(fen);
            }
        } else if (player instanceof Opponent){
            board.setupBoard((String) move, !player.isWhite());
        }
    }

    public boolean isBotsTurn() {
        return isBotsTurn;
    }

    public void setBotsTurn(boolean botsTurn) {
        isBotsTurn = botsTurn;
    }
}
