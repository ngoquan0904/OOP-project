
public class Knight extends Piece {

	public Knight(PieceColor color, Position position) {
		super(color, position);
	}

	@Override
	public boolean isValidMove(Position newPosition, Piece[][] board) {
		int rowDiff = Math.abs(position.getRow() - newPosition.getRow());
		int colDiff = Math.abs(position.getColumn() - newPosition.getColumn());
		boolean isValidMove = (rowDiff == 1 && colDiff == 2) || (rowDiff == 2 && colDiff == 1);
		if(!isValidMove) return false;
		
		Piece des = board[newPosition.getRow()][newPosition.getColumn()];
		return des == null || des.color != this.color;
	}
	
}
