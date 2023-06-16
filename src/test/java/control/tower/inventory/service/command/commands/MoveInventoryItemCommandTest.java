package control.tower.inventory.service.command.commands;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoveInventoryItemCommandTest {

    private final String SKU = "sku";
    private final String LOCATION_ID = "locationId";
    private final String BIN_ID = "binId";

    @Test
    void shouldBuildValidMoveInventoryItemCommand() {
        MoveInventoryItemCommand moveInventoryItemCommand = MoveInventoryItemCommand.builder()
                .sku(SKU)
                .locationId(LOCATION_ID)
                .binId(BIN_ID)
                .build();

        assertDoesNotThrow(moveInventoryItemCommand::validate);

        assertEquals(moveInventoryItemCommand.getSku(), SKU);
        assertEquals(moveInventoryItemCommand.getLocationId(), LOCATION_ID);
        assertEquals(moveInventoryItemCommand.getBinId(), BIN_ID);
    }

    @Test
    void shouldNotBuildValidMoveInventoryItemCommandWhenSkuIsNull() {
        MoveInventoryItemCommand moveInventoryItemCommand = MoveInventoryItemCommand.builder()
                .sku(null)
                .locationId(LOCATION_ID)
                .binId(BIN_ID)
                .build();

        assertThrows(IllegalArgumentException.class, moveInventoryItemCommand::validate);
    }

    @Test
    void shouldNotBuildValidMoveInventoryItemCommandWhenSkuIsEmpty() {
        MoveInventoryItemCommand moveInventoryItemCommand = MoveInventoryItemCommand.builder()
                .sku("")
                .locationId(LOCATION_ID)
                .binId(BIN_ID)
                .build();

        assertThrows(IllegalArgumentException.class, moveInventoryItemCommand::validate);
    }

    @Test
    void shouldNotBuildValidMoveInventoryItemCommandWhenLocationIdIsNull() {
        MoveInventoryItemCommand moveInventoryItemCommand = MoveInventoryItemCommand.builder()
                .sku(SKU)
                .locationId(null)
                .binId(BIN_ID)
                .build();

        assertThrows(IllegalArgumentException.class, moveInventoryItemCommand::validate);
    }

    @Test
    void shouldNotBuildValidMoveInventoryItemCommandWhenLocationIdIsEmpty() {
        MoveInventoryItemCommand moveInventoryItemCommand = MoveInventoryItemCommand.builder()
                .sku(SKU)
                .locationId("")
                .binId(BIN_ID)
                .build();

        assertThrows(IllegalArgumentException.class, moveInventoryItemCommand::validate);
    }

    @Test
    void shouldNotBuildValidMoveInventoryItemCommandWhenBinIdIsNull() {
        MoveInventoryItemCommand moveInventoryItemCommand = MoveInventoryItemCommand.builder()
                .sku(SKU)
                .locationId(LOCATION_ID)
                .binId(null)
                .build();

        assertThrows(IllegalArgumentException.class, moveInventoryItemCommand::validate);
    }

    @Test
    void shouldNotBuildValidMoveInventoryItemCommandWhenBinIdIsEmpty() {
        MoveInventoryItemCommand moveInventoryItemCommand = MoveInventoryItemCommand.builder()
                .sku(SKU)
                .locationId(LOCATION_ID)
                .binId("")
                .build();

        assertThrows(IllegalArgumentException.class, moveInventoryItemCommand::validate);
    }
}
