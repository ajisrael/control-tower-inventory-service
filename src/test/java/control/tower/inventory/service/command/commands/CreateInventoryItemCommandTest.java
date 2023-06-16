package control.tower.inventory.service.command.commands;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateInventoryItemCommandTest {

    private final String SKU = "sku";
    private final String PRODUCT_ID = "productId";
    private final String LOCATION_ID = "locationId";
    private final String BIN_ID = "binId";

    @Test
    void shouldBuildValidCreateInventoryItemCommand() {
        CreateInventoryItemCommand createInventoryItemCommand = CreateInventoryItemCommand.builder()
                .sku(SKU)
                .productId(PRODUCT_ID)
                .locationId(LOCATION_ID)
                .binId(BIN_ID)
                .build();

        assertDoesNotThrow(createInventoryItemCommand::validate);

        assertEquals(createInventoryItemCommand.getSku(), SKU);
        assertEquals(createInventoryItemCommand.getProductId(), PRODUCT_ID);
        assertEquals(createInventoryItemCommand.getLocationId(), LOCATION_ID);
        assertEquals(createInventoryItemCommand.getBinId(), BIN_ID);
    }

    @Test
    void shouldNotBuildValidCreateInventoryItemCommandWhenSkuIsNull() {
        CreateInventoryItemCommand createInventoryItemCommand = CreateInventoryItemCommand.builder()
                .sku(null)
                .productId(PRODUCT_ID)
                .locationId(LOCATION_ID)
                .binId(BIN_ID)
                .build();

        assertThrows(IllegalArgumentException.class, createInventoryItemCommand::validate);
    }

    @Test
    void shouldNotBuildValidCreateInventoryItemCommandWhenSkuIsEmpty() {
        CreateInventoryItemCommand createInventoryItemCommand = CreateInventoryItemCommand.builder()
                .sku("")
                .productId(PRODUCT_ID)
                .locationId(LOCATION_ID)
                .binId(BIN_ID)
                .build();

        assertThrows(IllegalArgumentException.class, createInventoryItemCommand::validate);
    }

    @Test
    void shouldNotBuildValidCreateInventoryItemCommandWhenProductIdIsNull() {
        CreateInventoryItemCommand createInventoryItemCommand = CreateInventoryItemCommand.builder()
                .sku(SKU)
                .productId(null)
                .locationId(LOCATION_ID)
                .binId(BIN_ID)
                .build();

        assertThrows(IllegalArgumentException.class, createInventoryItemCommand::validate);
    }

    @Test
    void shouldNotBuildValidCreateInventoryItemCommandWhenProductIdIsEmpty() {
        CreateInventoryItemCommand createInventoryItemCommand = CreateInventoryItemCommand.builder()
                .sku(SKU)
                .productId("")
                .locationId(LOCATION_ID)
                .binId(BIN_ID)
                .build();

        assertThrows(IllegalArgumentException.class, createInventoryItemCommand::validate);
    }

    @Test
    void shouldNotBuildValidCreateInventoryItemCommandWhenLocationIdIsNull() {
        CreateInventoryItemCommand createInventoryItemCommand = CreateInventoryItemCommand.builder()
                .sku(SKU)
                .productId(PRODUCT_ID)
                .locationId(null)
                .binId(BIN_ID)
                .build();

        assertThrows(IllegalArgumentException.class, createInventoryItemCommand::validate);
    }

    @Test
    void shouldNotBuildValidCreateInventoryItemCommandWhenLocationIdIsEmpty() {
        CreateInventoryItemCommand createInventoryItemCommand = CreateInventoryItemCommand.builder()
                .sku(SKU)
                .productId(PRODUCT_ID)
                .locationId("")
                .binId(BIN_ID)
                .build();

        assertThrows(IllegalArgumentException.class, createInventoryItemCommand::validate);
    }

    @Test
    void shouldNotBuildValidCreateInventoryItemCommandWhenBinIdIsNull() {
        CreateInventoryItemCommand createInventoryItemCommand = CreateInventoryItemCommand.builder()
                .sku(SKU)
                .productId(PRODUCT_ID)
                .locationId(LOCATION_ID)
                .binId(null)
                .build();

        assertThrows(IllegalArgumentException.class, createInventoryItemCommand::validate);
    }

    @Test
    void shouldNotBuildValidCreateInventoryItemCommandWhenBinIdIsEmpty() {
        CreateInventoryItemCommand createInventoryItemCommand = CreateInventoryItemCommand.builder()
                .sku(SKU)
                .productId(PRODUCT_ID)
                .locationId(LOCATION_ID)
                .binId("")
                .build();

        assertThrows(IllegalArgumentException.class, createInventoryItemCommand::validate);
    }
}
