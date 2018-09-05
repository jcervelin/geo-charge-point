package io.jcervelin.evchargingapi.domains;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "chargePoints")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="location")
public class ChargePoint {
    @Id
    private String chargeDeviceID;
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2D,name="location")
    private Point location;
    private String name;

}