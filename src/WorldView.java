import processing.core.PApplet;
import processing.core.PImage;

import java.util.Optional;

public final class WorldView {
    private final PApplet screen;
    WorldModel world;
    private final int tileWidth;
    private final int tileHeight;
    Viewport viewport;

    public WorldView(int numRows, int numCols, PApplet screen, WorldModel world, int tileWidth, int tileHeight) {
        this.screen = screen;
        this.world = world;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.viewport = new Viewport(numRows, numCols);
    }
    public Viewport getViewport(){ return this.viewport; }

    public  void shiftView( int colDelta, int rowDelta) {
        int newCol = clamp(this.viewport.getCol() + colDelta, 0, this.world.getNumCols() - this.viewport.getNumCols());
        int newRow = clamp(this.viewport.getRow() + rowDelta, 0, this.world.getNumRows() - this.viewport.getNumRows());

        this.viewport.shift(newCol, newRow);
    }
    public  Point worldToViewport( int col, int row) {
        return new Point(col - viewport.getCol(), row - viewport.getRow());
    }

    public Point viewportToWorld(int col, int row) {
        return new Point(col + viewport.getCol(), row + viewport.getRow());
    }

    public static int clamp(int value, int low, int high) {
        return Math.min(high, Math.max(value, low));
    }

    public void drawViewport() {
        this.drawBackground();
        this.drawEntities();
    }

    public void drawBackground() {
        for (int row = 0; row < this.viewport.getNumRows(); row++) {
            for (int col = 0; col < this.viewport.getNumCols(); col++) {
                Point worldPoint = this.viewportToWorld(col, row);
                Optional<PImage> image = this.world.getBackgroundImage( worldPoint);
                if (image.isPresent()) {
                    this.screen.image(image.get(), col * this.tileWidth, row * this.tileHeight);
                }
            }
        }
    }
    public void drawEntities() {
        for (Entity entity : this.world.getEntities()) {
            Point pos = entity.getPosition();

            if (this.viewport.contains(pos)) {
                Point viewPoint = this.worldToViewport( pos.getX(), pos.getY());
                this.screen.image(entity.getCurrentImage(entity), viewPoint.getX() * this.tileWidth, viewPoint.getY() * this.tileHeight);
            }
        }
    }
}
