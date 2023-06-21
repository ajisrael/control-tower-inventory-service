package control.tower.inventory.service.query.querymodels;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InventoryItemQueryModel {

    private String sku;
    private String productId;
    private String locationId;
    private String binId;
}
