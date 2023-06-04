package control.tower.inventory.service.command.interceptors;

import control.tower.inventory.service.command.CreateInventoryItemCommand;
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

class CreateInventoryItemCommandInterceptorTest {

    private CreateInventoryItemCommandInterceptor interceptor;
    private InventoryItemLookupRepository lookupRepository;

    @BeforeEach
    void setUp() {
        lookupRepository = mock(InventoryItemLookupRepository.class);
        interceptor = new CreateInventoryItemCommandInterceptor(lookupRepository);
    }

    @Test
    void testHandle_ValidCommand() {
        CreateInventoryItemCommand validCommand = CreateInventoryItemCommand.builder()
                .sku("123").productId("productId").locationId("locationId").binId("binId").build();
        CommandMessage<CreateInventoryItemCommand> commandMessage = new GenericCommandMessage<>(validCommand);

        BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> result = interceptor.handle(List.of(commandMessage));

        CommandMessage<?> processedCommand = result.apply(0, commandMessage);

        assertEquals(commandMessage, processedCommand);
    }

    @Test
    void testHandle_DuplicateSku_ThrowsException() {
        String sku = "123";
        CreateInventoryItemCommand duplicateCommand = CreateInventoryItemCommand.builder()
                .sku(sku).productId("productId").locationId("locationId").binId("binId").build();
        CommandMessage<CreateInventoryItemCommand> commandMessage = new GenericCommandMessage<>(duplicateCommand);

        InventoryItemLookupEntity existingEntity = new InventoryItemLookupEntity(sku);
        when(lookupRepository.findBySku(sku)).thenReturn(existingEntity);

        BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> result = interceptor.handle(List.of(commandMessage));

        assertThrows(IllegalStateException.class, () -> result.apply(0, commandMessage));

        verify(lookupRepository).findBySku(sku);
    }
}
