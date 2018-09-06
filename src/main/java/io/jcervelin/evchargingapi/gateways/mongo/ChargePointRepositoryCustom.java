package io.jcervelin.evchargingapi.gateways.mongo;

import io.jcervelin.evchargingapi.domains.ChargePoint;
import org.springframework.data.geo.Point;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ChargePointRepositoryCustom {
    List<ChargePoint> findLocationNear(Point location, int limit);
    Optional<ChargePoint> findByLocation(Point location);
    Optional<ChargePoint> findById(String id);
    void save(Collection<ChargePoint> chargePoint);
    ChargePoint save(ChargePoint chargePoint);
}
