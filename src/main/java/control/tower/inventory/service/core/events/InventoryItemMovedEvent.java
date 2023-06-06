package control.tower.inventory.service.core.events;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InventoryItemMovedEvent {

    private String sku;
    private String productId;
    private String locationId;
    private String binId;
}
