package za.co.dinoko.assignment.davidPoulton.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import za.co.dinoko.assignment.davidPoulton.db.models.Planet;
import za.co.dinoko.assignment.davidPoulton.db.PlanetRepository;

import java.net.URI;
import java.util.List;

/**
 * Handles any API operations related specifically to planets.
 */
@RestController
public class PlanetController {

    @Autowired
    private PlanetRepository planetRepository;

    // Gets information about every planet.
    @GetMapping("/planets")
    public ResponseEntity<Object> getPlanets() {
        List<Planet> planets = planetRepository.findAll();

        return ResponseEntity.ok().body(planets);
    }

    // Gets information about a specific planet.
    @GetMapping("/planet/{node}")
    public ResponseEntity<Object> getPlanet(@PathVariable String node) {
        Planet planet = planetRepository.findByNode(node);

        if(planet == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(planet);
    }

    // Allows for creating of new planets.
    @PostMapping("/planets")
    public ResponseEntity<Object> createPlanet(@RequestBody Planet planet) {
        planet = planetRepository.save(planet);

        // URI location for our new planet.
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("{node}")
                .buildAndExpand(planet.getNode())
                .toUri();

        return ResponseEntity.created(location).body(planet);
    }

    // Allows for deleting existing planets.
    @DeleteMapping("/planet/{node}")
    public ResponseEntity<Object> deletePlanet(@PathVariable String node) {
        planetRepository.deleteByNode(node);

        return ResponseEntity.ok().build();
    }

    // Allows for updating of existing planets.
    @PutMapping("/planet/{node}")
    public ResponseEntity<Object> updatePlanet(@RequestBody Planet planet, @PathVariable String node) {
        Planet oldPlanet = planetRepository.findByNode(node);

        if(oldPlanet == null) {
            return ResponseEntity.notFound().build();
        }

        // Handles missing variables in the provided planet, allowing for
        // partial updates.
        planet.setId(oldPlanet.getId());
        if(planet.getNode() == null) {
            planet.setNode(oldPlanet.getNode());
        }
        if(planet.getName() == null) {
            planet.setName(oldPlanet.getName());
        }

        planet = planetRepository.save(planet);

        return ResponseEntity.accepted().body(planet);
    }

}
