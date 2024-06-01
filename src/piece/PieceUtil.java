package piece;

import java.util.Objects;

public class PieceUtil {

    public static Piece createPieceFromCharacter(char c, boolean isBotWhite){
        return Pieces.valueOf(String.valueOf(c)).instantiate(isBotWhite);
    }
}
