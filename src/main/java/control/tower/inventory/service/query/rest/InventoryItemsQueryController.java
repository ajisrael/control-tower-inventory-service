package control.tower.inventory.service.query.rest;

import control.tower.inventory.service.core.data.entities.InventoryItemEntity;
import control.tower.inventory.service.query.queries.FindAllInventoryItemsQuery;
import control.tower.inventory.service.query.queries.FindInventoryItemQuery;
import control.tower.inventory.service.query.querymodels.InventoryItemQueryModel;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryItemsQueryController {

    @Autowired
    QueryGateway queryGateway;

    @GetMapping
    public List<InventoryItemQueryModel> getInventoryItems() {
        return queryGateway.query(new FindAllInventoryItemsQuery(),
                ResponseTypes.multipleInstancesOf(InventoryItemQueryModel.class)).join();
    }

    @GetMapping(params = "sku")
    public InventoryItemQueryModel getInventoryItem(String sku) {
        return queryGateway.query(new FindInventoryItemQuery(sku),
                ResponseTypes.instanceOf(InventoryItemQueryModel.class)).join();
    }
}
