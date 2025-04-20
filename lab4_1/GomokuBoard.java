import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class GomokuBoard extends Board {
    private boolean gameEnded = false;
    private Piece winner = Piece.EMPTY;

    public GomokuBoard() {
        super();
    }

    @Override
    protected void initializeBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                grid[row][col] = Piece.EMPTY; // 设置每个格子为空
            }
        }
        // 五子棋开始时没有初始棋子
    }

    @Override
    public boolean placePiece(int row, int col, Piece piece) {
        if (gameEnded) {
            return false; // 游戏已结束，不能再落子
        }
        
        if (isValidPosition(row, col) && grid[row][col] == Piece.EMPTY) {
            grid[row][col] = piece; // 放置棋子
            checkWin(row, col, piece); // 检查是否有玩家获胜
            return true;
        }
        return false; // 位置无效或已有棋子
    }

    /**
     * 检查指定的位置是否形成了五连子
     * 
     * @param row 行号
     * @param col 列号
     * @param piece 棋子类型
     */
    private void checkWin(int row, int col, Piece piece) {
        // 检查8个方向
        int[][] directions = {
            {1, 0}, {0, 1}, {1, 1}, {1, -1} // 横、竖、右斜、左斜
        };

        for (int[] direction : directions) {
            int count = 1; // 当前落子点算1个
            int dRow = direction[0];
            int dCol = direction[1];

            // 正向检查
            count += countSameInDirection(row, col, dRow, dCol, piece);
            // 反向检查
            count += countSameInDirection(row, col, -dRow, -dCol, piece);

            if (count >= 5) {
                gameEnded = true;
                winner = piece;
                return;
            }
        }
    }

    /**
     * 计算指定方向上相同棋子的数量
     * 
     * @param startRow 起始行
     * @param startCol 起始列
     * @param dRow 行方向增量
     * @param dCol 列方向增量
     * @param piece 棋子类型
     * @return 该方向上连续相同棋子的数量
     */
    private int countSameInDirection(int startRow, int startCol, int dRow, int dCol, Piece piece) {
        int count = 0;
        int currentRow = startRow + dRow;
        int currentCol = startCol + dCol;

        while (isValidPosition(currentRow, currentCol) && grid[currentRow][currentCol] == piece) {
            count++;
            currentRow += dRow;
            currentCol += dCol;
        }

        return count;
    }

    /**
     * 判断游戏是否结束
     * 
     * @return 如果游戏已结束返回true，否则返回false
     */
    public boolean isGameOver() {
        return gameEnded || isFull();
    }

    /**
     * 获取游戏结果，包括获胜者信息
     * 
     * @return 游戏结果字符串
     */
    public String getGameResult() {
        if (winner == Piece.BLACK) {
            return "黑棋（Player1）获胜！";
        } else if (winner == Piece.WHITE) {
            return "白棋（Player2）获胜！";
        } else if (isFull()) {
            return "棋盘已满，游戏平局！";
        } else {
            return "游戏尚未结束";
        }
    }

    @Override
    public void checkGameEnd(Player player1, Player player2) {
        if (isGameOver()) {
            System.out.println("游戏结束！" + getGameResult());
            
            clearScreen();
            display(player1, player2, player1, game.getCurrentBoardIndex()+1);
            System.out.println("游戏结束！" + getGameResult());
            
            // 等待用户确认
            System.out.println("按Enter键继续...");
            new Scanner(System.in).nextLine();
        }
    }

    @Override
    public void display(Player player1, Player player2, Player currentPlayer, int boardNumber) {
        clearScreen(); // 清屏
        System.out.println("当前棋盘编号：" + boardNumber + "（五子棋）"); // 显示当前棋盘编号和类型
        System.out.print("  ");
        for (char c = 'a'; c < 'a' + SIZE; c++) {
            System.out.print(c + " "); // 显示列标
        }
        System.out.println();
        
        List<String[]> gamesList = getRunningGamesList();
        
        for (int row = 0; row < SIZE; row++) {
            System.out.print((row + 1) + " "); // 显示行标
            for (int col = 0; col < SIZE; col++) {
                System.out.print(grid[row][col].getSymbol() + " "); // 显示棋子
            }
            
            // 在左侧显示玩家信息和当前游戏
            if (row == 0) {
                System.out.print("  ");
                if (boardNumber > 0) {
                    System.out.print("Game " + boardNumber + " (Gomoku)");
                }
            } else if (row == 1) {
                System.out.print("  Player1 [" + player1.getName() + "]");
                if (currentPlayer == player1) {
                    System.out.print(" " + player1.getPieceType().getSymbol());
                }
            } else if (row == 2) {
                System.out.print("  Player2 [" + player2.getName() + "]");
                if (currentPlayer == player2) {
                    System.out.print(" " + player2.getPieceType().getSymbol());
                }
            } else if (row == 3 && isGameOver()) {
                System.out.print("  " + getGameResult());
            }
            
            // 在右侧显示游戏列表
            if (row == 0) {
                System.out.print("\t\tGame List");
            } else if (row > 0 && row <= gamesList.size()) {
                int gameIndex = row - 1;
                String[] gameInfo = gamesList.get(gameIndex);
                
                // 根据索引调整制表位
                if (gameIndex < 2) { // 对于1和2项
                    System.out.print("\t" + gameInfo[0] + ". " + gameInfo[1]);
                } else { // 对于3及之后的项
                    System.out.print("\t\t\t" + gameInfo[0] + ". " + gameInfo[1]);
                }
            }
            
            System.out.println();
        }
        System.out.println();
    }
} 