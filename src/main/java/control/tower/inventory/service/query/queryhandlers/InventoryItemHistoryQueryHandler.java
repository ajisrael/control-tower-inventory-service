package control.tower.inventory.service.query.queryhandlers;

import control.tower.inventory.service.core.data.entities.InventoryItemHistoryEntity;
import control.tower.inventory.service.core.data.repositories.InventoryItemHistoryRepository;
import control.tower.inventory.service.query.queries.FindAllInventoryItemHistoriesQuery;
import control.tower.inventory.service.query.queries.FindInventoryItemHistoryQuery;
import lombok.AllArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;

import static control.tower.inventory.service.core.constants.ExceptionMessages.INVENTORY_ITEM_HISTORY_WITH_ID_DOES_NOT_EXIST;

@Component
@AllArgsConstructor
public class InventoryItemHistoryQueryHandler {

    private final InventoryItemHistoryRepository inventoryItemHistoryRepository;

    @QueryHandler
    public List<InventoryItemHistoryEntity> handle(FindAllInventoryItemHistoriesQuery query) {
        return inventoryItemHistoryRepository.findAll();
    }

    @QueryHandler
    public InventoryItemHistoryEntity handle(FindInventoryItemHistoryQuery query) {
        return inventoryItemHistoryRepository.findById(query.getSku()).orElseThrow(
                () -> new IllegalArgumentException(String.format(INVENTORY_ITEM_HISTORY_WITH_ID_DOES_NOT_EXIST, query.getSku())));
    }
}
