import processing.core.PImage;

import java.util.List;


public class Cottage extends Entity{
    public static final String COTTAGE_KEY = "cottage";
    public static final int COTTAGE_NUM_PROPERTIES = 0;

    public Cottage(String id, Point position, List<PImage> images) {
        super(id, position, images);
    }
    public static Entity createHouse(String id, Point position, List<PImage> images) {
        return new House(id, position, images);
    }

}