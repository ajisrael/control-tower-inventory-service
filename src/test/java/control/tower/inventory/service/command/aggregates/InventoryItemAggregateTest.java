package control.tower.inventory.service.command.aggregates;

import control.tower.inventory.service.command.aggregates.InventoryItemAggregate;
import control.tower.inventory.service.command.commands.CreateInventoryItemCommand;
import control.tower.inventory.service.command.commands.MoveInventoryItemCommand;
import control.tower.inventory.service.command.commands.RemoveInventoryItemCommand;
import control.tower.inventory.service.core.events.InventoryItemCreatedEvent;
import control.tower.inventory.service.core.events.InventoryItemMovedEvent;
import control.tower.inventory.service.core.events.InventoryItemRemovedEvent;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InventoryItemAggregateTest {

    private final String SKU = "1234";
    private final String PRODUCT_ID = "5678";
    private final String LOCATION_ID = "WHS";
    private final String BIN_ID = "101";

    private FixtureConfiguration<InventoryItemAggregate> fixture;

    @BeforeEach
    void setup() {
        fixture = new AggregateTestFixture<>(InventoryItemAggregate.class);
    }

    @Test
    void shouldCreateInventoryItemAggregate() {
        fixture.givenNoPriorActivity()
                .when(
                        CreateInventoryItemCommand.builder()
                                .sku(SKU)
                                .productId(PRODUCT_ID)
                                .locationId(LOCATION_ID)
                                .binId(BIN_ID)
                                .build())
                .expectEvents(
                        InventoryItemCreatedEvent.builder()
                                .sku(SKU)
                                .productId(PRODUCT_ID)
                                .locationId(LOCATION_ID)
                                .binId(BIN_ID)
                                .build())
                .expectState(
                        inventoryItemAggregate -> {
                            assertEquals(SKU, inventoryItemAggregate.getSku());
                            assertEquals(PRODUCT_ID, inventoryItemAggregate.getProductId());
                            assertEquals(LOCATION_ID, inventoryItemAggregate.getLocationId());
                            assertEquals(BIN_ID, inventoryItemAggregate.getBinId());
                        }
                );
    }

    @Test
    void shouldMoveInventoryItemAggregate() {
        String newLocationId = "newLocationId";
        String newBinId = "newBinId";

        fixture.given(InventoryItemCreatedEvent.builder()
                        .sku(SKU)
                        .productId(PRODUCT_ID)
                        .locationId(LOCATION_ID)
                        .binId(BIN_ID)
                        .build())
                .when(MoveInventoryItemCommand.builder()
                        .sku(SKU)
                        .locationId(newLocationId)
                        .binId(newBinId)
                        .build())
                .expectEvents(InventoryItemMovedEvent.builder()
                        .sku(SKU)
                        .productId(PRODUCT_ID)
                        .locationId(newLocationId)
                        .binId(newBinId)
                        .build())
                .expectState(
                        inventoryItemAggregate -> {
                            assertEquals(SKU, inventoryItemAggregate.getSku());
                            assertEquals(PRODUCT_ID, inventoryItemAggregate.getProductId());
                            assertEquals(newLocationId, inventoryItemAggregate.getLocationId());
                            assertEquals(newBinId, inventoryItemAggregate.getBinId());
                        }
                );
    }

    @Test
    void shouldRemoveInventoryItemAggregate() {
        fixture.given(InventoryItemCreatedEvent.builder()
                        .sku(SKU)
                        .productId(PRODUCT_ID)
                        .locationId(LOCATION_ID)
                        .binId(BIN_ID)
                        .build())
                .when(RemoveInventoryItemCommand.builder()
                        .sku(SKU)
                        .build())
                .expectEvents(InventoryItemRemovedEvent.builder()
                        .sku(SKU)
                        .productId(PRODUCT_ID)
                        .build());
    }
}
