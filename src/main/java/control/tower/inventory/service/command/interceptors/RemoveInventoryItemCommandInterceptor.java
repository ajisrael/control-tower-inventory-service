package control.tower.inventory.service.command.interceptors;

import control.tower.inventory.service.command.commands.RemoveInventoryItemCommand;
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
import static control.tower.core.utils.Helper.throwExceptionIfEntityDoesNotExist;
import static control.tower.inventory.service.core.constants.ExceptionMessages.INVENTORY_ITEM_WITH_ID_ALREADY_EXISTS;
import static control.tower.inventory.service.core.constants.ExceptionMessages.INVENTORY_ITEM_WITH_ID_DOES_NOT_EXIST;

@Component
public class RemoveInventoryItemCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveInventoryItemCommandInterceptor.class);

    private final InventoryItemLookupRepository inventoryItemLookupRepository;

    public RemoveInventoryItemCommandInterceptor(InventoryItemLookupRepository inventoryItemLookupRepository) {
        this.inventoryItemLookupRepository = inventoryItemLookupRepository;
    }

    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
            List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {

            if (RemoveInventoryItemCommand.class.equals(command.getPayloadType())) {
                LOGGER.info(String.format(INTERCEPTED_COMMAND, command.getPayloadType()));

                RemoveInventoryItemCommand removeInventoryItemCommand = (RemoveInventoryItemCommand) command.getPayload();

                removeInventoryItemCommand.validate();

                InventoryItemLookupEntity inventoryItemLookupEntity = inventoryItemLookupRepository.findBySku(
                        removeInventoryItemCommand.getSku());

                throwExceptionIfEntityDoesNotExist(inventoryItemLookupEntity,
                        String.format(INVENTORY_ITEM_WITH_ID_DOES_NOT_EXIST, removeInventoryItemCommand.getSku()));
            }

            return command;
        };
    }
}
