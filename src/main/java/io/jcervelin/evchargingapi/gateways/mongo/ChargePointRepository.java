package io.jcervelin.evchargingapi.gateways.mongo;

import io.jcervelin.evchargingapi.domains.ChargePoint;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChargePointRepository extends MongoRepository<ChargePoint, String> {
    Optional<ChargePoint> findByLocation(Point location);
}
