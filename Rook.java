
public class Rook extends Piece{

	public Rook(PieceColor color, Position position) {
		super(color, position);
	}

	@Override
	public boolean isValidMove(Position newPosition, Piece[][] board) {
		if(newPosition.getColumn() == position.getColumn()) {
			int startRow = Math.min(newPosition.getRow(), position.getRow());
			int endRow = Math.max(newPosition.getRow(), position.getRow());
			for(int i = startRow + 1; i < endRow; i++) {
				if(board[i][position.getColumn()] != null) {
					return false;
				}
			}
		}
		else if(newPosition.getRow() == position.getRow()) {
			int startColumn = Math.min(newPosition.getColumn(), position.getColumn());
			int endColumn = Math.max(newPosition.getColumn(), position.getColumn());
			for(int i = startColumn + 1; i < endColumn; i++) {
				if(board[position.getRow()][i] != null) {
					return false;
				}
			}
		}
		else {
			return false;
		}
		
		Piece des = board[newPosition.getRow()][newPosition.getColumn()];
		return des == null || des.color != this.color;
	}
}
