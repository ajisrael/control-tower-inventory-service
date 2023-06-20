package control.tower.inventory.service.query.eventhandlers;

import control.tower.inventory.service.core.data.entities.InventoryItemHistoryEntity;
import control.tower.inventory.service.core.data.repositories.InventoryItemHistoryRepository;
import control.tower.inventory.service.core.events.InventoryItemCreatedEvent;
import control.tower.inventory.service.core.events.InventoryItemMovedEvent;
import control.tower.inventory.service.core.events.InventoryItemRemovedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.Timestamp;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@ProcessingGroup("inventory-item-history")
public class InventoryItemHistoryEventsHandler {

    private final InventoryItemHistoryRepository inventoryItemHistoryRepository;

    public InventoryItemHistoryEventsHandler(InventoryItemHistoryRepository inventoryItemHistoryRepository) {
        this.inventoryItemHistoryRepository = inventoryItemHistoryRepository;
    }

    @EventHandler
    public void on(InventoryItemCreatedEvent event, @Timestamp Instant createdAt) {
        inventoryItemHistoryRepository.save(
                new InventoryItemHistoryEntity(event.getSku(), event.getLocationId(), event.getBinId(), createdAt));
    }

    @EventHandler
    public void on(InventoryItemMovedEvent event, @Timestamp Instant movedAt) {
        inventoryItemHistoryRepository.findById(event.getSku()).ifPresent(
                inventoryItemHistoryEntity -> {
                    inventoryItemHistoryEntity.addLocationToLocationHistory(event.getLocationId(), event.getBinId(), movedAt);
                    inventoryItemHistoryRepository.save(inventoryItemHistoryEntity);
                }
        );
    }

    @EventHandler
    public void on(InventoryItemRemovedEvent event) {
        inventoryItemHistoryRepository.deleteById(event.getSku());
    }
}
