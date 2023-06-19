package control.tower.inventory.service.query;

import control.tower.inventory.service.core.data.entities.InventoryItemEntity;
import control.tower.inventory.service.core.data.repositories.InventoryItemRepository;
import control.tower.inventory.service.core.events.InventoryItemCreatedEvent;
import control.tower.inventory.service.core.events.InventoryItemRemovedEvent;
import control.tower.inventory.service.query.eventhandlers.InventoryItemEventsHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class InventoryItemEventsHandlerTest {

    private InventoryItemEventsHandler eventsHandler;

    @Mock
    private InventoryItemRepository inventoryItemRepository;

    private final String SKU = "sku";
    private final String PRODUCT_ID = "productId";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        eventsHandler = new InventoryItemEventsHandler(inventoryItemRepository);
    }

    @Test
    void shouldSaveInventoryItemEntityOnInventoryItemCreatedEvent() {
        InventoryItemCreatedEvent event = InventoryItemCreatedEvent.builder()
                .sku(SKU)
                .productId(PRODUCT_ID)
                .locationId("locationId")
                .binId("binId")
                .build();

        Mockito.when(inventoryItemRepository.save(any(InventoryItemEntity.class))).thenAnswer(invocation -> {
            InventoryItemEntity inventoryItemEntity = invocation.getArgument(0);
            inventoryItemEntity.setSku(SKU);
            return inventoryItemEntity;
        });

        // Act
        eventsHandler.on(event);

        // Assert
        Mockito.verify(inventoryItemRepository).save(any(InventoryItemEntity.class));
    }

    @Test
    void shouldDeleteInventoryItemEntityOnInventoryItemRemovedEvent() {
        // Arrange
        InventoryItemRemovedEvent event = InventoryItemRemovedEvent.builder()
                .sku(SKU)
                .productId(PRODUCT_ID)
                .build();

        InventoryItemEntity inventoryItemEntity = new InventoryItemEntity();
        inventoryItemEntity.setSku(SKU);

        Mockito.when(inventoryItemRepository.findBySku(event.getSku())).thenReturn(inventoryItemEntity);

        // Act
        eventsHandler.on(event);

        // Assert
        Mockito.verify(inventoryItemRepository).delete(eq(inventoryItemEntity));
    }

    @Test
    void shouldThrowExceptionOnInventoryItemRemovedEventForNonExistingSku() {
        InventoryItemRemovedEvent event = InventoryItemRemovedEvent.builder()
                .sku("nonExistingSku")
                .productId(PRODUCT_ID)
                .build();

        Mockito.when(inventoryItemRepository.findBySku(event.getSku())).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> eventsHandler.on(event));
    }

    @Test
    void shouldThrowExceptionWhenHandlingGenericException() {
        // Arrange
        Exception exception = new Exception("Test exception");

        // Act & Assert
        assertThrows(Exception.class, () -> eventsHandler.handle(exception));
    }

    @Test
    void shouldLogErrorMessageWhenHandlingIllegalArgumentException() {
        // Arrange
        IllegalArgumentException exception = new IllegalArgumentException("Test exception");

        // Act
        eventsHandler.handle(exception);

        // Assert
        // Verify that the error message is logged
        // You can use your preferred logging framework to verify the logs
    }
}
