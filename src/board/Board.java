package board;

import movement.Move;
import piece.Piece;
import piece.PieceUtil;
import util.Pair;

import java.util.*;
import java.util.stream.IntStream;

public class Board {

    public static final Integer EMPTY_TILE_SCORE = 0; //Surely there is better place for this

    private Optional<Piece>[] tiles = new Optional[64];

    private List<Integer> pieceLocations = new ArrayList<>();

    private Set<Integer> whitePieceLocations = new HashSet<>();

    private Set<Integer> blackPieceLocations =  new HashSet<>();

    private boolean isCheckMate = false;

    public Board(Board board){
        tiles = board.getTiles();
        pieceLocations = board.getPieceLocations();
        whitePieceLocations = board.getWhitePieceLocations();
        blackPieceLocations = board.getBlackPieceLocations();
        isCheckMate = board.isCheckMate();
    }

    public void setupBoard(String fen, boolean isBotWhite){
        int tileIndex = 0;
        List<Character> fenList = fen.chars().mapToObj(c -> (char) c).toList();

        for (Character c : fenList) {
            if(c.equals(' ')) {
                return;
            } else if (c.equals('/')) {
                continue;
            } else if (Character.isDigit(c)) {
                int numOfBlankSpaces = Character.getNumericValue(c);
                IntStream.range(tileIndex, tileIndex + numOfBlankSpaces)
                        .forEach(index -> tiles[index] = Optional.empty());
                tileIndex += numOfBlankSpaces;
            } else {
                Piece piece = PieceUtil.createPieceFromCharacter(c, isBotWhite);
                tiles[tileIndex] = Optional.of(piece);

                cacheLocation(tileIndex, piece.isWhite());

                tileIndex++;
            }
        }
    }

    private void cacheLocation(int tileIndex, boolean isWhite){
        if (isWhite) {
            whitePieceLocations.add(tileIndex);
        } else {
            blackPieceLocations.add(tileIndex);
        }
        pieceLocations.add(tileIndex);
    }

    public Set<Integer> getFriendlyPieceLocations(boolean isWhite){
        return isWhite ? whitePieceLocations : blackPieceLocations;
    }

    public Set<Integer> getOpponentPieceLocations(boolean isWhite){
        return !isWhite ? whitePieceLocations : blackPieceLocations;
    }

    public void updateBoard(Move move){
        Optional<Piece> piece = tiles[move.getMove().left()];
        tiles[move.getMove().left()] = Optional.empty();
        tiles[move.getMove().right()] = piece;

        if(move.getCastleMove().isPresent()){
            Pair<Integer, Integer> castleMove = move.getCastleMove().get();
            piece = tiles[castleMove.left()];
            tiles[castleMove.left()] = Optional.empty();
            tiles[castleMove.right()] = piece;
        }
    }

    /**
     * Only ever call when you are certain there is something on this tile
     */
    public Piece getPieceByIndex(int index) {
        Optional<Piece> piece = tiles[index];
        return piece.orElseThrow();
    }

    public Optional<Piece> findPieceByIndex(int index) {
        return tiles[index];
    }

    public Optional<Piece>[] getTiles() {
        return tiles;
    }

    public List<Integer> getPieceLocations() {
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
//56 57 58 59 60 61 62 63
//48 49 50 51 52 53 54 55
//40 41 42 43 44 45 46 47
//32 33 34 35 36 37 38 39
//24 25 26 27 28 29 30 31
//16 17 18 19 20 21 22 23
//8  9  10 11 12 13 14 15
//0  1  2  3  4  5  6  7
