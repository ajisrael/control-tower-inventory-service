package control.tower.inventory.service.core.events;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InventoryItemRemovedEvent {

    private String sku;
    private String productId;
}
