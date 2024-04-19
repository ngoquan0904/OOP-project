import javax.swing.*;
import java.awt.*;

public class ChessSquareComponent extends JButton {
  private int row;
  private int col;

  public ChessSquareComponent(int row, int col) {
      this.row = row;
      this.col = col;
      initButton();
  }
  private void initButton() {
      setPreferredSize(new Dimension(100, 100));

      if ((row + col) % 2 == 0) {
          setBackground(new Color(200, 200, 200));
      } else {
          setBackground(new Color(180, 76, 43));
      }
      setHorizontalAlignment(SwingConstants.CENTER);
      setVerticalAlignment(SwingConstants.CENTER);
      setFont(new Font("Serif", Font.BOLD, 60));
  }

  public void setPieceSymbol(String symbol, Color color) {
      this.setText(symbol);
      this.setForeground(color);
  }

  public void clearPieceSymbol() {
      this.setText("");
  }
}