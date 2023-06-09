package control.tower.inventory.service.query;

import control.tower.inventory.service.core.data.InventoryItemEntity;
import control.tower.inventory.service.core.data.InventoryItemRepository;
import control.tower.inventory.service.query.queries.FindAllInventoryItemsQuery;
import control.tower.inventory.service.query.queries.FindInventoryItemQuery;
import lombok.AllArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;

import static control.tower.core.utils.Helper.throwErrorIfEntityDoesNotExist;

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
        InventoryItemEntity inventoryItemEntity = inventoryItemRepository.findBySku(query.getSku());

        throwErrorIfEntityDoesNotExist(inventoryItemEntity, String.format("Inventory item %s does not exist", query.getSku()));

        return inventoryItemEntity;
    }
}
