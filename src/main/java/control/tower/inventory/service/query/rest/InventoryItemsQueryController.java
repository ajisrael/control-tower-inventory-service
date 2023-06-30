package control.tower.inventory.service.query.rest;

import control.tower.inventory.service.query.queries.FindAllInventoryItemsQuery;
import control.tower.inventory.service.query.queries.FindInventoryItemQuery;
import control.tower.inventory.service.query.querymodels.InventoryItemQueryModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@Tag(name = "Inventory Item Query API")
public class InventoryItemsQueryController {

    @Autowired
    QueryGateway queryGateway;

    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all inventory items")
    public List<InventoryItemQueryModel> getInventoryItems() {
        return queryGateway.query(new FindAllInventoryItemsQuery(),
                ResponseTypes.multipleInstancesOf(InventoryItemQueryModel.class)).join();
    }

    @GetMapping(params = "sku")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get inventory item by sku")
    public InventoryItemQueryModel getInventoryItem(String sku) {
        return queryGateway.query(new FindInventoryItemQuery(sku),
                ResponseTypes.instanceOf(InventoryItemQueryModel.class)).join();
    }
}
