package io.jcervelin.evchargingapi.usecases;

import io.jcervelin.evchargingapi.domains.ChargePoint;
import io.jcervelin.evchargingapi.gateways.mongo.ChargePointRepositoryCustom;
import io.jcervelin.evchargingapi.support.UnitTestSupport;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.*;
import org.springframework.data.geo.Point;

import java.util.List;

import static br.com.six2six.fixturefactory.Fixture.from;
import static io.jcervelin.evchargingapi.templates.ChargePointTemplate.CHELTENHAM_CHASE_HOTEL;
import static io.jcervelin.evchargingapi.templates.ChargePointTemplate.POOLE_CIVIC_CENTRE_SURFACE_CAR_PARK;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ChargePointManagementTest extends UnitTestSupport {

    @InjectMocks
    private ChargePointManagement target;

    @Mock
    private ChargePointRepositoryCustom repository;

    @Captor
    private ArgumentCaptor<Point> pointArgumentCaptor;

    @Test
    public void findByLocation() {
        // GIVEN
        final List<ChargePoint> chargePointsExpected = from(ChargePoint.class)
                .gimme(2, CHELTENHAM_CHASE_HOTEL, POOLE_CIVIC_CENTRE_SURFACE_CAR_PARK);

        final Point pointExpected = new Point(51.488779, 0.022515);
        when(repository.findLocationNear(any(Point.class),Mockito.eq(3))).thenReturn(chargePointsExpected);

        // WHEN
        final List<ChargePoint> result = target.findByLocation(51.488779, 0.022515, 3);

        verify(repository,only()).findLocationNear(pointArgumentCaptor.capture(),eq(3));

        // THEN
        Assertions.assertThat(pointArgumentCaptor.getValue()).isEqualTo(pointExpected);
        Assertions.assertThat(result).containsExactlyInAnyOrderElementsOf(chargePointsExpected);

    }

}