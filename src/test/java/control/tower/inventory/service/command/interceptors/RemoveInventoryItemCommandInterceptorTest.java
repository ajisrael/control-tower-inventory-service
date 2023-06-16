package control.tower.inventory.service.command.interceptors;

import control.tower.inventory.service.command.commands.RemoveInventoryItemCommand;
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

class RemoveInventoryItemCommandInterceptorTest {

    private RemoveInventoryItemCommandInterceptor interceptor;
    private InventoryItemLookupRepository lookupRepository;

    private final String SKU = "123";

    @BeforeEach
    void setUp() {
        lookupRepository = mock(InventoryItemLookupRepository.class);
        interceptor = new RemoveInventoryItemCommandInterceptor(lookupRepository);
    }

    @Test
    void shouldProcessValidRemoveInventoryItemCommand() {
        RemoveInventoryItemCommand validCommand = RemoveInventoryItemCommand.builder()
                .sku(SKU)
                .build();

        CommandMessage<RemoveInventoryItemCommand> commandMessage = new GenericCommandMessage<>(validCommand);

        InventoryItemLookupEntity inventoryItemLookupEntity = new InventoryItemLookupEntity(SKU);
        when(lookupRepository.findBySku(SKU)).thenReturn(inventoryItemLookupEntity);

        BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> result = interceptor.handle(List.of(commandMessage));

        CommandMessage<?> processedCommand = result.apply(0, commandMessage);

        assertEquals(commandMessage, processedCommand);

        verify(lookupRepository).findBySku(SKU);
    }

    @Test
    void shouldThrowExceptionWhenProcessingNonExistingSku() {
        RemoveInventoryItemCommand duplicateCommand = RemoveInventoryItemCommand.builder()
                .sku(SKU)
                .build();

        CommandMessage<RemoveInventoryItemCommand> commandMessage = new GenericCommandMessage<>(duplicateCommand);

        when(lookupRepository.findBySku(SKU)).thenReturn(null);

        BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> result = interceptor.handle(List.of(commandMessage));

        assertThrows(IllegalArgumentException.class, () -> result.apply(0, commandMessage));

        verify(lookupRepository).findBySku(SKU);
    }
}
