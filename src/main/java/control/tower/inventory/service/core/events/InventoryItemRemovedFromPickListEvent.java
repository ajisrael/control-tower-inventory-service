package control.tower.inventory.service.core.events;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InventoryItemRemovedFromPickListEvent {

    private String pickId;
    private String sku;
}
