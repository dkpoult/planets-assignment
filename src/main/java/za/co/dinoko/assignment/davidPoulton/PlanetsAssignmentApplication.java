package za.co.dinoko.assignment.davidPoulton;

import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import za.co.dinoko.assignment.davidPoulton.api.RouteCsv;
import za.co.dinoko.assignment.davidPoulton.db.PlanetRepository;
import za.co.dinoko.assignment.davidPoulton.db.RouteRepository;
import za.co.dinoko.assignment.davidPoulton.db.models.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class PlanetsAssignmentApplication {

	private Logger logger = LoggerFactory.getLogger(PlanetsAssignmentApplication.class);

	@Autowired
	private PlanetRepository planetRepository;

	@Autowired
	private RouteRepository routeRepository;

	public static void main(String[] args) {
		SpringApplication.run(PlanetsAssignmentApplication.class, args);
	}

	@EventListener
	private void onApplicationRunning(ApplicationReadyEvent event) {
		try {
			List<Planet> planets = new CsvToBeanBuilder<Planet>(new FileReader("files/PlanetNames-V1.csv"))
					.withType(Planet.class)
					.build()
					.parse();

			planets = planetRepository.saveAll(planets);
			logger.info("Added " + planets.size() + " planets.");

			List<RouteCsv> routeCsvs = new CsvToBeanBuilder<RouteCsv>(new FileReader("files/Routes-V1.csv"))
					.withType(RouteCsv.class)
					.build()
					.parse();

			ArrayList<Route> routes = new ArrayList<>(routeCsvs.size());

			for(RouteCsv csv : routeCsvs) {
				Planet origin = planets.stream()
						.filter(planet -> csv.getOriginNode().equals(planet.getNode()))
						.findFirst()
						.orElse(null);
				Planet target = planets.stream()
						.filter(planet -> csv.getTargetNode().equals(planet.getNode()))
						.findFirst()
						.orElse(null);

				if(origin == null || target == null) {
					// Alternatively, we could throw in the above streams if it's important.
					continue;
				}

				Route route = new Route();
				route.setRouteId(csv.getRouteId());
				route.setOriginPlanetId(origin.getId());
				route.setTargetPlanetId(target.getId());
				route.setDistance(csv.getDistance());

				routes.add(route);
			}

			routeRepository.saveAll(routes);
			logger.info("Added " + routes.size() + " routes.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
