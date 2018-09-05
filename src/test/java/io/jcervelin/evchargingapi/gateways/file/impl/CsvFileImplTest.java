package io.jcervelin.evchargingapi.gateways.file.impl;

import io.jcervelin.evchargingapi.domains.ChargePoint;
import io.jcervelin.evchargingapi.domains.ReadResponse;
import io.jcervelin.evchargingapi.domains.exceptions.UnprocessableEntityException;
import io.jcervelin.evchargingapi.support.UnitTestSupport;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import static br.com.six2six.fixturefactory.Fixture.from;
import static io.jcervelin.evchargingapi.templates.ChargePointTemplate.*;
import static org.assertj.core.api.Assertions.assertThat;

public class CsvFileImplTest extends UnitTestSupport {

    @InjectMocks
    private CsvFileParserImpl target;

    @Test
    public void readCsvAndShouldReturn6ChangePointsAnd6Errors() throws IOException {
        // GIVEN
        final Collection<ChargePoint> chargePointsExpected = from(ChargePoint.class)
                .gimme(6, CHELTENHAM_CHASE_HOTEL, POOLE_CIVIC_CENTRE_SURFACE_CAR_PARK, CROSBY_LAKESIDE_ADVENTURE_CENTRE
                        ,WEBBS_OF_WYCHBOLD_RAPID_CHARGER,LONGWELL_GREEN_LEISURE_CENTRE,BIRKDALE_PRIMARY_SCHOOL);

        final InputStream inputStream = getClass().getResourceAsStream("/national-charge-point-registry-with-errors.csv");

        final MockMultipartFile mockMultipartFile = new MockMultipartFile("file","national-charge-point-registry-with-errors.csv","multipart/form-data", inputStream);

        // WHEN
        final ReadResponse resultList = target.read(mockMultipartFile);

        // THEN
        assertThat(resultList.getChargePoints().size()).isEqualTo(6);
        assertThat(resultList.getChargePoints()).containsExactlyInAnyOrderElementsOf(chargePointsExpected);
        assertThat(resultList.getErrors().size()).isEqualTo(6);
        resultList.getErrors().forEach(error -> assertThat(error).startsWith("Error read csv line:"));
    }

    @Test
    public void readCsvAndShouldReturn6Errors() throws IOException {
        // GIVEN
        final InputStream inputStream = getClass().getResourceAsStream("/national-charge-point-registry-with-errors-only.csv");

        final MockMultipartFile mockMultipartFile = new MockMultipartFile("file","national-charge-point-registry-with-errors-only.csv","multipart/form-data", inputStream);

        // WHEN
        final ReadResponse resultList = target.read(mockMultipartFile);

        // THEN
        assertThat(resultList.getChargePoints().size()).isEqualTo(0);
        assertThat(resultList.getErrors().size()).isEqualTo(6);
        resultList.getErrors().forEach(error -> assertThat(error).startsWith("Error read csv line:"));
    }

    @Test
    public void readCsvAndShouldReturn0Elements() throws IOException {
        // GIVEN
        final InputStream inputStream = getClass().getResourceAsStream("invalid");

        final MockMultipartFile mockMultipartFile = new MockMultipartFile("file","national-charge-point-registry-with-errors-only.csv","multipart/form-data", inputStream);

        // WHEN
        final ReadResponse resultList = target.read(mockMultipartFile);

        // THEN
        assertThat(resultList.getChargePoints().size()).isEqualTo(0);
        assertThat(resultList.getErrors().size()).isEqualTo(0);
    }

    @Test
    public void readCsvAndShouldReturnUnprocessableEntityException() {
        // GIVEN
        final MockMultipartFile mockMultipartFile = null;
        thrown.expectMessage("Invalid multipartFile file.");
        thrown.expect(UnprocessableEntityException.class);

        // WHEN
        target.read(mockMultipartFile);

        // THEN
        // UnprocessableEntityException is thrown
    }


}