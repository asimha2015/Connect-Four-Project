package application;



import java.util.ArrayList;
import java.util.Random;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class GameboardController {
	
	
	//@FXML 
	//private Circle Circle00;
	
	@FXML
	private GridPane Board;
	
	@FXML
	private Button RedPlayerChoose;
	
	@FXML
	private Button YellowPlayerChoose;
	
	@FXML 
	private Button Reset;
	
	@FXML
	private Label InstructionLabel;
	
	@FXML
	private Label RedWins;
	
	@FXML
	private Label YellowWins;
	
	
	private int _Columns=7;
	private int _Rows=6;
	
	private boolean WinnerChosen=false;
	private String Player_color;
	private String AI_color;
	private boolean ColorChosen=false;
	private boolean AI_turn=false;
												
	//Shadow Data Structure; Every value initialized to -1
	//When player or AI selects chip, the corresponding position
	//in the data structure will be changed to 0 for player and 1 for AI
	private ArrayList<ArrayList<Integer>> shadowArray = new ArrayList<ArrayList<Integer>>(_Rows);			
	
	private ArrayList<Integer> shadowA0= new ArrayList<Integer>();
	private ArrayList<Integer> shadowA1= new ArrayList<Integer>();
	private ArrayList<Integer> shadowA2= new ArrayList<Integer>();
	private ArrayList<Integer> shadowA3= new ArrayList<Integer>();
	private ArrayList<Integer> shadowA4= new ArrayList<Integer>();
	private ArrayList<Integer> shadowA5= new ArrayList<Integer>();
	
	
	public void initialize() {
		for (int i=0; i<_Columns; i++) {
			shadowA0.add(-1);
			shadowA1.add(-1);
			shadowA2.add(-1);
			shadowA3.add(-1);
			shadowA4.add(-1);
			shadowA5.add(-1);
		}
		shadowArray.add(shadowA0);
		shadowArray.add(shadowA1);
		shadowArray.add(shadowA2);
		shadowArray.add(shadowA3);
		shadowArray.add(shadowA4);
		shadowArray.add(shadowA5);
		
		InstructionLabel.setText("Hello, Welcome To a Connect Four Game Created Using JavaFX. Please select your color by Picking the Red or Yellow Buttons Above.");
		RedWins.setText("0");
		YellowWins.setText("0");
		Reset.setDisable(true);
		Reset.setVisible(false);
		RedPlayerChoose.setStyle("-fx-background-color: red");
		YellowPlayerChoose.setStyle("-fx-background-color: yellow");
		
	}
	
	
	public int OpenRow(int col_ind) {
		for (int i=_Rows-1; i>=0; i--) {
			if (shadowArray.get(i).get(col_ind)==-1) {
				return i;
			}
		}
		return -1;
	}
	
	
	public void RedPlayerButton(ActionEvent e) {
		RedPlayerChoose.setDisable(true);
		YellowPlayerChoose.setDisable(true);
		RedPlayerChoose.setVisible(false);
		YellowPlayerChoose.setVisible(false);
		Player_color="RED";
		AI_color = "YELLOW";
		InstructionLabel.setText("You Are Player Red. To place a chip, click any chip icon in the desired column. Wait for Opponent to Place Their Chip Befor You Place the Next One. Start Playing Whenever You Are Ready And Have Fun!!!");
		ColorChosen=true;
	}
	
	public void YellowPlayerButton(ActionEvent e) {
		YellowPlayerChoose.setDisable(true);
		RedPlayerChoose.setDisable(true);
		RedPlayerChoose.setVisible(false);
		YellowPlayerChoose.setVisible(false);
		Player_color="YELLOW";
		AI_color = "RED";
		InstructionLabel.setText("You Are Player Yellow. To place a chip, click any chip icon in the desired column. Wait for Opponent to Place Their Chip Befor You Place the Next One. Start Playing Whenever You Are Ready And Have Fun!!!");
		ColorChosen=true;
	}
	
	public void hoverColumn(MouseEvent e) {			//Outlines on circles in a column turn to white when hovering over a chip in that column
		if (!ColorChosen) {
			return;
		}
		if (WinnerChosen) {
			return;
		}
		Circle chip=(Circle) e.getSource();
		Integer cc=GridPane.getColumnIndex(chip);
		int chip_col;
		if (cc==null) {
			chip_col=0;
		}
		else {
			chip_col=cc;
		}
		for (Node child : Board.getChildren()) {
			Integer sc=GridPane.getColumnIndex(child);
			Integer sr=GridPane.getRowIndex(child);
			int scol;
			int srow;
			if (sc==null) {
				scol=0;
			}
			else {
				scol=sc;
			}
			if (sr==null) {
				srow=0;
			}
			else {
				srow=sr;
			}
			if (scol==chip_col) {
				((Circle) child).setStroke(javafx.scene.paint.Color.WHITE);
			}
		}
		
	}
	
	public void hoverExit(MouseEvent e) {			//Outlines on the circles in Columns go back to Black after mouse moves off of chip
		Circle chip=(Circle) e.getSource();
		Integer cc=GridPane.getColumnIndex(chip);
		int chip_col;
		if (cc==null) {
			chip_col=0;
		}
		else {
			chip_col=cc;
		}
		for (Node child : Board.getChildren()) {
			Integer sc=GridPane.getColumnIndex(child);
			Integer sr=GridPane.getRowIndex(child);
			int scol;
			int srow;
			if (sc==null) {
				scol=0;
			}
			else {
				scol=sc;
			}
			if (sr==null) {
				srow=0;
			}
			else {
				srow=sr;
			}
			if (scol==chip_col) {
				((Circle) child).setStroke(javafx.scene.paint.Color.BLACK);
			}
		}
	}
	
	
	public void chipListener(MouseEvent e) {
		if (!ColorChosen) {
			InstructionLabel.setText("Please Select a Color Before You Place A Chip");
			return;
		}
		if (WinnerChosen) {
			InstructionLabel.setText("Winner Has Already Been Chosen, Please Click Reset Button to Play Again.");
			return;
		}
		InstructionLabel.setText("");
		Reset.setDisable(false);
		Reset.setVisible(true);
		Circle chip=(Circle) e.getSource();
		Integer cc=GridPane.getColumnIndex(chip);
		Integer cr=GridPane.getRowIndex(chip);
		int chip_col;
		if (cc==null) {
			chip_col=0;
		}
		else {
			chip_col=cc;
		}
		int OpenRowSpace=OpenRow(chip_col);
		if (OpenRowSpace==-1) {
			InstructionLabel.setText("Chosen Column is Already Full Can't Place Chip Here. Please Select A Different Column.");
			//System.out.println("Can't Place Chip Here");
			return;
		}
		else {
			//System.out.println("chipPlaced");
			for (Node child : Board.getChildren()) {
				Integer sc=GridPane.getColumnIndex(child);
				Integer sr=GridPane.getRowIndex(child);
				int scol;
				int srow;
				if (sc==null) {
					scol=0;
				}
				else {
					scol=sc;
				}
				if (sr==null) {
					srow=0;
				}
				else {
					srow=sr;
				}
				if (srow==OpenRowSpace && scol==chip_col) {
					/*if (AI_turn) {
						return;
					}*/
					if (Player_color=="RED") {
						((Circle) child).setFill(javafx.scene.paint.Color.RED);
						//AI_turn=true;
					}
					else {
						((Circle) child).setFill(javafx.scene.paint.Color.YELLOW);
						//AI_turn=true;
					}
					(shadowArray.get(OpenRowSpace)).set(chip_col,0);
					if (columnWin(OpenRowSpace, chip_col) || rowWin(OpenRowSpace,chip_col) || diagWin(OpenRowSpace, chip_col)) {
						InstructionLabel.setText(Player_color + " PLAYER WINS!!!!!!! Please Hit Reset Button To Play Again");
						int newValue=Integer.parseInt(RedWins.getText()) + 1;
						RedWins.setText(newValue + "");
						WinnerChosen=true;
						//System.out.println("Red Winner");
						return;
					}
				}
			}
		}
		
		
		Random rand = new Random();								//Where Random AI code begins
		int AI_chip_col=0;
		int AI_chip_row=0;
		while (true) {													
			AI_chip_col=rand.nextInt(7);
			AI_chip_row=OpenRow(AI_chip_col);
			if (AI_chip_row!=-1) {
				break;
			}
		}
		for (Node child : Board.getChildren()) {
			Integer sc=GridPane.getColumnIndex(child);
			Integer sr=GridPane.getRowIndex(child);
			int scol;
			int srow;
			if (sc==null) {
				scol=0;
			}
			else {
				scol=sc;
			}
			if (sr==null) {
				srow=0;
			}
			else {
				srow=sr;
			}				
			if (srow==AI_chip_row && scol==AI_chip_col) {
				if (AI_color=="YELLOW") {
					PauseTransition pause = new PauseTransition(Duration.seconds(1));
					pause.setOnFinished(event -> ((Circle) child).setFill(javafx.scene.paint.Color.YELLOW));
					//AI_turn=false;
					pause.play();
				}
				else {
					PauseTransition pause = new PauseTransition(Duration.seconds(1));
					pause.setOnFinished(event ->((Circle) child).setFill(javafx.scene.paint.Color.RED));
					pause.play();
				}
				shadowArray.get(AI_chip_row).set(AI_chip_col,1);
				if (columnWin(AI_chip_row, AI_chip_col) || rowWin(AI_chip_row,AI_chip_col) || diagWin(AI_chip_row, AI_chip_col)) {
					//System.out.println("Yellow Winner");
					PauseTransition pause = new PauseTransition(Duration.seconds(1));
					pause.setOnFinished(event ->InstructionLabel.setText(AI_color + " PLAYER WINS!!!!!!! Please Hit Reset Button To Play Again"));
					pause.play();
					//InstructionLabel.setText(AI_color + " PLAYER WINS!!!!!!! Please Hit Reset Button To Play Again");
					int newValue=Integer.parseInt(YellowWins.getText()) + 1;
					PauseTransition AI_Win_pause = new PauseTransition(Duration.seconds(1));
					AI_Win_pause.setOnFinished(event ->YellowWins.setText(newValue + ""));
					AI_Win_pause.play();
					WinnerChosen=true;
				}
			}
		}
	}
	
	
	public boolean columnWin(int row_ind, int col_ind) {
		if (row_ind+1==_Rows) {
			if (shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind-1).get(col_ind)) {
				return false;
			}
			for (int i=1; i<4; i++) {
				if (shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind-i).get(col_ind)) {
					return false;
				}
			}
			return true;
		}
		if (row_ind-1<0) {
			if (shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind+1).get(col_ind)) {
				return false;
			}
			for (int i=1; i<4; i++) {
				if (shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind+i).get(col_ind)) {
					return false;
				}
			}
			return true;
		}
		if (row_ind<=_Rows-2 && shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind+1).get(col_ind)) {
			if (shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind-1).get(col_ind)) {
				return false;
			}
			
			for (int i=1; i<4; i++) {
				if (row_ind-i<0) {
					return false;
				}
				if (shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind-i).get(col_ind)) {
					return false;
				}
			}
			return true;
		}
		if (row_ind>=1 && shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind-1).get(col_ind)) {
			if (shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind+1).get(col_ind)) {
				return false;
			}
			for (int i=1; i<4; i++) {
				if (row_ind+i>=_Rows) {
					return false;
				}
				if (shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind+i).get(col_ind)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public boolean rowWin(int row_ind, int col_ind) {
		if (col_ind+1==_Columns) {
			if (shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind).get(col_ind-1)) {
				return false;
			}
			for (int i=1; i<4; i++) {
				if (shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind).get(col_ind-i)) {
					return false;
				}
			}
			return true;
		}
		if (col_ind-1<0) {
			if (shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind).get(col_ind+1)) {
				return false;
			}
			for (int i=1; i<4; i++) {
				if (shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind).get(col_ind+i)) {
					return false;
				}
			}
			return true;
		}
		if (col_ind<=_Columns-2 && shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind).get(col_ind+1)) {
			if (shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind).get(col_ind-1)) {
				return false;
			}
			
			for (int i=1; i<4; i++) {
				if (col_ind-i<0) {
					return false;
				}
				if (shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind).get(col_ind-i)) {
					return false;
				}
			}
			return true;
		}
		if (col_ind>=1 && shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind).get(col_ind-1)) {
			if (shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind).get(col_ind+1)) {
				return false;
			}
			for (int i=1; i<4; i++) {
				if (col_ind+i>=_Columns) {
					return false;
				}
				if (shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind).get(col_ind+i)) {
					return false;
				}
			}
			return true;
		}
		if (col_ind<=_Columns-2 && col_ind>=1 && shadowArray.get(row_ind).get(col_ind)==shadowArray.get(row_ind).get(col_ind+1) && shadowArray.get(row_ind).get(col_ind)==shadowArray.get(row_ind).get(col_ind-1)) {
			if (col_ind-2<0) {
				if (shadowArray.get(row_ind).get(col_ind+2)==shadowArray.get(row_ind).get(col_ind)) {
					return true;
				}
			}
			else if (shadowArray.get(row_ind).get(col_ind-2)==shadowArray.get(row_ind).get(col_ind)) {
				return true;
			}
			else {
				;
			}
			
			if (col_ind+2>=_Columns) {
				if (shadowArray.get(row_ind).get(col_ind-2)==shadowArray.get(row_ind).get(col_ind)) {
					return true;
				}
			}
			else if (shadowArray.get(row_ind).get(col_ind+2)==shadowArray.get(row_ind).get(col_ind)) {
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}
	
	public boolean diagWin(int row_ind, int col_ind) {
		boolean returnValue=true;
		boolean edge=false;
		for (int i=1; i<4; i++) {
			if (row_ind-i<0 || col_ind+i>=_Columns) {
				returnValue=false;
				break;
			}
			if (shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind-i).get(col_ind+i)) {
				returnValue=false;
				break;
			}
		}
		if (returnValue) {
			return true;
		}
		returnValue=true;
		for (int i=1; i<4; i++) {
			if (row_ind-i<0 || col_ind-i<0) {
				returnValue=false;
				break;
			}
			if (shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind-i).get(col_ind-i)) {
				returnValue=false;
				break;
			}
		}
		if (returnValue) {
			return true;
		}
		returnValue=true;
		for (int i=1; i<4; i++) {
			if (row_ind+i>=_Rows || col_ind-i<0) {
				returnValue=false;
				break;
			}
			if (shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind+i).get(col_ind-i)) {
				returnValue=false;
				break;
			}
		}
		if (returnValue) {
			return true;
		}
		returnValue=true;
		for (int i=1; i<4; i++) {
			if (row_ind+i>=_Rows || col_ind+i>=_Columns) {
				returnValue=false;
				break;
			}
			if (shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind+i).get(col_ind+i)) {
				returnValue=false;
				break;
			}
		}
		if (returnValue) {
			return true;
		}
		
		if (row_ind-1<0 || col_ind-1<0) {
			return false;
		}
		
		if (row_ind+1>=_Rows || col_ind+1>=_Columns) {
			return false;
		}
		
		if ((row_ind-2<0 && col_ind-2<0) && shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind-1).get(col_ind-1)) {				
			boolean fourCheckright=true;
			boolean fourCheckleft=true;
			for (int i=1; i<4; i++) {
				if (row_ind+i>=_Rows || col_ind+i>=_Columns) {
					fourCheckright=false;
					break;
				}
				if (shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind+i).get(col_ind+i)) {
					fourCheckright=false;
					break;
				}
			}
			if (fourCheckright) {
				return true;
			}
			
			for (int i=1; i<4; i++) {
				if (row_ind-i<0 || col_ind-i<=0) {
					fourCheckleft=false;
					break;
				}
				if (shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind-i).get(col_ind-i)) {
					fourCheckleft=false;
					break;
				}
			}
			if (fourCheckleft) {
				return true;
			}
		}
		if ((row_ind+2>=_Rows && col_ind+2>=_Columns) && shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind+1).get(col_ind+1)) {
			boolean fourCheckright=true;
			boolean fourCheckleft=true;
			for (int i=1; i<4; i++) {
				if (row_ind+i>=_Rows || col_ind+i>=_Columns) {
					fourCheckright=false;
					break;
				}
				if (shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind+i).get(col_ind+i)) {
					fourCheckright=false;
					break;
				}
			}
			if (fourCheckright) {
				return true;
			}
			
			for (int i=1; i<4; i++) {
				if (row_ind-i<0 || col_ind-i<=0) {
					fourCheckleft=false;
					break;
				}
				if (shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind-i).get(col_ind-i)) {
					fourCheckleft=false;
					break;
				}
			}
			if (fourCheckleft) {
				return true;
			}
		}
		if ((row_ind+2>=_Rows && col_ind-2<0) && shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind+1).get(col_ind-1)) {
			boolean fourCheckright=true;
			boolean fourCheckleft=true;
			for (int i=1; i<4; i++) {
				if (row_ind+i>=_Rows || col_ind+i>=_Columns) {
					fourCheckright=false;
					break;
				}
				if (shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind+i).get(col_ind+i)) {
					fourCheckright=false;
					break;
				}
			}
			if (fourCheckright) {
				return true;
			}
			
			for (int i=1; i<4; i++) {
				if (row_ind-i<0 || col_ind-i<=0) {
					fourCheckleft=false;
					break;
				}
				if (shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind-i).get(col_ind-i)) {
					fourCheckleft=false;
					break;
				}
			}
			if (fourCheckleft) {
				return true;
			}
		}
		if ((row_ind-2<0 && col_ind+2>=_Columns) && shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind-1).get(col_ind+1)) {
			boolean fourCheckright=true;
			boolean fourCheckleft=true;
			for (int i=1; i<4; i++) {
				if (row_ind+i>=_Rows || col_ind+i>=_Columns) {
					fourCheckright=false;
					break;
				}
				if (shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind+i).get(col_ind+i)) {
					fourCheckright=false;
					break;
				}
			}
			if (fourCheckright) {
				return true;
			}
			
			for (int i=1; i<4; i++) {
				if (row_ind-i<0 || col_ind-i<=0) {
					fourCheckleft=false;
					break;
				}
				if (shadowArray.get(row_ind).get(col_ind)!=shadowArray.get(row_ind-i).get(col_ind-i)) {
					fourCheckleft=false;
					break;
				}
			}
			if (fourCheckleft) {
				return true;
			}
		}
		
		
		edge=false;
		if (shadowArray.get(row_ind).get(col_ind)==shadowArray.get(row_ind-1).get(col_ind-1) && shadowArray.get(row_ind).get(col_ind)==shadowArray.get(row_ind+1).get(col_ind+1)) {
			if (row_ind-1==0 && col_ind+2<_Columns) {
				edge=true;
				if (shadowArray.get(row_ind).get(col_ind)==shadowArray.get(row_ind+2).get(col_ind+2)) {
					return true;
				}
			}
			if (col_ind-1==0 && row_ind+2<_Rows) {
				edge=true;
				if (shadowArray.get(row_ind).get(col_ind)==shadowArray.get(row_ind+2).get(col_ind+2)) {
					return true;
				}
			}
			if (row_ind+1==_Rows-1 && col_ind-2>=0) {
				edge=true;
				if (shadowArray.get(row_ind).get(col_ind)==shadowArray.get(row_ind-2).get(col_ind-2)) {
					return true;
				}
			}
			if (col_ind+1==_Columns-1 && row_ind-2>=0) {
				edge=true;
				if (shadowArray.get(row_ind).get(col_ind)==shadowArray.get(row_ind-2).get(col_ind-2)) {
					return true;
				}
			}
			if (!edge) {
				if (shadowArray.get(row_ind).get(col_ind)==shadowArray.get(row_ind+2).get(col_ind+2)) {
					return true;
				}
				if (shadowArray.get(row_ind).get(col_ind)==shadowArray.get(row_ind-2).get(col_ind-2)) {
					return true;
				}
			}
		}
		edge=false;
		if (shadowArray.get(row_ind).get(col_ind)==shadowArray.get(row_ind+1).get(col_ind-1) && shadowArray.get(row_ind).get(col_ind)==shadowArray.get(row_ind-1).get(col_ind+1)) {
			if (row_ind-1==0 && col_ind-2>=0) {
				edge=true;
				if (shadowArray.get(row_ind).get(col_ind)==shadowArray.get(row_ind+2).get(col_ind-2)) {
					return true;
				}
			}
			if (col_ind-1==0 && row_ind-2>=0) {
				edge=true;
				if (shadowArray.get(row_ind).get(col_ind)==shadowArray.get(row_ind-2).get(col_ind+2)) {
					return true;
				}
			}
			if (row_ind+1==_Rows-1 && col_ind+2<_Columns) {
				edge=true;
				if (shadowArray.get(row_ind).get(col_ind)==shadowArray.get(row_ind-2).get(col_ind+2)) {
					return true;
				}
			}
			if (col_ind+1==_Columns-1 && row_ind+2<_Rows) {
				edge=true;
				if (shadowArray.get(row_ind).get(col_ind)==shadowArray.get(row_ind+2).get(col_ind-2)) {
					return true;
				}
			}
			if (!edge) {
				if (shadowArray.get(row_ind).get(col_ind)==shadowArray.get(row_ind-2).get(col_ind+2)) {
					return true;
				}
				if (shadowArray.get(row_ind).get(col_ind)==shadowArray.get(row_ind+2).get(col_ind-2)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void ResetListener(ActionEvent e) {
		for (Node child : Board.getChildren()) {
			((Circle) child).setStroke(javafx.scene.paint.Color.BLACK);
			
		}
		WinnerChosen=false;
		Reset.setDisable(true);
		Reset.setVisible(false);
		InstructionLabel.setText("");
		for (Node child : Board.getChildren()) {
			((Circle) child).setFill(Color.web("#c0c8ce"));
		}
		for(int i=0; i<_Rows; i++) {
			for (int j=0; j<_Columns; j++) {
				shadowArray.get(i).set(j, -1);
			}
		}
		RedPlayerChoose.setVisible(true);
		YellowPlayerChoose.setVisible(true);
		RedPlayerChoose.setDisable(false);
		YellowPlayerChoose.setDisable(false);
		InstructionLabel.setText("Please Choose Your Color.");
		ColorChosen=false;
		AI_turn=false;
	}
}
