package control.tower.inventory.service.query.rest;

import control.tower.inventory.service.core.data.InventoryItemEntity;
import control.tower.inventory.service.query.FindAllInventoryItemsQuery;
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
    public List<InventoryItemRestModel> getInventoryItems() {
        FindAllInventoryItemsQuery findAllInventoryItemsQuery = new FindAllInventoryItemsQuery();

        List<InventoryItemEntity> inventoryItemEntities = queryGateway.query(findAllInventoryItemsQuery,
                ResponseTypes.multipleInstancesOf(InventoryItemEntity.class)).join();

        return convertInventoryItemEntitiesToInventoryItemRestModels(inventoryItemEntities);
    }

    private List<InventoryItemRestModel> convertInventoryItemEntitiesToInventoryItemRestModels(
            List<InventoryItemEntity> inventoryItemEntities) {
        List<InventoryItemRestModel> inventoryItemRestModels = new ArrayList<>();

        for (InventoryItemEntity inventoryItemEntity: inventoryItemEntities) {
            inventoryItemRestModels.add(new InventoryItemRestModel(
                    inventoryItemEntity.getSku(),
                    inventoryItemEntity.getProductId(),
                    inventoryItemEntity.getLocationId(),
                    inventoryItemEntity.getBinId()
            ));
        }

        return inventoryItemRestModels;
    }
}
