package control.tower.inventory.service.command.commands;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RemoveInventoryItemCommandTest {

    private final String SKU = "sku";

    @Test
    void shouldBuildValidRemoveInventoryItemCommand() {
        RemoveInventoryItemCommand removeInventoryItemCommand = RemoveInventoryItemCommand.builder()
                .sku(SKU)
                .build();

        assertDoesNotThrow(removeInventoryItemCommand::validate);

        assertEquals(removeInventoryItemCommand.getSku(), SKU);
        assertEquals(removeInventoryItemCommand.isCompensatingTransaction(), false);
    }

    @Test
    void shouldBuildValidRemoveInventoryItemCommandThatIsACompensatingTransaction() {
        RemoveInventoryItemCommand removeInventoryItemCommand = RemoveInventoryItemCommand.builder()
                .sku(SKU)
                .isCompensatingTransaction(true)
                .build();

        assertDoesNotThrow(removeInventoryItemCommand::validate);

        assertEquals(removeInventoryItemCommand.getSku(), SKU);
        assertEquals(removeInventoryItemCommand.isCompensatingTransaction(), true);
    }

    @Test
    void shouldNotBuildValidRemoveInventoryItemCommandWhenSkuIsNull() {
        RemoveInventoryItemCommand removeInventoryItemCommand = RemoveInventoryItemCommand.builder()
                .sku(null)
                .build();

        assertThrows(IllegalArgumentException.class, removeInventoryItemCommand::validate);
    }

    @Test
    void shouldNotBuildValidRemoveInventoryItemCommandWhenSkuIsEmpty() {
        RemoveInventoryItemCommand removeInventoryItemCommand = RemoveInventoryItemCommand.builder()
                .sku("")
                .build();

        assertThrows(IllegalArgumentException.class, removeInventoryItemCommand::validate);
    }
}
