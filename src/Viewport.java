public final class Viewport {
private int row;
private int col;
private int numRows;
private int numCols;
private static int rowDelta;
private static int colDelta;

public int getRow() {
    return row;
}
public int getCol() {
    return col;
}
public int getNumRows() {
    return numRows;
}
public int getNumCols() {
    return numCols;
}
public static int getRowDelta() {return rowDelta;}
public static int getColDelta() {return colDelta;}

public Viewport(int numRows, int numCols) {
    this.numRows = numRows;
    this.numCols = numCols;
    this.rowDelta = 0;
    this.colDelta = 0;
}
public void shift( int col, int row) {
    this.col = col;
    this.row = row;
}
public  boolean contains( Point p) {
    return p.getY() >= this.row && p.getY() < this.row + this.numRows && p.getX() >= this.col && p.getX()< this.col + this.numCols;
}

}
