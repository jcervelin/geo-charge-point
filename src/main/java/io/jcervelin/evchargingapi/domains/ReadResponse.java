package io.jcervelin.evchargingapi.domains;

import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Data
@Builder
public class ReadResponse {
    private Collection<ChargePoint> chargePoints;
    private Collection<String> errors;
}
