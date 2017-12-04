import java.awt.Color;

import edu.illinois.cs.cs125.lib.mazemaker.Maze;
import edu.illinois.cs.cs125.lib.zen.Zen;

public class MazeSolver {

	/** Scale to display maze pieces */
	private static final int SCALE = 20;
	
	/** Delay between moving current piece */
	private static final int DELAY = 20;
	
	/** X dimension of maze */
	private static int xDim = 60;
	
	/** Y dimension of maze */
	private static int yDim = 30;

	/** Colors used throughout program */
	private static final Color BACKGROUND_COLOR = new Color(0,0,0);
	private static final Color WALL_COLOR = new Color(100,100,100);
	private static final Color CURRENT_COLOR = new Color(0,255,0);
	private static final Color END_COLOR = new Color(255,0,0);
	private static final Color SEEN_COLOR = new Color(255,255,255);

	/** Creates new maze with given dimensions */
	private static final Maze maze = new Maze(xDim, yDim);

	/**
	 * Runner for Maze Solver
	 * @param unused unused
	 */
	public static void main(String[] unused) {
		maze.startAtRandomLocation();
		maze.endAtRandomLocation();

		// X and Y dimensions of Zen according to how MazeMaker creates its mazes
		xDim = xDim * SCALE * 2 + SCALE;
		yDim = yDim * SCALE * 2 + SCALE;

		// Creating the Zen window
		Zen.create(xDim, yDim);

		displayMaze();
		displaySolve(SolveType.HUG_LEFT);
	}

	/**
	 * Returns a 2d array of characters that makes up the maze.
	 * @return a 2d String array with the characters of the array
	 */
	public static String[][] getMazeArray() {
		String mazeStr = maze.toString();
		String[] rows = mazeStr.split("\n");
		String[][] result = new String[rows.length][rows[0].length()];

		for (int ind = 0; ind < rows.length; ind++) {
			result[ind] = rows[ind].split("");
		}

		return result;
	}

	/**
	 * Displays the maze in Zen
	 */
	private static void displayMaze() {
		String[][] mazeArr = getMazeArray();
		String current;
		int xCoord, yCoord;

		setColor(WALL_COLOR);
		for (int x = 0; x < mazeArr.length; x++) {
			for (int y = 0; y < mazeArr[x].length; y++) {
				current = mazeArr[x][y];

				// X and Y coordinates swapped from array to Zen due to the way MazeMaker
				//		handles coordinates
				xCoord = y * SCALE;
				yCoord = x * SCALE;

				switch (current) {
				
				// Wall piece
				case "#":
					Zen.fillRect(xCoord, yCoord, SCALE, SCALE);
					continue;
					
				// End piece
				case "E":
					setColor(END_COLOR);
					Zen.fillRect(xCoord, yCoord, SCALE, SCALE);
					setColor(WALL_COLOR);
					continue;
					
				// Initial location piece
				case "X":
					setColor(CURRENT_COLOR);
					Zen.fillRect(xCoord, yCoord, SCALE, SCALE);
					setColor(WALL_COLOR);
					continue;
				default:
					continue;
				}
			}
		}
	}

	/**
	 * Displays the maze being solved using the given solve type.
	 * @param solveType Way to solve the maze
	 */
	private static void displaySolve(SolveType solveType) {
		// Represents the coordinates where the current piece used to be
		int seenX, seenY;
		
		// Represents the coordinates where the current piece is now
		int currX, currY;
		
		// Represents the coodinates of where the piece skipped over
		int midX, midY;
		
		// Keep on going while the maze is not finished
		while (!maze.isFinished()) {
			seenX = getSolveXScale();
			seenY = getSolveYScale();
			
			setColor(SEEN_COLOR);
			Zen.fillRect(seenX, seenY, SCALE, SCALE);
			
			switch (solveType) {
			case HUG_RIGHT: doHugRight();
			case HUG_LEFT: doHugLeft();
			case RECURSIVE: doRecursive();
			}
			
			currX = getSolveXScale();
			currY = getSolveYScale();
			
			// Finding coordinates of skipped piece
			midX = (seenX + currX) / 2;
			midY = (seenY + currY) / 2;
			
			setColor(SEEN_COLOR);
			Zen.fillRect(midX, midY, SCALE, SCALE);
			
			setColor(CURRENT_COLOR);
			Zen.fillRect(currX, currY, SCALE, SCALE);
			
			Zen.sleep(DELAY);
		}
	}
	
	/**
	 * Returns a mapped X coordinate from the current maze position to a Zen position
	 * @return X coordinate for Zen use
	 */
	private static int getSolveXScale() {
		return maze.getCurrentLocation().x() * SCALE * 2 + SCALE;
	}
	
	/**
	 * Returns a mapped Y coordinate from the current maze position to a Zen position
	 * @return Y coordinate for Zen use
	 */
	private static int getSolveYScale() {
		return yDim - (maze.getCurrentLocation().y() * SCALE * 2) - 2 * SCALE;
	}
	
	/**
	 * Executes the left wall hug solve.
	 */
	private static void doHugLeft() {
		maze.turnLeft();
        while (!maze.canMove()) {
            maze.turnRight();
        }
        maze.move();
	}
	
	/**
	 * Executes the right wall hug solve.
	 */
	private static void doHugRight() {
		maze.turnRight();
        while (!maze.canMove()) {
            maze.turnLeft();
        }
        maze.move();
	}
	
	/**
	 * Executes the recursive solve.
	 */
	private static void doRecursive() {
		return;
	}

	/**
	 * Types of solves
	 */
	private enum SolveType {
		/** Hugging the right wall. */
		HUG_RIGHT, 
		
		/** Hugging the left wall. */
		HUG_LEFT, 
		
		/** Recursive solve. */
		RECURSIVE;
	}

	/**
	 * Sets color of Zen.
	 * @param color Color to set Zen
	 */
	private static void setColor(Color color) {
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		Zen.setColor(r,g,b);
	}

	/**
	 * Prints a 2d array.
	 * @param arr Array to print
	 */
	private static void displayArr(String[][] arr) {
		for (int row = 0; row < arr.length; row++) {
			for (int col = 0; col < arr[row].length; col++) {
				System.out.print(arr[row][col] + " ");
			}
			System.out.println();
		}
	}

}
