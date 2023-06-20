package control.tower.inventory.service.query.rest;

import control.tower.inventory.service.core.data.entities.InventoryItemHistoryEntity;
import control.tower.inventory.service.query.queries.FindAllInventoryItemHistoriesQuery;
import control.tower.inventory.service.query.queries.FindInventoryItemHistoryQuery;
import control.tower.inventory.service.query.rest.model.InventoryItemHistoryRestModel;
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
    public List<InventoryItemHistoryRestModel> getInventoryItemHistories() {
        List<InventoryItemHistoryEntity> inventoryItemHistoryEntities = queryGateway.query(new FindAllInventoryItemHistoriesQuery(),
                ResponseTypes.multipleInstancesOf(InventoryItemHistoryEntity.class)).join();

        return convertInventoryItemHistoryEntitiesToInventoryItemHistoryRestModels(inventoryItemHistoryEntities);
    }

    @GetMapping(params = "sku")
    public InventoryItemHistoryRestModel getInventoryItemHistory(String sku) {
        InventoryItemHistoryEntity inventoryItemHistoryEntity = queryGateway.query(new FindInventoryItemHistoryQuery(sku),
                ResponseTypes.instanceOf(InventoryItemHistoryEntity.class)).join();

        return convertInventoryItemHistoryEntityToInventoryItemRestModel(inventoryItemHistoryEntity);
    }

    private List<InventoryItemHistoryRestModel> convertInventoryItemHistoryEntitiesToInventoryItemHistoryRestModels(List<InventoryItemHistoryEntity> inventoryItemHistoryEntities) {
        List<InventoryItemHistoryRestModel> inventoryItemHistoryRestModels = new ArrayList<>();

        for (InventoryItemHistoryEntity inventoryItemHistoryEntity: inventoryItemHistoryEntities) {
            inventoryItemHistoryRestModels.add(convertInventoryItemHistoryEntityToInventoryItemRestModel(inventoryItemHistoryEntity));
        }

        return inventoryItemHistoryRestModels;
    }

    private InventoryItemHistoryRestModel convertInventoryItemHistoryEntityToInventoryItemRestModel(InventoryItemHistoryEntity inventoryItemHistoryEntity) {
        return new InventoryItemHistoryRestModel(inventoryItemHistoryEntity.getSku(), inventoryItemHistoryEntity.getLocationHistory());
    }
}
