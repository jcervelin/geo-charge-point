package io.jcervelin.evchargingapi.gateways.mongo;

import io.jcervelin.evchargingapi.domains.ChargePoint;
import org.springframework.data.geo.Point;

import java.util.Collection;
import java.util.List;

public interface ChargePointRepositoryCustom {
    List<ChargePoint> findLocationNear(Point location, int limit);
    void save(Collection<ChargePoint> chargePointStream);
}
