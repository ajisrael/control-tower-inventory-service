package control.tower.inventory.service.query.rest;

import control.tower.inventory.service.core.data.entities.InventoryItemHistoryEntity;
import control.tower.inventory.service.query.queries.FindAllInventoryItemHistoriesQuery;
import control.tower.inventory.service.query.queries.FindInventoryItemHistoryQuery;
import control.tower.inventory.service.query.querymodels.InventoryItemHistoryQueryModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/inventoryHistory")
@Tag(name = "Inventory Item History Query API")
public class InventoryItemHistoriesQueryController {

    @Autowired
    QueryGateway queryGateway;

    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all inventory item histories")
    public List<InventoryItemHistoryQueryModel> getInventoryItemHistories() {
        List<InventoryItemHistoryEntity> inventoryItemHistoryEntities = queryGateway.query(new FindAllInventoryItemHistoriesQuery(),
                ResponseTypes.multipleInstancesOf(InventoryItemHistoryEntity.class)).join();

        return convertInventoryItemHistoryEntitiesToInventoryItemHistoryRestModels(inventoryItemHistoryEntities);
    }

    @GetMapping(params = "sku")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get inventory item history for sku")
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
