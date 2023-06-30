package control.tower.inventory.service.query.queryhandlers;

import control.tower.inventory.service.core.data.entities.InventoryItemHistoryEntity;
import control.tower.inventory.service.core.data.repositories.InventoryItemHistoryRepository;
import control.tower.inventory.service.query.queries.FindAllInventoryItemHistoriesQuery;
import control.tower.inventory.service.query.queries.FindInventoryItemHistoryQuery;
import control.tower.inventory.service.query.querymodels.InventoryItemHistoryQueryModel;
import lombok.AllArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static control.tower.inventory.service.core.constants.ExceptionMessages.INVENTORY_ITEM_HISTORY_WITH_ID_DOES_NOT_EXIST;

@Component
@AllArgsConstructor
public class InventoryItemHistoryQueryHandler {

    private final InventoryItemHistoryRepository inventoryItemHistoryRepository;

    @QueryHandler
    public List<InventoryItemHistoryQueryModel> handle(FindAllInventoryItemHistoriesQuery query) {
        return inventoryItemHistoryRepository.findAll().stream()
                .map(this::convertInventoryItemHistoryEntityToInventoryItemRestModel)
                .collect(Collectors.toList());
    }

    @QueryHandler
    public InventoryItemHistoryQueryModel handle(FindInventoryItemHistoryQuery query) {
        InventoryItemHistoryEntity inventoryItemHistoryEntity = inventoryItemHistoryRepository.findById(query.getSku()).orElseThrow(
                () -> new IllegalArgumentException(String.format(INVENTORY_ITEM_HISTORY_WITH_ID_DOES_NOT_EXIST, query.getSku())));

        return convertInventoryItemHistoryEntityToInventoryItemRestModel(inventoryItemHistoryEntity);
    }

    private List<InventoryItemHistoryQueryModel> convertInventoryItemHistoryEntitiesToInventoryItemHistoryRestModels(List<InventoryItemHistoryEntity> inventoryItemHistoryEntities) {
        List<InventoryItemHistoryQueryModel> inventoryItemHistoryQueryModels = new ArrayList<>();

        for (InventoryItemHistoryEntity inventoryItemHistoryEntity: inventoryItemHistoryEntities) {
            inventoryItemHistoryQueryModels.add(convertInventoryItemHistoryEntityToInventoryItemRestModel(inventoryItemHistoryEntity));
        }

        return inventoryItemHistoryQueryModels;
    }

    private InventoryItemHistoryQueryModel convertInventoryItemHistoryEntityToInventoryItemRestModel(InventoryItemHistoryEntity inventoryItemHistoryEntity) {
        return new InventoryItemHistoryQueryModel(inventoryItemHistoryEntity.getSku(), inventoryItemHistoryEntity.getLocationHistory());
    }
}
