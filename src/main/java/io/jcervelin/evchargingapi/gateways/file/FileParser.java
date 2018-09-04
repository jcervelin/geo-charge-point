package io.jcervelin.evchargingapi.gateways.file;

import io.jcervelin.evchargingapi.domains.ReadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileParser {

    ReadResponse read(MultipartFile multipartFile) ;

}