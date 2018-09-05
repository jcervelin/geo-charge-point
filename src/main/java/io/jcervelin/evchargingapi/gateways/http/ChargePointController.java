package io.jcervelin.evchargingapi.gateways.http;

import io.jcervelin.evchargingapi.domains.ChargePoint;
import io.jcervelin.evchargingapi.usecases.ChargePointManagement;
import io.jcervelin.evchargingapi.usecases.UploadFile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

import static io.jcervelin.evchargingapi.domains.Endpoints.CHARGE_POINTS;
import static io.jcervelin.evchargingapi.domains.Endpoints.UPLOAD_CSV;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(CHARGE_POINTS)
@Api(value = CHARGE_POINTS, description = "Operations pertaining to Charge Points")
public class ChargePointController {
    @Autowired
    private ChargePointManagement chargePointManagement;

    @Autowired
    private UploadFile uploadFile;

    @GetMapping
    @ApiOperation(value = "Given latitude and longitude, find the near 'n' charge points")
    public List<ChargePoint> findChargePoint(@RequestParam double latitude,
                                             @RequestParam double longitude,
                                             @RequestParam int amount) {
        return chargePointManagement.findByLocation(latitude, longitude,amount);
    }

    @ResponseStatus(CREATED)
    @ApiOperation(value = "Upload csv with charge points")
    @PostMapping(produces = APPLICATION_JSON_VALUE, value = UPLOAD_CSV, headers = "content-type=multipart/form-data",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Collection<String> updateChargePoints(@RequestParam("file") final MultipartFile multipartFile) {
        return uploadFile.uploadFile(multipartFile);
    }
}