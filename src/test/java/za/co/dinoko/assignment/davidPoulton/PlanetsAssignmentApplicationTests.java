package za.co.dinoko.assignment.davidPoulton;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.co.dinoko.assignment.davidPoulton.db.PlanetRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PlanetsAssignmentApplicationTests {

	@Autowired
	private PlanetRepository planetRepository;

	@Test
	void contextLoads() {
		// Sanity check that the system is actually loading.
		assertThat(planetRepository).isNotNull();
	}

}
