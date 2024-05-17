import java.text.AttributedCharacterIterator;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Board {

    private char[] tiles = new char[64];

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
                        .forEach(index -> tiles[index] = ' ');
                tileIndex += numOfBlankSpaces;
            } else {
                tiles[tileIndex] = c;
                tileIndex++;
            }
        }
    }
}
