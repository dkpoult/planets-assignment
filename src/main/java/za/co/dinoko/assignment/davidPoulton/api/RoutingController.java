package za.co.dinoko.assignment.davidPoulton.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import za.co.dinoko.assignment.davidPoulton.db.PlanetRepository;
import za.co.dinoko.assignment.davidPoulton.db.RouteRepository;
import za.co.dinoko.assignment.davidPoulton.db.models.Planet;
import za.co.dinoko.assignment.davidPoulton.db.models.Route;
import za.co.dinoko.assignment.davidPoulton.shared.Pair;

import java.util.*;

/**
 * Exposes our pathfinding API.
 */
@RestController
public class RoutingController {

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private RouteRepository routeRepository;

    /**
     * Uses A* to find the shortest path between to planets, if it exists.
     * @param planets The planets to use.
     * @param routes The routes available.
     * @param start The planet to start at.
     * @param end The planet to end at.
     * @return null if there is no route, otherwise the list of in-order planet codes.
     */
    private List<Planet> getPlanetRoute(List<Planet> planets, List<Route> routes, Planet start, Planet end) {
        // FIXME: The code to generate the connections is really ugly, and could probably be improved by caching it or
        // FIXME: making use of our ORM better.
        RoutingEntity[] routingEntities = new RoutingEntity[planets.size()];

        int startIndex = -1;

        // Go through each planet and create a RoutingEntity for it.
        for(int i = 0; i < planets.size(); i++) {
            Planet planet = planets.get(i);
            if(planet == start) {
                startIndex = i;
            }

            routingEntities[i] = new RoutingEntity(0.0, planet);
        }

        // Our starting node doesn't exist, there's obviously no path.
        if(startIndex == -1) {
            return null;
        }

        // Go through each entity to ad dthe connections.
        for(RoutingEntity entity : routingEntities) {
            ArrayList<Pair<RoutingEntity, Double>> neighbours = new ArrayList<>();

            // Find all routes involving this planet...
            routes.stream()
                    .filter(route -> (route.getOriginPlanetId() == entity.getValue().getId()
                            || route.getTargetPlanetId() == entity.getValue().getId()))
                    .forEach(route -> {
                        // Get the ID of the neighbour
                        int neighbourId = route.getTargetPlanetId();
                        if(route.getOriginPlanetId() != entity.getValue().getId()) {
                            neighbourId = route.getOriginPlanetId();
                        }

                        // Find the actual neighbour entity.
                        int finalNeighbourId = neighbourId;
                        RoutingEntity neighbour = Arrays.stream(routingEntities)
                                .filter(routingEntity -> (finalNeighbourId == routingEntity.getValue().getId()))
                                .findFirst()
                                .orElse(null);

                        neighbours.add(new Pair<>(neighbour, route.getDistance()));
                    });

            entity.setNeighbours(neighbours);
        }

        // Create a queue to store our "open" nodes and add the start.
        PriorityQueue<RoutingEntity> open = new PriorityQueue<>();
        open.add(routingEntities[startIndex]);

        // While we've got nodes left...
        while(!open.isEmpty()) {
            // Pop and close the next entity.
            RoutingEntity e = open.poll();
            e.setClosed(true);

            // If we found the end...
            if(e.getValue() == end) {
                ArrayList<Planet> path = new ArrayList<>();

                // Keep adding nodes and climbing the path until we find the start.
                while(e.getValue() != start) {
                    path.add(e.getValue());
                    e = e.getParent();
                }

                // Add the start.
                path.add(e.getValue());

                // Reverse it to make it correct.
                Collections.reverse(path);
                return path;
            }

            // Go through all our neighhbour routes.
            for(Pair<RoutingEntity, Double> connection : e.getNeighbours()) {
                // Skip any neighbours we've already closed.
                if(connection.getKey().isClosed()) {
                    continue;
                }

                // Get the neighbour entity and the distance to it.
                RoutingEntity neighbour = connection.getKey();
                double distance = connection.getValue();
                double newDistance = distance + e.getKey();

                // Our neighbour hasn't been seen yet, the entity is the parent regardless.
                if(!open.contains(neighbour)) {
                    neighbour.setParent(e);
                    neighbour.setKey(newDistance);
                    open.add(neighbour);
                // The neighbour has been seen, we should only update it if our new route is shorter.
                } else if(newDistance < neighbour.getKey()) {
                    neighbour.setParent(e);
                    neighbour.setKey(newDistance);

                    // Needed to re-sort the queue
                    open.remove(neighbour);
                    open.add(neighbour);
                }
            }
        }

        return null;
    }

    @GetMapping("/routing")
    private ResponseEntity<Object> getRoute(@RequestBody RouteRequest request) {
        // Get all the planets and routes.
        // TODO: Could probably make this faster by storing this.
        List<Planet> planets = planetRepository.findAll();
        List<Route> routes = routeRepository.findAll();

        // Get the starting planet from the code.
        Planet startPlanet = planets.stream()
                .filter(planet -> request.getStartNode().equals(planet.getNode()))
                .findFirst()
                .orElse(null);

        // Get the ending planet from the code.
        Planet endPlanet = planets.stream()
                .filter(planet -> request.getEndNode().equals(planet.getNode()))
                .findFirst()
                .orElse(null);

        // Use our A* algorithm to find the route (if there is one).
        List<Planet> planetRoute = getPlanetRoute(planets, routes, startPlanet, endPlanet);
        if(planetRoute == null) {
            return ResponseEntity.badRequest().body("There was no route between those nodes.");
        }

        // Convert our planet list to a node list.
        ArrayList<String> routeNodes = new ArrayList<>(planetRoute.size());
        planetRoute.forEach(planet -> routeNodes.add(planet.getNode()));

        return ResponseEntity.ok().body(new RouteResponse(routeNodes));
    }

}
