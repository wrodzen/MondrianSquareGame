package MondrianSquare;

import java.awt.Color;
//import java.lang.annotation.Target;

public class BlobGoal extends Goal{

	public BlobGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		Color[][] c = board.flatten();
		int size = c.length;
		int max = 0;

		boolean[][] visited = new boolean[size][size];

		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				int score = undiscoveredBlobSize(i, j, c, visited);

				if(score > max) {
					max = score;
				}
			}
		}

		return max;
	}

	@Override
	public String description() {
		return "Create the largest connected blob of " + GameColors.colorToString(targetGoal) 
		+ " blocks, anywhere within the block";
	}


	public int undiscoveredBlobSize(int i, int j, Color[][] unitCells, boolean[][] visited) {
		int score = 0;
		int size = unitCells[0].length;

		// base case:
		if (!unitCells[i][j].equals(targetGoal) || visited[i][j]) {
			return score;
		}

		visited[i][j] = true;

		//recursive case: when is equal to target color
		score += 1;

		if (i - 1 >= 0 && !visited[i-1][j]) {
			score += undiscoveredBlobSize(i-1,j,unitCells,visited);
		}

		if (i + 1 < size && !visited[i+1][j]) {
			score += undiscoveredBlobSize(i+1,j,unitCells,visited);
		}

		if (j - 1 >= 0 && !visited[i][j-1]) {
			score += undiscoveredBlobSize(i,j-1,unitCells,visited);
		}

		if (j + 1 < size && !visited[i][j+1]) {
			score += undiscoveredBlobSize(i,j+1,unitCells,visited);
		}

		return score;

	}

}
