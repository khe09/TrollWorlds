import java.util.*;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public abstract class Entity {
    protected String id;
    protected Point position;
    protected List<PImage> images;
    protected int imageIndex;
    public String getId() {
        return id;
    }
    public Point getPosition() {
        return position;
    }
    public List<PImage> getImages() {
        return images;
    }
    public int getImageIndex() {
        return imageIndex;
    }
    public void setPosition(Point position) {
        this.position = position;
    }
        public Entity(String id, Point position, List<PImage> images) {
            this.id = id;
            this.position = position;
            this.images = images;
            this.imageIndex = 0;
        }

        public static PImage getCurrentImage(Object object) {
            if (object instanceof Background background) {
                return background.getImages().get(background.getImageIndex());
            } else if (object instanceof Entity entity) {
                return entity.images.get(entity.imageIndex % entity.images.size());
            } else {
                throw new UnsupportedOperationException(String.format("getCurrentImage not supported for %s", object));
            }
        }
    public void nextImage() {
        this.imageIndex = this.imageIndex + 1;
    }

        /**
         * Helper method for testing. Preserve this functionality while refactoring.
         */
        public String log () {
            return this.id.isEmpty() ? null :
                    String.format("%s %d %d %d", this.id, this.position.getX(), this.position.getX(), this.imageIndex);
        }
    }

