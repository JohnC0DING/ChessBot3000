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
        board.updateBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

        while (!board.isCheckMate()){

            //If is bots move
                //Move
                //Update board
                    //Check for checkmate and check
                //Update file
            //Else
                //Get opponents Move
                //Update board
                    //Check for checkmate and check

            String fen;

            if(isBotsTurn) {
                fen = bot.makeMove();
                MoveWriter.writeMove(fen);
                isBotsTurn = false;
            } else {
                fen = opponent.makeMove();
                if (opponent.isBot()){
                    MoveWriter.writeMove(fen);
                }
                isBotsTurn = true;
            }

            board.updateBoard(fen);

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
