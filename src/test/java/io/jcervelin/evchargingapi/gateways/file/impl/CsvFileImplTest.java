package io.jcervelin.evchargingapi.gateways.file.impl;

import io.jcervelin.evchargingapi.EvChargingApiApplication;
import io.jcervelin.evchargingapi.domains.ChargePoint;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {EvChargingApiApplication.class})
public class CsvFileImplTest {

    @Autowired
    private CsvFileParserImpl target;

    @Test
    public void read() throws IOException {

        final InputStream inputStream = getClass().getResourceAsStream("/national-charge-point-registry-with-errors.csv");
        byte[] bytes = IOUtils.toByteArray(inputStream);

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file","national-charge-point-registry-with-errors.csv","multipart/form-data", bytes);
        final Collection<ChargePoint> resultList = target.read(mockMultipartFile).getChargePoints();
        Assertions.assertThat(resultList.size()).isEqualTo(6);

    }
}