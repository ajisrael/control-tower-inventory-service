package control.tower.inventory.service.query.eventhandlers;

import control.tower.inventory.service.core.data.entities.InventoryItemEntity;
import control.tower.inventory.service.core.data.repositories.InventoryItemRepository;
import control.tower.inventory.service.core.events.InventoryItemCreatedEvent;
import control.tower.inventory.service.core.events.InventoryItemMovedEvent;
import control.tower.inventory.service.core.events.InventoryItemRemovedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import static control.tower.core.utils.Helper.throwExceptionIfEntityDoesNotExist;
import static control.tower.inventory.service.core.constants.ExceptionMessages.INVENTORY_ITEM_WITH_ID_DOES_NOT_EXIST;

@Component
@ProcessingGroup("inventory-item-group")
public class InventoryItemEventsHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryItemEventsHandler.class);

    private final InventoryItemRepository inventoryItemRepository;

    public InventoryItemEventsHandler(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    @ExceptionHandler(resultType = Exception.class)
    public void handle(Exception exception) throws Exception {
        throw exception;
    }

    @ExceptionHandler(resultType = IllegalArgumentException.class)
    public void handle(IllegalArgumentException exception) {
        LOGGER.error(exception.getLocalizedMessage());
    }

    @EventHandler
    public void on(InventoryItemCreatedEvent event) {
        InventoryItemEntity inventoryItemEntity = new InventoryItemEntity();
        BeanUtils.copyProperties(event, inventoryItemEntity);
        inventoryItemRepository.save(inventoryItemEntity);
    }

    @EventHandler
    public void on(InventoryItemMovedEvent event) {
        InventoryItemEntity inventoryItemEntity = inventoryItemRepository.findBySku(event.getSku());

        throwExceptionIfEntityDoesNotExist(inventoryItemEntity, String.format(INVENTORY_ITEM_WITH_ID_DOES_NOT_EXIST, event.getSku()));

        inventoryItemEntity.setLocationId(event.getLocationId());
        inventoryItemEntity.setBinId(event.getBinId());

        inventoryItemRepository.save(inventoryItemEntity);
    }

    @EventHandler
    public void on(InventoryItemRemovedEvent event) {
        InventoryItemEntity inventoryItemEntity = inventoryItemRepository.findBySku(event.getSku());
        throwExceptionIfEntityDoesNotExist(inventoryItemEntity, String.format(INVENTORY_ITEM_WITH_ID_DOES_NOT_EXIST, event.getSku()));
        inventoryItemRepository.delete(inventoryItemEntity);
    }
}
