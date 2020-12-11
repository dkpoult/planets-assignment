package za.co.dinoko.assignment.davidPoulton.api;

import za.co.dinoko.assignment.davidPoulton.db.models.Planet;
import za.co.dinoko.assignment.davidPoulton.shared.Pair;
import za.co.dinoko.assignment.davidPoulton.shared.SortablePair;

import java.util.ArrayList;

/**
 * A type of SortablePair used for our A* algorithm. It needs to be sortable because of the queue.
 */
public class RoutingEntity extends SortablePair<Double, Planet> {

    private RoutingEntity parent = null;
    private ArrayList<Pair<RoutingEntity, Double>> neighbours = null;
    private boolean closed = false;

    public RoutingEntity(Double key, Planet value) {
        super(key, value);
    }

    public RoutingEntity getParent() {
        return parent;
    }

    public void setParent(RoutingEntity parent) {
        this.parent = parent;
    }

    public ArrayList<Pair<RoutingEntity, Double>> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(ArrayList<Pair<RoutingEntity, Double>> neighbours) {
        this.neighbours = neighbours;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
}
