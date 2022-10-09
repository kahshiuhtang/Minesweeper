/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        FILE:   Square.java
        NAME:   KAH SHIUH TANG
        DATE:   Jan 29, 2020
  SAUCE CODE:   6
     PURPOSE:   

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
public class Square {
    //How many mines, whether square was revealed, how many surrounding mines
    private boolean minePresent;
    private boolean revealed;
    private int surroundingBombs;
    
    public Square(){
        minePresent = false;
        revealed = false;
        surroundingBombs = 0;
    }
    public Square(boolean status){
        minePresent = status;
        revealed = false;
        surroundingBombs = 0;
    }
    public boolean getMineStatus(){
        return minePresent;
    }
    public void setMine(boolean status){
        minePresent = status;
    }
    public void setRevealed(boolean status){
        revealed = status;
    }
    public boolean getRevealedStatus(){
        return revealed;
    }
    public int getSurroundingBombs(){
        return surroundingBombs;
    }
    public void setSurroundingBombs(int sb){
        surroundingBombs = sb;
    }

}
