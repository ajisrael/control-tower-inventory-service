package control.tower.inventory.service.command;

import control.tower.inventory.service.core.data.InventoryItemLookupEntity;
import control.tower.inventory.service.core.data.InventoryItemLookupRepository;
import control.tower.inventory.service.core.events.InventoryItemCreatedEvent;
import lombok.AllArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@ProcessingGroup("inventory-item-group")
public class InventoryItemLookupEventsHandler {

    private InventoryItemLookupRepository inventoryItemLookupRepository;

    @EventHandler
    public void on(InventoryItemCreatedEvent event) {
        inventoryItemLookupRepository.save(new InventoryItemLookupEntity(event.getSku()));
    }
}
