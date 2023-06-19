package control.tower.inventory.service.query.rest;

import control.tower.inventory.service.core.data.entities.InventoryItemEntity;
import control.tower.inventory.service.query.queries.FindAllInventoryItemsQuery;
import control.tower.inventory.service.query.queries.FindInventoryItemQuery;
import control.tower.inventory.service.query.rest.model.InventoryItemRestModel;
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
        List<InventoryItemEntity> inventoryItemEntities = queryGateway.query(new FindAllInventoryItemsQuery(),
                ResponseTypes.multipleInstancesOf(InventoryItemEntity.class)).join();

        return convertInventoryItemEntitiesToInventoryItemRestModels(inventoryItemEntities);
    }

    @GetMapping(params = "sku")
    public InventoryItemRestModel getInventoryItem(String sku) {
        InventoryItemEntity inventoryItemEntity = queryGateway.query(new FindInventoryItemQuery(sku),
                ResponseTypes.instanceOf(InventoryItemEntity.class)).join();

        return convertInventoryItemEntityToInventoryItemRestModel(inventoryItemEntity);
    }

    private List<InventoryItemRestModel> convertInventoryItemEntitiesToInventoryItemRestModels(
            List<InventoryItemEntity> inventoryItemEntities) {
        List<InventoryItemRestModel> inventoryItemRestModels = new ArrayList<>();

        for (InventoryItemEntity inventoryItemEntity: inventoryItemEntities) {
            inventoryItemRestModels.add(convertInventoryItemEntityToInventoryItemRestModel(inventoryItemEntity));
        }

        return inventoryItemRestModels;
    }

    private InventoryItemRestModel convertInventoryItemEntityToInventoryItemRestModel(InventoryItemEntity inventoryItemEntity) {
        return new InventoryItemRestModel(
                inventoryItemEntity.getSku(),
                inventoryItemEntity.getProductId(),
                inventoryItemEntity.getLocationId(),
                inventoryItemEntity.getBinId()
        );
    }
}
