package control.tower.inventory.service.query.queryhandlers;

import control.tower.inventory.service.core.data.entities.InventoryItemEntity;
import control.tower.inventory.service.core.data.repositories.InventoryItemRepository;
import control.tower.inventory.service.query.queries.FindAllInventoryItemsQuery;
import control.tower.inventory.service.query.queries.FindInventoryItemQuery;
import control.tower.inventory.service.query.querymodels.InventoryItemQueryModel;
import lombok.AllArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static control.tower.inventory.service.core.constants.ExceptionMessages.INVENTORY_ITEM_WITH_ID_DOES_NOT_EXIST;

@Component
@AllArgsConstructor
public class InventoryItemsQueryHandler {

    private final InventoryItemRepository inventoryItemRepository;

    @QueryHandler
    public Page<InventoryItemQueryModel> findAllInventoryItems(FindAllInventoryItemsQuery query) {
        return inventoryItemRepository.findAll(query.getPageable())
                .map(this::convertInventoryItemEntityToInventoryItemQueryModel);
    }

    @QueryHandler
    public  InventoryItemQueryModel findInventoryItem(FindInventoryItemQuery query) {
        InventoryItemEntity inventoryItemEntity = inventoryItemRepository.findById(query.getSku()).orElseThrow(
                () -> new IllegalArgumentException(String.format(INVENTORY_ITEM_WITH_ID_DOES_NOT_EXIST, query.getSku())));

        return convertInventoryItemEntityToInventoryItemQueryModel(inventoryItemEntity);
    }

    private InventoryItemQueryModel convertInventoryItemEntityToInventoryItemQueryModel(InventoryItemEntity inventoryItemEntity) {
        return new InventoryItemQueryModel(
                inventoryItemEntity.getSku(),
                inventoryItemEntity.getProductId(),
                inventoryItemEntity.getLocationId(),
                inventoryItemEntity.getBinId()
        );
    }
}
