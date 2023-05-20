package MondrianSquare;

import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;

public class Block {
 public static Random gen = new Random();
 private int xCoord;
 private int yCoord;
 private int size; // height/width of the square
 private int level; // the root (outer most block) is at level 0
 private int maxDepth;
 private Color color;
 private Block[] children; // {UR, UL, LL, LR}

 /*
  * These two constructors are here for testing purposes. 
  */
 public Block() {}
 
 public Block(int x, int y, int size, int lvl, int  maxD, Color c, Block[] subBlocks) {
  this.xCoord=x;
  this.yCoord=y;
  this.size=size;
  this.level=lvl;
  this.maxDepth = maxD;
  this.color=c;
  this.children = subBlocks;
 }
 
 

 /*
  * Creates a random block given its level and a max depth. 
  * 
  * xCoord, yCoord, size, and highlighted should not be initialized
  * (i.e. they will all be initialized by default)
  */
 public Block(int lvl, int maxDepth) {
  /*
   * ADD YOUR CODE HERE
   */
    this.level = lvl;
    this.maxDepth = maxDepth;
    if(lvl < maxDepth) {
        double randoNum = gen.nextDouble(0, 1);
        if (randoNum < Math.exp(-0.25 * this.level)) {
            this.children = new Block[4];
            for (int i = 0; i < 4; i++) {
                this.children[i] = new Block(lvl + 1, maxDepth);
            }
            return;
        }
    } else if (lvl > maxDepth) {
        throw new IllegalArgumentException();
    }
    // if lvl == maxDepth, none of the above will happen, only code below will be executed
    this.color = GameColors.BLOCK_COLORS[gen.nextInt(4)];
    this.children = new Block[0];
 }

 private static void coloredPrint(String message, Color color) {
  System.out.print(GameColors.colorToANSIColor(color));
  System.out.print(message);
  System.out.print(GameColors.colorToANSIColor(Color.WHITE));
 }

//  // recursive step:
//  if (children.length == 4) {
//
//
//   for (int i = 0; i < children.length; i++) {
//    this.children[i].size = halfSize; // why can't this be outside of for loop? I tried putting it out and didn't work
//
//    System.out.println(this.children[i].xCoord + "," + this.children[i].yCoord + "," + this.children[i].size);
//    if (children[i].children.length == 4 && children[i].level < maxDepth) {
//     //children[i].level++;
//     System.out.println("recursive call here");
//     System.out.println("the child's level is:" + children[i].level);
//     System.out.println(maxDepth);
////     System.out.println(this.children[i].xCoord);
////     System.out.println(this.children[i].yCoord);
////     System.out.println(this.children[i].size);
//     //level++;
//     System.out.println("input to method:" + this.children[i].xCoord + "," + this.children[i].yCoord + "," + this.children[i].size);
//     this.updateSizeAndPosition(halfSize, this.children[i].xCoord, this.children[i].yCoord);
//    }
//    System.out.println(this.children[i].xCoord);
//    System.out.println(this.children[i].yCoord);
//    System.out.println(this.children[i].size);
//    updateSizeAndPosition(halfSize, this.children[i].xCoord, this.children[i].yCoord);

 /*
  * Updates size and position for the block and all of its sub-blocks, while
  * ensuring consistency between the attributes and the relationship of the
  * blocks.
  *
  *  The size is the height and width of the block. (xCoord, yCoord) are the
  *  coordinates of the top left corner of the block.
  */
 public void updateSizeAndPosition (int size, int xCoord, int yCoord) {
  if (size <= 0) {
   throw new IllegalArgumentException();
  }

    //check if size is divisible by 2 or is size = 1
  if(size % 2 != 0) {
      if(size != 1) {
          throw new IllegalArgumentException();
      }
  }
  // base case:
  this.size = size;
  this.xCoord = xCoord;
  this.yCoord = yCoord;

  // recursive case:
  if (children.length == 4) {
   int halfSize = size / 2;

   this.children[0].xCoord = xCoord + halfSize;
   this.children[0].yCoord = yCoord;

   this.children[1].xCoord = xCoord;
   this.children[1].yCoord = yCoord;

   this.children[2].xCoord = xCoord;
   this.children[2].yCoord = yCoord + halfSize;

   this.children[3].xCoord = xCoord + halfSize;
   this.children[3].yCoord = yCoord + halfSize;

   for (int i = 0; i < 4; i++) {
	this.children[i].updateSizeAndPosition(halfSize, children[i].xCoord, children[i].yCoord);
   }
  }
 }

 /*
  * Returns a List of blocks to be drawn to get a graphical representation of this block.
  *
  * This includes, for each undivided assignment3.Block:
  * - one BlockToDraw in the color of the block
  * - another one in the FRAME_COLOR and stroke thickness 3
  *
  * Note that a stroke thickness equal to 0 indicates that the block should be filled with its color.
  *
  * The order in which the blocks to draw appear in the list does NOT matter.
  */
 public ArrayList<BlockToDraw> getBlocksToDraw() {
  ArrayList<BlockToDraw> arrList = new ArrayList<BlockToDraw>();

  //base case:
  if(children.length == 0) {
   //add fill colour
   arrList.add(new BlockToDraw(this.color, this.xCoord, this.yCoord, this.size, 0));
   arrList.add(new BlockToDraw(GameColors.FRAME_COLOR, this.xCoord, this.yCoord, this.size, 3));

   return arrList; // this arrList returns a collection of a singular block (if it is a leaf)
  }

  for (Block child : children) { // don't think children is the exact thing I should be putting in here, (might actually work if do this with a recursive method)
   arrList.addAll(child.getBlocksToDraw());
  }

  return arrList; // this arrList returns a collection of 4 blocks
 }
 
 /*
  * This method is provided and you should NOT modify it.
  */
 public BlockToDraw getHighlightedFrame() {
  return new BlockToDraw(GameColors.HIGHLIGHT_COLOR, this.xCoord, this.yCoord, this.size, 5);
 }

 /*
  * Return the assignment3.Block within this assignment3.Block that includes the given location
  * and is at the given level. If the level specified is lower than
  * the lowest block at the specified location, then return the block
  * at the location with the closest level value.
  *
  * The location is specified by its (x, y) coordinates. The lvl indicates
  * the level of the desired assignment3.Block. Note that if a assignment3.Block includes the location
  * (x, y), and that assignment3.Block is subdivided, then one of its sub-Blocks will
  * contain the location (x, y) too. This is why we need lvl to identify
  * which assignment3.Block should be returned.
  *
  * Input validation:
  * - this.level <= lvl <= maxDepth (if not throw exception)
  * - if (x,y) is not within this assignment3.Block, return null.
  */
 public Block getSelectedBlock(int x, int y, int lvl) {
	if (lvl < this.level || lvl > this.maxDepth) {
		throw new IllegalArgumentException();
	}

    //out of bounds
    if(x < this.xCoord || x >= this.xCoord +  this.size || y < this.yCoord || y >= this.yCoord + this.size) {
        return null;
    }

	// base case: looking for coordinates at the same level or lower
	if (lvl == this.level) {
		return this;
	}

	if (this.children.length == 0 && lvl > this.level) {
		return this;
	} else {
        int childSize = this.children[0].size;
        if ((this.children[1].xCoord <= x && x < this.children[1].xCoord + childSize) && (this.children[1].yCoord <= y && y < this.children[1].yCoord + childSize)) { // if clicked in the range of a square, return the deepest child where you clicked
            return this.children[1].getSelectedBlock(x, y, lvl);
        } else if ((this.children[0].xCoord <= x && x <= this.children[0].xCoord + childSize) && (this.children[0].yCoord <= y && y < this.children[0].yCoord + childSize)) { // if clicked in the range of a square, return the deepest child where you clicked
            return this.children[0].getSelectedBlock(x, y, lvl);
        } else if ((this.children[2].xCoord <= x && x < this.children[2].xCoord + childSize) && (this.children[2].yCoord <= y && y <= this.children[2].yCoord + childSize)) { // if clicked in the range of a square, return the deepest child where you clicked
            return this.children[2].getSelectedBlock(x, y, lvl);
        } else {
            return this.children[3].getSelectedBlock(x, y, lvl);
        }

//		for (int i = 0; i < 4; i++) {
//			// naming fields for legibility
//			int topLeftX = this.children[i].xCoord;
//			int topLeftY = this.children[i].yCoord;
//			int childSize = this.children[i].size;
//
//			if ((topLeftX <= x && x <= topLeftX + childSize) && (topLeftY <= y && y <= topLeftY + childSize)) { // if clicked in the range of a square, return the deepest child where you clicked
//				return this.children[i].getSelectedBlock(x, y, lvl);
//			} else {
//
//            }
//		}

        //System.out.println("this block size, x, y, level:" + this.size + " " + this.xCoord + " " + this.yCoord + " " + this.level);
        //this.printBlock();
		//throw new IllegalArgumentException(x + ", " + y + " , lvl: " + lvl + " is outside of range of all four children");
	}
 }
 
 /*
  * Swaps the child Blocks of this assignment3.Block.
  * If input is 1, swap vertically. If 0, swap horizontally.
  * If this assignment3.Block has no children, do nothing. The swap
  * should be propagate, effectively implementing a reflection
  * over the x-axis or over the y-axis.
  *
  */
 public void reflect(int direction) {
	//TODO: check direction first

	// base case:
	//if(this.children.length == 0) {
	//nothing
	//} else {
	if (direction == 0 && this.children.length == 4) { // reflect across x-axis
		//reflect top level blocks
		Block temp = this.children[1];
		this.children[1] = this.children[2];
		this.children[2] = temp;

		temp = this.children[0];
		this.children[0] = this.children[3];
		this.children[3] = temp;
		//reflect children
		for(int i = 0; i < 4; i++) {
			this.children[i].reflect(direction);
		}
	} else if (direction == 1 && this.children.length == 4) { // reflect across y-axis
		//reflect right side blocks
		Block temp = this.children[0];
		this.children[0] = this.children[1];
		this.children[1] = temp;

		temp = this.children[3];
		this.children[3] = this.children[2];
		this.children[2] = temp;
		//reflect children
		for(int i = 0; i < 4; i++) {
			this.children[i].reflect(direction);
		}
	} else if (direction == 0 || direction == 1 && this.children.length == 0) {
		// do nothing
	} else {
		throw new IllegalArgumentException();
	}

     this.updateSizeAndPosition(this.size, this.xCoord, this.yCoord);
//    //reflect children
//    for (int i = 0; i < 4; i++) {
//        this.children[i].reflect(direction);
//    }
	// other way to do this is the implementation that is used in the following method, called "rotate", which is much cleaner
 }
 
 /*
  * Rotate this assignment3.Block and all its descendants.
  * If the input is 1, rotate clockwise. If 0, rotate
  * counterclockwise. If this assignment3.Block has no children, do nothing.
  */
 public void rotate(int direction) {
	//TODO: check direction first

	// base case:
//    if(this.children.length == 0) {
//        //nothing
//    } else {
	if (direction == 1 && this.children.length == 4) { // rotate clockwise
		Block temp = this.children[0];
		this.children[0] = this.children[1];
		this.children[1] = this.children[2];
		this.children[2] = this.children[3];
		this.children[3] = temp;

//        //rotate children
//        for(int i = 0; i < 4; i++) {
//            this.children[i].rotate(direction);
//        }
	} else if (direction == 0 && this.children.length == 4) { // rotate counter-clockwise
		//TODO: finish this
		Block temp = this.children[0];
		this.children[0] = this.children[3];
		this.children[3] = this.children[2];
		this.children[2] = this.children[1];
		this.children[1] = temp;

//        //rotate children
//        for(int i = 0; i < 4; i++) {
//            this.children[i].rotate(direction);
//        }
	} else if (direction == 0 || direction == 1 && this.children.length == 0) {
		return;
	} else {
		throw new IllegalArgumentException();
	}
	//rotate children
	for(int i = 0; i < 4; i++) {
		this.children[i].rotate(direction);
	}

     this.updateSizeAndPosition(this.size, this.xCoord, this.yCoord);
 }
 
 /*
  * Smash this assignment3.Block.
  *
  * If this assignment3.Block can be smashed,
  * randomly generate four new children Blocks for it.
  * (If it already had children Blocks, discard them.)
  * Ensure that the invariants of the Blocks remain satisfied.
  *
  * A assignment3.Block can be smashed iff it is not the top-level assignment3.Block
  * and it is not already at the level of the maximum depth.
  *
  * Return True if this assignment3.Block was smashed and False otherwise.
  *
  */
 public boolean smash() {
	if (this.level == 0 || this.level >= maxDepth) {
		return false; // if didn't smash
	} else {
		this.children = new Block[4]; // if can smash, create assignment3.Block array of 4 children

		// make each child block with 1 level deeper than the block that was smashed
		for(int i = 0; i < 4; i++) {
			this.children[i] = new Block(this.level + 1, this.maxDepth);
		}

        this.updateSizeAndPosition(this.size, this.xCoord, this.yCoord);
		return true; // if did smash
	}
 }

// helper functions:
//public assignment3.Block getBlock(int x, int y) {
//     this.xCoord = x;
//     this.yCoord = y;
//     return this;
//}
 
 /*
  * Return a two-dimensional array representing this assignment3.Block as rows and columns of unit cells.
  *
  * Return and array arr where, arr[i] represents the unit cells in row i,
  * arr[i][j] is the color of unit cell in row i and column j.
  *
  * arr[0][0] is the color of the unit cell in the upper left corner of this assignment3.Block.
  */
 public Color[][] flatten() {
     int boardSideLength = (int)Math.pow(2,this.maxDepth);
	 int unitSideLength = this.size / boardSideLength;

     int oneUnitCell = (int)Math.pow(4,this.maxDepth); // total number of unit cells on entire board
    Color[][] arr = new Color[(int)Math.pow(2,this.maxDepth)][(int)Math.pow(2,this.maxDepth)];
    // arr[i] = all unit cells in row i
    // arr[i][j] = color of unit cell in row i and column j
    // i'th row: same y for multiple x's
    // j'th column: same x for multiple y's -> i = xCoord & j = yCoord
    for (int i = 0; i < Math.pow(2,this.maxDepth); i++) {
        for (int j = 0; j < Math.pow(2,this.maxDepth); j++) {
            //System.out.println("x: " + i + " y: " + j);
            arr[j][i] = getSelectedBlock(i * unitSideLength,j * unitSideLength, this.getMaxDepth()).color;
            //arr[i][j] = arr[i][j].getColor(, this.color);
            // arr[i][j] = getBlock(i,j).color; // use this with helper function below
            // I don't think you need to do any method recursively, as no one is gonna check if you did.
                //But for many methods implementing it recursively is significantly easier (including flatten imo)
        }
    }
    return arr;
  //return null;
 }
 
 // These two get methods have been provided. Do NOT modify them.
 public int getMaxDepth() {
  return this.maxDepth;
 }

 public int getLevel() {
  return this.level;
 }

 /*
  * The next 5 methods are needed to get a text representation of a block.
  * You can use them for debugging. You can modify these methods if you wish.
  */
 public String toString() {
  return String.format("pos=(%d,%d), size=%d, level=%d"
	, this.xCoord, this.yCoord, this.size, this.level);
 }

 public void printBlock() {
  this.printBlockIndented(0);
 }
 
 private void printBlockIndented(int indentation) {
  String indent = "";
  for (int i=0; i<indentation; i++) {
   indent += "\t";
  }

  if (this.children.length == 0) {
   // it's a leaf. Print the color!
   String colorInfo = GameColors.colorToString(this.color) + ", ";
   System.out.println(indent + colorInfo + this);
  } else {
   System.out.println(indent + this);
   for (Block b : this.children)
	b.printBlockIndented(indentation + 1);
  }
 }

 public void printColoredBlock(){
  Color[][] colorArray = this.flatten();
  for (Color[] colors : colorArray) {
   for (Color value : colors) {
	String colorName = GameColors.colorToString(value).toUpperCase();
	if(colorName.length() == 0){
	 colorName = "\u2588";
	}else{
	 colorName = colorName.substring(0, 1);
	}
	coloredPrint(colorName, value);
   }
   System.out.println();
  }
 }
 
}
