package za.co.dinoko.assignment.davidPoulton.db;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.dinoko.assignment.davidPoulton.db.models.Route;

import javax.transaction.Transactional;

public interface RouteRepository extends JpaRepository<Route, Integer> {

    Route findByRouteId(int routeId);

    @Transactional
    void deleteByRouteId(int routeId);

}
