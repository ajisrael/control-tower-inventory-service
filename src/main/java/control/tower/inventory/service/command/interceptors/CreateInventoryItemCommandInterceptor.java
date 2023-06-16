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

import static control.tower.core.constants.LogMessages.INTERCEPTED_COMMAND;
import static control.tower.core.utils.Helper.throwExceptionIfEntityDoesExist;
import static control.tower.inventory.service.core.constants.ExceptionMessages.INVENTORY_ITEM_WITH_ID_ALREADY_EXISTS;

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

            if (CreateInventoryItemCommand.class.equals(command.getPayloadType())) {
                LOGGER.info(String.format(INTERCEPTED_COMMAND, command.getPayloadType()));

                CreateInventoryItemCommand createInventoryItemCommand = (CreateInventoryItemCommand) command.getPayload();

                InventoryItemLookupEntity inventoryItemLookupEntity = inventoryItemLookupRepository.findBySku(
                        createInventoryItemCommand.getSku());

                throwExceptionIfEntityDoesExist(inventoryItemLookupEntity,
                        String.format(INVENTORY_ITEM_WITH_ID_ALREADY_EXISTS, createInventoryItemCommand.getSku()));
            }

            return command;
        };
    }
}
