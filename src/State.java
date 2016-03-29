/*Oscar Rojas
 * Kiera Crist
 * Othello Program 
 * CSC 242
 * 
 */

import java.util.ArrayList;
import java.util.*;
public class State {
	
	private int [][] boardRep;
	private ArrayList<Move> legalActions;
	private double utility; 
	private int player; 
	private int opponent;
	private double alpha,beta,value;
	private Move bestAction, action;
	private double maxStability; // number of black stable discs
	private double minStability; // number of white stable discs
	private int seconds;
	private int depth;

	//Constructor used to initialize the game. 	
	public State(int[][] boardRep, int player, int depth) {
		this.player = player;
		this.depth = depth;
		this.boardRep = boardRep;
		this.action = new Move(3,4);
		if(this.player == 1){
			this.value = Double.NEGATIVE_INFINITY;
			this.opponent = -1;
		}else{
			this.value = Double.POSITIVE_INFINITY;
			this.opponent = 1;
		}
		
		
		this.legalActions = new ArrayList<Move>();
		generateMoves();
		
		//Initialize alpha and beta 
		this.alpha = Double.NEGATIVE_INFINITY;
		this.beta = Double.POSITIVE_INFINITY;
		

	}
	//Constructor used to instantiate new instances of the current and future game
	// possibilities. 
	public State(int[][] boardRep, int player, Move Action, int depth) {
		this.player = player;
		this.depth = depth;
		this.boardRep = boardRep;
		this.action = Action; 
		if(this.player == 1){
			this.value = Double.NEGATIVE_INFINITY;
			this.opponent = -1;
		}else{
			this.value = Double.POSITIVE_INFINITY;
			this.opponent = 1;
		}
		this.legalActions = new ArrayList<Move>();
		generateMoves();
		
		//Initialize alpha and beta 
		this.alpha = Double.NEGATIVE_INFINITY;
		this.beta = Double.POSITIVE_INFINITY;
		

	}
	
	public void calculateStability(){
			this.maxStability = 0.0;
			this.minStability = 0.0;
			boolean stable = true;
			for (int y = 0; y < boardRep.length; y++){
				for(int x = 0; x < boardRep[y].length; x++){
					
					//break if at empty location
					if(this.boardRep[y][x] == 0){
						continue;
					}
					
					
						// save the current position.
						int xTemp = x;
						int yTemp = y;
						stable = true;
						//explore right
						while(xTemp < 7){
							xTemp ++;
							if(this.boardRep[y][xTemp] == 0){
								stable = false;
							}
						}
	
						xTemp = x;
						yTemp = y;
	
						while(xTemp > 0){
							xTemp --;
							if(this.boardRep[y][xTemp] == 0){
								stable = false;
							}
						}
					
						
						xTemp = x;
						yTemp = y;
					
						while(yTemp > 0){
							yTemp --;
							
							if(this.boardRep[yTemp][x] == 0){
								stable = false;
							}
							
							
						}
					
						xTemp = x;
						yTemp = y;
						
						while(yTemp < 7){
							yTemp ++;
							
							if(this.boardRep[yTemp][x] == 0){
								stable = false;
							}
						}
						
						yTemp = y;
						xTemp = x;
				
						//The next four loops check the diagonals 
						
						
						while((xTemp < 7) && (yTemp < 7)){
							xTemp++;
							yTemp++;
							
							if(this.boardRep[yTemp][xTemp] == 0){
								stable = false;
							}
							
						}
						
						
						yTemp = y;
						xTemp = x;
						
						while((xTemp > 0) && (yTemp > 0)){
							xTemp--;
							yTemp--;
							
							if(this.boardRep[yTemp][xTemp] == 0){
								stable = false;
							}
						}
						
					
						yTemp = y;
						xTemp = x;
						
					
						while((xTemp < 7) && (yTemp > 0)){
							xTemp++;
							yTemp--;
							if(this.boardRep[yTemp][xTemp] == 0){
								stable = false;
							}
				
							
						}
						
						yTemp = y;
						xTemp = x;
					
						while((xTemp > 0) && (yTemp < 7)){
							xTemp--;
							yTemp++;
							
							if(this.boardRep[yTemp][xTemp] == 0){
								stable = false;
							}
							
						}
						yTemp = y;
						xTemp = x;
						
						if(stable == true){
							if(this.boardRep[y][x] == 1){
								this.maxStability ++;
							}else{
								this.minStability ++;
							}
						}
				}

				
			}
			
			
			
			
	}


	
	/*This method will fill the LegalMoves ArrayList with Move objects. 
	 * This is an exhaustive search of all the legal moves in a particular state.  */
	public void generateMoves(){
		
		for (int y = 0; y < boardRep.length; y ++){
			for(int x = 0; x < boardRep[y].length; x ++){
				
				int [][] newBoard = this.copyBoard();
				int flippedChips = 0;
				ArrayList<Move> chipsToFlip = new ArrayList<Move>();
				// Check if this is our chip
				//System.out.println("Player " + this.player );
				if(this.boardRep[y][x] == this.player){
					// save the current position.
					int xTemp = x;
					int yTemp = y;
				
					//explore right
					while(xTemp < 7){
						xTemp ++;
						
						//Check if we are trying to jump over our own pieces to complete a line.
						
						if(boardRep[y][xTemp] == this.player){
							break;
						}
						
						
						
						if(boardRep[y][xTemp] == this.opponent){
							
							flippedChips ++;
							newBoard[y][xTemp] = this.player;
							chipsToFlip.add(new Move(xTemp,y));
						}
						
						if((boardRep[y][xTemp] == 0) && (flippedChips == 0)){
							break;
						}
						
						
						//check if there is a place to put a new chip
						//if this never executes then we know we could not place a chip 
						if(boardRep[y][xTemp] == 0 && flippedChips > 0){
							newBoard[y][xTemp] = this.player;
							
							addMove(new Move(xTemp,yTemp,newBoard,flippedChips,chipsToFlip));
							break;
						}
					}
					newBoard = this.copyBoard();
					flippedChips = 0;
					xTemp = x;
					yTemp = y;
					chipsToFlip.clear();
					while(xTemp > 0){
						xTemp --;
						if(boardRep[y][xTemp] == this.player){
							break;
						}
						if(boardRep[y][xTemp] == this.opponent){
							newBoard[y][xTemp] = this.player;
							chipsToFlip.add(new Move(xTemp,y));
							flippedChips ++;
						}
						
						if((boardRep[y][xTemp] == 0) && (flippedChips == 0)){
							break;
						}
						//check if there is a place to put a new chip
						//if this never executes then we know we could not place a chip 
						if(boardRep[y][xTemp] == 0 && flippedChips > 0){
							newBoard[y][xTemp] = this.player;
							
							addMove(new Move(xTemp,yTemp,newBoard,flippedChips,chipsToFlip)); // add a LegalMove to the set of legal moves.
							break;
						}
					}
				
					flippedChips = 0;
					xTemp = x;
					yTemp = y;
					newBoard = this.copyBoard();
					chipsToFlip.clear();
					while(yTemp > 0){
						yTemp --;
						if(boardRep[yTemp][x] == this.player){
							break;
						}
						if(boardRep[yTemp][x] == this.opponent){
							newBoard[yTemp][x] = this.player;
							chipsToFlip.add(new Move(x,yTemp));
							flippedChips ++;
						}
						
						if((boardRep[yTemp][x] == 0) && (flippedChips == 0)){
							break;
						}
						//check if there is a place to put a new chip
						//if this never executes then we know we could not place a chip 
						if(boardRep[yTemp][x] == 0 && flippedChips > 0){
							newBoard[yTemp][x] = this.player;
							
							addMove(new Move(xTemp,yTemp,newBoard,flippedChips,chipsToFlip));// add a LegalMove to the set of legal moves.
							break;
						}
					}
					newBoard = this.copyBoard();
					flippedChips = 0;
					xTemp = x;
					yTemp = y;
					chipsToFlip.clear();
					while(yTemp < 7){
						yTemp ++;
						if(boardRep[yTemp][x] == this.player){
							break;
						}
						if(boardRep[yTemp][x] == this.opponent){
							newBoard[yTemp][x] = this.player;
							chipsToFlip.add(new Move(x,yTemp));
							flippedChips ++;
						}
						
						if((boardRep[yTemp][x] == 0) && (flippedChips == 0)){
							break;
						}
						
						//check if there is a place to put a new chip
						//if this never executes then we know we could not place a chip 
						if(boardRep[yTemp][x] == 0 && flippedChips > 0){
							newBoard[yTemp][x] = this.player;
							
							addMove(new Move(x,yTemp,newBoard,flippedChips,chipsToFlip)); // add a LegalMove to the set of legal moves.
							break;
						}
					}
					flippedChips = 0;
					yTemp = y;
					xTemp = x;
					newBoard = this.copyBoard();
					chipsToFlip.clear();
					//The next four loops check the diagonals 
					
					
					while((xTemp < 7) && (yTemp < 7)){
						xTemp++;
						yTemp++;
						
						if(boardRep[yTemp][xTemp] == this.player){
							break;
						}
						
						if(boardRep[yTemp][xTemp] == this.opponent){
							flippedChips ++;
							newBoard[yTemp][xTemp] = this.player;
							chipsToFlip.add(new Move(xTemp,yTemp));
						}
						
						if((boardRep[yTemp][xTemp] == 0) && (flippedChips == 0)){
							break;
						}
						
						//check if there is a place to put a new chip
						//if this never executes then we didn't flip any chips or we didn't have a place to put a new chip. 
						if(boardRep[yTemp][xTemp] == 0 && flippedChips > 0){
							newBoard[yTemp][xTemp] = this.player;
							
							addMove(new Move(xTemp,yTemp,newBoard,flippedChips,chipsToFlip)); // add a LegalMove to the set of legal moves. 
							break;
						}
						
					}
					
					flippedChips = 0;
					yTemp = y;
					xTemp = x;
					newBoard = this.copyBoard();
					chipsToFlip.clear();
					while((xTemp > 0) && (yTemp > 0)){
						xTemp--;
						yTemp--;
						
						if(boardRep[yTemp][xTemp] == this.player){
							break;
						}
						if(boardRep[yTemp][xTemp] == this.opponent){
							flippedChips ++;
							newBoard[yTemp][xTemp] = this.player; 
							chipsToFlip.add(new Move(xTemp,yTemp));
						}
						
						if((boardRep[yTemp][xTemp] == 0) && (flippedChips == 0)){
							break;
						}
						
						//check if there is a place to put a new chip
						//if this never executes then we didn't flip any chips or we didn't have a place to put a new chip. 
						if(boardRep[yTemp][xTemp] == 0 && flippedChips > 0){
							newBoard[yTemp][xTemp] = this.player;
							
							addMove(new Move(xTemp,yTemp,newBoard,flippedChips,chipsToFlip)); // add a LegalMove to the set of legal moves.
							break;
						}
						
					}
					
					flippedChips = 0;
					yTemp = y;
					xTemp = x;
					newBoard = this.copyBoard();
					chipsToFlip.clear();
					
					while((xTemp < 7) && (yTemp > 0)){
						xTemp++;
						yTemp--;
						if(this.boardRep[yTemp][xTemp] == this.player){
							break;
						}
						if(this.boardRep[yTemp][xTemp] == this.opponent){
		
							flippedChips ++; 
							newBoard[yTemp][xTemp] = this.player;
							chipsToFlip.add(new Move(xTemp,yTemp));
						}
						if((boardRep[yTemp][xTemp] == 0) && (flippedChips == 0)){
							break;
						}
						
						
						//check if there is a place to put a new chip
						//if this never executes then we didn't flip any chips or we didn't have a place to put a new chip. 
						if((this.boardRep[yTemp][xTemp] == 0) && (flippedChips > 0)){
							newBoard[yTemp][xTemp] = this.player;
							
							addMove(new Move(xTemp,yTemp,newBoard,flippedChips,chipsToFlip)); // add a LegalMove to the set of legal moves.
							break;
						}
						
					}
					
					flippedChips = 0;
					yTemp = y;
					xTemp = x;
					newBoard = this.copyBoard();
					chipsToFlip.clear();
					
					while((xTemp > 0) && (yTemp < 7)){
						xTemp--;
						yTemp++;
						if(this.boardRep[yTemp][xTemp] == this.player){
							break;
						}
						if(this.boardRep[yTemp][xTemp] == this.opponent){

							flippedChips ++;
							newBoard[yTemp][xTemp] = this.player;
							chipsToFlip.add(new Move(xTemp,yTemp));
						}
						if((boardRep[yTemp][xTemp] == 0) && (flippedChips == 0)){
							break;
						}
						
						
						//check if there is a place to put a new chip
						//if this never executes then we didn't flip any chips or we didn't have a place to put a new chip. 
						if((this.boardRep[yTemp][xTemp] == 0) && (flippedChips > 0)){
							newBoard[yTemp][xTemp] = this.player;
							
							addMove(new Move(xTemp,yTemp,newBoard,flippedChips,chipsToFlip)); // add a LegalMove to the set of legal moves.
							break;
						}
						
					}
					newBoard = this.copyBoard();
					chipsToFlip.clear();
					flippedChips = 0;
					yTemp = y;
					xTemp = x;

				}
				
			}
		}
		
		
		
		
		
	}
	
	/*This method checks to see if we are trying to add a duplicate move.
	 *If this is the case then we know that this move will make two lines.
	 *So we need to merge the boards to account for all the chips that this move
	 *will flip */
	public void addMove(Move newAction){
		boolean found = false;
		for(int i = 0 ; i < this.legalActions.size(); i++){
			if(this.legalActions.get(i).isEqual(newAction)){
				found = true;
				// We already have this move. So we need to merge the boards.
				// So that we only have one board for this move. 
				// Get this move's chipsToFlip and flip them on this board.
				int [][] tempBoard = this.legalActions.get(i).getBoardCopy();
				for(int j = 0; j < newAction.getChipsToFlip().size(); j ++){
					Move chip = newAction.getChipsToFlip().get(j);
					tempBoard[chip.getY()][chip.getX()] = this.player;
				}
				this.legalActions.get(i).setBoard(tempBoard);
			}
		}
		
		if(!found){
			// add new move 
			legalActions.add(newAction);
		}
			
		
	}
	
	
	/*This method will assign a utility value to a state
	 * It accounts for number of moves available from this state
	 * disc parity,corners,squares adjacent to corners, and the stability of discs.*/
	public void calculateUtility(){
		this.utility = 0.0; 
		
		//Obtain the number of moves for both min and max
		// and take the relative difference.
		double mobilityMax = 0;
		double mobilityMin = 0;
		double mobility = 0;
		
		State max = new State(copyBoard(),1,this.depth);
		State min = new State(copyBoard(),-1,this.depth);
		mobilityMax = (double) max.getLegalActions().size();
		mobilityMin = (double) min.getLegalActions().size();
		
		mobility = 100 * ((mobilityMax - mobilityMin) / (mobilityMax + mobilityMin)); 
		
		
		int sumBoard = 0;
		double discParity = 0;
		
		for(int i = 0; i < this.boardRep.length; i++){
			for(int j = 0; j < this.boardRep[i].length; j++){
				sumBoard += this.boardRep[i][j];
			}
		}
		
		discParity = 100 * ((double)sumBoard / ((double)this.chips()));
		this.calculateStability();
		if((this.minStability + this.maxStability > 0)){
			double coinStability = 100 * ((this.maxStability - this.minStability) / (this.maxStability + this.minStability));		
			this.utility += coinStability;
		}
		//Corners
		int cornersMax = 0;
		int cornersMin = 0;
		if(this.boardRep[0][0] == 1){
			cornersMax++;
		}else if(this.boardRep[0][0] == -1){
			cornersMin++;
		}
		if(this.boardRep[7][0] == 1){
			cornersMax++;
		}else if(this.boardRep[7][0] == -1){
			cornersMin++;
		}
		
		if(this.boardRep[0][7] == 1){
			cornersMax++;
		}else if(this.boardRep[0][7] == -1){
			cornersMin++;
		}
		
		if(this.boardRep[7][7] == 1){
			cornersMax++;
		}else if(this.boardRep[7][7] == -1){
			cornersMin++;
		}
		if((cornersMax + cornersMin) > 0){
			double corners = 100 * ((cornersMax - cornersMin) / (cornersMax + cornersMin));
			this.utility += corners;
		}
		
		this.utility += (mobility + discParity);
		//First set of corner spots 
		ArrayList<Move> cornerSet = new ArrayList<Move>();
		cornerSet.add(new Move(1,1));
		cornerSet.add(new Move(1,6));
		cornerSet.add(new Move(6,1));
		cornerSet.add(new Move(6,6));
		
		//Second set of corner spots
		ArrayList<Move> cornerSet2 = new ArrayList<Move>();
		cornerSet2.add(new Move(1,0));
		cornerSet2.add(new Move(0,1));
		
		cornerSet2.add(new Move(6,0));
		cornerSet2.add(new Move(7,1));
		
		cornerSet2.add(new Move(0,6));
		cornerSet2.add(new Move(1,7));
		
		cornerSet2.add(new Move(7,6));
		cornerSet2.add(new Move(6,7));
		
		for(int i = 0; i < cornerSet.size(); i++){
			if(cornerSet.get(i).isEqual(this.action)){
				if(getPlayer() == -1){
					this.utility -= 20000.0;
				}else{
					this.utility += 20000.0;
				}
			}
		}
		
		for(int i = 0; i < cornerSet2.size(); i++){
			if(cornerSet2.get(i).isEqual(this.action)){
				if(getPlayer() == -1){
					this.utility -= 20000.0;
					
				}else{
					this.utility += 20000.0;
				}
			}
		}
				
		
	}
	
	public boolean isOver(){
		boolean over = false;
		if(this.chips() == 64){
			over = true;
		}
		return over;
	}

	
	public void setPlayer(int player) {
		this.player = player;
	}
	public void setOpponent(int opponent) {
		this.opponent = opponent;
	}
	public ArrayList<Move> getLegalActions() {
		return legalActions;
	}

	public void setLegalActions(ArrayList<Move> legalActions) {
		this.legalActions = legalActions;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public Move getAction() {
		return action;
	}

	public Move getBestAction() {
		
		return bestAction;
	}
	
	public void setBestAction(Move bestAction) {
		this.bestAction = bestAction;
	}
	
	public int getPlayer() {

		return player;
	}
	
	public int getOpponent() {
		return opponent;
	}

	public void apply(Move action){
		this.boardRep[action.getY()][action.getX()] = this.player;
			
	}
	
	public int [][] getBoardRep() {
		return boardRep;
	}

	public ArrayList<Move> getMoves() {
		return this.legalActions;
	}
	
	/*Prints the board represented by this state*/
	public void printState(){
		System.out.println("   0\t1\t2\t3\t4\t5\t6\t7\n");
		for(int i = 0; i < this.boardRep.length; i++){
			System.out.print(i + ": ");
			for(int j = 0; j < this.boardRep[i].length; j++){
				System.out.print(this.boardRep[i][j] + "\t");
			}
			System.out.println();
		}
		
		System.out.println();
		
	}
	
	/*Prints legal moves from this state */
	public void printLegalMoves(){
		for(int i = 0; i < legalActions.size(); i++){
			System.out.println(legalActions.get(i).toString());
		}
	}
	
	public int [][] copyBoard(){
		int temp [][] = new int [8][8];
		for(int i = 0; i < this.boardRep.length; i ++){
			for(int j = 0; j < this.boardRep[i].length; j++){
				temp[i][j] = this.boardRep[i][j];
			}
		}
		return temp;
		
	}

	public void alphaBeta(){
		
		alphaBeta(this,0,this.alpha,this.beta);
		//miniMax(this,0);
		
		
	}
	
	
	public void printBestAction(){
		if(this.bestAction == null){
			if(!this.legalActions.isEmpty()){
				System.out.println(legalActions.get(0));
			}
		}
		System.out.println(getBestAction().toString());
	}
	
	private double alphaBeta(State s, int depth, double alpha, double beta){
		
		if(depth == this.depth){
		
				s.bestAction = null;
				s.calculateUtility(); //Calculate the heuristic for this state.
				s.value = s.utility;  //Set the value 
				return s.value;
			

		}
		
		if(s.player == 1){
			
			for(int i = 0; i < s.legalActions.size(); i++){
				//Instantiate first child to this node. 
				State child = new State(s.legalActions.get(i).getBoard(),-1,s.legalActions.get(i),this.depth);
				child.alpha = s.alpha;
				//Calculate the child's value in a recursive manner. 
				double result = alphaBeta(child, depth + 1, alpha, beta);
				if(result > alpha){
					alpha = result;
					s.bestAction = child.getAction();
				}
				//If beta is less than alpha then prune.
				if(beta <= alpha){
					
					return alpha;
				}
			}
			s.value = alpha;
			return alpha;
		}
		
		else{
			
			for(int i = 0; i < s.legalActions.size(); i++){
				//Instantiate first child to this node. 
				State child = new State(s.legalActions.get(i).getBoard(),1,s.legalActions.get(i),this.depth);
				child.beta = s.beta;
				//Calculate the child's value in a recursive manner.
				double result = alphaBeta(child, depth + 1, alpha, beta);
				if(result < beta){
					beta = result;
					s.bestAction = child.getAction();
				}
				//If beta is less than alpha, prune.
				if(beta <= alpha){
					
					return beta;
				}
		}
		  s.value = beta;
		  return beta;
		
		
	}
}

	public int chips(){
		int chips = 0;
		for(int i = 0; i < this.boardRep.length; i ++){
			for(int j = 0; j < this.boardRep[i].length; j++){
				if(this.boardRep[i][j] != 0){
				chips++;
				}
			}
		}
		return chips;
	}
	/*This miniMax method was used to measure the correctness of the 
	 * alphabeta search algorithm. Given that both search algorithms should return the same
	 * results.*/
	private double miniMax(State s, int depth){
		if(depth == 4 || s.legalActions.size() == 0){
			s.bestAction = null;
			s.calculateUtility();
			s.value = s.utility;
			return s.value;
		}
		//If MAX 
		if(s.player == 1){
			//System.out.println("IN MAX");
			s.value = Double.NEGATIVE_INFINITY;
			for(int i = 0; i < s.legalActions.size(); i++){
				State child = new State(s.legalActions.get(i).getBoard(),-1,s.legalActions.get(i),this.depth);
				double result = miniMax(child,depth+1);
				if(result > s.value){
					s.value = result;
					s.bestAction = child.action;
				}
				
			}
			return s.value;
		}
		
		else{
			//System.out.println("IN MIN");
			s.value = Double.POSITIVE_INFINITY;
			for(int i = 0; i < s.legalActions.size(); i++){
				State child = new State(s.legalActions.get(i).getBoard(),1,s.legalActions.get(i),this.depth);
				double result = miniMax(child,depth+1);
				if(result < s.value){
					s.value = result;
					s.bestAction = child.bestAction;
				}
				
			}
			return s.value;
		
	
	}
}

}
