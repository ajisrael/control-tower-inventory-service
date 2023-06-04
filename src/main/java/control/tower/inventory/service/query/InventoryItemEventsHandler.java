package control.tower.inventory.service.query;

import control.tower.inventory.service.core.data.InventoryItemEntity;
import control.tower.inventory.service.core.data.InventoryItemRepository;
import control.tower.inventory.service.core.events.InventoryItemCreatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class InventoryItemEventsHandler {

    private final InventoryItemRepository inventoryItemRepository;

    public InventoryItemEventsHandler(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    @EventHandler
    public void on(InventoryItemCreatedEvent event) {
        InventoryItemEntity inventoryItemEntity = new InventoryItemEntity();
        BeanUtils.copyProperties(event, inventoryItemEntity);
        inventoryItemRepository.save(inventoryItemEntity);
    }
}
