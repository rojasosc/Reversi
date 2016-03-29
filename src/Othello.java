/*Oscar Rojas
 * Kiera Crist
 * Othello Program 
 * CSC 242
 * 
 */

import java.util.Scanner;
import java.util.StringTokenizer;


public class Othello {
	
	private static int [][] board;
	private static State gameState;
	private static Scanner input;
	private static int depth; 
	public static void main(String [] args){
		
		input = new Scanner(System.in);
		
		String stdin = input.nextLine();
		char c = stdin.charAt(5);
		StringTokenizer toke = new StringTokenizer(stdin.substring(9));
		int time = Integer.parseInt(toke.nextToken());
		
		
		
		switch (time) {
		case 1000:
			depth = 4;
			break;
		case 2000:
			depth = 4;
		case 4000:
			depth = 5;
			break;
		case 16000: 
			depth = 6;
			break;
		case 60000: 
			depth = 8; 
			break; 
		case 240000: 
			depth = 10;
			break;
		default: 
			depth = 4; 
			break;
		}
		if((c == 'B') || c == 'b' ){
			/*initialize the game
			 *Fill the board with 0s and then place the first four chips */
			board = new int [8][8];
			for(int i = 0; i < board.length; i++){
				for(int j = 0; j < board[i].length; j++){
					board[i][j] = 0;
				}
			}
			
			board [3][3] = -1;
			board [4][3] = 1;
			board [4][4] = -1;
			board [3][4] = 1;
			
			gameState = new State(board,1,depth);
			
			
			// We play as max. Serve first move.
			while(!gameState.isOver()){
				 
				serveMove(1);
				// Read opponents move.
				// Try to match the opponents move
				// to generate the new board state.
				
				Move oppMove = requestOppMove();
				
				if(oppMove == null){
					gameState = new State(gameState.copyBoard(),1,depth);
					continue;
				}else{
					matchMove(oppMove,-1);
				}
				
				
			}

		}else{
			/*initialize the game
			 *Fill the board with 0s and then place the first four chips */
			board = new int [8][8];
			for(int i = 0; i < board.length; i++){
				for(int j = 0; j < board[i].length; j++){
					board[i][j] = 0;
				}
			}
			
			
			board [3][3] = -1;
			board [4][3] = 1;
			board [4][4] = -1;
			board [3][4] = 1;
			
			gameState = new State(board,1,depth);
	
			// Read opponents move.
			// Try to match the opponents move
			// to generate the new board state.
			//System.out.println("Current Board");

			while(!gameState.isOver()){
				Move oppMove = requestOppMove();
				if(oppMove == null){
					gameState = new State(gameState.copyBoard(),-1,depth);
					serveMove(-1);
					continue;
				}else{
					matchMove(oppMove, 1);
					serveMove(-1);
				}

			}

			
		}
		
	}
	
	public static void printBoard(){
		for(int i = 0; i < board.length; i++){
			
			for(int j = 0; j < board[i].length; j++){
				System.out.print(board[i][j] + "\t");
			}
			System.out.println();
		}
		
		System.out.println();
	}
	
	public static Move requestOppMove(){
	
		String move = input.nextLine();
		
		if(move.equalsIgnoreCase("pass")){
			// no moves. serve new move
			
			return null;
		}else{
			
			StringTokenizer toke = new StringTokenizer(move);
			int x = Integer.parseInt(toke.nextToken());
			int y = Integer.parseInt(toke.nextToken());
			Move oppMove = new Move(x,y);
			
			return oppMove;
		}

	}
	
	public static boolean matchMove(Move oppMove, int player){

		boolean found = false;
		
		for(int i = 0; i < gameState.getLegalActions().size(); i++){
			if(oppMove.isEqual(gameState.getLegalActions().get(i))){
				found = true;
				board = gameState.getLegalActions().get(i).getBoard();
				gameState = new State(board,-player,oppMove,depth);
		
				
			}
		}
		
		return found;
	}
	
	public static void serveMove(int player){
		gameState.alphaBeta();
		if(gameState.getBestAction() == null){
			if(gameState.getLegalActions().size() > 0){
				System.out.println(gameState.getLegalActions().get(0));
				board = gameState.getLegalActions().get(0).getBoard();
				gameState  = new State(board,-player,gameState.getLegalActions().get(0),depth);
			}else{
				System.out.println("pass");
			}
				
			
		}else{
			
	
			board = gameState.getBestAction().getBoard();
			System.out.println(gameState.getBestAction().toString());
			gameState  = new State(board,-player,gameState.getBestAction(),depth);

		}

	}
	
	
}
