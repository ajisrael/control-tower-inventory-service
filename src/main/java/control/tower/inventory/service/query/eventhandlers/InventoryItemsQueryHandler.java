package control.tower.inventory.service.query.eventhandlers;

import control.tower.inventory.service.core.data.entities.InventoryItemEntity;
import control.tower.inventory.service.core.data.repositories.InventoryItemRepository;
import control.tower.inventory.service.query.queries.FindAllInventoryItemsQuery;
import control.tower.inventory.service.query.queries.FindInventoryItemQuery;
import lombok.AllArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;

import static control.tower.inventory.service.core.constants.ExceptionMessages.INVENTORY_ITEM_WITH_ID_DOES_NOT_EXIST;

@Component
@AllArgsConstructor
public class InventoryItemsQueryHandler {

    private final InventoryItemRepository inventoryItemRepository;

    @QueryHandler
    public List<InventoryItemEntity> findAllInventoryItems(FindAllInventoryItemsQuery query) {
        return inventoryItemRepository.findAll();
    }

    @QueryHandler
    public  InventoryItemEntity findInventoryItem(FindInventoryItemQuery query) {
        return inventoryItemRepository.findById(query.getSku()).orElseThrow(
                () -> new IllegalStateException(String.format(INVENTORY_ITEM_WITH_ID_DOES_NOT_EXIST, query.getSku())));
    }
}
