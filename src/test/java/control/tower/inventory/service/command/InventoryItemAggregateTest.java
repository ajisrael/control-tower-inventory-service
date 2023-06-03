package control.tower.inventory.service.command;

import control.tower.inventory.service.core.events.InventoryItemCreatedEvent;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InventoryItemAggregateTest {

    private String SKU = "1234";
    private String PRODUCT_ID = "5678";
    private String LOCATION_ID = "WHS";
    private String BIN_ID = "101";

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
    void shouldNotCreateInventoryItemAggregateWhenSkuIsNull() {
        fixture.givenNoPriorActivity()
                .when(
                        CreateInventoryItemCommand.builder()
                                .sku(null)
                                .productId(PRODUCT_ID)
                                .locationId(LOCATION_ID)
                                .binId(BIN_ID)
                                .build())
                .expectException(IllegalArgumentException.class);
    }

    @Test
    void shouldNotCreateInventoryItemAggregateWhenSkuIsEmpty() {
        fixture.givenNoPriorActivity()
                .when(
                        CreateInventoryItemCommand.builder()
                                .sku("")
                                .productId(PRODUCT_ID)
                                .locationId(LOCATION_ID)
                                .binId(BIN_ID)
                                .build())
                .expectException(IllegalArgumentException.class);
    }

    @Test
    void shouldNotCreateInventoryItemAggregateWhenProductIdIsNull() {
        fixture.givenNoPriorActivity()
                .when(
                        CreateInventoryItemCommand.builder()
                                .sku(SKU)
                                .productId(null)
                                .locationId(LOCATION_ID)
                                .binId(BIN_ID)
                                .build())
                .expectException(IllegalArgumentException.class);
    }

    @Test
    void shouldNotCreateInventoryItemAggregateWhenProductIdIsEmpty() {
        fixture.givenNoPriorActivity()
                .when(
                        CreateInventoryItemCommand.builder()
                                .sku(SKU)
                                .productId("")
                                .locationId(LOCATION_ID)
                                .binId(BIN_ID)
                                .build())
                .expectException(IllegalArgumentException.class);
    }

    @Test
    void shouldNotCreateInventoryItemAggregateWhenLocationIdIsNull() {
        fixture.givenNoPriorActivity()
                .when(
                        CreateInventoryItemCommand.builder()
                                .sku(SKU)
                                .productId(PRODUCT_ID)
                                .locationId(null)
                                .binId(BIN_ID)
                                .build())
                .expectException(IllegalArgumentException.class);
    }

    @Test
    void shouldNotCreateInventoryItemAggregateWhenLocationIdIsEmpty() {
        fixture.givenNoPriorActivity()
                .when(
                        CreateInventoryItemCommand.builder()
                                .sku(SKU)
                                .productId(PRODUCT_ID)
                                .locationId("")
                                .binId(BIN_ID)
                                .build())
                .expectException(IllegalArgumentException.class);
    }

    @Test
    void shouldNotCreateInventoryItemAggregateWhenBinIdIsNull() {
        fixture.givenNoPriorActivity()
                .when(
                        CreateInventoryItemCommand.builder()
                                .sku(SKU)
                                .productId(PRODUCT_ID)
                                .locationId(LOCATION_ID)
                                .binId(null)
                                .build())
                .expectException(IllegalArgumentException.class);
    }

    @Test
    void shouldNotCreateInventoryItemAggregateWhenBinIdIsEmpty() {
        fixture.givenNoPriorActivity()
                .when(
                        CreateInventoryItemCommand.builder()
                                .sku(SKU)
                                .productId(PRODUCT_ID)
                                .locationId(LOCATION_ID)
                                .binId("")
                                .build())
                .expectException(IllegalArgumentException.class);
    }
}
