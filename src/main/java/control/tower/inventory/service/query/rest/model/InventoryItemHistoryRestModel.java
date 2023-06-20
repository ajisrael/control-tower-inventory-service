package control.tower.inventory.service.query.rest.model;

import control.tower.inventory.service.core.data.entities.LocationHistoryEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class InventoryItemHistoryRestModel {

    private String sku;
    private LocationHistoryEntity locationHistory;
}
