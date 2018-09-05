package io.jcervelin.evchargingapi.gateways.mongo;

import io.jcervelin.evchargingapi.EvChargingApiApplication;
import io.jcervelin.evchargingapi.domains.ChargePoint;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static br.com.six2six.fixturefactory.Fixture.from;
import static br.com.six2six.fixturefactory.loader.FixtureFactoryLoader.loadTemplates;
import static io.jcervelin.evchargingapi.templates.ChargePointTemplate.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {EvChargingApiApplication.class})
public class ChargePointRepositoryCustomImplIT {

    private static final String TEMPLATE_PACKAGE = "io.jcervelin.evchargingapi.templates";

    @Autowired
    private ChargePointRepositoryCustom repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeClass
    public static void setup() {
        loadTemplates(TEMPLATE_PACKAGE);
    }

    @Before
    public void setUp() {
        mongoTemplate
                .getCollectionNames()
                .forEach(mongoTemplate::dropCollection);
    }

    @Test
    public void findLocationNearShouldReturnTheNearest3() {
        final List<ChargePoint> chargePoints = from(ChargePoint.class)
                .gimme(4, RC_BOTHWELL, RC_ROWNHAMS, ASDA_CHARLTON,ASDA_GREENHITHE);

        repository.save(chargePoints);

        final List<ChargePoint> locationNear = repository.findLocationNear(new Point(51.488779, 0.022515), 3);

        assertThat(locationNear.size()).isEqualTo(3);
        assertThat(locationNear).containsExactly(
                from(ChargePoint.class).gimme(ASDA_CHARLTON),
                from(ChargePoint.class).gimme(ASDA_GREENHITHE),
                from(ChargePoint.class).gimme(RC_ROWNHAMS)
        );
    }

    @Test
    public void findLocationNearShouldReturnZeroElements() {
        final List<ChargePoint> locationNear = repository.findLocationNear(new Point(51.488779, 0.022515), 3);

        assertThat(locationNear.size()).isEqualTo(0);
    }

}