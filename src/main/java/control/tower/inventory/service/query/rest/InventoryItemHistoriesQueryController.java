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
        return queryGateway.query(new FindAllInventoryItemHistoriesQuery(),
                ResponseTypes.multipleInstancesOf(InventoryItemHistoryQueryModel.class)).join();
    }

    @GetMapping(params = "sku")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get inventory item history for sku")
    public InventoryItemHistoryQueryModel getInventoryItemHistory(String sku) {
        return queryGateway.query(new FindInventoryItemHistoryQuery(sku),
                ResponseTypes.instanceOf(InventoryItemHistoryQueryModel.class)).join();
    }

}
