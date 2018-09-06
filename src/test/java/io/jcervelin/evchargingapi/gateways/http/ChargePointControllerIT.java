package io.jcervelin.evchargingapi.gateways.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jcervelin.evchargingapi.EvChargingApiApplication;
import io.jcervelin.evchargingapi.domains.ChargePoint;
import io.jcervelin.evchargingapi.domains.api.ErrorResponse;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static br.com.six2six.fixturefactory.Fixture.from;
import static br.com.six2six.fixturefactory.loader.FixtureFactoryLoader.loadTemplates;
import static io.jcervelin.evchargingapi.domains.Endpoints.CHARGE_POINTS;
import static io.jcervelin.evchargingapi.domains.Endpoints.UPLOAD_CSV;
import static io.jcervelin.evchargingapi.templates.ChargePointTemplate.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {EvChargingApiApplication.class})
public class ChargePointControllerIT {

    private static final String TEMPLATE_PACKAGE = "io.jcervelin.evchargingapi.templates";

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    private MockMvc mockMvc;

    @BeforeClass
    public static void setup() {
        loadTemplates(TEMPLATE_PACKAGE);
    }

    @Before
    public void setUp() {
        mongoTemplate
                .getCollectionNames()
                .forEach(mongoTemplate::dropCollection);

        mockMvc = webAppContextSetup(webAppContext).build();
    }

    @Test
    public void findChargePoint()  {

    }

    @Test
    public void updateChargePointsShouldReturnErrorsAndSave6ChargePointsWith201Status() throws Exception {

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

        final InputStream inputStream = getClass().getResourceAsStream("/national-charge-point-registry-with-errors.csv");

        final MockMultipartFile mockMultipartFile = new MockMultipartFile("file","national-charge-point-registry-with-errors.csv","multipart/form-data", inputStream);

        final MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(CHARGE_POINTS.concat("/").concat(UPLOAD_CSV))
                        .file(mockMultipartFile);

        // WHEN
        final MvcResult mvcResult = mockMvc.perform(builder)
                .andExpect(status().isCreated())
                .andReturn();

        // THEN
        final String content = mvcResult
                .getResponse()
                .getContentAsString();

        final Collection<String> result = objectMapper.readValue(content, new TypeReference<Collection<String>>() {});

        assertThat(result).containsExactlyInAnyOrderElementsOf(errorsExpected);

        final List<ChargePoint> chargePointsSaved = mongoTemplate.findAll(ChargePoint.class);

        assertThat(chargePointsSaved).containsExactlyInAnyOrderElementsOf(chargePointsExpected);
    }

    @Test
    public void updateChargePointsShouldReturnSuccessMessageAndSave6ChargePointsWith201Status() throws Exception {

        // GIVEN
        final Collection<ChargePoint> chargePointsExpected = from(ChargePoint.class)
                .gimme(6, CHELTENHAM_CHASE_HOTEL, POOLE_CIVIC_CENTRE_SURFACE_CAR_PARK, CROSBY_LAKESIDE_ADVENTURE_CENTRE
                        ,WEBBS_OF_WYCHBOLD_RAPID_CHARGER,LONGWELL_GREEN_LEISURE_CENTRE,BIRKDALE_PRIMARY_SCHOOL);

        final Collection<String> errorsExpected = Collections.singleton("All lines were successfully saved.");

        final InputStream inputStream = getClass().getResourceAsStream("/national-charge-point-registry-with-success.csv");

        final MockMultipartFile mockMultipartFile = new MockMultipartFile("file","national-charge-point-registry-with-errors.csv","multipart/form-data", inputStream);

        final MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(CHARGE_POINTS.concat("/").concat(UPLOAD_CSV))
                        .file(mockMultipartFile);

        // WHEN
        final MvcResult mvcResult = mockMvc.perform(builder)
                .andExpect(status().isCreated())
                .andReturn();

        // THEN
        final String content = mvcResult
                .getResponse()
                .getContentAsString();

        final Collection<String> result = objectMapper.readValue(content, new TypeReference<Collection<String>>() {});

        assertThat(result).containsExactlyInAnyOrderElementsOf(errorsExpected);

        final List<ChargePoint> chargePointsSaved = mongoTemplate.findAll(ChargePoint.class);

        assertThat(chargePointsSaved).containsExactlyInAnyOrderElementsOf(chargePointsExpected);
    }

    @Test
    public void updateChargePointsShouldReturn6ErrorsWithStatus422AndNoDataHaveBeenSaved() throws Exception {

        // GIVEN
        final Collection<String> errorsExpected = Arrays.asList(
                "No messages have been saved.",
                "Error read csv line: [Must have source West membership\",Cheltenham Chase Hotel,,,,Gloucestershire County Council,http://www.gloucestershire.gov.uk,01452 425000,,Charge Your Car,http://www.chargeyourcar.org.uk,01912 650500,,Charge Your Car,In service,Published,0,2015-02-03 11:36:04,0000-00-00 00:00:00,N,,,Charge Your Car,n/a,1,,1,,0,,,0,,0,,0,Other,,,,,,,,,,,,,,,,,124739,JEVS G105 (CHAdeMO) DC,50.0,125,400,DC,4,1,In service,,0,124740,Type 2 Combo (IEC62196) DC,50.0,125,400,DC,4,1,In service,,0,124741,Type 2 Mennekes (IEC62196),43.0,63,400,Three Phase AC,3,1,In service,,0,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,], error message: [null]",
                "Error read csv line: [Vehicles must display a valid parking ticket whilst in the charging bay.], error message: [2]",
                "Error read csv line: [\",The charger is located in the layby to the far right of the building.,\"Turn off the main Abbey Road and drive along the leisure centreâ€™s one way access road and around the car park. ], error message: [3]",
                "Error read csv line: [Continue past the front of the building and turn left into the service road next to the timber clad portion. ], error message: [2]",
                "Error read csv line: [The chargepoint is located in the layby next to the building. ], error message: [2]",
                "Error read csv line: [\",,,Worcestershire County Council,http://www.worcestershire.gov.uk,03300 165 126,,Chargemaster (POLAR),https://www.chargemasterplc.com,01582 400331,,Charge Your Car,In service,Published,0,2015-09-09 14:37:12,2015-09-09 14:37:12,Y,2015-08-18 15:25:05,NCR Admin,Charge Your Car,n/a,1,,0,,0,,,0,,1,Behind barrier.,0,Leisure centre,,0,,,,,,,,,,,,,,,125124,JEVS G105 (CHAdeMO) DC,50.0,125,400,DC,4,1,In service,,0,125182,Type 2 Mennekes (IEC62196),43.0,63,400,Three Phase AC,3,1,In service,,0,125183,Type 2 Combo (IEC62196) DC,50.0,125,400,DC,4,1,In service,,0,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,], error message: [null]"
        );

        final InputStream inputStream = getClass().getResourceAsStream("/national-charge-point-registry-with-errors-only.csv");

        final MockMultipartFile mockMultipartFile = new MockMultipartFile("file","national-charge-point-registry-with-errors.csv","multipart/form-data", inputStream);

        final MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(CHARGE_POINTS.concat("/").concat(UPLOAD_CSV))
                        .file(mockMultipartFile);

        // WHEN
        final MvcResult mvcResult = mockMvc.perform(builder)
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        // THEN
        final String content = mvcResult
                .getResponse()
                .getContentAsString();

        final ErrorResponse result = objectMapper.readValue(content, ErrorResponse.class);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(result.getErrors()).containsExactlyInAnyOrderElementsOf(errorsExpected);

        final List<ChargePoint> chargePointsSaved = mongoTemplate.findAll(ChargePoint.class);

        assertThat(chargePointsSaved).isEmpty();
    }

    @Test
    public void updateShouldChange() throws Exception {
        final Collection<ChargePoint> chargePointsExpected = from(ChargePoint.class)
                .gimme(9, SAINSBURY_S_LEICESTER_NORTH_1,SAINSBURY_S_LEICESTER_NORTH_2,SAINSBURY_S_LEICESTER_NORTH_3,CHELTENHAM_CHASE_HOTEL, POOLE_CIVIC_CENTRE_SURFACE_CAR_PARK, CROSBY_LAKESIDE_ADVENTURE_CENTRE
                        ,WEBBS_OF_WYCHBOLD_RAPID_CHARGER,LONGWELL_GREEN_LEISURE_CENTRE,BIRKDALE_PRIMARY_SCHOOL);

        chargePointsExpected.forEach(mongoTemplate::save);

        final ChargePoint chargePointChanged = from(ChargePoint.class)
                .gimme(SAINSBURY_S_LEICESTER_NORTH_1);

        chargePointChanged.setName("Sainsbury's Leicester 10");
        chargePointChanged.setLocation(new Point(chargePointChanged.getLocation().getX() + 1.0, chargePointChanged.getLocation().getX() + 1.0));

        final MvcResult mvcResult = mockMvc.perform(put(CHARGE_POINTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(chargePointChanged)))
                .andExpect(status().isCreated())
                .andReturn();

        final String content = mvcResult.getResponse().getContentAsString();

        final ChargePoint result = objectMapper.readValue(content, ChargePoint.class);

        Assertions.assertThat(result).isNotNull();

        final ChargePoint found = mongoTemplate.findById(result.getChargeDeviceID(), ChargePoint.class);

        Assertions.assertThat(chargePointChanged).isEqualToComparingFieldByField(found);
    }

    @Test
    public void updateShouldNotFindNothing() throws Exception {

        final ChargePoint chargePointChanged = from(ChargePoint.class)
                .gimme(SAINSBURY_S_LEICESTER_NORTH_1);

        final MvcResult mvcResult = mockMvc.perform(put(CHARGE_POINTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(chargePointChanged)))
                .andExpect(status().isNoContent())
                .andReturn();

        final String content = mvcResult.getResponse().getContentAsString();


        Assertions.assertThat(content).isEqualToIgnoringWhitespace("{\"status\":\"NO_CONTENT\",\"errors\":[\"No Charge Point Found.\"]}");
    }

    @Test
    public void updateShouldTryToRecordAnLocationThatExistsAlready() throws Exception {

        final Collection<ChargePoint> chargePointsExpected = from(ChargePoint.class)
                .gimme(9, SAINSBURY_S_LEICESTER_NORTH_1,SAINSBURY_S_LEICESTER_NORTH_2,SAINSBURY_S_LEICESTER_NORTH_3,CHELTENHAM_CHASE_HOTEL, POOLE_CIVIC_CENTRE_SURFACE_CAR_PARK, CROSBY_LAKESIDE_ADVENTURE_CENTRE
                        ,WEBBS_OF_WYCHBOLD_RAPID_CHARGER,LONGWELL_GREEN_LEISURE_CENTRE,BIRKDALE_PRIMARY_SCHOOL);

        chargePointsExpected.forEach(mongoTemplate::save);

        final ChargePoint chargePointChanged = from(ChargePoint.class)
                .gimme(SAINSBURY_S_LEICESTER_NORTH_1);

        final ChargePoint chargePointLongWell = from(ChargePoint.class)
                .gimme(LONGWELL_GREEN_LEISURE_CENTRE);

        chargePointChanged.setLocation(chargePointLongWell.getLocation());

        final MvcResult mvcResult = mockMvc.perform(put(CHARGE_POINTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(chargePointChanged)))
                .andExpect(status().isUnprocessableEntity())
                .andReturn();

        final String content = mvcResult.getResponse().getContentAsString();

        Assertions.assertThat(content).isEqualToIgnoringWhitespace("{\"status\":\"UNPROCESSABLE_ENTITY\",\"errors\":[\"There is another Charge Point at this place\"]}");
    }
}