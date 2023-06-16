package control.tower.inventory.service.command.interceptors;

import control.tower.inventory.service.command.commands.MoveInventoryItemCommand;
import control.tower.inventory.service.core.data.InventoryItemLookupEntity;
import control.tower.inventory.service.core.data.InventoryItemLookupRepository;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MoveInventoryItemCommandInterceptorTest {

    private MoveInventoryItemCommandInterceptor interceptor;
    private InventoryItemLookupRepository lookupRepository;

    private final String SKU = "123";
    private final String LOCATION_ID = "locationId";
    private final String BIN_ID = "binId";

    @BeforeEach
    void setUp() {
        lookupRepository = mock(InventoryItemLookupRepository.class);
        interceptor = new MoveInventoryItemCommandInterceptor(lookupRepository);
    }

    @Test
    void shouldProcessValidMoveInventoryItemCommand() {
        MoveInventoryItemCommand validCommand = MoveInventoryItemCommand.builder()
                .sku(SKU)
                .locationId(LOCATION_ID)
                .binId(BIN_ID)
                .build();

        CommandMessage<MoveInventoryItemCommand> commandMessage = new GenericCommandMessage<>(validCommand);

        InventoryItemLookupEntity inventoryItemLookupEntity = new InventoryItemLookupEntity(SKU);
        when(lookupRepository.findBySku(SKU)).thenReturn(inventoryItemLookupEntity);

        BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> result = interceptor.handle(List.of(commandMessage));

        CommandMessage<?> processedCommand = result.apply(0, commandMessage);

        assertEquals(commandMessage, processedCommand);

        verify(lookupRepository).findBySku(SKU);
    }

    @Test
    void shouldThrowExceptionWhenProcessingNonExistingSku() {
        MoveInventoryItemCommand duplicateCommand = MoveInventoryItemCommand.builder()
                .sku(SKU)
                .locationId(LOCATION_ID)
                .binId(BIN_ID)
                .build();

        CommandMessage<MoveInventoryItemCommand> commandMessage = new GenericCommandMessage<>(duplicateCommand);

        when(lookupRepository.findBySku(SKU)).thenReturn(null);

        BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> result = interceptor.handle(List.of(commandMessage));

        assertThrows(IllegalArgumentException.class, () -> result.apply(0, commandMessage));

        verify(lookupRepository).findBySku(SKU);
    }
}
