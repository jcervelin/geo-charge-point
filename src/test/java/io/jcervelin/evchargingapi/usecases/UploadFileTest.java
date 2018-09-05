package io.jcervelin.evchargingapi.usecases;

import io.jcervelin.evchargingapi.domains.ChargePoint;
import io.jcervelin.evchargingapi.domains.ReadResponse;
import io.jcervelin.evchargingapi.domains.exceptions.UnprocessableEntityException;
import io.jcervelin.evchargingapi.gateways.file.FileParser;
import io.jcervelin.evchargingapi.gateways.mongo.ChargePointRepositoryCustom;
import io.jcervelin.evchargingapi.support.UnitTestSupport;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static br.com.six2six.fixturefactory.Fixture.from;
import static io.jcervelin.evchargingapi.templates.ChargePointTemplate.*;
import static org.mockito.Mockito.*;

public class UploadFileTest extends UnitTestSupport {

    @InjectMocks
    private UploadFile target;

    @Mock
    private FileParser fileParserGateway;

    @Mock
    private ChargePointRepositoryCustom repository;

    @Captor
    private ArgumentCaptor<Collection<ChargePoint>> chargePointsCaptor;

    @Test
    public void uploadFileShouldReturn6ErrorsAndSend6ChargePointsToBeSaved() throws IOException {
        // GIVEN
        final Collection<ChargePoint> chargePointsExpected = from(ChargePoint.class)
                .gimme(6, CHELTENHAM_CHASE_HOTEL, POOLE_CIVIC_CENTRE_SURFACE_CAR_PARK, CROSBY_LAKESIDE_ADVENTURE_CENTRE
                        ,WEBBS_OF_WYCHBOLD_RAPID_CHARGER,LONGWELL_GREEN_LEISURE_CENTRE,BIRKDALE_PRIMARY_SCHOOL);
        final Collection<String> errorsExpected = Arrays.asList(
            "Error read csv line: [Must have source West membership\",Cheltenham Chase Hotel,,,,Gloucestershire County Council,http://www.gloucestershire.gov.uk,01452 425000,,Charge Your Car,http://www.chargeyourcar.org.uk,01912 650500,,Charge Your Car,In service,Published,0,2015-02-03 11:36:04,0000-00-00 00:00:00,N,,,Charge Your Car,n/a,1,,1,,0,,,0,,0,,0,Other,,,,,,,,,,,,,,,,,124739,JEVS G105 (CHAdeMO) DC,50.0,125,400,DC,4,1,In service,,0,124740,Type 2 Combo (IEC62196) DC,50.0,125,400,DC,4,1,In service,,0,124741,Type 2 Mennekes (IEC62196),43.0,63,400,Three Phase AC,3,1,In service,,0,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,], error message: [null]",
            "Error read csv line: [Vehicles must display a valid parking ticket whilst in the charging bay.], error message: [2]",
            "Error read csv line: [\",The charger is located in the layby to the far right of the building.,\"Turn off the main Abbey Road and drive along the leisure centreâ€™s one way access road and around the car park. ], error message: [3]",
            "Error read csv line: [Continue past the front of the building and turn left into the service road next to the timber clad portion. ], error message: [2]",
            "Error read csv line: [The chargepoint is located in the layby next to the building. ], error message: [2]",
            "Error read csv line: [\",,,Worcestershire County Council,http://www.worcestershire.gov.uk,03300 165 126,,Chargemaster (POLAR),https://www.chargemasterplc.com,01582 400331,,Charge Your Car,In service,Published,0,2015-09-09 14:37:12,2015-09-09 14:37:12,Y,2015-08-18 15:25:05,NCR Admin,Charge Your Car,n/a,1,,0,,0,,,0,,1,Behind barrier.,0,Leisure centre,,0,,,,,,,,,,,,,,,125124,JEVS G105 (CHAdeMO) DC,50.0,125,400,DC,4,1,In service,,0,125182,Type 2 Mennekes (IEC62196),43.0,63,400,Three Phase AC,3,1,In service,,0,125183,Type 2 Combo (IEC62196) DC,50.0,125,400,DC,4,1,In service,,0,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,], error message: [null]"
        );

        final ReadResponse readResponseExpected = ReadResponse.builder()
                .chargePoints(chargePointsExpected)
                .errors(errorsExpected)
                .build();

        final InputStream inputStream = getClass().getResourceAsStream("/national-charge-point-registry-with-errors.csv");

        final MockMultipartFile mockMultipartFile = new MockMultipartFile("file","national-charge-point-registry-with-errors.csv","multipart/form-data", inputStream);

        doReturn(readResponseExpected)
                .when(fileParserGateway).read(mockMultipartFile);

        // WHEN
        final Collection<String> errors = target.uploadFile(mockMultipartFile);

        verify(repository,only()).save(chargePointsCaptor.capture());

        // THEN
        final Collection<ChargePoint> results = chargePointsCaptor.getValue();

        Assertions.assertThat(results).containsExactlyInAnyOrderElementsOf(chargePointsExpected);
        Assertions.assertThat(errors).containsExactlyInAnyOrderElementsOf(errorsExpected);
    }

    @Test
    public void uploadFileShouldSend6ChargePointsToBeSaved() throws IOException {
        // GIVEN
        final Collection<ChargePoint> chargePointsExpected = from(ChargePoint.class)
                .gimme(6, CHELTENHAM_CHASE_HOTEL, POOLE_CIVIC_CENTRE_SURFACE_CAR_PARK, CROSBY_LAKESIDE_ADVENTURE_CENTRE
                        ,WEBBS_OF_WYCHBOLD_RAPID_CHARGER,LONGWELL_GREEN_LEISURE_CENTRE,BIRKDALE_PRIMARY_SCHOOL);
        final Collection<String> errorsExpected = new ArrayList<>();

        final ReadResponse readResponseExpected = ReadResponse.builder()
                .chargePoints(chargePointsExpected)
                .errors(errorsExpected)
                .build();

        final InputStream inputStream = getClass().getResourceAsStream("/national-charge-point-registry-with-success.csv");
        final MockMultipartFile mockMultipartFile = new MockMultipartFile("file","national-charge-point-registry-with-errors.csv","multipart/form-data", inputStream);

        doReturn(readResponseExpected)
                .when(fileParserGateway).read(mockMultipartFile);

        // WHEN
        final Collection<String> errors = target.uploadFile(mockMultipartFile);

        verify(repository,only()).save(chargePointsCaptor.capture());

        // THEN
        final Collection<ChargePoint> results = chargePointsCaptor.getValue();

        Assertions.assertThat(results).containsExactlyInAnyOrderElementsOf(chargePointsExpected);
        Assertions.assertThat(errors.size()).isEqualTo(1);
        Assertions.assertThat(errors.stream().findFirst().get()).isEqualTo("All lines were successfully saved.");
    }

    @Test
    public void uploadFileShouldReturn6ErrorsAndReturnException() throws IOException {
        // GIVEN
        final Collection<String> errorsExpected = Arrays.asList(
                "Error read csv line: [Must have source West membership\",Cheltenham Chase Hotel,,,,Gloucestershire County Council,http://www.gloucestershire.gov.uk,01452 425000,,Charge Your Car,http://www.chargeyourcar.org.uk,01912 650500,,Charge Your Car,In service,Published,0,2015-02-03 11:36:04,0000-00-00 00:00:00,N,,,Charge Your Car,n/a,1,,1,,0,,,0,,0,,0,Other,,,,,,,,,,,,,,,,,124739,JEVS G105 (CHAdeMO) DC,50.0,125,400,DC,4,1,In service,,0,124740,Type 2 Combo (IEC62196) DC,50.0,125,400,DC,4,1,In service,,0,124741,Type 2 Mennekes (IEC62196),43.0,63,400,Three Phase AC,3,1,In service,,0,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,], error message: [null]",
                "Error read csv line: [Vehicles must display a valid parking ticket whilst in the charging bay.], error message: [2]",
                "Error read csv line: [\",The charger is located in the layby to the far right of the building.,\"Turn off the main Abbey Road and drive along the leisure centreâ€™s one way access road and around the car park. ], error message: [3]",
                "Error read csv line: [Continue past the front of the building and turn left into the service road next to the timber clad portion. ], error message: [2]",
                "Error read csv line: [The chargepoint is located in the layby next to the building. ], error message: [2]",
                "Error read csv line: [\",,,Worcestershire County Council,http://www.worcestershire.gov.uk,03300 165 126,,Chargemaster (POLAR),https://www.chargemasterplc.com,01582 400331,,Charge Your Car,In service,Published,0,2015-09-09 14:37:12,2015-09-09 14:37:12,Y,2015-08-18 15:25:05,NCR Admin,Charge Your Car,n/a,1,,0,,0,,,0,,1,Behind barrier.,0,Leisure centre,,0,,,,,,,,,,,,,,,125124,JEVS G105 (CHAdeMO) DC,50.0,125,400,DC,4,1,In service,,0,125182,Type 2 Mennekes (IEC62196),43.0,63,400,Three Phase AC,3,1,In service,,0,125183,Type 2 Combo (IEC62196) DC,50.0,125,400,DC,4,1,In service,,0,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,], error message: [null]"
        );

        final ReadResponse readResponseExpected = ReadResponse.builder()
                .chargePoints(null)
                .errors(errorsExpected)
                .build();

        final InputStream inputStream = getClass().getResourceAsStream("/national-charge-point-registry-with-errors-only.csv");

        final MockMultipartFile mockMultipartFile = new MockMultipartFile("file","national-charge-point-registry-with-errors.csv","multipart/form-data", inputStream);

        doReturn(readResponseExpected)
                .when(fileParserGateway).read(mockMultipartFile);

        thrown.expectMessage("No messages have been saved.");
        thrown.expect(UnprocessableEntityException.class);

        // WHEN
        target.uploadFile(mockMultipartFile);

        // THEN
        // UnprocessableEntityException is thrown
    }
}