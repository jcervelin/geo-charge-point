package io.jcervelin.evchargingapi.gateways.mongo.impl;

import io.jcervelin.evchargingapi.domains.ChargePoint;
import io.jcervelin.evchargingapi.gateways.mongo.ChargePointRepository;
import io.jcervelin.evchargingapi.gateways.mongo.ChargePointRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChargePointRepositoryCustomImpl implements ChargePointRepositoryCustom {

    private final MongoOperations template;
    private final ChargePointRepository repository;

    @Override
    public List<ChargePoint> findLocationNear(Point location,int limit) {
        final Query query = new Query();
        query.addCriteria(Criteria.where("location").near(location)).limit(limit);

        return template.find(query, ChargePoint.class);
    }

    @Override
    public Optional<ChargePoint> findByLocation(Point location) {
        return repository.findByLocation(location);
    }

    @Override
    public Optional<ChargePoint> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public void save(Collection<ChargePoint> chargePointStream) {
        repository.saveAll(chargePointStream);
    }

    @Override
    public ChargePoint save(ChargePoint chargePoint) {
        return repository.save(chargePoint);
    }

}
