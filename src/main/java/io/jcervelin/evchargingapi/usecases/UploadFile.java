package io.jcervelin.evchargingapi.usecases;

import io.jcervelin.evchargingapi.domains.ReadResponse;
import io.jcervelin.evchargingapi.domains.exceptions.UnprocessableEntityException;
import io.jcervelin.evchargingapi.gateways.file.FileParser;
import io.jcervelin.evchargingapi.gateways.mongo.ChargePointRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;


@Component
@RequiredArgsConstructor
public class UploadFile {

    private final FileParser fileParserGateway;
    private final ChargePointRepositoryCustom repository;

    public Collection<String> uploadFile(final MultipartFile multipartFile) {
        final ReadResponse read = fileParserGateway.read(multipartFile);
        if(read.getMsgs().size() == 0)
            read.getMsgs().add("All lines were successfully saved.");
        if (CollectionUtils.emptyIfNull(read.getChargePoints()).size() == 0)
            throw new UnprocessableEntityException("No messages have been saved.",read.getMsgs());
        repository.save(read.getChargePoints());
        return read.getMsgs();
    }
}
