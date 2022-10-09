
/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        FILE:   MinesweeperFrame.java
        NAME:   KAH SHIUH TANG
        DATE:   Jan 29, 2020
  SAUCE CODE:   6
     PURPOSE:   

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class MinesweeperFrame extends javax.swing.JFrame implements ActionListener {

    Timer t1 = new Timer(1000, this);
    public int level;
    private boolean right = false;
    private int flagsRemaining;
    private boolean firstClick = true;
    public final int BUTTON_SIZE = 10;
    public final int LEVEL1_SIZE = 9;
    public final int LEVEL1_MINES = 10;
    public final int LEVEL1_PANEL_SIZE = 90;
    public final int LEVEL2_SIZE = 16;
    public final int LEVEL2_MINES = 40;
    public final int LEVEL2_PANEL_SIZE = 50;
    public final int LEVEL3_SIZE = 24;
    public final int LEVEL3_MINES = 49;
    public final int LEVEL3_PANEL_SIZE = 35;
    public int picSize = 40;
    private ImageIcon bomb, empty, flag, bombdeath, logo;
    private ImageIcon[] numbers = new ImageIcon[10];
    private ImageIcon[] clockNums = new ImageIcon[10];
    private ImageIcon[] flagNums = new ImageIcon[10];
    private JButton[][] butArray;
    private boolean[][] shown;
    private boolean[][] flagged;
    private JLabel[] clock;
    private JLabel[] flagCount;
    private JLabel logoLBL;
    private int timeElapsed;

    MinesweeperModel model1 = new MinesweeperModel(LEVEL3_SIZE, LEVEL3_MINES);
    MouseListener ms = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent me) {
        }

        @Override
        public void mousePressed(MouseEvent me) {
            // Checks to see if it is a rightClick
            if (SwingUtilities.isRightMouseButton(me)) {
                right = true;
            }
            // Loops through to find source of click
            for (int i = 0; i < model1.getSize(); i++) {
                for (int j = 0; j < model1.getSize(); j++) {
                    // Correct Button Found
                    if (me.getSource() == butArray[i][j]) {
                        // Flags square
                        if (right == true && !shown[i][j] && !model1.getSquare(i, j).getRevealedStatus()
                                && flagsRemaining > 0 && firstClick == false) {
                            butArray[i][j].setIcon(flag);
                            shown[i][j] = true;
                            flagged[i][j] = true;
                            flagsRemaining--;
                            updateFlagCount();
                            // Unflags Square
                        } else if (right == true && shown[i][j] && !model1.getSquare(i, j).getRevealedStatus()) {
                            butArray[i][j].setIcon(empty);
                            shown[i][j] = false;
                            flagged[i][j] = false;
                            flagsRemaining++;
                            updateFlagCount();
                        } else {
                            flagged[i][j] = false;
                        }

                    }
                }
            }
            // resets rightClick setting
            right = false;
        }

        @Override
        public void mouseReleased(MouseEvent me) {
        }

        @Override
        public void mouseEntered(MouseEvent me) {
        }

        @Override
        public void mouseExited(MouseEvent me) {
        }

    };

    public void actionPerformed(ActionEvent ae) {
        // Loops through to find correct Button Source
        for (int r = 0; r < model1.getSize(); r++) {
            for (int c = 0; c < model1.getSize(); c++) {
                if (ae.getSource() == butArray[r][c]) {
                    // First Click, starts clock, sets start point, updates flags left
                    if (firstClick == true) {
                        t1.start();
                        model1.setStart(r, c);
                        updateFlagCount();
                        firstClick = false;
                    }
                    // Uncovers surrounding squares everytime
                    model1.uCS(r, c);
                    // Loops through to find places to reveal
                    for (int i = 0; i < model1.getSize(); i++) {
                        for (int j = 0; j < model1.getSize(); j++) {
                            if (model1.getSquare(i, j).getRevealedStatus() == true && !shown[i][j]) {
                                // For end game checker
                                if (!flagged[i][j]) {
                                    shown[i][j] = true;
                                }
                                // If you hit a mine
                                if (model1.getSquare(i, j).getSurroundingBombs() == -1) {
                                    // Clock stops
                                    t1.stop();
                                    // Reveals rest of bombs
                                    for (int ii = 0; ii < model1.getSize(); ii++) {
                                        for (int jj = 0; jj < model1.getSize(); jj++) {
                                            if (model1.getSquare(ii, jj).getMineStatus()) {
                                                butArray[ii][jj].setIcon(bomb);
                                            }
                                            // Not sure about this line
                                            if (flagged[ii][jj]) {
                                                shown[ii][jj] = false;
                                            }
                                        }
                                    }
                                    // The square that your bomb was clicked on
                                    butArray[i][j].setIcon(bombdeath);
                                } else {
                                    // Reveals numbers
                                    butArray[i][j].setIcon(numbers[model1.getSquare(i, j).getSurroundingBombs()]);
                                }

                            }
                            if (flagged[i][j] && model1.getSquare(i, j).getSurroundingBombs() == 0
                                    && model1.getSquare(i, j).getRevealedStatus()) {
                                butArray[i][j].setIcon(numbers[model1.getSquare(i, j).getSurroundingBombs()]);
                            }
                            if (flagged[i][j] && model1.getSquare(i, j).getRevealedStatus()) {
                                butArray[i][j].setIcon(numbers[model1.getSquare(i, j).getSurroundingBombs()]);
                            }
                        }
                    }

                }
            }
        }
        // Stops clock if game has been won
        if (checkWin()) {
            t1.stop();
        }
        // Clock is set, depends on variable timeElapsed
        if (ae.getSource() == t1) {
            timeElapsed++;
            if (timeElapsed >= 1000) {
                clock[0].setIcon(clockNums[Integer.parseInt(String.valueOf(timeElapsed).substring(0, 1))]);
                clock[1].setIcon(clockNums[Integer.parseInt(String.valueOf(timeElapsed).substring(1, 2))]);
                clock[2].setIcon(clockNums[Integer.parseInt(String.valueOf(timeElapsed).substring(2, 3))]);
                clock[3].setIcon(clockNums[timeElapsed % 10]);
            } else if (timeElapsed >= 100) {
                clock[1].setIcon(clockNums[Integer.parseInt(String.valueOf(timeElapsed).substring(0, 1))]);
                clock[2].setIcon(clockNums[Integer.parseInt(String.valueOf(timeElapsed).substring(1, 2))]);
                clock[3].setIcon(clockNums[timeElapsed % 10]);
            } else if (timeElapsed >= 10) {
                clock[2].setIcon(clockNums[timeElapsed / 10]);
                clock[3].setIcon(clockNums[timeElapsed % 10]);
            } else {
                clock[3].setIcon(clockNums[timeElapsed % 10]);
            }
        }
    }

    public MinesweeperFrame() {
        initComponents();
        loadCards(LEVEL3_PANEL_SIZE);
        initialize(model1.getSize());
    }

    // Updates the flag count in GUI
    public void updateFlagCount() {
        if (flagsRemaining > 99) {
            flagCount[0].setIcon(flagNums[Integer.parseInt(String.valueOf(flagsRemaining).substring(0, 1))]);
            flagCount[1].setIcon(flagNums[Integer.parseInt(String.valueOf(flagsRemaining).substring(1, 2))]);
            flagCount[2].setIcon(flagNums[Integer.parseInt(String.valueOf(flagsRemaining).substring(2, 3))]);
        } else if (flagsRemaining > 9) {
            flagCount[0].setIcon(flagNums[0]);
            flagCount[1].setIcon(flagNums[Integer.parseInt(String.valueOf(flagsRemaining).substring(0, 1))]);
            flagCount[2].setIcon(flagNums[Integer.parseInt(String.valueOf(flagsRemaining).substring(1, 2))]);
        } else {
            flagCount[0].setIcon(flagNums[0]);
            flagCount[1].setIcon(flagNums[0]);
            flagCount[2].setIcon(flagNums[Integer.parseInt(String.valueOf(flagsRemaining).substring(0, 1))]);
        }
    }

    // Loads cards
    // Self explanatory, parameter is the sizing constant you are using
    public void loadCards(int cons) {
        try {
            Image image = ImageIO.read(new File("images/bomb.png"));
            Image image1 = image.getScaledInstance(cons, cons, java.awt.Image.SCALE_SMOOTH);
            bomb = new ImageIcon(image1);
            image = ImageIO.read(new File("images/bombdeath.gif"));
            image1 = image.getScaledInstance(cons, cons, java.awt.Image.SCALE_SMOOTH);
            bombdeath = new ImageIcon(image1);
            image = ImageIO.read(new File("images/logo.jpg"));
            image1 = image.getScaledInstance(350, 150, java.awt.Image.SCALE_SMOOTH);
            logo = new ImageIcon(image1);
            image = ImageIO.read(new File("images/flag.png"));
            image1 = image.getScaledInstance(cons, cons, java.awt.Image.SCALE_SMOOTH);
            flag = new ImageIcon(image1);
            image = ImageIO.read(new File("images/empty.png"));
            image1 = image.getScaledInstance(cons, cons, java.awt.Image.SCALE_SMOOTH);
            empty = new ImageIcon(image1);
            for (int i = 0; i < 9; i++) {
                String name = "images/";
                name = name + i + ".png";
                image = ImageIO.read(new File(name));
                image1 = image.getScaledInstance(cons, cons, java.awt.Image.SCALE_SMOOTH);
                numbers[i] = new ImageIcon(image1);
                name = "images/moves" + i + ".gif";
                image = ImageIO.read(new File(name));
                image1 = image.getScaledInstance(60, 120, java.awt.Image.SCALE_SMOOTH);
                clockNums[i] = new ImageIcon(image1);
                name = "images/time";
                name = name + i + ".gif";
                image = ImageIO.read(new File(name));
                image1 = image.getScaledInstance(60, 120, java.awt.Image.SCALE_SMOOTH);
                flagNums[i] = new ImageIcon(image1);
            }
            image = ImageIO.read(new File("images/time9.gif"));
            image1 = image.getScaledInstance(60, 120, java.awt.Image.SCALE_SMOOTH);
            // Nine doesn't work because there is a nine for the suurrounding bombs icon
            flagNums[9] = new ImageIcon(image1);
            image = ImageIO.read(new File("images/moves9.gif"));
            image1 = image.getScaledInstance(60, 120, java.awt.Image.SCALE_SMOOTH);
            clockNums[9] = new ImageIcon(image1);
        } catch (IOException ex) {
            Logger.getLogger(MinesweeperFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Checks if the game is over
    public boolean checkWin() {
        for (int i = 0; i < model1.getSize(); i++) {
            for (int j = 0; j < model1.getSize(); j++) {
                // Returns false if not all squares that aren't bombs have not been revealed
                if (!model1.getSquare(i, j).getMineStatus() && !shown[i][j]) {
                    System.out.println("2");
                    return false;
                }
                // Returns false if not all bombs have been flagged
                if (model1.getSquare(i, j).getMineStatus() != flagged[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    // scrw you mr hanley iom done with thi opressibve behavior im DONE Done im
    // about to call CPS on you and report you on the GRAPEVINEEEEEE
    private void initialize(int size) {
        // Default level is level 3
        flagsRemaining = LEVEL3_MINES;
        logoLBL = new JLabel();
        logoLBL.setIcon(logo);
        titlePAN.add(logoLBL);
        gamePAN.setLayout(new GridLayout(size, size));
        butArray = new JButton[size][size];
        shown = new boolean[size][size];
        flagged = new boolean[size][size];
        clock = new JLabel[4];
        flagCount = new JLabel[3];
        // Creating Array of Buttons and Listeners, Putting them on screen
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                butArray[r][c] = new JButton();
                shown[r][c] = false;
                flagged[r][c] = false;
                butArray[r][c].addActionListener(this);
                butArray[r][c].addMouseListener(ms);
                butArray[r][c].setSize(BUTTON_SIZE, BUTTON_SIZE);
                butArray[r][c].setIcon(empty);
                gamePAN.add(butArray[r][c]);
            }
        }
        // Clock Pics
        for (int i = 0; i < 4; i++) {
            clock[i] = new JLabel();
            clock[i].setSize(100, 200);
            clock[i].setIcon(clockNums[0]);
            clockPAN.add(clock[i]);
        }
        // Flag Pics
        for (int i = 0; i < 3; i++) {
            flagCount[i] = new JLabel();
            flagCount[i].setIcon(flagNums[0]);
            flagsPAN.add(flagCount[i]);
        }
    }

    public void reset() {
        // Resets flag count
        flagsRemaining = model1.getMines();
        updateFlagCount();
        // Resets arrays
        for (int i = 0; i < model1.getSize(); i++) {
            for (int j = 0; j < model1.getSize(); j++) {
                shown[i][j] = false;
                flagged[i][j] = false;
                butArray[i][j].setIcon(empty);
            }
        }
        firstClick = true;
        t1.stop();
        timeElapsed = 0;
        // Icons are set to original state
        clock[0].setIcon(clockNums[timeElapsed / 1000]);
        clock[1].setIcon(clockNums[timeElapsed / 100]);
        clock[2].setIcon(clockNums[timeElapsed / 10]);
        clock[3].setIcon(clockNums[timeElapsed % 10]);
        flagCount[0].setIcon(flagNums[0]);
        flagCount[1].setIcon(flagNums[0]);
        flagCount[2].setIcon(flagNums[0]);
        // Resets Model
        model1.reset();
    }

    public void resetFrame(int size) {
        gamePAN.removeAll();
        gamePAN.setLayout(new GridLayout(size, size));
        gamePAN.revalidate();
        butArray = new JButton[size][size];
        shown = new boolean[size][size];
        flagged = new boolean[size][size];
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                butArray[r][c] = new JButton();
                shown[r][c] = false;
                flagged[r][c] = false;
                butArray[r][c].addActionListener(this);
                butArray[r][c].addMouseListener(ms);
                butArray[r][c].setSize(BUTTON_SIZE, BUTTON_SIZE);
                butArray[r][c].setIcon(empty);
                gamePAN.add(butArray[r][c]);
            }
        }
        revalidate();
        repaint();
    }

    public void setLevel(int mines, int size) {
        // Level Chooser
        flagsRemaining = mines;
        model1.setBombCount(mines);
        model1.setSize(size);
        // Constant for picture sizes
        int siz = (int) (size * 1.275);
        loadCards(1000 / (siz));
        resetFrame(size);
        reset();
    }

    public void setLevel(int lev) {
        // Default level setters
        switch (lev) {
            case 1:
                level = lev;
                model1.setBombCount(LEVEL1_MINES);
                flagsRemaining = LEVEL1_MINES;
                model1.setSize(LEVEL1_SIZE);
                loadCards(LEVEL1_PANEL_SIZE);
                resetFrame(model1.getSize());
                reset();
                break;
            case 2:
                level = lev;
                model1.setBombCount(LEVEL2_MINES);
                flagsRemaining = LEVEL2_MINES;
                model1.setSize(LEVEL2_SIZE);
                loadCards(LEVEL2_PANEL_SIZE);
                resetFrame(model1.getSize());
                reset();
                break;
            case 3:
                level = lev;
                model1.setBombCount(LEVEL3_MINES);
                flagsRemaining = LEVEL3_MINES;
                model1.setSize(LEVEL3_SIZE);
                loadCards(LEVEL3_PANEL_SIZE);
                resetFrame(model1.getSize());
                reset();
                break;

        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        gamePAN = new javax.swing.JPanel();
        clockPAN = new javax.swing.JPanel();
        titlePAN = new javax.swing.JPanel();
        minesLBL = new javax.swing.JLabel();
        flagsPAN = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        level1BUT = new javax.swing.JButton();
        level2BUT = new javax.swing.JButton();
        level3BUT = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        sizeTF = new javax.swing.JTextField();
        minesTF = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        setCustomGameBUT = new javax.swing.JButton();
        resetBUT = new javax.swing.JButton();

        jButton1.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        getContentPane().setLayout(null);

        gamePAN.setLayout(null);
        getContentPane().add(gamePAN);
        gamePAN.setBounds(40, 40, 800, 800);
        getContentPane().add(clockPAN);
        clockPAN.setBounds(870, 220, 410, 160);
        getContentPane().add(titlePAN);
        titlePAN.setBounds(880, 30, 400, 190);
        getContentPane().add(minesLBL);
        minesLBL.setBounds(1080, 930, 100, 40);
        getContentPane().add(flagsPAN);
        flagsPAN.setBounds(920, 660, 300, 180);

        jPanel1.setLayout(null);

        level1BUT.setBackground(new java.awt.Color(0, 0, 0));
        level1BUT.setForeground(new java.awt.Color(255, 255, 255));
        level1BUT.setText("Level 1");
        level1BUT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                level1BUTActionPerformed(evt);
            }
        });
        jPanel1.add(level1BUT);
        level1BUT.setBounds(70, 20, 80, 32);

        level2BUT.setBackground(new java.awt.Color(0, 0, 0));
        level2BUT.setForeground(new java.awt.Color(255, 255, 255));
        level2BUT.setText("Level 2");
        level2BUT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                level2BUTActionPerformed(evt);
            }
        });
        jPanel1.add(level2BUT);
        level2BUT.setBounds(150, 20, 80, 32);

        level3BUT.setBackground(new java.awt.Color(0, 0, 0));
        level3BUT.setForeground(new java.awt.Color(255, 255, 255));
        level3BUT.setText("Level 3");
        level3BUT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                level3BUTActionPerformed(evt);
            }
        });
        jPanel1.add(level3BUT);
        level3BUT.setBounds(230, 20, 80, 32);

        jLabel1.setFont(new java.awt.Font("Microsoft JhengHei Light", 1, 12)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Mines");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(1030, 920, 100, 40);

        sizeTF.setFont(new java.awt.Font("Ebrima", 1, 24)); // NOI18N
        sizeTF.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(sizeTF);
        sizeTF.setBounds(70, 100, 120, 30);

        minesTF.setFont(new java.awt.Font("Ebrima", 1, 24)); // NOI18N
        minesTF.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(minesTF);
        minesTF.setBounds(200, 100, 110, 30);

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("CUSTOM SIZE");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(90, 80, 100, 20);

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Ebrima", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("CUSTOM MINES");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(210, 80, 100, 20);

        setCustomGameBUT.setBackground(new java.awt.Color(0, 0, 0));
        setCustomGameBUT.setFont(new java.awt.Font("Ebrima", 1, 24)); // NOI18N
        setCustomGameBUT.setForeground(new java.awt.Color(255, 255, 255));
        setCustomGameBUT.setText("Set Custom Game");
        setCustomGameBUT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setCustomGameBUTActionPerformed(evt);
            }
        });
        jPanel1.add(setCustomGameBUT);
        setCustomGameBUT.setBounds(70, 140, 240, 40);

        resetBUT.setBackground(new java.awt.Color(0, 0, 0));
        resetBUT.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        resetBUT.setForeground(new java.awt.Color(255, 255, 255));
        resetBUT.setText("Reset");
        resetBUT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetBUTActionPerformed(evt);
            }
        });
        jPanel1.add(resetBUT);
        resetBUT.setBounds(110, 190, 170, 63);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(880, 380, 380, 290);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void resetBUTActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_resetBUTActionPerformed
        // TODO add your handling code here:
        reset();
    }// GEN-LAST:event_resetBUTActionPerformed

    private void level2BUTActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_level2BUTActionPerformed
        // TODO add your handling code here:
        setLevel(2);
        repaint();
    }// GEN-LAST:event_level2BUTActionPerformed

    private void level1BUTActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_level1BUTActionPerformed
        // TODO add your handling code here:
        setLevel(1);
        repaint();
    }// GEN-LAST:event_level1BUTActionPerformed

    private void level3BUTActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_level3BUTActionPerformed
        // TODO add your handling code here:
        setLevel(3);
        repaint();
    }// GEN-LAST:event_level3BUTActionPerformed

    private void setCustomGameBUTActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_setCustomGameBUTActionPerformed
        // TODO add your handling code here:
        int size = Integer.parseInt(sizeTF.getText());
        int mines = Integer.parseInt(minesTF.getText());
        if (size * size > mines) {
            setLevel(mines, size);
        }

    }// GEN-LAST:event_setCustomGameBUTActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MinesweeperFrame.class.getName()).log(java.util.logging.Level.SEVERE,
                    null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MinesweeperFrame.class.getName()).log(java.util.logging.Level.SEVERE,
                    null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MinesweeperFrame.class.getName()).log(java.util.logging.Level.SEVERE,
                    null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MinesweeperFrame.class.getName()).log(java.util.logging.Level.SEVERE,
                    null, ex);
        }
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MinesweeperFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel clockPAN;
    private javax.swing.JPanel flagsPAN;
    private javax.swing.JPanel gamePAN;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton level1BUT;
    private javax.swing.JButton level2BUT;
    private javax.swing.JButton level3BUT;
    private javax.swing.JLabel minesLBL;
    private javax.swing.JTextField minesTF;
    private javax.swing.JButton resetBUT;
    private javax.swing.JButton setCustomGameBUT;
    private javax.swing.JTextField sizeTF;
    private javax.swing.JPanel titlePAN;
    // End of variables declaration//GEN-END:variables

}
