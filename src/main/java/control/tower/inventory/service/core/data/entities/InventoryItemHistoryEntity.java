package control.tower.inventory.service.core.data.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "inventoryItemHistory")
public class InventoryItemHistoryEntity {

    @Id
    private String sku;
    @Embedded
    private LocationHistoryEntity locationHistory;

    public InventoryItemHistoryEntity(String sku, String startingLocationId, String startingBinId, Instant timestamp) {
        this.sku = sku;
        this.locationHistory = new LocationHistoryEntity();
        this.locationHistory.addLocation(startingLocationId, startingBinId, timestamp);
    }

    public void addLocationToLocationHistory(String locationId, String binId, Instant timestamp) {
        this.locationHistory.addLocation(locationId, binId, timestamp);
    }
}
