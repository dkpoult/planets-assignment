package za.co.dinoko.assignment.davidPoulton.db;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.co.dinoko.assignment.davidPoulton.db.models.Planet;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PlanetTests {

    @Autowired
    private PlanetRepository planetRepository;

    @Test
    void testPlanet() {
        Planet planet = new Planet();

        planet.setId(12);
        planet.setNode("F");
        planet.setName("Fake");

        assertThat(planet.getId()).isEqualTo(12);
        assertThat(planet.getNode()).isEqualTo("F");
        assertThat(planet.getName()).isEqualTo("Fake");
        assertThat(planet.toString()).isEqualTo("12 (F): Fake");
    }

    @Test
    void testRepository() {
        assertThat(planetRepository.findAll().size()).isGreaterThan(0);
        assertThat(planetRepository.findByNode("A").getNode()).isEqualTo("A");
        assertThat(planetRepository.findByNode("A").getName()).isEqualTo("Earth");
        assertThat(planetRepository.findByNode("FAKE")).isNull();
    }

}
