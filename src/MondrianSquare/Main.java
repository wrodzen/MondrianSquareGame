package MondrianSquare;

import java.awt.*;

public class Main {
    public static void main(String[] args) {

        // assignment3.Block b1 = new assignment3.Block(0, 0, 16, 0, 2, null,[assignment3.Block UR, assignment3.Block UL, assignment3.Block LL, assignment3.Block LR]);
        Block blockDepth2 = new Block(0,2);
        blockDepth2.updateSizeAndPosition(16,0,0);
        blockDepth2.printBlock();
        Color[][] asd = blockDepth2.flatten();
        //System.out.println(asd[0][0]);
        blockDepth2.printColoredBlock();

//        assignment3.Block blockDepth3 = new assignment3.Block(0,3);
//        blockDepth3.updateSizeAndPosition(16, 0, 0);
//        // 1st test:
//        assignment3.Block b1 = blockDepth3.getSelectedBlock(2, 15, 1);
//        /* 2nd test */
//        // assignment3.Block b1 = blockDepth3.getSelectedBlock(3, 5, 2);
//        b1.printBlock();
//        //b1.printColoredBlock();
    }
}