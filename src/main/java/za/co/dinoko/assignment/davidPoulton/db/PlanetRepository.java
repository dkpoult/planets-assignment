package za.co.dinoko.assignment.davidPoulton.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.dinoko.assignment.davidPoulton.db.models.Planet;

import javax.transaction.Transactional;

@Repository
public interface PlanetRepository extends JpaRepository<Planet, Integer> {

    Planet findByNode(String node);

    @Transactional
    void deleteByNode(String node);

}
