/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        FILE:   Test.java
        NAME:   KAH SHIUH TANG
        DATE:   Jan 29, 2020
  SAUCE CODE:   6
     PURPOSE:   

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
import java.awt.Color;
import javax.swing.*;

public class TestClass extends JFrame {

    public static void main(String[] args) {
        MinesweeperFrame mf1 = new MinesweeperFrame();
        mf1.setTitle("Minesweeper");
        ImageIcon icon = new ImageIcon("images/bombdeath.gif");
        mf1.setIconImage(icon.getImage());
        mf1.setBounds(50, 50, 1300, 950);
        mf1.setBackground(Color.BLACK);
        mf1.setResizable(false);
        mf1.setVisible(true);

    }

}
