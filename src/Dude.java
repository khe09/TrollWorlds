import processing.core.PImage;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class Dude extends AbstractAction implements Move{
    protected int resourceLimit;
    protected int resourceCount;

    public static final String DUDE_KEY = "dude";
    public static final int DUDE_ACTION_PERIOD = 0;
    public static final int DUDE_ANIMATION_PERIOD = 1;
    public static final int DUDE_LIMIT = 2;
    public static final int DUDE_NUM_PROPERTIES = 3;

    public int getResourceCount() {
        return resourceCount;
    }

    public int getResourceLimit() {
        return resourceLimit;
    }

    public void setResourceCount(int resourceCount) {
        this.resourceCount = resourceCount;
    }

    public void setResourceLimit(int resourceLimit) {
        this.resourceLimit = resourceLimit;
    }
    public Dude(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);

    }
    public Point nextPosition(WorldModel world, Point destPos) {
        AStarPathingStrategy path = new AStarPathingStrategy();
        Predicate<Point> canPassThrough = pt -> (world.withinBounds(pt) && !world.isOccupied(pt));
        BiPredicate<Point, Point> withinReach = (pt1, pt2) -> Point.adjacent(pt1, pt2);
        Function<Point, Stream<Point>> potentialNeighbors = point -> path.CARDINAL_NEIGHBORS.apply(point).filter(canPassThrough);

        if (path.computePath(position, destPos, canPassThrough, withinReach, potentialNeighbors).isEmpty()) {
            return position;
        } else {
            List<Point> paths = path.computePath(position, destPos, canPassThrough, withinReach, potentialNeighbors);
            return paths.get(0);
        }
    }

    public abstract boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore);
    public abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);
}