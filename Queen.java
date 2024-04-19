
public class Queen extends Piece {
	public Queen(PieceColor color, Position position) {
		super(color, position);
	}

	@Override
	public boolean isValidMove(Position newPosition, Piece[][] board) {
		int rowDiff = Math.abs(newPosition.getRow() - position.getRow());
		int colDiff = Math.abs(newPosition.getColumn() - position.getColumn());
		
		boolean straight = (position.getRow() == newPosition.getRow()) || (position.getColumn() == newPosition.getColumn());
		boolean diagonal = rowDiff == colDiff;
		if(!straight && !diagonal) {
			return false;
		}
		
		int rowDirection = Integer.compare(newPosition.getRow(), position.getRow());
		int colDirection = Integer.compare(newPosition.getColumn(), position.getColumn());
		
		int currentRow = position.getRow() + rowDirection;
		int currentCol = position.getColumn() + colDirection;
		while(currentRow != newPosition.getRow() || currentCol != newPosition.getColumn()) {
			if(board[currentRow][currentCol] != null) {
				return false;
			}
			currentRow += rowDirection;
			currentCol += colDirection;
		}
		
		Piece des = board[newPosition.getRow()][newPosition.getColumn()];
		return des == null || des.getColor() != this.getColor();
	}
	
}
