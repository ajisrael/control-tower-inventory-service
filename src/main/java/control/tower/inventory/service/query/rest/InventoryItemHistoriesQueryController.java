package control.tower.inventory.service.query.rest;

import control.tower.inventory.service.core.data.entities.InventoryItemHistoryEntity;
import control.tower.inventory.service.query.queries.FindAllInventoryItemHistoriesQuery;
import control.tower.inventory.service.query.queries.FindInventoryItemHistoryQuery;
import control.tower.inventory.service.query.querymodels.InventoryItemHistoryQueryModel;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/inventoryHistory")
public class InventoryItemHistoriesQueryController {

    @Autowired
    QueryGateway queryGateway;

    @GetMapping
    public List<InventoryItemHistoryQueryModel> getInventoryItemHistories() {
        List<InventoryItemHistoryEntity> inventoryItemHistoryEntities = queryGateway.query(new FindAllInventoryItemHistoriesQuery(),
                ResponseTypes.multipleInstancesOf(InventoryItemHistoryEntity.class)).join();

        return convertInventoryItemHistoryEntitiesToInventoryItemHistoryRestModels(inventoryItemHistoryEntities);
    }

    @GetMapping(params = "sku")
    public InventoryItemHistoryQueryModel getInventoryItemHistory(String sku) {
        InventoryItemHistoryEntity inventoryItemHistoryEntity = queryGateway.query(new FindInventoryItemHistoryQuery(sku),
                ResponseTypes.instanceOf(InventoryItemHistoryEntity.class)).join();

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
