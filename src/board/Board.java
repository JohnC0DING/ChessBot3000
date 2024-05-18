package board;

import piece.Piece;
import piece.PieceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class Board {

    public static final Integer EMPTY_TILE_SCORE = 0; //Surely there is better place for this

    private Optional<Piece>[] tiles = new Optional[64];

    private List<Integer> pieceLocations = new ArrayList<>();

    private boolean isCheckMate = false;

    public boolean isCheckMate() {
        return isCheckMate;
    }

    public void setCheckMate(boolean checkMate) {
        isCheckMate = checkMate;
    }

    public void updateBoard(String fen){
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
                tiles[tileIndex] = Optional.ofNullable(PieceUtil.getPieceFromCharacter(c));
                pieceLocations.add(tileIndex);
                tileIndex++;
            }
        }
    }

    public Optional<Piece>[] getTiles() {
        return tiles;
    }

    public List<Integer> getPieceLocations() {
        return pieceLocations;
    }

}
