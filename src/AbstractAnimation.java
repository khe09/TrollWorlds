import processing.core.PImage;

import java.util.List;

public abstract class AbstractAnimation extends Entity {

    protected double animationPeriod;

    public AbstractAnimation(String id, Point position, List<PImage> images, double animationPeriod) {
        super(id, position, images);
        this.animationPeriod = animationPeriod;
    }
    public double getAnimationPeriod() {
        return this.animationPeriod;
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, new AnimationAction (this, 0), this.getAnimationPeriod());
    }

}