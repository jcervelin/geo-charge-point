package io.jcervelin.evchargingapi.gateways.file.impl;

import io.jcervelin.evchargingapi.domains.ChargePoint;
import io.jcervelin.evchargingapi.domains.ReadResponse;
import io.jcervelin.evchargingapi.gateways.file.FileParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static java.util.stream.Collectors.toSet;

@Slf4j
@Component
public class CsvFileParserImpl implements FileParser {

    private static final String COMMA = ",";

    public ReadResponse read(final MultipartFile multipartFile) {
        final Collection<String> errors = new ArrayList<>();
        try (InputStreamReader reader = new InputStreamReader(multipartFile.getInputStream());
             BufferedReader br = new BufferedReader(reader)
        ) {
            final Collection<ChargePoint> collect = br.lines()
                    .skip(1)
                    .map(line -> convertRecordToChargePoint(line,errors))
                    .filter(Objects::nonNull)
                    .collect(toSet());
            return ReadResponse.builder()
                    .chargePoints(collect)
                    .errors(errors)
                    .build();
        } catch (final Throwable e) {
            log.error("Error reading csv file.", e.getMessage());
            throw new RuntimeException();
        }
    }

    private ChargePoint convertRecordToChargePoint (String line, Collection<String> errors) {
        try {
            final String[] split = line.split(COMMA);
            ChargePoint chargePoint = new ChargePoint();
            if(StringUtils.isNoneEmpty(split[0]))
                chargePoint.setChargeDeviceID(split[0]);
            else throw new RuntimeException();

            if(StringUtils.isNoneEmpty(split[2]))
                chargePoint.setName(split[2]);
            else throw new RuntimeException();
            chargePoint.setLocation(new Point(
                    Double.parseDouble(split[3]),
                    Double.parseDouble(split[4]))
            );
            return chargePoint;
        } catch (RuntimeException e) {
            String error = String.format("Error read csv line: [%s], error message: [%s]", line, e.getMessage());
            log.error(error);
            errors.add(error);
            return null;
        }
    }


}
