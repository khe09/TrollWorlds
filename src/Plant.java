import processing.core.PImage;
import java.util.List;

public abstract class Plant extends AbstractAction implements Transform {
    protected int health;
    protected int healthLimit;
    public Plant(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod, int health, int healthLimit) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.health = health;
        this.healthLimit = healthLimit;
    }

    public int getHealth() {
        return health;
    }
    public int getHealthLimit() {
        return healthLimit;
    }
    public void setHealth(int health) {
        this.health = health;
    }
    public void setHealthLimit(int healthLimit) {
        this.healthLimit = healthLimit;
    }

}