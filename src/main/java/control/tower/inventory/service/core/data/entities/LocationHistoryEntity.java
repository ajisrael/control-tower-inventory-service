package control.tower.inventory.service.core.data.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OrderBy;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@NoArgsConstructor
public class LocationHistoryEntity {

    @ElementCollection(fetch = FetchType.EAGER)
    @OrderBy("timestamp DESC")
    private List<LocationEntryEntity> locations = new ArrayList<>();

    public void addLocation(String locationId, String binId, Instant timestamp) {
        locations.add(new LocationEntryEntity(locationId, binId, timestamp));
    }
}
