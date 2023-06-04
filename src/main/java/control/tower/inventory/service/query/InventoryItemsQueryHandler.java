package control.tower.inventory.service.query;

import control.tower.inventory.service.core.data.InventoryItemEntity;
import control.tower.inventory.service.core.data.InventoryItemRepository;
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
}
