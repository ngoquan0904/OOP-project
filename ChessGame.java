import java.util.List;
import java.util.ArrayList;
public class ChessGame {
	private ChessBoard board;
	private boolean whiteTurn = true;
	
	public ChessGame() {
		this.board = new ChessBoard();
	}
//	Add
	public ChessBoard getBoard() {
		return this.board;
	}
	
	public void resetGame() {
		this.board = new ChessBoard();
		this.whiteTurn = true;
	}
	public PieceColor getCurrentPlayerColor() {
	    return whiteTurn ? PieceColor.WHITE : PieceColor.BLACK;
	}

	private Position selectedPosition;

	public boolean isPieceSelected() {
		return selectedPosition != null;
	 }

	public boolean handleSquareSelection(int row, int col) {
	      if (selectedPosition == null) {
	          Piece selectedPiece = board.getPiece(row, col);
	          if (selectedPiece != null
	                  && selectedPiece.getColor() == (whiteTurn ? PieceColor.WHITE : PieceColor.BLACK)) {
	              selectedPosition = new Position(row, col);
	              return false;
	          }
	      } else {
//	    	  Move the selectedPosition piece 
	          boolean moveMade = makeMove(selectedPosition, new Position(row, col));
	          selectedPosition = null;
	          return moveMade;
	      }
	      return false; // if no piece was selected or move was not made
	  }
	 public boolean makeMove(Position start, Position end) {
		 PieceColor diffPlayerColor = (getCurrentPlayerColor() == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE);
		 Position kingPosition = findKingPosition(diffPlayerColor);
		 Piece movingPiece = board.getPiece(start.getRow(), start.getColumn());
		 if(movingPiece == null || movingPiece.getColor() != (whiteTurn ? PieceColor.WHITE : PieceColor.BLACK)) {
			 return false;
		 }
		 if(movingPiece.isValidMove(end, board.getBoard())) {
			 board.movePiece(start, end);
			 whiteTurn = !whiteTurn;
			 return true;
		 }
		 return false;
	 }
	 
//	 Check position on board
	 private boolean isPositionOnBoard(Position position) {
		 if(position.getRow() >= 0 && position.getRow() < board.getBoard().length 
				 && position.getColumn() >= 0 && position.getColumn() < board.getBoard()[0].length) {
			 return true;
		 }
		 return false;
	 }
	 
//	 find King's Position
	 public Position findKingPosition(PieceColor color) {
		 for(int i = 0; i < board.getBoard().length; i++) {
			 for(int j = 0; j < board.getBoard()[i].length;j++) {
				 Piece piece = board.getPiece(i, j);
				 if(piece instanceof King && piece.getColor() == color) {
					 return new Position(i, j);
				 }
			 }
		 }
		 throw new RuntimeException("King not found, which should never happen.");
	 }
	 
//	 Checking for King is in check
	 public boolean isInCheck(PieceColor kingColor) {
		 Position kingPosition = findKingPosition(kingColor);
		 for(int i = 0; i < board.getBoard().length; i++) {
			 for(int j = 0; j < board.getBoard()[i].length;j++) {
				 	Piece piece = board.getPiece(i, j);
				 	if(piece != null && piece.getColor() != kingColor 
				 		&& piece.isValidMove(kingPosition, board.getBoard())) {
				 			return true;
				 	}
				 }
			 }
		 return false;
	 }
	 public boolean isCheckmate(PieceColor kingColor) {
	      if (!isInCheck(kingColor)) {
	          return false;
	      }

	      Position kingPosition = findKingPosition(kingColor);
	      King king = (King) board.getPiece(kingPosition.getRow(), kingPosition.getColumn());

	      for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
	          for (int colOffset = -1; colOffset <= 1; colOffset++) {
	              if (rowOffset == 0 && colOffset == 0) {
	                  continue;
	              }
	              Position newPosition = new Position(kingPosition.getRow() + rowOffset,
	                      kingPosition.getColumn() + colOffset);

	              if (isPositionOnBoard(newPosition) && king.isValidMove(newPosition, board.getBoard())
	                      && !isInCheckAfterMove(kingColor, kingPosition, newPosition)) {
	                  return false;
	              }
	          }
	      }
	      return true;
	 }
	 
//	 Checking for King in Check after a piece move
	 private boolean isInCheckAfterMove(PieceColor kingColor, Position start, Position end) {
		 Piece temp = board.getPiece(end.getRow(), end.getColumn());
		 board.setPiece(end.getRow(), end.getColumn(), board.getPiece(start.getRow(), start.getColumn()));
		 board.setPiece(start.getRow(), start.getColumn(), null);
		 
		 boolean inCheck = isInCheck(kingColor);
		 
		 board.setPiece(start.getRow(), start.getColumn(), board.getPiece(end.getRow(), end.getColumn()));
		 board.setPiece(end.getRow(), end.getColumn(), temp);
		 return inCheck;
	 }
//	 Add
	 public List<Position> getLegalMovesForPieceAt(Position position) {
	      Piece selectedPiece = board.getPiece(position.getRow(), position.getColumn());
	      if (selectedPiece == null)
	          return new ArrayList<>();

	      List<Position> legalMoves = new ArrayList<>();
	      switch (selectedPiece.getClass().getSimpleName()) {
	          case "Pawn":
	              addPawnMoves(position, selectedPiece.getColor(), legalMoves);
	              break;
	          case "Rook":
	              addLineMoves(position, new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } }, legalMoves);
	              break;
	          case "Knight":
	              addSingleMoves(position, new int[][] { { 2, 1 }, { 2, -1 }, { -2, 1 }, { -2, -1 }, { 1, 2 }, { -1, 2 },
	                      { 1, -2 }, { -1, -2 } }, legalMoves);
	              break;
	          case "Bishop":
	              addLineMoves(position, new int[][] { { 1, 1 }, { -1, -1 }, { 1, -1 }, { -1, 1 } }, legalMoves);
	              break;
	          case "Queen":
	              addLineMoves(position, new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, { 1, 1 }, { -1, -1 },
	                      { 1, -1 }, { -1, 1 } }, legalMoves);
	              break;
	          case "King":
	              addSingleMoves(position, new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, { 1, 1 }, { -1, -1 },
	                      { 1, -1 }, { -1, 1 } }, legalMoves);
	              break;
	      }
	      return legalMoves;
	  }

	  private void addLineMoves(Position position, int[][] directions, List<Position> legalMoves) {
		  PieceColor diffPlayerColor = (getCurrentPlayerColor() == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE);
		  Position kingPosition = findKingPosition(diffPlayerColor);
	      for (int[] d : directions) {
	          Position newPos = new Position(position.getRow() + d[0], position.getColumn() + d[1]);
	          while (isPositionOnBoard(newPos)) {
	        	  if(newPos == kingPosition) {
	 	        	 System.out.println("Khong an vua");
	        	  }
	        	  else {
	        		  if (board.getPiece(newPos.getRow(), newPos.getColumn()) == null) {
		                  legalMoves.add(new Position(newPos.getRow(), newPos.getColumn()));
		                  newPos = new Position(newPos.getRow() + d[0], newPos.getColumn() + d[1]);
		              } else {
		                  if (board.getPiece(newPos.getRow(), newPos.getColumn()).getColor() != board
		                          .getPiece(position.getRow(), position.getColumn()).getColor()) {
		                      legalMoves.add(newPos);
		                  }
		                  break;
		              } 
	        	  }
	              
	          }
	      }
	  }

	  private void addSingleMoves(Position position, int[][] moves, List<Position> legalMoves) {
		  PieceColor diffPlayerColor = (getCurrentPlayerColor() == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE);
		  Position kingPosition = findKingPosition(diffPlayerColor);
	      for (int[] move : moves) {
	          Position newPos = new Position(position.getRow() + move[0], position.getColumn() + move[1]);
	          if(newPos == kingPosition) {
	 	        	 break;
	          }
	          if (isPositionOnBoard(newPos) && (board.getPiece(newPos.getRow(), newPos.getColumn()) == null ||
	                  board.getPiece(newPos.getRow(), newPos.getColumn()).getColor() != board
	                          .getPiece(position.getRow(), position.getColumn()).getColor())) {
	              legalMoves.add(newPos);
	          }
	      }
	  }

	  private void addPawnMoves(Position position, PieceColor color, List<Position> legalMoves) {
	      int direction = color == PieceColor.WHITE ? -1 : 1;
	      Position newPos = new Position(position.getRow() + direction, position.getColumn());
	      if (isPositionOnBoard(newPos) && board.getPiece(newPos.getRow(), newPos.getColumn()) == null) {
	          legalMoves.add(newPos);
	      }

	      if ((color == PieceColor.WHITE && position.getRow() == 6)
	              || (color == PieceColor.BLACK && position.getRow() == 1)) {
	          newPos = new Position(position.getRow() + 2 * direction, position.getColumn());
	          Position intermediatePos = new Position(position.getRow() + direction, position.getColumn());
	          if (isPositionOnBoard(newPos) && board.getPiece(newPos.getRow(), newPos.getColumn()) == null
	                  && board.getPiece(intermediatePos.getRow(), intermediatePos.getColumn()) == null) {
	              legalMoves.add(newPos);
	          }
	      }

	      int[] captureCols = { position.getColumn() - 1, position.getColumn() + 1 };
	      for (int col : captureCols) {
	          newPos = new Position(position.getRow() + direction, col);
	          if (isPositionOnBoard(newPos) && board.getPiece(newPos.getRow(), newPos.getColumn()) != null &&
	                  board.getPiece(newPos.getRow(), newPos.getColumn()).getColor() != color) {
	              legalMoves.add(newPos);
	          }
	      }
	  }
}
