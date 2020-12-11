package za.co.dinoko.assignment.davidPoulton.api;

public class RouteRequest {

    private String startNode;
    private String endNode;

    public RouteRequest(String startNode, String endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
    }

    public String getStartNode() {
        return startNode;
    }

    public void setStartNode(String startNode) {
        this.startNode = startNode;
    }

    public String getEndNode() {
        return endNode;
    }

    public void setEndNode(String endNode) {
        this.endNode = endNode;
    }
}
