package control.tower.inventory.service.core.data.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Embeddable;
import java.time.Instant;

@Embeddable
@Getter
@AllArgsConstructor
public class LocationEntryEntity {

    private final String locationId;
    private final String binId;
    private final Instant timestamp;

    public LocationEntryEntity() {
        this.locationId = "default";
        this.binId = "location";
        this.timestamp = Instant.now();
    }
}

