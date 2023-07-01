package control.tower.inventory.service.query;

import control.tower.inventory.service.core.data.entities.InventoryItemEntity;
import control.tower.inventory.service.core.data.repositories.InventoryItemRepository;
import control.tower.inventory.service.query.queryhandlers.InventoryItemsQueryHandler;
import control.tower.inventory.service.query.queries.FindAllInventoryItemsQuery;
import control.tower.inventory.service.query.queries.FindInventoryItemQuery;
import control.tower.inventory.service.query.querymodels.InventoryItemQueryModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InventoryItemsQueryHandlerTest {

    private InventoryItemsQueryHandler queryHandler;

    @Mock
    private InventoryItemRepository inventoryItemRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        queryHandler = new InventoryItemsQueryHandler(inventoryItemRepository);
    }

    private void populateInventoryItemEntity(InventoryItemEntity inventoryItemEntity, String sku, String productId, String locationId, String binId) {
        inventoryItemEntity.setSku(sku);
        inventoryItemEntity.setProductId(productId);
        inventoryItemEntity.setLocationId(locationId);
        inventoryItemEntity.setBinId(binId);
    }

    private void compareInventoryItemEntityWithInventoryItemQueryModel(InventoryItemEntity inventoryItemEntity, InventoryItemQueryModel inventoryItemQueryModel) {
        assertEquals(inventoryItemEntity.getSku(), inventoryItemQueryModel.getSku());
        assertEquals(inventoryItemEntity.getProductId(), inventoryItemQueryModel.getProductId());
        assertEquals(inventoryItemEntity.getLocationId(), inventoryItemQueryModel.getLocationId());
        assertEquals(inventoryItemEntity.getBinId(), inventoryItemQueryModel.getBinId());
    }

//    @Test
//    void shouldHandleFindAllInventoryItemsQueryAndReturnAllInventoryItems() {
//        // Arrange
//        InventoryItemEntity inventoryItemEntity1 = new InventoryItemEntity();
//        populateInventoryItemEntity(inventoryItemEntity1, "sku1", "productId1", "locationId1", "binId1");
//
//        InventoryItemEntity inventoryItemEntity2 = new InventoryItemEntity();
//        populateInventoryItemEntity(inventoryItemEntity1, "sku2", "productId2", "locationId2", "binId2");
//
//        List<InventoryItemEntity> inventoryItemEntities = new ArrayList<>();
//        inventoryItemEntities.add(inventoryItemEntity1);
//        inventoryItemEntities.add(inventoryItemEntity2);
//
//        Mockito.when(inventoryItemRepository.findAll()).thenReturn(inventoryItemEntities);
//
//        FindAllInventoryItemsQuery query = new FindAllInventoryItemsQuery();
//
//        // Act
//        List<InventoryItemQueryModel> result = queryHandler.findAllInventoryItems(query);
//
//        // Assert
//        assertEquals(inventoryItemEntities.size(), result.size());
//        compareInventoryItemEntityWithInventoryItemQueryModel(inventoryItemEntities.get(0), result.get(0));
//        compareInventoryItemEntityWithInventoryItemQueryModel(inventoryItemEntities.get(1), result.get(1));
//    }

    @Test
    void shouldHandleFindInventoryItemQueryForExistingSkuAndReturnInventoryItemEntity() {
        // Arrange
        InventoryItemEntity inventoryItemEntity = new InventoryItemEntity();
        populateInventoryItemEntity(inventoryItemEntity,"sku", "productId", "locationId", "binId");

        Mockito.when(inventoryItemRepository.findById(inventoryItemEntity.getSku())).thenReturn(Optional.of(inventoryItemEntity));

        FindInventoryItemQuery query = new FindInventoryItemQuery(inventoryItemEntity.getSku());

        // Act
        InventoryItemQueryModel result = queryHandler.findInventoryItem(query);

        // Assert
        compareInventoryItemEntityWithInventoryItemQueryModel(inventoryItemEntity, result);
    }

    @Test
    void shouldHandleFindInventoryItemQueryForNonExistingSkuAndThrowException() {
        // Arrange
        String sku = "nonExistingSku";

        Mockito.when(inventoryItemRepository.findById(sku)).thenReturn(Optional.empty());

        FindInventoryItemQuery query = new FindInventoryItemQuery(sku);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> queryHandler.findInventoryItem(query));
    }
}
