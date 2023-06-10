package control.tower.inventory.service.query;

import control.tower.inventory.service.core.data.InventoryItemEntity;
import control.tower.inventory.service.core.data.InventoryItemRepository;
import control.tower.inventory.service.query.queries.FindAllInventoryItemsQuery;
import control.tower.inventory.service.query.queries.FindInventoryItemQuery;
import lombok.AllArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;

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
                () -> new IllegalStateException(String.format("Inventory item %s does not exist", query.getSku())));
    }
}
