package control.tower.inventory.service.command.interceptors;

import control.tower.inventory.service.command.commands.CreateInventoryItemCommand;
import control.tower.inventory.service.core.data.entities.InventoryItemLookupEntity;
import control.tower.inventory.service.core.data.repositories.InventoryItemLookupRepository;
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

    private final String SKU = "123";
    private final String PRODUCT_ID = "productId";
    private final String LOCATION_ID = "locationId";
    private final String BIN_ID = "binId";

    @BeforeEach
    void setUp() {
        lookupRepository = mock(InventoryItemLookupRepository.class);
        interceptor = new CreateInventoryItemCommandInterceptor(lookupRepository);
    }

    @Test
    void shouldProcessValidCreateInventoryItemCommand() {
        CreateInventoryItemCommand validCommand = CreateInventoryItemCommand.builder()
                .sku(SKU)
                .productId(PRODUCT_ID)
                .locationId(LOCATION_ID)
                .binId(BIN_ID)
                .build();

        CommandMessage<CreateInventoryItemCommand> commandMessage = new GenericCommandMessage<>(validCommand);

        BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> result = interceptor.handle(List.of(commandMessage));

        CommandMessage<?> processedCommand = result.apply(0, commandMessage);

        assertEquals(commandMessage, processedCommand);
    }

    @Test
    void shouldThrowExceptionWhenProcessingExistingSku() {
        CreateInventoryItemCommand duplicateCommand = CreateInventoryItemCommand.builder()
                .sku(SKU)
                .productId(PRODUCT_ID)
                .locationId(LOCATION_ID)
                .binId(BIN_ID)
                .build();

        CommandMessage<CreateInventoryItemCommand> commandMessage = new GenericCommandMessage<>(duplicateCommand);

        InventoryItemLookupEntity existingEntity = new InventoryItemLookupEntity(SKU);
        when(lookupRepository.findBySku(SKU)).thenReturn(existingEntity);

        BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> result = interceptor.handle(List.of(commandMessage));

        assertThrows(IllegalArgumentException.class, () -> result.apply(0, commandMessage));

        verify(lookupRepository).findBySku(SKU);
    }
}
