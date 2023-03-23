import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy implements PathingStrategy
{
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        List<Point> path = new ArrayList<>();
        List<Point> place = new ArrayList<>();
        PriorityQueue<ANode> queue = new PriorityQueue<>();

        place.add(start);
        queue.add(new ANode(start, null, 0));
        int gCost = 0;

        while(!queue.isEmpty())
        {
            ANode current = queue.poll();
            if(withinReach.test(current.getPoint(), end))
            {
                path.add(current.getPoint());
                while(current.getParent().getPoint() != start)
                {
                    path.add(current.getParent().getPoint());
                    current = current.getParent();
                }
                Collections.reverse(path);
                return path;
            }
            List<Point> neighbors = potentialNeighbors.apply(current.getPoint()).collect(Collectors.toList());
            for(Point pt : neighbors)
            {
                if(!place.contains(pt) && canPassThrough.test(pt))
                {
                    place.add(pt);
                    queue.add(new ANode(pt, current, gCost + hCost(pt, end)));
                }
            }
            gCost++;

        }
        return path;
    }

    private double hCost(Point pt1, Point pt2)
    {
        double Squared = Math.pow(pt2.getX() - pt1.getX(), 2) + Math.pow(pt2.getY() - pt1.getY(), 2);
        return Math.sqrt(Squared);
    }
    class ANode implements Comparable {
        private Point point;
        private ANode parent;
        private double fScore;

        public ANode(Point point, ANode parent, double fScore) {
            this.point = point;
            this.parent = parent;
            this.fScore = fScore;
        }

        public Point getPoint() {
            return point;
        }

        public ANode getParent() {
            return parent;
        }

        public double getFScore() {
            return fScore;
        }
        public int compareTo(Object o) {
            if (getClass() == o.getClass()) {
                ANode n = (ANode) o;
                if (this.fScore > n.getFScore()) {
                    return 1;
                }
                if (this.fScore < n.getFScore()) {
                    return -1;
                }
            }
            return 0;
        }
    }
}

