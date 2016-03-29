/*Oscar Rojas
 * Kiera Crist
 * Othello Program 
 * CSC 242
 * 
 */

import java.util.ArrayList;


public class Move {
	private int x;
	private	int y;
	private int [][] board;
	private int flippedChips;
	private ArrayList<Move> chipsToFlip;
	
	public Move(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Move(int x, int y, int [][] board, int flippedChips, ArrayList<Move> chipsToFlip){
		this.x = x;
		this.y = y;
		this.board = board;
		this.chipsToFlip = new ArrayList<Move>();
		for(int i = 0; i < chipsToFlip.size(); i ++){
			this.chipsToFlip.add(chipsToFlip.get(i));
		}
		this.flippedChips = flippedChips;
	}
	
	public int [][] getBoardCopy(){
		int [][] temp = new int [8][8];
		
		for(int i = 0; i < this.board.length; i++){
			for(int j = 0; j < this.board[i].length; j++){
				temp [i][j] = this.board[i][j];
			}
		}
		
		return temp;
	}
	public void setBoard(int[][] board) {
		this.board = board;
	}

	public ArrayList<Move> getChipsToFlip() {
		return chipsToFlip;
	}

	public void setChipsToFlip(ArrayList<Move> chipsToFlip) {
		this.chipsToFlip = chipsToFlip;
	}

	public int[][] getBoard() {
		return board;
	}

	public int getFlippedChips() {
		return flippedChips;
	}

	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public String toString(){
			
			return getX() + " " + getY();
			
		
	}
	
	/* Used to determine if two move objects 
	 * are equal.
	 */
	public boolean isEqual(Move m){
		if((this.x == m.getX()) && (this.y == m.getY())){
			return true;
		}else{
			return false;
		}
	}

	

}
