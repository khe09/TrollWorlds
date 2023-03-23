import processing.core.PImage;
import java.util.Random;
import java.util.List;

public class Mushroom extends Plant {
    public static final String MUSHROOM_KEY = "mushroom";
    public static final int MUSHROOM_ANIMATION_PERIOD = 0;
    public static final int MUSHROOM_ACTION_PERIOD = 1;
    public static final int MUSHROOM_HEALTH = 2;
    public static final int MUSHROOM_NUM_PROPERTIES = 3;
    private static final double MUSHROOM_ANIMATION_MAX = 0.600;
    private static final double MUSHROOM_ANIMATION_MIN = 0.050;
    private static final double MUSHROOM_ACTION_MAX = 1.400;
    private static final double MUSHROOM_ACTION_MIN = 1.000;
    private static final int MUSHROOM_HEALTH_MAX = 3;
    private static final int MUSHROOM_HEALTH_MIN = 1;

    public static String getMushroomKey() {
        return MUSHROOM_KEY;
    }
    public static int getMushroomAnimationPeriod() {
        return MUSHROOM_ANIMATION_PERIOD;
    }
    public static int getMushroomActionPeriod() {
        return MUSHROOM_ACTION_PERIOD;
    }
    public static int getMushroomHealth() {
        return MUSHROOM_HEALTH;
    }
    public static int getMushroomNumProperties() {
        return MUSHROOM_NUM_PROPERTIES;
    }
    public static double getMushroomAnimationMax() {
        return MUSHROOM_ANIMATION_MAX;
    }
    public static double getMushroomAnimationMin() {
        return MUSHROOM_ANIMATION_MIN;
    }
    public static double getMushroomActionMax() {
        return MUSHROOM_ACTION_MAX;
    }
    public static double getMushroomActionMin() {
        return MUSHROOM_ACTION_MIN;
    }

    public static int getMushroomHealthMax() {
        return MUSHROOM_HEALTH_MAX;
    }

    public static int getMushroomHealthMin() {
        return MUSHROOM_HEALTH_MIN;
    }

    public Mushroom(String id, Point position, double actionPeriod, double animationPeriod, int health, List<PImage> images) {
        super(id, position, images, actionPeriod, animationPeriod, health, 0);
    }

    public static Entity createMushroom(String id, Point position, double actionPeriod, double animationPeriod, int health, List<PImage> images) {
        return new Tree(id, position, actionPeriod, animationPeriod, health, images);
    }

    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.health <= 0) {
            Entity stump = new Stump(Stump.STUMP_KEY + "_" + this.id, this.position, imageStore.getImageList(Stump.STUMP_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(stump);
            return true;
        }
        return false;
    }
    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        if (!this.transform(world, scheduler, imageStore)) {

            scheduler.scheduleEvent(this, new ActivityAction(this, world, imageStore), this.actionPeriod);
        }
    }
}

