package control.tower.inventory.service.command.eventhandlers;

import control.tower.inventory.service.core.data.entities.InventoryItemLookupEntity;
import control.tower.inventory.service.core.data.repositories.InventoryItemLookupRepository;
import control.tower.inventory.service.core.events.InventoryItemCreatedEvent;
import control.tower.inventory.service.core.events.InventoryItemRemovedEvent;
import lombok.AllArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import static control.tower.core.utils.Helper.throwExceptionIfEntityDoesNotExist;
import static control.tower.inventory.service.core.constants.ExceptionMessages.INVENTORY_ITEM_LOOKUP_ENTITY_WITH_ID_DOES_NOT_EXIST;

@Component
@AllArgsConstructor
@ProcessingGroup("inventory-item-group")
public class InventoryItemLookupEventsHandler {

    private InventoryItemLookupRepository inventoryItemLookupRepository;

    @EventHandler
    public void on(InventoryItemCreatedEvent event) {
        inventoryItemLookupRepository.save(new InventoryItemLookupEntity(event.getSku()));
    }

    @EventHandler
    public void on(InventoryItemRemovedEvent event) {
        InventoryItemLookupEntity inventoryItemLookupEntity = inventoryItemLookupRepository.findBySku(event.getSku());
        throwExceptionIfEntityDoesNotExist(inventoryItemLookupEntity, String.format(INVENTORY_ITEM_LOOKUP_ENTITY_WITH_ID_DOES_NOT_EXIST, event.getSku()));
        inventoryItemLookupRepository.delete(inventoryItemLookupEntity);
    }
}
