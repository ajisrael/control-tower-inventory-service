package control.tower.inventory.service.query;

import control.tower.inventory.service.core.data.entities.InventoryItemEntity;
import control.tower.inventory.service.core.data.repositories.InventoryItemRepository;
import control.tower.inventory.service.query.queryhandlers.InventoryItemsQueryHandler;
import control.tower.inventory.service.query.queries.FindAllInventoryItemsQuery;
import control.tower.inventory.service.query.queries.FindInventoryItemQuery;
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

    @Test
    void shouldHandleFindAllInventoryItemsQueryAndReturnAllInventoryItems() {
        // Arrange
        InventoryItemEntity inventoryItemEntity1 = new InventoryItemEntity();
        populateInventoryItemEntity(inventoryItemEntity1, "sku1", "productId1", "locationId1", "binId1");

        InventoryItemEntity inventoryItemEntity2 = new InventoryItemEntity();
        populateInventoryItemEntity(inventoryItemEntity1, "sku2", "productId2", "locationId2", "binId2");

        List<InventoryItemEntity> inventoryItemEntities = new ArrayList<>();
        inventoryItemEntities.add(inventoryItemEntity1);
        inventoryItemEntities.add(inventoryItemEntity2);

        Mockito.when(inventoryItemRepository.findAll()).thenReturn(inventoryItemEntities);

        FindAllInventoryItemsQuery query = new FindAllInventoryItemsQuery();

        // Act
        List<InventoryItemEntity> result = queryHandler.findAllInventoryItems(query);

        // Assert
        assertEquals(inventoryItemEntities.size(), result.size());
        assertEquals(inventoryItemEntities.get(0), result.get(0));
        assertEquals(inventoryItemEntities.get(1), result.get(1));
    }

    @Test
    void shouldHandleFindInventoryItemQueryForExistingSkuAndReturnInventoryItemEntity() {
        // Arrange
        InventoryItemEntity inventoryItemEntity = new InventoryItemEntity();
        populateInventoryItemEntity(inventoryItemEntity,"sku", "productId", "locationId", "binId");

        Mockito.when(inventoryItemRepository.findById(inventoryItemEntity.getSku())).thenReturn(Optional.of(inventoryItemEntity));

        FindInventoryItemQuery query = new FindInventoryItemQuery(inventoryItemEntity.getSku());

        // Act
        InventoryItemEntity result = queryHandler.findInventoryItem(query);

        // Assert
        assertEquals(inventoryItemEntity, result);
    }

    @Test
    void shouldHandleFindInventoryItemQueryForNonExistingSkuAndThrowException() {
        // Arrange
        String sku = "nonExistingSku";

        Mockito.when(inventoryItemRepository.findById(sku)).thenReturn(Optional.empty());

        FindInventoryItemQuery query = new FindInventoryItemQuery(sku);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> queryHandler.findInventoryItem(query));
    }
}
