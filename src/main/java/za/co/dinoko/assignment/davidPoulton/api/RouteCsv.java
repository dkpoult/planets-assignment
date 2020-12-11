package za.co.dinoko.assignment.davidPoulton.api;

import com.opencsv.bean.CsvBindByName;

public class RouteCsv {

    @CsvBindByName(column = "Route Id", required = true)
    private int routeId;
    @CsvBindByName(column = "Planet Origin", required = true)
    private String originNode;
    @CsvBindByName(column = "Planet Destination", required = true)
    private String targetNode;
    @CsvBindByName(column = "Distance(Light Years)", required = true)
    private double distance;

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getOriginNode() {
        return originNode;
    }

    public void setOriginNode(String originNode) {
        this.originNode = originNode;
    }

    public String getTargetNode() {
        return targetNode;
    }

    public void setTargetNode(String targetNode) {
        this.targetNode = targetNode;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

}
