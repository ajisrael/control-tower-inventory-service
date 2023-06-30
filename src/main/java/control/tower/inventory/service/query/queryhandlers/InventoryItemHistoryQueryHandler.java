package control.tower.inventory.service.query.queryhandlers;

import control.tower.inventory.service.core.data.entities.InventoryItemHistoryEntity;
import control.tower.inventory.service.core.data.repositories.InventoryItemHistoryRepository;
import control.tower.inventory.service.query.queries.FindAllInventoryItemHistoriesQuery;
import control.tower.inventory.service.query.queries.FindInventoryItemHistoryQuery;
import control.tower.inventory.service.query.querymodels.InventoryItemHistoryQueryModel;
import lombok.AllArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import static control.tower.inventory.service.core.constants.ExceptionMessages.INVENTORY_ITEM_HISTORY_WITH_ID_DOES_NOT_EXIST;

@Component
@AllArgsConstructor
public class InventoryItemHistoryQueryHandler {

    private final InventoryItemHistoryRepository inventoryItemHistoryRepository;

    @QueryHandler
    public Page<InventoryItemHistoryQueryModel> handle(FindAllInventoryItemHistoriesQuery query) {
        return inventoryItemHistoryRepository.findAll(query.getPageable())
                .map(this::convertInventoryItemHistoryEntityToInventoryItemQueryModel);
    }

    @QueryHandler
    public InventoryItemHistoryQueryModel handle(FindInventoryItemHistoryQuery query) {
        InventoryItemHistoryEntity inventoryItemHistoryEntity = inventoryItemHistoryRepository.findById(query.getSku()).orElseThrow(
                () -> new IllegalArgumentException(String.format(INVENTORY_ITEM_HISTORY_WITH_ID_DOES_NOT_EXIST, query.getSku())));

        return convertInventoryItemHistoryEntityToInventoryItemQueryModel(inventoryItemHistoryEntity);
    }

    private InventoryItemHistoryQueryModel convertInventoryItemHistoryEntityToInventoryItemQueryModel(InventoryItemHistoryEntity inventoryItemHistoryEntity) {
        return new InventoryItemHistoryQueryModel(inventoryItemHistoryEntity.getSku(), inventoryItemHistoryEntity.getLocationHistory());
    }
}
