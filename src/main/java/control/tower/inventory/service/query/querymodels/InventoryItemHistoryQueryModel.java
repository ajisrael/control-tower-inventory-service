package control.tower.inventory.service.query.querymodels;

import control.tower.inventory.service.core.data.entities.LocationHistoryEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InventoryItemHistoryQueryModel {

    private String sku;
    private LocationHistoryEntity locationHistory;
}
