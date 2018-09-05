package io.jcervelin.evchargingapi.gateways.mongo;

import io.jcervelin.evchargingapi.domains.ChargePoint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChargePointRepository extends MongoRepository<ChargePoint, String> {
}
