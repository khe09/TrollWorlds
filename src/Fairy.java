import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Fairy extends AbstractAction implements Move{
    public static final String FAIRY_KEY = "fairy";
    public static final int FAIRY_ANIMATION_PERIOD = 0;
    public static final int FAIRY_ACTION_PERIOD = 1;
    public static final int FAIRY_NUM_PROPERTIES = 2;

    public Fairy(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        super(id, position, images, actionPeriod, animationPeriod);
    }
    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fairyTarget = world.findNearest(this.getPosition(), ((entity -> entity instanceof Stump)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (this.moveTo(world, fairyTarget.get(), scheduler)) {

                Sapling sapling = (Sapling) new Sapling (Sapling.SAPLING_KEY + "_" + fairyTarget.get().getId(), tgtPos, imageStore.getImageList(Sapling.SAPLING_KEY), 0);

                world.addEntity(sapling);
                sapling.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this, new ActivityAction (this, world, imageStore), this.getActionPeriod());

    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (Point.adjacent(this.getPosition(), target.getPosition())) {
            world.removeEntity(scheduler, target);
            return true;
        } else {
            Point nextPos = nextPosition(world, ((Stump)target).getPosition());

            if (!this.getPosition().equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
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
}