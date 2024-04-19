import javax.swing.*;
import java.awt.*;
import java.awt.MultipleGradientPaint.ColorSpaceType;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class ChessGameGUI extends JFrame {
  private final ChessSquareComponent[][] squares = new ChessSquareComponent[8][8];
  private final ChessGame game = new ChessGame();

  private final Map<Class<? extends Piece>, String> pieceUnicodeMap = new HashMap<>() {
      {
          put(Pawn.class, "\u265F");
          put(Rook.class, "\u265C");
          put(Knight.class, "\u265E");
          put(Bishop.class, "\u265D");
          put(Queen.class, "\u265B");
          put(King.class, "\u265A");
      }
  };

  public ChessGameGUI() {
      setTitle("Chess Game");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLocation(300,5);
      setLayout(new GridLayout(8, 8));
      initializeBoard();
      addGameResetOption();
      pack();
      setVisible(true);	
  }

  private void initializeBoard() {
      for (int row = 0; row < squares.length; row++) {
          for (int col = 0; col < squares[row].length; col++) {
              final int finalRow = row;
              final int finalCol = col;
              ChessSquareComponent square = new ChessSquareComponent(row, col);
              square.addMouseListener(new MouseAdapter() {
                  @Override
                  public void mouseClicked(MouseEvent e) {
                      handleSquareClick(finalRow, finalCol);
                  }
              });
              add(square);
              squares[row][col] = square;
          }
      }
      refreshBoard();
  }

  private void refreshBoard() {
      ChessBoard board = game.getBoard();
      for (int row = 0; row < 8; row++) {
          for (int col = 0; col < 8; col++) {
              Piece piece = board.getPiece(row, col);
              if (piece != null) {
                  // If using Unicode symbols:
                  String symbol = pieceUnicodeMap.get(piece.getClass());
                  Color color = (piece.getColor() == PieceColor.WHITE) ? Color.WHITE : Color.BLACK;
                  squares[row][col].setPieceSymbol(symbol, color);
              } else {
                  squares[row][col].clearPieceSymbol();
              }
          }
      }
  }

  private void handleSquareClick(int row, int col) {
      boolean moveResult = game.handleSquareSelection(row, col);
      clearHighlights();
      checkGameState();
      if (moveResult) {
          refreshBoard();
          if (game.isCheckmate(game.getCurrentPlayerColor())) {
        	  GameOver();
          }
      } else if (game.isPieceSelected()) {
          highlightLegalMoves(new Position(row, col));
      }
      refreshBoard();
  }

  private void checkGameState() {
      PieceColor currentPlayer = game.getCurrentPlayerColor();
      boolean inCheck = game.isInCheck(currentPlayer);
      
      PieceColor diffPlayer = currentPlayer == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
      boolean diffInCheck = game.isInCheck(diffPlayer);
      if (inCheck) {
    	  Position currentPlayerPosition = game.findKingPosition(currentPlayer);
          squares[currentPlayerPosition.getRow()][currentPlayerPosition.getColumn()].setBackground(Color.RED);
      }
      if(diffInCheck) {
    	  Position diffPlayerPosition = game.findKingPosition(diffPlayer); 
          squares[diffPlayerPosition.getRow()][diffPlayerPosition.getColumn()].setBackground(Color.RED);
      }
  }

  private void highlightLegalMoves(Position position) {
      List<Position> legalMoves = game.getLegalMovesForPieceAt(position);
      for (Position move : legalMoves) {
          squares[move.getRow()][move.getColumn()].setBackground(Color.GREEN);
      }
  }

  private void clearHighlights() {
      for (int row = 0; row < 8; row++) {
          for (int col = 0; col < 8; col++) {
              squares[row][col].setBackground((row + col) % 2 == 0 ? new Color(200, 200, 200) : new Color(180, 76, 43));
          }
      }
  }

  private void addGameResetOption() {
      JMenuBar menuBar = new JMenuBar();
      JMenu gameMenu = new JMenu("Game");
      JMenuItem resetItem = new JMenuItem("Reset");
      resetItem.addActionListener(e -> resetGame());
      gameMenu.add(resetItem);
      menuBar.add(gameMenu);
      setJMenuBar(menuBar);
  }

  private void resetGame() {
      game.resetGame();
      refreshBoard();
      clearHighlights();
  }

  private void GameOver() {
	  		
          int response = JOptionPane.showConfirmDialog(this, "Would you like to play again?", "Game Over",
                  JOptionPane.YES_NO_OPTION);
          if (response == JOptionPane.YES_OPTION) {
              resetGame();
          } else {
              System.exit(0);
          }
  }

  public static void main(String[] args) {
      SwingUtilities.invokeLater(ChessGameGUI::new);
  }
}