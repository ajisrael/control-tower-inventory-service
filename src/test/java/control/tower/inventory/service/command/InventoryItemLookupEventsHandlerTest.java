package control.tower.inventory.service.command;

import control.tower.inventory.service.command.eventhandlers.InventoryItemLookupEventsHandler;
import control.tower.inventory.service.core.data.entities.InventoryItemLookupEntity;
import control.tower.inventory.service.core.data.repositories.InventoryItemLookupRepository;
import control.tower.inventory.service.core.events.InventoryItemCreatedEvent;
import control.tower.inventory.service.core.events.InventoryItemRemovedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InventoryItemLookupEventsHandlerTest {

    private InventoryItemLookupEventsHandler inventoryItemLookupEventsHandler;

    @Mock
    private InventoryItemLookupRepository inventoryItemLookupRepository;

    private final String SKU = "sku";
    private final String PRODUCT_ID = "productId";
    private final String LOCATION_ID = "locationId";
    private final String BIN_ID = "binId";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        inventoryItemLookupEventsHandler = new InventoryItemLookupEventsHandler(inventoryItemLookupRepository);
    }

    @Test
    void shouldSaveInventoryItemLookupEntityOnInventoryItemCreatedEvent() {
        // Arrange
        InventoryItemCreatedEvent event = InventoryItemCreatedEvent.builder()
                .sku(SKU)
                .productId(PRODUCT_ID)
                .locationId(LOCATION_ID)
                .binId(BIN_ID)
                .build();

        // Act
        inventoryItemLookupEventsHandler.on(event);

        // Assert
        ArgumentCaptor<InventoryItemLookupEntity> captor = ArgumentCaptor.forClass(InventoryItemLookupEntity.class);
        Mockito.verify(inventoryItemLookupRepository).save(captor.capture());
        InventoryItemLookupEntity savedEntity = captor.getValue();

        assertEquals(event.getSku(), savedEntity.getSku());
    }

    @Test
    void shouldDeleteInventoryItemLookupEntityOnInventoryItemRemovedEvent() {
        // Arrange
        InventoryItemRemovedEvent event = InventoryItemRemovedEvent.builder()
                .sku(SKU)
                .productId(PRODUCT_ID)
                .build();

        InventoryItemLookupEntity inventoryItemLookupEntity = new InventoryItemLookupEntity(SKU);

        Mockito.when(inventoryItemLookupRepository.findBySku(SKU)).thenReturn(inventoryItemLookupEntity);

        // Act
        inventoryItemLookupEventsHandler.on(event);

        // Assert
        Mockito.verify(inventoryItemLookupRepository).delete(inventoryItemLookupEntity);
    }

    @Test
    void shouldThrowExceptionOnInventoryItemRemovedEventForNonExistingInventoryItem() {
        // Arrange
        InventoryItemRemovedEvent event = InventoryItemRemovedEvent.builder()
                .sku(SKU)
                .productId(PRODUCT_ID)
                .build();

        Mockito.when(inventoryItemLookupRepository.findBySku(SKU)).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> inventoryItemLookupEventsHandler.on(event));
    }
}
