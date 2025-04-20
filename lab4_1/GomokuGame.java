public class GomokuGame {
    public static void main(String[] args) {
        Player player1 = new Player("玩家1", Piece.BLACK);
        Player player2 = new Player("玩家2", Piece.WHITE);
        Game game = new Game(player1, player2);
        game.start();
    }
} 