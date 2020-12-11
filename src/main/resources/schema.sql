DROP TABLE IF EXISTS planets;
DROP TABLE IF EXISTS routes;

CREATE TABLE planets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    node VARCHAR(5) NOT NULL,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE routes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    routeId INT NOT NULL,
    OriginPlanetId INT NOT NULL,
    TargetPlanetId INT NOT NULL,
    distance FLOAT NOT NULL,
    FOREIGN KEY (OriginPlanetId) REFERENCES planets(id),
    FOREIGN KEY (TargetPlanetId) REFERENCES planets(id)
);