package control.tower.inventory.service.query.rest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InventoryItemRestModel {

    private String sku;
    private String productId;
    private String locationId;
    private String binId;
}
