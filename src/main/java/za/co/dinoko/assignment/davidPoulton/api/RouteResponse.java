package za.co.dinoko.assignment.davidPoulton.api;

import java.util.ArrayList;

public class RouteResponse {

    private ArrayList<String> planetNodes;

    public RouteResponse(ArrayList<String> planetNodes) {
        this.planetNodes = planetNodes;
    }

    public ArrayList<String> getPlanetNodes() {
        return planetNodes;
    }

    public void setPlanetNodes(ArrayList<String> planetNodes) {
        this.planetNodes = planetNodes;
    }

}
