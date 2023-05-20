package MondrianSquare;

import java.awt.Color;

public class PerimeterGoal extends Goal{

	public PerimeterGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		// returns the score of this goal on that board based on its target color
		// can start by flattening the assignment3.Block to make your job easier
		// loop through xCoord's
		// loop through yCoords
		Color [][] cArray = board.flatten();

		int score = 0;
		int sideLength = cArray[0].length;

		for(int i = 0; i < sideLength; i++) {
			//count top
			if(cArray[0][i].equals(targetGoal)) {
				score++;
			}

			//count bottom
			if(cArray[sideLength - 1][i].equals(targetGoal)) {
				score++;
			}

			//count right
			if(cArray[i][0].equals(targetGoal)) {
				score++;
			}

			//count left
			if(cArray[i][sideLength - 1].equals(targetGoal)) {
				score++;
			}
		}

		return score;
	}

	@Override
	public String description() {
		return "Place the highest number of " + GameColors.colorToString(targetGoal) 
		+ " unit cells along the outer perimeter of the board. Corner cell count twice toward the final score!";
	}

}
