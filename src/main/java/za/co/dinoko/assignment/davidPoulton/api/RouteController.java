package za.co.dinoko.assignment.davidPoulton.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import za.co.dinoko.assignment.davidPoulton.db.models.Route;
import za.co.dinoko.assignment.davidPoulton.db.RouteRepository;

import java.net.URI;
import java.util.List;

/**
 * Handles all the relevant API calls for routes specifically.
 */
@RestController
public class RouteController {

    @Autowired
    private RouteRepository routeRepository;

    // Gets information about all routes.
    @GetMapping("/routes")
    public ResponseEntity<Object> getRoutes() {
        List<Route> routes = routeRepository.findAll();

        return ResponseEntity.ok().body(routes);
    }

    // Gets information for a specific route.
    @GetMapping("/route/{routeId}")
    public ResponseEntity<Object> getRoute(@PathVariable int routeId) {
        Route route = routeRepository.findByRouteId(routeId);

        if(route == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(route);
    }

    // Allows for creation of a new route.
    @PostMapping("/routes")
    public ResponseEntity<Object> createRoute(@RequestBody Route route) {
        route = routeRepository.save(route);

        // URI location of our new route.
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("{id}")
                .buildAndExpand(route.getRouteId())
                .toUri();

        return ResponseEntity.created(location).body(route);
    }

    // Allows for deleting of routes.
    @DeleteMapping("/route/{routeId}")
    public ResponseEntity<Object> deleteRoute(@PathVariable int routeId) {
        routeRepository.deleteByRouteId(routeId);

        return ResponseEntity.ok().build();
    }

    // Allows for updating of routes.
    @PutMapping("/route/{routeId}")
    public ResponseEntity<Object> updateRoute(@RequestBody Route route, @PathVariable int routeId) {
        Route oldRoute = routeRepository.findByRouteId(routeId);

        if(oldRoute == null) {
            return ResponseEntity.notFound().build();
        }

        // Handles missing fields in the provided route, allowing for partial updates.
        route.setId(oldRoute.getId());
        if(route.getOriginPlanetId() == 0) {
            route.setOriginPlanetId(oldRoute.getOriginPlanetId());
        }
        if(route.getTargetPlanetId() == 0) {
            route.setTargetPlanetId(oldRoute.getTargetPlanetId());
        }
        if(route.getDistance() == 0) {
            route.setDistance(oldRoute.getDistance());
        }

        route = routeRepository.save(route);

        return ResponseEntity.accepted().body(route);
    }

}
