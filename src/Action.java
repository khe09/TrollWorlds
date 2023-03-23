public abstract class Action {

    protected Entity entity;
    public abstract void executeAction(EventScheduler scheduler);

    public Entity getActionEntity() {
        return this.entity;
    }

}