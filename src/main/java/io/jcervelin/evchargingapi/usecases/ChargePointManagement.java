package io.jcervelin.evchargingapi.usecases;

import io.jcervelin.evchargingapi.domains.ChargePoint;
import io.jcervelin.evchargingapi.domains.exceptions.NoChargePointFoundException;
import io.jcervelin.evchargingapi.domains.exceptions.WrongChargingPointFoundException;
import io.jcervelin.evchargingapi.gateways.mongo.ChargePointRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ChargePointManagement {

    private final ChargePointRepositoryCustom repository;

    public List<ChargePoint> findByLocation(final double latitude, final double longitude, final int amount) {
        final Point location = new Point(latitude,longitude);
        return repository.findLocationNear(location, amount);
    }

    public ChargePoint update(final ChargePoint chargePoint) {
        final ChargePoint chargePointFound = repository.findById(chargePoint.getChargeDeviceID())
                .orElseThrow(() -> new NoChargePointFoundException("No Charge Point Found."));

        final Optional<ChargePoint> chargePointByLocationFound = repository.findByLocation(chargePoint.getLocation());

        chargePointByLocationFound.ifPresent(cp -> {
            if (!cp.getChargeDeviceID().equals(chargePointFound.getChargeDeviceID())) {
                throw new WrongChargingPointFoundException("There is another Charge Point at this place");
            }
        });

        return repository.save(chargePoint);
    }
}
