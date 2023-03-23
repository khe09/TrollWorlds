public class ActivityAction extends Action{
    private AbstractAction entity;
    private WorldModel world;
    private ImageStore imageStore;

    public ActivityAction(AbstractAction entity, WorldModel world, ImageStore imageStore){
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }
    @Override
    public void executeAction(EventScheduler scheduler) {
        entity.executeActivity(this.world, this.imageStore, scheduler);
    }
    public static ActivityAction createActivityAction(AbstractAction entity, WorldModel world, ImageStore imageStore) {
        return new ActivityAction(entity, world, imageStore);
    }
}
