package za.co.dinoko.assignment.davidPoulton.db;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.co.dinoko.assignment.davidPoulton.api.RouteRequest;
import za.co.dinoko.assignment.davidPoulton.db.models.Planet;
import za.co.dinoko.assignment.davidPoulton.db.models.Route;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RouteTests {

    @Autowired
    private RouteRepository routeRepository;

    @Test
    void testRoute() {
        Route route = new Route();

        route.setId(12);
        route.setRouteId(14);
        route.setOriginPlanetId(4);
        route.setTargetPlanetId(5);
        route.setDistance(100.05);

        assertThat(route.getId()).isEqualTo(12);
        assertThat(route.getRouteId()).isEqualTo(14);
        assertThat(route.getOriginPlanetId()).isEqualTo(4);
        assertThat(route.getTargetPlanetId()).isEqualTo(5);
        assertThat(route.getDistance()).isEqualTo(100.05);
        assertThat(route.toString()).isEqualTo("12 (14): 4 -> 5 (100.05 light years)");
    }

    @Test
    void testRepository() {
        assertThat(routeRepository.findAll().size()).isGreaterThan(0);
        assertThat(routeRepository.findByRouteId(1)).isNotNull();
        assertThat(routeRepository.findByRouteId(1).getOriginPlanetId()).isEqualTo(1);
        assertThat(routeRepository.findByRouteId(1).getTargetPlanetId()).isEqualTo(2);
    }

}
