import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import processing.core.*;

public final class VirtualWorld extends PApplet {
    private static String[] ARGS;

    private static final int VIEW_WIDTH = 640;
    private static final int VIEW_HEIGHT = 480;
    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;

    private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;

    private static final String IMAGE_LIST_FILE_NAME = "imagelist";
    private static final String DEFAULT_IMAGE_NAME = "background_default";
    private static final int DEFAULT_IMAGE_COLOR = 0x808080;

    private static final String FAST_FLAG = "-fast";
    private static final String FASTER_FLAG = "-faster";
    private static final String FASTEST_FLAG = "-fastest";
    private static final double FAST_SCALE = 0.5;
    private static final double FASTER_SCALE = 0.25;
    private static final double FASTEST_SCALE = 0.10;

    private String loadFile = "world.sav";
    private long startTimeMillis = 0;
    private double timeScale = 1.0;
    private ImageStore imageStore;
    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;
    public static final String swamp="swamp";


    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        parseCommandLine(ARGS);
        loadImages(IMAGE_LIST_FILE_NAME);
        loadWorld(loadFile, this.imageStore);

        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH, TILE_HEIGHT);
        this.scheduler = new EventScheduler();
        this.startTimeMillis = System.currentTimeMillis();
        this.scheduleActions(world, scheduler, imageStore);
    }
    public void update(double frameTime){
        scheduler.updateOnTime( frameTime);
    }

    // Just for debugging and for P5
    // Be sure to refactor this method as appropriate
    public void mousePressed() {
            Point pressed = mouseToPoint(mouseX, mouseY);

            if(!checkDude(pressed))
            {
                String id1 = "swamp";
                world.setBackground(pressed, new Background(id1, imageStore.getImageList(id1)));

                for(Point point:getNeighbors(pressed))
                {
                    if(world.getOccupancyCell(point) != null)
                    {
                        {
                            checkDude(point);
                        }
                    }
                    world.setBackground(point, new Background(id1, imageStore.getImageList(id1)));
                    Entity dwarf = new Dwarf("dwarf", pressed,1,1, imageStore.getImageList("dwarf"),1);

                    world.addEntity(dwarf);
                    ((Dwarf)dwarf).scheduleActions(scheduler, world, imageStore);


                }
            }

        }
    private boolean checkDude(Point pressed) {
        Optional<Entity> entityOptional = world.getOccupant(pressed);
        if (entityOptional.isPresent()) {
            Entity entity = entityOptional.get();
            //System.out.println(entity.getId());


            if (entity instanceof Dude) {
                world.removeEntityAt(entity.getPosition());
                scheduler.unscheduleAllEvents(entity);

                Entity troll = new TrollNotFull("troll", pressed,1,1,720, imageStore.getImageList("troll"));

                world.addEntity(troll);
                ((TrollNotFull)troll).scheduleActions(scheduler, world, imageStore);


                return true;
            }
            if (entity instanceof House) {
                world.removeEntityAt(entity.getPosition());
                scheduler.unscheduleAllEvents(entity);

                Entity Cottage = new Cottage("cottage", pressed, imageStore.getImageList("cottage"));

                world.addEntity(Cottage);


                return true;
            }

            if (entity instanceof Tree) {
                world.removeEntityAt(entity.getPosition());
                scheduler.unscheduleAllEvents(entity);

                Entity mushroom = new Mushroom("mushrooms", pressed,1,1,1, imageStore.getImageList("mushrooms"));

                world.addEntity(mushroom);
                ((Mushroom)mushroom).scheduleActions(scheduler, world, imageStore);


                return true;

            }

        }
    return false;
    }
    private List<Point> getNeighbors(Point point)
    {
        List<Point> Neighbors = new ArrayList<>();

        for(int i=-1; i<=1; i++)
        {
            for(int j=-1; j<=1; j++)
            {
                Point temp = new Point(point.getX() + i, point.getY() + j);
                if(world.withinBounds(temp) && !(i==0 && j==0))
                {
                    Neighbors.add(temp);
                }
            }
        }

        return Neighbors;
    }
    public void draw() {
        double appTime = (System.currentTimeMillis() - startTimeMillis) * 0.001;
        double frameTime = (appTime - scheduler.getCurrentTime())/timeScale;
        this.update(frameTime);
        view.drawViewport();
    }
    public void scheduleActions(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        for (Entity entity : world.getEntities()) {
            if (entity instanceof AbstractAnimation) {
                ((AbstractAnimation) entity).scheduleActions(scheduler, world, imageStore);
            }
        }
    }
    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP -> dy -= 1;
                case DOWN -> dy += 1;
                case LEFT -> dx -= 1;
                case RIGHT -> dx += 1;
            }
            view.shiftView( dx, dy);
        }
    }
    private Point mouseToPoint(int x, int y)
    {
        return new Point((x/(TILE_WIDTH)+Viewport.getColDelta()), (y/TILE_HEIGHT)+Viewport.getRowDelta());
    }

    public static Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME, imageStore.getImageList( DEFAULT_IMAGE_NAME));
    }

    public static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        Arrays.fill(img.pixels, color);
        img.updatePixels();
        return img;
    }

    public void loadWorld(String file, ImageStore imageStore) {
        this.world = new WorldModel();
        try {
            Scanner in = new Scanner(new File(file));
            world.load( in, imageStore, createDefaultBackground(imageStore));
        } catch (FileNotFoundException e) {
            Scanner in = new Scanner(file);
            world.load( in, imageStore, createDefaultBackground(imageStore));
        }
    }
    public void loadImages(String filename) {
        this.imageStore = new ImageStore(createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
        try {
            Scanner in = new Scanner(new File(filename));
            imageStore.loadImages(in,this);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public void parseCommandLine(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG -> timeScale = Math.min(FAST_SCALE, timeScale);
                case FASTER_FLAG -> timeScale = Math.min(FASTER_SCALE, timeScale);
                case FASTEST_FLAG -> timeScale = Math.min(FASTEST_SCALE, timeScale);
                default -> loadFile = arg;
            }
        }
    }

    public static void main(String[] args) {
        VirtualWorld.ARGS = args;
        PApplet.main(VirtualWorld.class);
    }

    public static List<String> headlessMain(String[] args, double lifetime){
        VirtualWorld.ARGS = args;

        VirtualWorld virtualWorld = new VirtualWorld();
        virtualWorld.setup();
        virtualWorld.update(lifetime);

        return virtualWorld.world.log();
    }
}
