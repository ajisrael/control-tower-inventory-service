package control.tower.inventory.service.command.interceptors;

import control.tower.inventory.service.command.commands.CreateInventoryItemCommand;
import control.tower.inventory.service.core.data.InventoryItemLookupEntity;
import control.tower.inventory.service.core.data.InventoryItemLookupRepository;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BiFunction;

@Component
public class CreateInventoryItemCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateInventoryItemCommandInterceptor.class);

    private final InventoryItemLookupRepository inventoryItemLookupRepository;

    public CreateInventoryItemCommandInterceptor(InventoryItemLookupRepository inventoryItemLookupRepository) {
        this.inventoryItemLookupRepository = inventoryItemLookupRepository;
    }

    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
            List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {

            LOGGER.info("Intercepted command: " + command.getPayloadType());

            if (CreateInventoryItemCommand.class.equals(command.getPayloadType())) {

                CreateInventoryItemCommand createInventoryItemCommand = (CreateInventoryItemCommand) command.getPayload();

                InventoryItemLookupEntity inventoryItemLookupEntity = inventoryItemLookupRepository.findBySku(
                        createInventoryItemCommand.getSku());

                if (inventoryItemLookupEntity != null) {
                    throw new IllegalStateException(
                            String.format("Inventory item with sku %s already exists",
                                    createInventoryItemCommand.getSku())
                    );
                }
            }

            return command;
        };
    }
}
