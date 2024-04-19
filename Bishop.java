
public class Bishop extends Piece {

	public Bishop(PieceColor color, Position position) {
		super(color, position);
	}

	@Override
	public boolean isValidMove(Position newPosition, Piece[][] board) {
		int rowDiff = Math.abs(position.getRow() - newPosition.getRow());
		int colDiff = Math.abs(position.getColumn() - newPosition.getColumn());
		
		if(colDiff != rowDiff) {
			return false;
		}
		
		int rowStep = position.getRow() > newPosition.getRow() ? -1 : 1;
		int colStep = position.getColumn() > newPosition.getColumn() ? -1 : 1;
		
		for(int i = 1; i < rowDiff; i++) {
			if(board[position.getRow() + i * rowStep][position.getColumn() + i * colStep] != null) {
				return false;
			}
		}
		
		Piece des = board[newPosition.getRow()][newPosition.getColumn()];
		return des == null || des.color != this.color;
	}
	
}
