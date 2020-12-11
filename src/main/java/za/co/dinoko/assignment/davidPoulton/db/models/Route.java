package za.co.dinoko.assignment.davidPoulton.db.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Routes")
public class Route {

    @Id
    @GeneratedValue()
    private int id;
    private int routeId;
    private int originPlanetId;
    private int targetPlanetId;
    private double distance;

    public Route() {

    }

    public Route(int id, int routeId, int originPlanetId, int targetPlanetId, double distance) {
        this.id = id;
        this.routeId = routeId;
        this.originPlanetId = originPlanetId;
        this.targetPlanetId = targetPlanetId;
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getOriginPlanetId() {
        return originPlanetId;
    }

    public void setOriginPlanetId(int originPlanetId) {
        this.originPlanetId = originPlanetId;
    }

    public int getTargetPlanetId() {
        return targetPlanetId;
    }

    public void setTargetPlanetId(int targetPlanetId) {
        this.targetPlanetId = targetPlanetId;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return this.id + " (" + this.routeId + "): " + originPlanetId + " -> " + targetPlanetId + " (" + distance + " light years)";
    }
}
