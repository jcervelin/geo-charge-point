package io.jcervelin.evchargingapi.usecases;

import io.jcervelin.evchargingapi.domains.ChargePoint;
import io.jcervelin.evchargingapi.gateways.mongo.ChargePointRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ChargePointManagement {

    private final ChargePointRepositoryCustom repository;

    public List<ChargePoint> findByLocation(final double latitude, final double longitude, final int amount) {
        final Point location = new Point(latitude,longitude);
        return repository.findLocationNear(location, amount);
    }
}
