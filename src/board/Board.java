package board;

import movement.Move;
import movement.PieceMoveUtil;
import piece.*;
import util.Pair;

import java.util.*;
import java.util.stream.IntStream;

public class Board {

    public static final Integer EMPTY_SQUARE_SCORE = 0; //Surely there is better place for this

    private Optional<Piece>[] squares = new Optional[64];

    private Set<Integer> pieceLocations = new HashSet<>();

    private Set<Integer> whitePieceLocations = new HashSet<>();

    private Set<Integer> blackPieceLocations = new HashSet<>();

    private boolean isCheckMate = false;

    public Board() {
    }

    public Board(Board board) {
        squares = board.getSquares().clone();
        pieceLocations = new HashSet<>(board.getPieceLocations());
        whitePieceLocations = new HashSet<>(board.getWhitePieceLocations());
        blackPieceLocations = new HashSet<>(board.getBlackPieceLocations());
        isCheckMate = board.isCheckMate();
    }

    public void setupBoard(String fen, boolean isBotWhite) {
        clearBoardState();
        int squareIndex = 0;
        List<Character> fenList = fen.chars().mapToObj(c -> (char) c).toList();

        for (Character c : fenList) {
            if (c.equals(' ')) {
                return;
            } else if (c.equals('/')) {
                continue;
            } else if (Character.isDigit(c)) {
                int numOfBlankSpaces = Character.getNumericValue(c);
                IntStream.range(squareIndex, squareIndex + numOfBlankSpaces)
                        .forEach(index -> squares[index] = Optional.empty());
                squareIndex += numOfBlankSpaces;
            } else {
                Piece piece = PieceUtil.createPieceFromCharacter(c, isBotWhite);
                squares[squareIndex] = Optional.of(piece);

                cacheLocation(squareIndex, piece.isWhite());

                squareIndex++;
            }
        }
    }

    private void clearBoardState() {
        squares = new Optional[64];
        pieceLocations = new HashSet<>();
        whitePieceLocations = new HashSet<>();
        blackPieceLocations = new HashSet<>();
    }

    public String convertBoardToFen(boolean isWhiteToMove) {
        StringBuilder fen = new StringBuilder();
        for (int row = 0; row < 8; row++) {
            int emptyCount = 0;
            for (int file = 0; file < 8; file++) {
                Optional<Piece> piece = squares[row * 8 + file];
                if (piece.isEmpty()) {
                    emptyCount++;
                } else {
                    if (emptyCount > 0) {
                        fen.append(emptyCount);
                        emptyCount = 0;
                    }
                    fen.append(piece.get().getCharacterRepresentation());
                }
            }
            if (emptyCount > 0) {
                fen.append(emptyCount);
            }
            if (row < 7) {
                fen.append('/');
            }
        }

        fen.append(' ');
        if (isWhiteToMove) {
            fen.append('w');
        } else {
            fen.append('b');
        }

        return fen.toString();
    }

    private void cacheLocation(int squareIndex, boolean isWhite) {
        if (isWhite) {
            whitePieceLocations.add(squareIndex);
        } else {
            blackPieceLocations.add(squareIndex);
        }
        pieceLocations.add(squareIndex);
    }

    public Set<Integer> getFriendlyPieceLocations(boolean isCallerWhite) {
        return isCallerWhite ? whitePieceLocations : blackPieceLocations;
    }

    public Set<Integer> getOpponentPieceLocations(boolean isCallerWhite) {
        return !isCallerWhite ? whitePieceLocations : blackPieceLocations;
    }

    public void updateBoard(Move move, boolean isWhite) {
        Optional<Piece> piece = squares[move.getMove().left()];
        squares[move.getMove().left()] = Optional.empty();
        squares[move.getMove().right()] = piece;

        if(piece.isPresent() && piece.get() instanceof Castleable){
            ((Castleable) piece.get()).setHasMoved(true);
        }

        getFriendlyPieceLocations(isWhite).remove(move.getMove().left());
        getFriendlyPieceLocations(isWhite).add(move.getMove().right());
        getOpponentPieceLocations(isWhite).remove(move.getMove().right());

        if (move.getCastleMove().isPresent()) {
            Pair<Integer, Integer> castleMove = move.getCastleMove().get();
            piece = squares[castleMove.left()];
            squares[castleMove.left()] = Optional.empty();
            squares[castleMove.right()] = piece;

            if(piece.isPresent() && piece.get() instanceof Castleable){
                ((Castleable) piece.get()).setHasMoved(true);
            }

            getFriendlyPieceLocations(isWhite).remove(castleMove.left());
            getFriendlyPieceLocations(isWhite).add(castleMove.right());
        } else if (isPawnAdvancingToQueen(move, piece)){
            squares[move.getMove().right()] = Optional.of(new Queen(piece.get().isWhite(), piece.get().isFriendly()));
        }
    }

    private boolean isPawnAdvancingToQueen(Move move, Optional<Piece> piece) {
        return piece.isPresent() && piece.get() instanceof Pawn && PieceMoveUtil.isPawnQueenable(move.getMove().right(), piece.get());
    }

    /**
     * Only ever call when you are certain there is something on this square
     */
    public Piece getPieceByIndex(int index) {
        Optional<Piece> piece = squares[index];
        return piece.orElseThrow();
    }

    public Optional<Piece> findPieceByIndex(int index) {
        return squares[index];
    }

    public Optional<Piece>[] getSquares() {
        return squares;
    }

    public Set<Integer> getPieceLocations() {
        return pieceLocations;
    }

    public Set<Integer> getWhitePieceLocations() {
        return whitePieceLocations;
    }

    public Set<Integer> getBlackPieceLocations() {
        return blackPieceLocations;
    }


    public boolean isCheckMate() {
        return isCheckMate;
    }

    public void setCheckMate(boolean checkMate) {
        isCheckMate = checkMate;
    }

}

//Board layout
//         Q  K
//56 57 58 59 60 61 62 63 White
//48 49 50 51 52 53 54 55
//40 41 42 43 44 45 46 47
//32 33 34 35 36 37 38 39
//24 25 26 27 28 29 30 31
//16 17 18 19 20 21 22 23
//8  9  10 11 12 13 14 15
//0  1  2  3  4  5  6  7  Black
//         q  k
