package piece;

public enum Pieces {
    p {
        @Override
        public Piece instantiate(boolean isBotWhite){
            return new Pawn(false, !isBotWhite);
        }
    },
    P{
        @Override
        public Piece instantiate(boolean isBotWhite) {
            return new Pawn(true, isBotWhite);
        }
    },
    n {
        @Override
        public Piece instantiate(boolean isBotWhite) {
            return null;
        }
    },
    N {
        @Override
        public Piece instantiate(boolean isBotWhite) {
            return null;
        }
    },
    b {
        @Override
        public Piece instantiate(boolean isBotWhite) {
            return new Bishop(false, !isBotWhite);
        }
    },
    B {
        @Override
        public Piece instantiate(boolean isBotWhite) {
            return new Bishop(true, isBotWhite);
        }
    },
    r {
        @Override
        public Piece instantiate(boolean isBotWhite) {
            return new Rook(false, !isBotWhite);
        }
    },
    R {
        @Override
        public Piece instantiate(boolean isBotWhite) {
            return new Rook(true, isBotWhite);
        }
    },
    q {
        @Override
        public Piece instantiate(boolean isBotWhite) {
            return new Queen(false, !isBotWhite);
        }
    },
    Q {
        @Override
        public Piece instantiate(boolean isBotWhite) {
            return new Queen(true, isBotWhite);
        }
    },
    k {
        @Override
        public Piece instantiate(boolean isBotWhite) {
            return null;
        }
    },
    K {
        @Override
        public Piece instantiate(boolean isBotWhite) {
            return null;
        }
    };

    public abstract Piece instantiate(boolean isBotWhite);
}
